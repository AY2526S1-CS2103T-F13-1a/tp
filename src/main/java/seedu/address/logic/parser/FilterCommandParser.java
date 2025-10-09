package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.logic.commands.FilterCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.TagContainsKeywordPredicate;

public class FilterCommandParser implements Parser<FilterCommand> {
    /**
     * Parses the given {@code String} of arguments in the context of the FilterCommand
     * and returns a FilterCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    @Override
    public FilterCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty() || !trimmedArgs.startsWith("t/")) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE));
        }

        String keyword = trimmedArgs.substring(2).trim();
        if (keyword.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE));
        }

        return new FilterCommand(new TagContainsKeywordPredicate(keyword));
    }
}
