package seedu.address.logic.parser;

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
        if (trimmedArgs.equals("asc")) {
            return new SortByClosenessCommand(SortByClosenessCommand.SortOrder.ASCENDING);
        } else if (trimmedArgs.equals("desc")) {
            return new SortByClosenessCommand(SortByClosenessCommand.SortOrder.DESCENDING);
        } else {
            throw new ParseException(SortByClosenessCommand.MESSAGE_INVALID_ORDER);
        }
    }
}
