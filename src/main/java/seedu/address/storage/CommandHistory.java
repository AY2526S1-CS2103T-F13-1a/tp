package seedu.address.storage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.Optional;

/**
 * Methods in this class are called to return the previous commands.
 * Commands are stored into the storage Path object
 */
public class CommandHistory {
    private final Deque<String> entries = new ArrayDeque<>();
    private final int maxSize;
    private int index = -1;
    private String snapshotBeforeNav = "";
    private final Path store;

    /**
     * Creates a new CommandHistory object
     * @param maxSize the maximum number of lines stored
     * @param store the file to save the entries in
     */
    public CommandHistory(int maxSize, Path store) {
        this.maxSize = Math.max(1, maxSize);
        this.store = store;
        load();
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
        snapshotBeforeNav = "";
    }

    /**
     * Loads the stored commands into the entries deque
     */
    private void load() {
        if (store == null) {
            return;
        }
        try {
            if (Files.exists(store)) {
                List<String> lines = Files.readAllLines(store);
                for (int i = lines.size() - 1; i >= 0; i--) {
                    String l = lines.get(i).strip();
                    if (!l.isEmpty()) {
                        entries.addFirst(l);
                    }
                }
            }
        } catch (IOException ignored) {
            // No op
        }
    }

    /**
     * Saves the updated entries into the storage
     */
    public void save() {
        if (store == null) {
            return;
        }
        try {
            Files.createDirectories(store.getParent());
            List<String> oldestFirst = new ArrayList<>(entries);
            Collections.reverse(oldestFirst);
            Files.write(store, oldestFirst, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException ignored) {
            // No op
        }
    }
}
