package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.logic.commands.SortByClosenessCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new SortByClosenessCommand object
 */
public class SortByClosenessCommandParser implements Parser<SortByClosenessCommand> {
    /**
     * Parses the given {@code String} of arguments in the context of the SortByClosenessCommand
     * and returns a SortByClosenessCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    @Override
    public SortByClosenessCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim().toLowerCase();
        if (trimmedArgs.isEmpty() || !trimmedArgs.startsWith("o/")) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, SortByClosenessCommand.MESSAGE_USAGE));
        }

        String keyword = trimmedArgs.substring(2).trim();
        if (keyword.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, SortByClosenessCommand.MESSAGE_USAGE));
        } else if (!keyword.equals("asc") && !keyword.equals("desc")) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, SortByClosenessCommand.MESSAGE_USAGE));
        } else if (keyword.equals("asc")) {
            return new SortByClosenessCommand(SortByClosenessCommand.SortOrder.ASCENDING);
        } else {
            return new SortByClosenessCommand(SortByClosenessCommand.SortOrder.DESCENDING);
        }
    }
}
