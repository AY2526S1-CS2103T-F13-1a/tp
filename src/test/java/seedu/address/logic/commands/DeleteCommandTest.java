package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.model.tag.Tag;

public class DeleteCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_validIndexUnfilteredList_success() {
        Person personToDelete = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        DeleteCommand deleteCommand = new DeleteCommand(INDEX_FIRST_PERSON);

        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_PERSON_SUCCESS,
                Messages.format(personToDelete));

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.deletePerson(personToDelete);

        assertCommandSuccess(deleteCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_gracefulFailure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        List<Index> indexes = Arrays.asList(INDEX_FIRST_PERSON, outOfBoundIndex);
        DeleteCommand deleteCommand = new DeleteCommand(indexes);

        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_PERSON_SUCCESS,
                Messages.format(model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased())));
        String invalidMessage = String.format(DeleteCommand.INVALID_INDEXES_MESSAGE, outOfBoundIndex.getOneBased());

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.deletePerson(model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased()));

        assertCommandSuccess(deleteCommand, model, expectedMessage + "\n" + invalidMessage, expectedModel);
    }

    @Test
    public void execute_validTagDelete_success() {
        Set<Tag> tags = Set.of(new Tag("friends"));
        DeleteCommand deleteCommand = new DeleteCommand(tags);

        List<Person> toDelete = model.getFilteredPersonList().stream()
                .filter(p -> p.getTags().contains(new Tag("friends")))
                .collect(Collectors.toList());

        // Prepare expected message
        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_PERSONS_SUCCESS, toDelete.size(),
                toDelete.stream().map(Messages::format).collect(Collectors.joining("\n")));

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        toDelete.forEach(expectedModel::deletePerson);

        assertCommandSuccess(deleteCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_multipleTagsDelete_success() {
        Set<Tag> tags = Set.of(new Tag("friends"), new Tag("colleagues"));
        DeleteCommand deleteCommand = new DeleteCommand(tags);

        List<Person> toDelete = model.getFilteredPersonList().stream()
                .filter(p -> p.getTags().contains(new Tag("friends")) || p.getTags().contains(new Tag("colleagues")))
                .collect(Collectors.toList());

        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_PERSONS_SUCCESS, toDelete.size(),
                toDelete.stream().map(Messages::format).collect(Collectors.joining("\n")));

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        toDelete.forEach(expectedModel::deletePerson);

        assertCommandSuccess(deleteCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void equals() {
        DeleteCommand deleteFirstCommand = new DeleteCommand(INDEX_FIRST_PERSON);
        DeleteCommand deleteSecondCommand = new DeleteCommand(INDEX_SECOND_PERSON);

        assertTrue(deleteFirstCommand.equals(deleteFirstCommand));
        DeleteCommand deleteFirstCommandCopy = new DeleteCommand(INDEX_FIRST_PERSON);
        assertTrue(deleteFirstCommand.equals(deleteFirstCommandCopy));

        assertFalse(deleteFirstCommand.equals(1));
        assertFalse(deleteFirstCommand.equals(null));
        assertFalse(deleteFirstCommand.equals(deleteSecondCommand));
    }

    @Test
    public void toString_singleIndex() {
        Index targetIndex = Index.fromOneBased(1);
        DeleteCommand deleteCommand = new DeleteCommand(targetIndex);
        String expected = DeleteCommand.class.getCanonicalName()
                + "{mode=BY_INDEX, targetIndexes=[" + targetIndex + "], targetTags=null}";
        assertEquals(expected, deleteCommand.toString());
    }

    @Test
    public void toString_multipleIndexes() {
        List<Index> indices = Arrays.asList(Index.fromOneBased(2), Index.fromOneBased(5));
        DeleteCommand deleteCommand = new DeleteCommand(indices);
        String expected = DeleteCommand.class.getCanonicalName()
                + "{mode=BY_INDEX, targetIndexes=[" + indices.get(0) + ", " + indices.get(1) + "], targetTags=null}";
        assertEquals(expected, deleteCommand.toString());
    }

    @Test
    public void toString_byTag() {
        Tag tag = new Tag("friends");
        DeleteCommand deleteCommand = new DeleteCommand(Set.of(tag));
        String expected = DeleteCommand.class.getCanonicalName()
                + "{mode=BY_TAGS, targetIndexes=null, targetTags=[" + tag + "]}";
        assertEquals(expected, deleteCommand.toString());
    }

    private void showNoPerson(Model model) {
        model.updateFilteredPersonList(p -> false);
        assertTrue(model.getFilteredPersonList().isEmpty());
    }
}
