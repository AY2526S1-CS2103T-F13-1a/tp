package seedu.address.storage;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import seedu.address.MainApp;
import seedu.address.commons.core.LogsCenter;


/**
 * Methods in this class are called to return the previous commands.
 * Commands are stored into the storage Path object
 */
public class CommandHistory {
    private static final Logger logger = LogsCenter.getLogger(MainApp.class);
    private final Deque<String> entries = new ArrayDeque<>();
    private final int maxSize;
    private final CommandHistoryStorage storage;
    private int index = -1;
    private String snapshotBeforeNav = "";

    /**
     * Initiates the CommandHistory object
     * @param storage wrapper of the file it is going to store in
     */
    public CommandHistory(int maxSize, CommandHistoryStorage storage) {
        this.maxSize = Math.max(1, maxSize);
        this.storage = storage;
        try {
            List<String> oldestFirst = storage.readCommandHistory().orElse(List.of());
            for (String cmd : oldestFirst) {
                String s = cmd.strip();
                if (!s.isEmpty()) {
                    entries.addFirst(s); // newest ends up at front
                }
            }
        } catch (IOException e) {
            logger.info("File to store command history does not exist");
        }
    }

    /**
     * Adds a command to history, disallows consecutive duplicates
     */
    public void push(String cmd) {
        String s = (cmd == null ? "" : cmd.strip());
        if (s.isEmpty()) {
            return;
        }
        if (!entries.isEmpty() && entries.peekFirst().equals(s)) {
            return;
        }
        entries.addFirst(s);
        while (entries.size() > maxSize) {
            entries.removeLast();
        }
        save();
        resetNav();
        snapshotBeforeNav = "";
    }

    /**
     * Call this right before your first UP press handling, passing current input
     */
    public void beginNavigation(String currentInput) {
        if (index == -1) {
            snapshotBeforeNav = currentInput == null ? "" : currentInput;
            index = 0;
        }
    }

    /**
     * Go one step older. Returns Optional.empty() if no entries
     */
    public Optional<String> up() {
        if (entries.isEmpty()) {
            return Optional.empty();
        }
        if (index == -1) {
            beginNavigation("");
        }
        index = Math.min(index + 1, entries.size());
        return Optional.of(entries.stream().skip(index - 1).findFirst().orElse(snapshotBeforeNav));
    }

    /**
     * Go one step newer. When leaving history, restores the snapshot text
     */
    public Optional<String> down() {
        if (entries.isEmpty()) {
            return Optional.of(snapshotBeforeNav);
        }
        if (index <= 1) {
            String snap = snapshotBeforeNav;
            resetNav();
            return Optional.of(snap); // returns whatever was written before
        }
        index -= 1;
        return Optional.of(entries.stream().skip(index - 1).findFirst().orElse(snapshotBeforeNav));
    }

    /**
     * Reset navigation state after submitting a command
     */
    public void resetNav() {
        index = -1;
    }

    /**
     * Saves the updated entries into the storage
     */
    public void save() {
        try {
            storage.saveCommandHistory(toOldestFirstList());
        } catch (IOException e) {
            logger.info("File to store command history does not exist");
        }
    }

    private List<String> toOldestFirstList() {
        List<String> list = new ArrayList<>(entries);
        Collections.reverse(list);
        return list;
    }

}

