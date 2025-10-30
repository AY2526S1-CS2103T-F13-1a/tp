package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.Messages.MESSAGE_PERSONS_LISTED_OVERVIEW;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BENSON;
import static seedu.address.testutil.TypicalPersons.CARL;
import static seedu.address.testutil.TypicalPersons.DANIEL;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.logic.parser.ParserUtil;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.model.person.TagContainsKeywordPredicate;
import seedu.address.model.tag.Tag;

/**
 * Contains integration tests (with Model) and unit tests for FilterCommand.
 */
public class FilterCommandTest {

    private Model model;
    private Model expectedModel;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
    }

    @Test
    public void execute_singleTag_personFound() throws Exception {
        // --- Setup Predicate and Command ---

        // FIX 1: Update the expected count. (e.g., 3 people have "friends")
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 3);

        Set<Tag> tags = ParserUtil.parseTags(Collections.singletonList("friends"));
        TagContainsKeywordPredicate predicate = new TagContainsKeywordPredicate(tags);
        FilterCommand command = new FilterCommand(predicate);

        // --- Setup Expected Model ---
        expectedModel.updateFilteredPersonList(predicate);

        // --- Execute and Assert ---
        assertCommandSuccess(command, model, expectedMessage, expectedModel);

        // FIX 2: Compare with a Set to ignore order. Update with your actual persons.
        Set<Person> expectedPersons = new HashSet<>(Arrays.asList(ALICE, BENSON, DANIEL));
        Set<Person> actualPersons = new HashSet<>(model.getSortedPersonList());
        assertEquals(expectedPersons, actualPersons);
    }

    @Test
    public void execute_multipleTags_multiplePersonsFound() throws Exception {
        // --- Setup Predicate and Command ---

        // FIX 1: Update the expected count. (e.g., 3 people have "friends" or "owesMoney")
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 3);

        Set<Tag> tags = ParserUtil.parseTags(Arrays.asList("friends", "owesMoney"));
        TagContainsKeywordPredicate predicate = new TagContainsKeywordPredicate(tags);
        FilterCommand command = new FilterCommand(predicate);

        // --- Setup Expected Model ---
        expectedModel.updateFilteredPersonList(predicate);

        // --- Execute and Assert ---
        assertCommandSuccess(command, model, expectedMessage, expectedModel);

        // FIX 2: Compare with a Set. Update with your actual persons.
        Set<Person> expectedPersons = new HashSet<>(Arrays.asList(ALICE, BENSON, DANIEL));
        Set<Person> actualPersons = new HashSet<>(model.getSortedPersonList());
        assertEquals(expectedPersons, actualPersons);
    }

    @Test
    public void execute_noSuchTag_noPersonFound() throws Exception {
        // --- Setup Predicate and Command ---
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 0);
        Set<Tag> tags = ParserUtil.parseTags(Collections.singletonList("nonexistent"));
        TagContainsKeywordPredicate predicate = new TagContainsKeywordPredicate(tags);
        FilterCommand command = new FilterCommand(predicate);

        // --- Setup Expected Model ---
        expectedModel.updateFilteredPersonList(predicate);

        // --- Execute and Assert ---
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Collections.emptyList(), model.getSortedPersonList());
    }

    @Test
    public void equals() throws Exception {
        Set<Tag> friendsTagSet = ParserUtil.parseTags(Collections.singletonList("friends"));
        Set<Tag> owesMoneyTagSet = ParserUtil.parseTags(Collections.singletonList("owesMoney"));

        TagContainsKeywordPredicate firstPredicate = new TagContainsKeywordPredicate(friendsTagSet);
        TagContainsKeywordPredicate secondPredicate = new TagContainsKeywordPredicate(owesMoneyTagSet);

        FilterCommand filterFirstCommand = new FilterCommand(firstPredicate);
        FilterCommand filterSecondCommand = new FilterCommand(secondPredicate);

        // same object -> returns true
        assertTrue(filterFirstCommand.equals(filterFirstCommand));

        // same values -> returns true
        FilterCommand filterFirstCommandCopy = new FilterCommand(firstPredicate);
        assertTrue(filterFirstCommand.equals(filterFirstCommandCopy));

        // different types -> returns false
        assertFalse(filterFirstCommand.equals(1));

        // null -> returns false
        assertFalse(filterFirstCommand.equals(null));

        // different predicate -> returns false
        assertFalse(filterFirstCommand.equals(filterSecondCommand));
    }
}