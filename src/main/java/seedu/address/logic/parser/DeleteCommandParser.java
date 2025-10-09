package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.tag.Tag;

import java.util.ArrayList;
import java.util.List;

/**
 * Parses input arguments and creates a new DeleteCommand object
 */
public class DeleteCommandParser implements Parser<DeleteCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the DeleteCommand
     * and returns a DeleteCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public DeleteCommand parse(String args) throws ParseException {

        String trimmed = args.trim();
        if (trimmed.isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
        }

        // if input is "delete all <tag>"
        if (trimmed.startsWith("all")) {
            String remainder = trimmed.length() == 3 ? "" : trimmed.substring(3);
            ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(remainder, PREFIX_TAG);

            // ensure /t tag is present and there is no preamble
            if (!argMultimap.getValue(PREFIX_TAG).isPresent() || !argMultimap.getPreamble().isEmpty()) {
                throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
            }

            Tag tag = ParserUtil.parseTag(argMultimap.getValue(PREFIX_TAG).get());
            return new DeleteCommand(tag);
        }

        // parse tokens into list of target indexes
        String[] tokens = trimmed.split("\\s+");
        List<Index> indexes = new ArrayList<Index>(tokens.length);

        try {
            for (String token : tokens) {
                indexes.add(ParserUtil.parseIndex(token));
            }
        } catch (ParseException pe) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE), pe);
        }

        // return delete command with single target index
        if (indexes.size() == 1) {
            return new DeleteCommand(indexes.get(0));
        }
        // return delete command with list of target indexes
        return new DeleteCommand(indexes);
    }

}
