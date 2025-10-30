package seedu.address.logic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX;
import static seedu.address.logic.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.address.logic.commands.CommandTestUtil.ADDRESS_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.CLOSENESS_DESC_HIGH;
import static seedu.address.logic.commands.CommandTestUtil.EMAIL_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.HANDLE_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.PHONE_DESC_AMY;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.AMY;
import static seedu.address.testutil.TypicalPersons.BENSON;
import static seedu.address.testutil.TypicalPersons.DANIEL;
import static seedu.address.testutil.TypicalPersons.ELLE;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.Path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import javafx.collections.ObservableList;
import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.storage.CommandHistory;
import seedu.address.storage.CommandHistoryStorage;
import seedu.address.storage.FileCommandHistoryStorage;
import seedu.address.storage.JsonAddressBookStorage;
import seedu.address.storage.JsonUserPrefsStorage;
import seedu.address.storage.StorageManager;
import seedu.address.testutil.PersonBuilder;

public class LogicManagerTest {
    private static final IOException DUMMY_IO_EXCEPTION = new IOException("dummy IO exception");
    private static final IOException DUMMY_AD_EXCEPTION = new AccessDeniedException("dummy access denied exception");

    @TempDir
    public Path temporaryFolder;

    private Model model = new ModelManager();
    private Logic logic;

    @BeforeEach
    public void setUp() {
        JsonAddressBookStorage addressBookStorage =
                new JsonAddressBookStorage(temporaryFolder.resolve("addressBook.json"));
        JsonUserPrefsStorage userPrefsStorage = new JsonUserPrefsStorage(temporaryFolder.resolve("userPrefs.json"));
        FileCommandHistoryStorage commandHistoryStorage = new FileCommandHistoryStorage(temporaryFolder
                .resolve("command_history.txt"));
        StorageManager storage = new StorageManager(addressBookStorage, userPrefsStorage, commandHistoryStorage);
        CommandHistory commandHistory = new CommandHistory(500, commandHistoryStorage);
        logic = new LogicManager(model, storage, commandHistory);
    }

    @Test
    public void execute_invalidCommandFormat_throwsParseException() {
        String invalidCommand = "uicfhmowqewca";
        assertParseException(invalidCommand, MESSAGE_UNKNOWN_COMMAND);
    }

    @Test
    public void execute_commandExecutionError_throwsCommandException() {
        String deleteCommand = "delete 9";
        assertCommandException(deleteCommand, MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_validCommand_success() throws Exception {
        String listCommand = ListCommand.COMMAND_WORD;
        assertCommandSuccess(listCommand, ListCommand.MESSAGE_SUCCESS, model);
    }

    @Test
    public void execute_storageThrowsIoException_throwsCommandException() {
        assertCommandFailureForExceptionFromStorage(DUMMY_IO_EXCEPTION, String.format(
                LogicManager.FILE_OPS_ERROR_FORMAT, DUMMY_IO_EXCEPTION.getMessage()));
    }

    @Test
    public void execute_storageThrowsAdException_throwsCommandException() {
        assertCommandFailureForExceptionFromStorage(DUMMY_AD_EXCEPTION, String.format(
                LogicManager.FILE_OPS_PERMISSION_ERROR_FORMAT, DUMMY_AD_EXCEPTION.getMessage()));
    }

    @Test
    public void getFilteredPersonList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> logic.getFilteredPersonList().remove(0));
    }

    @Test
    public void execute_filterThenDelete_deletesCorrectDisplayedPerson() throws Exception {
        // --- Setup: Add typical persons ---
        model.setAddressBook(getTypicalAddressBook()); // Assumes you have this helper from TypicalPersons

        // --- 1. Modify State: Filter to show only BENSON ---
        logic.execute("filter t/owesMoney"); // Assuming only Benson has this tag

        // Pre-assertion: check that the displayed list is correct
        ObservableList<Person> displayedList = model.getSortedPersonList();
        assertEquals(1, displayedList.size());
        assertEquals(BENSON, displayedList.get(0));

        // --- 2. Execute Command: Delete index 1 (which must be BENSON) ---
        logic.execute("delete 1");

        // --- 3. Assert: The master list (AddressBook) should no longer contain BENSON ---
        assertFalse(model.hasPerson(BENSON));

        // --- 4. Assert: The displayed list should now be empty ---
        assertEquals(0, model.getSortedPersonList().size());
    }

