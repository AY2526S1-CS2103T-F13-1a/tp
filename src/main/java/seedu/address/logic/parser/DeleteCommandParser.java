package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.tag.Tag;

/**
 * Parses input arguments and creates a {@link DeleteCommand}.
 *
 * Supported forms:
 * - delete INDEX [MORE_INDEXES]    (delete by displayed indices)
 * - delete all t/TAG [t/TAG]...    (delete all with any of the tags; union)
 *
 * Notes:
 * - Tags are read via PREFIX_TAG (t/); original order is preserved for messages.
 * - Indices are parsed with ParserUtil.parseIndex(...) and de-duplicated.
 * - On invalid input, throws ParseException with DeleteCommand.MESSAGE_USAGE.
 */
public class DeleteCommandParser implements Parser<DeleteCommand> {

    @Override
    public DeleteCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap map = ArgumentTokenizer.tokenize(args, PREFIX_TAG);
        String preamble = map.getPreamble().trim();

        if (isAllDelete(preamble)) {
            Set<Tag> tags = parseTags(map);
            if (tags.isEmpty()) {
                throw new ParseException(String.format(
                        MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
            }
            return new DeleteCommand(tags); // ✅ pass Set<Tag>
        }

        List<Index> indexes = parseIndexes(preamble);
        if (indexes.isEmpty()) {
            throw new ParseException(String.format(
                    MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
        }
        return new DeleteCommand(indexes);
    }

    /* ---------------- helpers (each < 30 LOC) ---------------- */

    private static boolean isAllDelete(String preamble) {
        // allow "all", or "all " followed by anything (tags are read from PREFIX_TAG)
        return preamble.equals("all") || preamble.startsWith("all ");
    }

    private static Set<Tag> parseTags(ArgumentMultimap map) throws ParseException {
        // collect all t/ values; preserve order for nicer error strings
        List<String> raw = map.getAllValues(PREFIX_TAG);
        Set<Tag> tags = new LinkedHashSet<>();
        for (String t : raw) {
            String trimmed = t.trim();
            if (trimmed.isEmpty()) {
                continue;
            }
            tags.add(new Tag(trimmed));
        }
        return tags;
    }

    private static List<Index> parseIndexes(String preamble) throws ParseException {
        if (preamble.isEmpty()) {
            return List.of();
        }
        String[] parts = preamble.split("\\s+");
        List<Index> indexes = new ArrayList<>(parts.length);
        for (String p : parts) {
            indexes.add(ParserUtil.parseIndex(p));
        }
        // remove duplicates while preserving order, then return
        return indexes.stream().distinct().collect(Collectors.toList());
    }
}
