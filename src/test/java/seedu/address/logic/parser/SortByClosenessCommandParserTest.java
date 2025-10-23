package seedu.address.logic.parser;

import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.SortByClosenessCommand;

public class SortByClosenessCommandParserTest {
    private SortByClosenessCommandParser parser = new SortByClosenessCommandParser();

    @Test
    public void parse_validArgs_returnsSortByClosenessCommand() {
        assertParseSuccess(parser, "asc", new SortByClosenessCommand(SortByClosenessCommand.SortOrder.ASCENDING));
        assertParseSuccess(parser, "desc", new SortByClosenessCommand(SortByClosenessCommand.SortOrder.DESCENDING));
    }

    @Test
    public void parse_invalidValue_throwsParseException() {
        // Test for invalid order
        String invalidOrder = "invalid";
        String expectedMessage = SortByClosenessCommand.MESSAGE_INVALID_ORDER;
        assertParseFailure(parser, invalidOrder, expectedMessage);

        // Test for empty order
        String emptyInput = "   ";
        assertParseFailure(parser, emptyInput, expectedMessage);
    }
}
