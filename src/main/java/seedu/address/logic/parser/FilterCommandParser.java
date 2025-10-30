package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.model.tag.Tag.MESSAGE_CONSTRAINTS;
import static seedu.address.model.tag.Tag.isValidTagName;

import java.util.Collection;
import java.util.Set;

import seedu.address.logic.commands.FilterCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.TagContainsKeywordPredicate;
import seedu.address.model.tag.Tag;

/**
 * Parses input arguments and creates a new FilterCommand object
 */
public class FilterCommandParser implements Parser<FilterCommand> {
    /**
     * Parses the given {@code String} of arguments in the context of the FilterCommand
     * and returns a FilterCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    @Override
    public FilterCommand parse(String args) throws ParseException {
        ArgumentMultimap argumentMultimap = ArgumentTokenizer.tokenize(args, PREFIX_TAG);

        // Cases where preamble is not empty and the prefix tag is not detected
        if (!argumentMultimap.getPreamble().isEmpty()
            || argumentMultimap.getAllValues(PREFIX_TAG).isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE));
        }

        // This ensures no duplicate prefixes like n/ or p/ are present,
        // although filter only uses t/. We leave it empty to check all.
        argumentMultimap.verifyNoDuplicatePrefixesFor();

        Collection<String> tagKeywords = argumentMultimap.getAllValues(PREFIX_TAG);

        try {
            Set<Tag> tagSet = ParserUtil.parseTags(tagKeywords);
            return new FilterCommand(new TagContainsKeywordPredicate(tagSet));
        } catch (ParseException pe) {
            // Catch errors from parseTags (e.g., invalid tag name)
            throw new ParseException(pe.getMessage(), pe);
        }
    }
}
