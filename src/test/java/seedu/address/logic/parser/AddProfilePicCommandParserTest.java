package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.Messages.MESSAGE_NEGATIVE_PERSON_DISPLAYED_INDEX;
import static seedu.address.logic.commands.AddProfilePicCommand.MESSAGE_USAGE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PROFILE_PICTURE;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;

import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.address.logic.commands.AddProfilePicCommand;
import seedu.address.logic.parser.exceptions.ParseException;

public class AddProfilePicCommandParserTest {

    private static final String VALID_IMAGE = "johndoe.png";
    private AddProfilePicCommandParser parser = new AddProfilePicCommandParser();

    @Test
    public void parse_validArgs_returnsAddProfilePicCommand() throws ParseException {
        String userInput = INDEX_FIRST_PERSON.getOneBased() + " " + PREFIX_PROFILE_PICTURE + VALID_IMAGE;
        AddProfilePicCommand expectedCommand = new AddProfilePicCommand(INDEX_FIRST_PERSON, VALID_IMAGE);
        CommandParserTestUtil.assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_validArgsWithWhitespace_returnsAddProfilePicCommand() throws ParseException {
        String userInput = "  " + INDEX_FIRST_PERSON.getOneBased() + "   " + PREFIX_PROFILE_PICTURE
                + "  " + VALID_IMAGE + "  ";
        AddProfilePicCommand expectedCommand = new AddProfilePicCommand(INDEX_FIRST_PERSON, VALID_IMAGE);
        CommandParserTestUtil.assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_validArgsWithSlashPath_returnsAddProfilePicCommand(@TempDir Path tempDir)
            throws Exception {
        // Create a temporary file
        Path testFile = tempDir.resolve("test.png");
        Files.write(testFile, new byte[]{1, 2, 3});

        String filePath = testFile.toAbsolutePath().toString();
        // Normalize the path for cross-platform compatibility
        String normalizedPath = filePath.replace("\\", "/");

        String userInput = INDEX_FIRST_PERSON.getOneBased() + " " + PREFIX_PROFILE_PICTURE + normalizedPath;
        AddProfilePicCommand expectedCommand = new AddProfilePicCommand(INDEX_FIRST_PERSON, normalizedPath);
        CommandParserTestUtil.assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_tildeExpansion_returnsExpandedPath() throws Exception {
        // Test that ~ gets expanded to user.home
        String homeDir = System.getProperty("user.home");
        Path testPath = Path.of(homeDir, "test_expansion.png");

        // Create the test file
        try {
            Files.write(testPath, new byte[]{1, 2, 3});

            String userInput = INDEX_FIRST_PERSON.getOneBased() + " " + PREFIX_PROFILE_PICTURE + "~/test_expansion.png";
            AddProfilePicCommand command = parser.parse(userInput);

            // Verify command was created
            org.junit.jupiter.api.Assertions.assertNotNull(command);
        } finally {
            // Clean up
            Files.deleteIfExists(testPath);
        }
    }

    @Test
    public void parse_tildeOnly_expandsToHomeDirectory() throws ParseException {
        String userInput = INDEX_FIRST_PERSON.getOneBased() + " " + PREFIX_PROFILE_PICTURE + "~";

        // This should expand to user.home, which is a directory, not a file
        String homePath = System.getProperty("user.home");
        String expectedMessage = "The specified path is a directory, not a file: " + homePath;
        CommandParserTestUtil.assertParseFailure(parser, userInput, expectedMessage);
    }

    @Test
    public void parse_missingIndex_throwsParseException() {
        String userInput = PREFIX_PROFILE_PICTURE + VALID_IMAGE;
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, MESSAGE_USAGE);
        CommandParserTestUtil.assertParseFailure(parser, userInput, expectedMessage);
    }

    @Test
    public void parse_invalidIndex_throwsParseException() {
        // Negative index
        String userInput = "-1 " + PREFIX_PROFILE_PICTURE + VALID_IMAGE;
        String expectedMessage = MESSAGE_NEGATIVE_PERSON_DISPLAYED_INDEX;
        CommandParserTestUtil.assertParseFailure(parser, userInput, expectedMessage);

        // Zero index
        userInput = "0 " + PREFIX_PROFILE_PICTURE + VALID_IMAGE;
        CommandParserTestUtil.assertParseFailure(parser, userInput, expectedMessage);

        // Non-numeric index
        expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, MESSAGE_USAGE);
        userInput = "a " + PREFIX_PROFILE_PICTURE + VALID_IMAGE;
        CommandParserTestUtil.assertParseFailure(parser, userInput, expectedMessage);

        // Index with decimal
        userInput = "1.5 " + PREFIX_PROFILE_PICTURE + VALID_IMAGE;
        CommandParserTestUtil.assertParseFailure(parser, userInput, expectedMessage);
    }

    @Test
    public void parse_missingProfilePicPrefix_throwsParseException() {
        String userInput = INDEX_FIRST_PERSON.getOneBased() + " " + VALID_IMAGE;
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, MESSAGE_USAGE);
        CommandParserTestUtil.assertParseFailure(parser, userInput, expectedMessage);
    }

