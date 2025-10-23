package seedu.address.logic.commands;

import java.util.Arrays;
import java.util.List;

public final class CommandHints {
    private CommandHints() {}

    public static final List<String> COMMANDS = Arrays.asList(
        "add", "edit", "delete", "clear", "find", "list",
        "help", "exit", "addProfilePic"
    );
}