    @Test
    public void execute_filterThenSortThenDelete_deletesCorrectDisplayedPerson() throws Exception {
        // Setup
        model.setAddressBook(getTypicalAddressBook());

        // 1. Modify State (Filter): Get all "friends"
        // Insertion order: [ALICE (c/5), BENSON (c/3), DANIEL (c/4)]
        logic.execute("filter t/friends");
        assertEquals(3, model.getSortedPersonList().size());

        // 2. Modify State (Sort): Sort the filtered list by closeness (ascending)
        // Displayed list: [BENSON (c/3), DANIEL (c/4), ALICE (c/5)]
        logic.execute("sortByCloseness o/asc");

        // Pre-assertion: check that the displayed list is correct
        ObservableList<Person> displayedList = model.getSortedPersonList();
        assertEquals(BENSON, displayedList.get(0)); // Benson (c/3) is at index 1
        assertEquals(DANIEL, displayedList.get(1)); // Daniel (c/4) is at index 2
        assertEquals(ALICE, displayedList.get(2)); // Alice (c/5) is at index 3

        // 3. Execute Command: Delete index 1 (which must be BENSON)
        logic.execute("delete 1");

        // 4. Assert: The master list should no longer contain BENSON
        assertFalse(model.hasPerson(BENSON));
        assertTrue(model.hasPerson(ALICE)); // Others are safe
        assertTrue(model.hasPerson(DANIEL));

        // 5. Assert: The displayed list should now contain DANIEL and ALICE, still sorted
        displayedList = model.getSortedPersonList();
        assertEquals(2, displayedList.size());
        assertEquals(DANIEL, displayedList.get(0)); // Daniel is now at index 1
        assertEquals(ALICE, displayedList.get(1)); // Alice is now at index 2
    }

    @Test
    public void execute_sortThenEditCloseness_updatesSortOrder() throws Exception {
        // Setup: ALICE (c/5), BENSON (c/3), CARL (c/2), DANIEL (c/4), ELLE (c/1), ...
        model.setAddressBook(getTypicalAddressBook());

        // 1. Modify State: Sort by closeness (ascending)
        // Displayed list: [ELLE (c/1), CARL (c/2), FIONA (c/2), BENSON (c/3), GEORGE (c/3), DANIEL (c/4), ALICE (c/5)]
        logic.execute("sortByCloseness o/asc");

        // Pre-assertion: ELLE (c/1) is at index 1
        ObservableList<Person> displayedList = model.getSortedPersonList();
        assertEquals(ELLE, displayedList.get(0));

        // 2. Execute Command: Edit index 1 (ELLE) and change closeness from 1 to 5
        logic.execute("edit 1 c/5");

        // 3. Assert: The list should re-sort itself. ELLE (now c/5) should be at the end.
        displayedList = model.getSortedPersonList();
        assertEquals(7, displayedList.size());

        // Check that ELLE is no longer at the start
        assertFalse(displayedList.get(0).equals(ELLE));

        // Check that ELLE is now at the end (or tied with ALICE, also c/5)
        Person editedElle = new PersonBuilder(ELLE).withCloseness("5").build();
        assertTrue(displayedList.get(5).equals(ALICE) || displayedList.get(6).equals(ALICE));
        assertTrue(displayedList.get(5).equals(editedElle) || displayedList.get(6).equals(editedElle));
    }

