package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PROFILE_PICTURE;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.AddProfilePicCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new AddProfilePicCommand object
 */
public class AddProfilePicCommandParser implements Parser<AddProfilePicCommand> {

    @Override
    public AddProfilePicCommand parse(String args) throws ParseException {
        requireNonNull(args);

        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_PROFILE_PICTURE);

        Index index;
        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    AddProfilePicCommand.MESSAGE_USAGE), pe);
        }

        if (!argMultimap.getValue(PREFIX_PROFILE_PICTURE).isPresent()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    AddProfilePicCommand.MESSAGE_USAGE));
        }

        String pp = argMultimap.getValue(PREFIX_PROFILE_PICTURE).get().trim();
        String expandedPp = expandTilde(pp);
        validatePicturePath(expandedPp);
        return new AddProfilePicCommand(index, expandedPp);
    }

    private void validatePicturePath(String pp) throws ParseException {
        if (pp.isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    AddProfilePicCommand.MESSAGE_USAGE));
        }

        if (!pp.contains("/") && !pp.contains("\\")) {
            java.nio.file.Path candidate = java.nio.file.Paths.get(System.getProperty("user.dir"),
                    "docs", "images", pp);
            if (!java.nio.file.Files.exists(candidate)) {
                throw new ParseException("Image '" + pp + "' not found in docs/images");
            }
        } else {
            java.nio.file.Path candidate;
            try {
                candidate = java.nio.file.Paths.get(pp);
            } catch (Exception e) {
                throw new ParseException("Invalid file path: " + pp);
            }
            if (!java.nio.file.Files.exists(candidate)) {
                throw new ParseException("Image file not found at path: " + candidate.toString());
            }
        }
    }

    /**
     * Expands tilde (~) to the user's home directory path.
     * This method is used to read the image from the user's home directory.
     * @param path The path that may contain tilde
     * @return The expanded path
     */
    private String expandTilde(String path) {
        if (path.startsWith("~/")) {
            return System.getProperty("user.home") + "/" + path.substring(2);
        } else if (path.equals("~")) {
            return System.getProperty("user.home");
        }
        return path;
    }
}


