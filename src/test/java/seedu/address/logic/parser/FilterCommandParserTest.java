package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.FilterCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.TagContainsKeywordPredicate;
import seedu.address.model.tag.Tag;

public class FilterCommandParserTest {

    private FilterCommandParser parser = new FilterCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        // No prefixes
        assertParseFailure(parser, "     ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE));

        // Empty prefix value
        assertParseFailure(parser, " t/", Tag.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_preambleNotEmpty_throwsParseException() {
        // Preamble present
        assertParseFailure(parser, " 1 t/friends",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidTagValue_throwsParseException() {
        // Invalid tag name
        assertParseFailure(parser, " t/friends!", Tag.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_validSingleTag_returnsFilterCommand() throws ParseException {
        // --- This is the test from your screenshot, now updated ---

        // 1. Create the expected Set<Tag>
        Set<Tag> expectedTagSet = ParserUtil.parseTags(Collections.singletonList("friends"));

        // 2. Create the expected Command
        FilterCommand expectedFilterCommand =
                new FilterCommand(new TagContainsKeywordPredicate(expectedTagSet));

        // 3. Test parsing with a single tag
        assertParseSuccess(parser, " t/friends", expectedFilterCommand);

        // 4. Test with extra whitespace
        assertParseSuccess(parser, " \n t/friends \t", expectedFilterCommand);
    }

    @Test
    public void parse_validMultipleTags_returnsFilterCommand() throws ParseException {
        // --- This is a new test for your multi-tag feature ---

        // 1. Create the expected Set<Tag>
        Set<Tag> expectedTagSet = ParserUtil.parseTags(Arrays.asList("friends", "owesMoney"));

        // 2. Create the expected Command
        FilterCommand expectedFilterCommand =
                new FilterCommand(new TagContainsKeywordPredicate(expectedTagSet));

        // 3. Test parsing with multiple tags
        assertParseSuccess(parser, " t/friends t/owesMoney", expectedFilterCommand);

        // 4. Test with extra whitespace and different order
        assertParseSuccess(parser, " \t t/owesMoney \n t/friends \t", expectedFilterCommand);
    }
}