    /**
     * Executes the command and confirms that
     * - no exceptions are thrown <br>
     * - the feedback message is equal to {@code expectedMessage} <br>
     * - the internal model manager state is the same as that in {@code expectedModel} <br>
     * @see #assertCommandFailure(String, Class, String, Model)
     */
    private void assertCommandSuccess(String inputCommand, String expectedMessage,
            Model expectedModel) throws CommandException, ParseException {
        CommandResult result = logic.execute(inputCommand);
        assertEquals(expectedMessage, result.getFeedbackToUser());
        assertEquals(expectedModel, model);
    }

    /**
     * Executes the command, confirms that a ParseException is thrown and that the result message is correct.
     * @see #assertCommandFailure(String, Class, String, Model)
     */
    private void assertParseException(String inputCommand, String expectedMessage) {
        assertCommandFailure(inputCommand, ParseException.class, expectedMessage);
    }

    /**
     * Executes the command, confirms that a CommandException is thrown and that the result message is correct.
     * @see #assertCommandFailure(String, Class, String, Model)
     */
    private void assertCommandException(String inputCommand, String expectedMessage) {
        assertCommandFailure(inputCommand, CommandException.class, expectedMessage);
    }

    /**
     * Executes the command, confirms that the exception is thrown and that the result message is correct.
     * @see #assertCommandFailure(String, Class, String, Model)
     */
    private void assertCommandFailure(String inputCommand, Class<? extends Throwable> expectedException,
            String expectedMessage) {
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        assertCommandFailure(inputCommand, expectedException, expectedMessage, expectedModel);
    }

    /**
     * Executes the command and confirms that
     * - the {@code expectedException} is thrown <br>
     * - the resulting error message is equal to {@code expectedMessage} <br>
     * - the internal model manager state is the same as that in {@code expectedModel} <br>
     * @see #assertCommandSuccess(String, String, Model)
     */
    private void assertCommandFailure(String inputCommand, Class<? extends Throwable> expectedException,
            String expectedMessage, Model expectedModel) {
        assertThrows(expectedException, expectedMessage, () -> logic.execute(inputCommand));
        assertEquals(expectedModel, model);
    }

    /**
     * Tests the Logic component's handling of an {@code IOException} thrown by the Storage component.
     *
     * @param e the exception to be thrown by the Storage component
     * @param expectedMessage the message expected inside exception thrown by the Logic component
     */
    private void assertCommandFailureForExceptionFromStorage(IOException e, String expectedMessage) {
        Path prefPath = temporaryFolder.resolve("ExceptionUserPrefs.json");

        // Inject LogicManager with an AddressBookStorage that throws the IOException e when saving
        JsonAddressBookStorage addressBookStorage = new JsonAddressBookStorage(prefPath) {
            @Override
            public void saveAddressBook(ReadOnlyAddressBook addressBook, Path filePath)
                    throws IOException {
                throw e;
            }
        };

        JsonUserPrefsStorage userPrefsStorage =
                new JsonUserPrefsStorage(temporaryFolder.resolve("ExceptionUserPrefs.json"));
        CommandHistoryStorage commandHistoryStorage =
                new FileCommandHistoryStorage(temporaryFolder.resolve("ExceptionCommandHistory.txt"));
        StorageManager storage = new StorageManager(addressBookStorage, userPrefsStorage, commandHistoryStorage);
        CommandHistory commandHistory = new CommandHistory(500, commandHistoryStorage);

        logic = new LogicManager(model, storage, commandHistory);

        // Triggers the saveAddressBook method by executing an add command
        String addCommand = AddCommand.COMMAND_WORD + NAME_DESC_AMY + PHONE_DESC_AMY
                + EMAIL_DESC_AMY + ADDRESS_DESC_AMY + HANDLE_DESC_AMY + CLOSENESS_DESC_HIGH;
        Person expectedPerson = new PersonBuilder(AMY).withTags().build();
        ModelManager expectedModel = new ModelManager();
        expectedModel.addPerson(expectedPerson);
        assertCommandFailure(addCommand, CommandException.class, expectedMessage, expectedModel);
    }
}
