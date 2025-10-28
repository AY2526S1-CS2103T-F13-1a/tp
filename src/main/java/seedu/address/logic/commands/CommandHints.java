package seedu.address.logic.commands;

import java.util.Arrays;
import java.util.List;

/**
 * List of all command words used for autocomplete and help.
 * Keep this consistent with each command class's COMMAND_WORD.
 */
public final class CommandHints {
    public static final List<String> COMMANDS = Arrays.asList(
        "add", "edit", "delete", "clear", "find", "list",
        "help", "exit", "addProfilePic", "filter", "sortByCloseness"
    );
}
