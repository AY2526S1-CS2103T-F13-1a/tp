package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_REMARK;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.RemarkCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Remark;

public class RemarkCommandParser implements Parser<RemarkCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of RemarkCommand
     * and returns a RemarkCommand object for execution
     * @throws ParseException if the user input does not conform the expected format
     */
    public RemarkCommand parse(String args) throws ParseException {
        requireNonNull(args); // args do not have the remark keyword, removed by AddressBookParser
        // extract the actual remark after the r/
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args,
                PREFIX_REMARK);

        Index index;
        try {
            // getPreamble extracts what is in front of the r/
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (IllegalValueException ive) {
            // there is no number or index to extract
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    RemarkCommand.MESSAGE_USAGE), ive);
        }
        // extracts the actual remark in String
        String stringRemark = argMultimap.getValue(PREFIX_REMARK).orElse("");
        // wraps the string inside the Remark object
        Remark remark = ParserUtil.parseRemark(stringRemark);
        // passes both the index and the remark to the command
        // so that the command can extract the person at the index
        // and add the remark accordingly
        return new RemarkCommand(index, remark);
    }
}
