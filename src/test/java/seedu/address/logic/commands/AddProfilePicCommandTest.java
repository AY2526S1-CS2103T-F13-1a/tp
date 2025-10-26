package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Address;
import seedu.address.model.person.Closeness;
import seedu.address.model.person.Email;
import seedu.address.model.person.Handle;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.tag.Tag;
import seedu.address.testutil.Assert;
import seedu.address.testutil.PersonBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for AddProfilePicCommand.
 */
public class AddProfilePicCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void constructor_nullIndex_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> new AddProfilePicCommand(null, "test.png"));
    }

    @Test
    public void execute_updateProfilePic_success() throws Exception {
        Person personInList = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person editedPerson = new PersonBuilder(personInList).build();
        editedPerson = createPersonWithProfilePic(editedPerson, "johndoe.png");

        AddProfilePicCommand addProfilePicCommand = new AddProfilePicCommand(INDEX_FIRST_PERSON, "johndoe.png");

        String expectedMessage = String.format(AddProfilePicCommand.MESSAGE_SUCCESS,
                editedPerson.getName().fullName);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(personInList, editedPerson);

        assertCommandSuccess(addProfilePicCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_updateProfilePicWithEmptyString_success() throws Exception {
        Person personInList = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person editedPerson = new PersonBuilder(personInList).build();

        AddProfilePicCommand addProfilePicCommand = new AddProfilePicCommand(INDEX_FIRST_PERSON, "");

        String expectedMessage = String.format(AddProfilePicCommand.MESSAGE_SUCCESS,
                editedPerson.getName().fullName);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(personInList, editedPerson);

        assertCommandSuccess(addProfilePicCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidPersonIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        AddProfilePicCommand addProfilePicCommand = new AddProfilePicCommand(outOfBoundIndex, "johndoe.png");

        assertCommandFailure(addProfilePicCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        final AddProfilePicCommand standardCommand = new AddProfilePicCommand(INDEX_FIRST_PERSON, "johndoe.png");

        // same values -> returns true
        AddProfilePicCommand commandWithSameValues = new AddProfilePicCommand(INDEX_FIRST_PERSON, "johndoe.png");
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different index -> returns false
        assertFalse(standardCommand.equals(new AddProfilePicCommand(INDEX_SECOND_PERSON, "johndoe.png")));

        // different profile picture -> returns false
        assertFalse(standardCommand.equals(new AddProfilePicCommand(INDEX_FIRST_PERSON, "other.png")));
    }

    @Test
    public void execute_validFilePath_success(@TempDir Path tempDir) throws Exception {
        // Create a temporary image file
        Path testImage = tempDir.resolve("test.png");
        Files.write(testImage, new byte[]{1, 2, 3});

        Person personInList = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        // Convert to absolute path string
        String imagePath = testImage.toAbsolutePath().toString();
        AddProfilePicCommand addProfilePicCommand = new AddProfilePicCommand(INDEX_FIRST_PERSON, imagePath);

        // This should copy the file and update the person
        String expectedMessage = String.format(AddProfilePicCommand.MESSAGE_SUCCESS,
                personInList.getName().fullName);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        Person editedPerson = createPersonWithProfilePic(personInList, "test.png");
        expectedModel.setPerson(personInList, editedPerson);

        assertCommandSuccess(addProfilePicCommand, model, expectedMessage, expectedModel);
        // Verify the file was copied to docs/images directory
        Path docsImagesDir = Path.of(System.getProperty("user.dir"), "docs", "images", "test.png");
        assertTrue(Files.exists(docsImagesDir), "Image should be copied to docs/images directory");
        // Clean up
        Files.deleteIfExists(docsImagesDir);
    }

    @Test
    public void execute_invalidFilePath_throwsCommandException(@TempDir Path tempDir) {
        // Create a path to a non-existent file
        String invalidPath = tempDir.resolve("nonexistent.png").toString();
        AddProfilePicCommand addProfilePicCommand = new AddProfilePicCommand(INDEX_FIRST_PERSON, invalidPath);
        // The error message includes the full file path, so we check for the prefix
        CommandException thrown = assertThrows(CommandException.class, () -> addProfilePicCommand.execute(model));
        assertTrue(thrown.getMessage().startsWith("Failed to copy image file:"),
                "Error message should start with 'Failed to copy image file:' but was: " + thrown.getMessage());
    }

    @Test
    public void execute_updateProfilePicTwice_success() throws Exception {
        Person personInList = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person firstEdit = createPersonWithProfilePic(personInList, "johndoe.png");

        AddProfilePicCommand firstCommand = new AddProfilePicCommand(INDEX_FIRST_PERSON, "johndoe.png");

        String expectedMessage1 = String.format(AddProfilePicCommand.MESSAGE_SUCCESS,
                firstEdit.getName().fullName);

        Model expectedModel1 = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel1.setPerson(personInList, firstEdit);

        assertCommandSuccess(firstCommand, model, expectedMessage1, expectedModel1);

        // Get the updated person after first command
        Person updatedPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person secondEdit = createPersonWithProfilePic(updatedPerson, "alice.png");

        AddProfilePicCommand secondCommand = new AddProfilePicCommand(INDEX_FIRST_PERSON, "alice.png");

        String expectedMessage2 = String.format(AddProfilePicCommand.MESSAGE_SUCCESS,
                secondEdit.getName().fullName);

        Model expectedModel2 = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel2.setPerson(updatedPerson, secondEdit);

        assertCommandSuccess(secondCommand, model, expectedMessage2, expectedModel2);
    }

    /**
     * Creates a Person object with the specified profile picture.
     */
    private Person createPersonWithProfilePic(Person person, String profilePic) {
        Name name = person.getName();
        Phone phone = person.getPhone();
        Email email = person.getEmail();
        Address address = person.getAddress();
        java.util.Set<Tag> tags = person.getTags();
        Handle handle = person.getHandle();
        Closeness closeness = person.getCloseness();

        return new Person(name, phone, email, address, tags, handle, profilePic, closeness);
    }
}