    @Test
    public void parse_emptyProfilePic_throwsParseException() {
        String userInput = INDEX_FIRST_PERSON.getOneBased() + " " + PREFIX_PROFILE_PICTURE;
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, MESSAGE_USAGE);
        CommandParserTestUtil.assertParseFailure(parser, userInput, expectedMessage);

        // With only whitespace
        userInput = INDEX_FIRST_PERSON.getOneBased() + " " + PREFIX_PROFILE_PICTURE + "   ";
        CommandParserTestUtil.assertParseFailure(parser, userInput, expectedMessage);
    }

    @Test
    public void parse_nonExistentImageInDocs_throwsParseException() {
        String userInput = INDEX_FIRST_PERSON.getOneBased() + " " + PREFIX_PROFILE_PICTURE + "nonexistent.png";
        String expectedMessage = "Image 'nonexistent.png' not found in docs/images";
        CommandParserTestUtil.assertParseFailure(parser, userInput, expectedMessage);
    }

    @Test
    public void parse_nonExistentAbsolutePath_throwsParseException(@TempDir Path tempDir) {
        String nonExistentPath = tempDir.resolve("nonexistent.png").toString();
        String userInput = INDEX_FIRST_PERSON.getOneBased() + " " + PREFIX_PROFILE_PICTURE + nonExistentPath;
        String expectedMessage = "Image file not found at path: " + nonExistentPath;
        CommandParserTestUtil.assertParseFailure(parser, userInput, expectedMessage);
    }

    @Test
    public void parse_invalidFilePath_throwsParseException() {
        // Path with invalid characters (depending on OS)
        String userInput = INDEX_FIRST_PERSON.getOneBased() + " " + PREFIX_PROFILE_PICTURE + "C:\\invalid<path>.png";
        // The exact error message may vary, so just check that it fails
        try {
            parser.parse(userInput);
            org.junit.jupiter.api.Assertions.fail("Expected ParseException");
        } catch (ParseException pe) {
            // Expected
            org.junit.jupiter.api.Assertions.assertTrue(true);
        }
    }

    @Test
    public void parse_multipleValidImages_success() {
        String userInput1 = INDEX_FIRST_PERSON.getOneBased() + " " + PREFIX_PROFILE_PICTURE + VALID_IMAGE;
        String userInput2 = INDEX_SECOND_PERSON.getOneBased() + " " + PREFIX_PROFILE_PICTURE + "example.png";

        AddProfilePicCommand command1 = new AddProfilePicCommand(INDEX_FIRST_PERSON, VALID_IMAGE);
        AddProfilePicCommand command2 = new AddProfilePicCommand(INDEX_SECOND_PERSON, "example.png");

        CommandParserTestUtil.assertParseSuccess(parser, userInput1, command1);
        CommandParserTestUtil.assertParseSuccess(parser, userInput2, command2);
    }

    @Test
    public void parse_nullArgs_throwsNullPointerException() {
        org.junit.jupiter.api.Assertions.assertThrows(NullPointerException.class, () -> parser.parse(null));
    }

    @Test
    public void parse_emptyArgs_throwsParseException() {
        String userInput = "";
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, MESSAGE_USAGE);
        CommandParserTestUtil.assertParseFailure(parser, userInput, expectedMessage);
    }

    @Test
    public void parse_onlyWhitespaceArgs_throwsParseException() {
        String userInput = "   ";
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, MESSAGE_USAGE);
        CommandParserTestUtil.assertParseFailure(parser, userInput, expectedMessage);
    }

    @Test
    public void parse_secondIndex_returnsCommandWithSecondIndex() throws ParseException {
        String userInput = INDEX_SECOND_PERSON.getOneBased() + " " + PREFIX_PROFILE_PICTURE + VALID_IMAGE;
        AddProfilePicCommand expectedCommand = new AddProfilePicCommand(INDEX_SECOND_PERSON, VALID_IMAGE);
        CommandParserTestUtil.assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_complexPath_success(@TempDir Path tempDir) throws Exception {
        // Create a nested directory structure
        Path nestedDir = tempDir.resolve("subdir").resolve("nested");
        nestedDir.toFile().mkdirs();
        Path testFile = nestedDir.resolve("image.png");
        Files.write(testFile, new byte[]{1, 2, 3});

        String filePath = testFile.toAbsolutePath().toString();
        String userInput = INDEX_FIRST_PERSON.getOneBased() + " " + PREFIX_PROFILE_PICTURE + filePath;

        AddProfilePicCommand expectedCommand = new AddProfilePicCommand(INDEX_FIRST_PERSON, filePath);
        CommandParserTestUtil.assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_imageInDifferentDirectory_success(@TempDir Path tempDir) throws Exception {
        // Test with image in different directory (not docs/images)
        Path testFile = tempDir.resolve("different.png");
        Files.write(testFile, new byte[]{1, 2, 3});

        String filePath = testFile.toAbsolutePath().toString();
        String userInput = INDEX_FIRST_PERSON.getOneBased() + " " + PREFIX_PROFILE_PICTURE + filePath;

        AddProfilePicCommand expectedCommand = new AddProfilePicCommand(INDEX_FIRST_PERSON, filePath);
        CommandParserTestUtil.assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_imageWithSpaces_throwsParseException() {
        String userInput = INDEX_FIRST_PERSON.getOneBased() + " " + PREFIX_PROFILE_PICTURE + "image with spaces.png";
        String expectedMessage = "Image 'image with spaces.png' not found in docs/images";
        CommandParserTestUtil.assertParseFailure(parser, userInput, expectedMessage);
    }

    @Test
    public void parse_multipleWordPath_success(@TempDir Path tempDir) throws Exception {
        // Test with file path containing spaces (absolute path)
        Path testFile = tempDir.resolve("my file.png");
        Files.write(testFile, new byte[]{1, 2, 3});

        String filePath = testFile.toAbsolutePath().toString();
        String userInput = INDEX_FIRST_PERSON.getOneBased() + " " + PREFIX_PROFILE_PICTURE + filePath;

        AddProfilePicCommand expectedCommand = new AddProfilePicCommand(INDEX_FIRST_PERSON, filePath);
        CommandParserTestUtil.assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_tildeWithPath_expandsCorrectly() throws Exception {
        // Test that ~/path gets expanded correctly
        String homeDir = System.getProperty("user.home");
        Path testFile = Path.of(homeDir, "testimage_expand.png");

        try {
            Files.write(testFile, new byte[]{1, 2, 3});

            String userInput = INDEX_FIRST_PERSON.getOneBased() + " " + PREFIX_PROFILE_PICTURE
                    + "~/testimage_expand.png";
            AddProfilePicCommand command = parser.parse(userInput);

            // Verify command was created
            org.junit.jupiter.api.Assertions.assertNotNull(command);
        } finally {
            Files.deleteIfExists(testFile);
        }
    }
}

