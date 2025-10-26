package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Tests for CommandHistory when wired to CommandHistoryStorage.
 */
class CommandHistoryTest {

    @TempDir Path tmp;

    @Test
    void pushTrimsIgnoresBlankAndNoConsecutiveDup() throws Exception {
        Path store = tmp.resolve("hist.txt");
        CommandHistoryStorage chStorage = new FileCommandHistoryStorage(store);
        CommandHistory h = new CommandHistory(10, chStorage);

        h.push("  ls  ");
        h.push("ls");
        h.push("   ");
        h.push("cd src");

        h.beginNavigation("");
        assertEquals(Optional.of("cd src"), h.up());
        assertEquals(Optional.of("ls"), h.up());

        // file should contain oldest->newest
        var lines = Files.readAllLines(store);
        assertEquals(2, lines.size());
        assertEquals("ls", lines.get(0));
        assertEquals("cd src", lines.get(1));
    }

    @Test
    void sizeCap_evictsOldest() {
        CommandHistory h = new CommandHistory(3, new NoopCommandHistoryStorage());

        h.push("a");
        h.push("b");
        h.push("c");
        h.push("d"); // evicts "a"

        h.beginNavigation("");
        assertEquals(Optional.of("d"), h.up());
        assertEquals(Optional.of("c"), h.up());
        assertEquals(Optional.of("b"), h.up());
        assertEquals(Optional.of("b"), h.up()); // stays at oldest
    }

    @Test
    void upDown_restoresSnapshot() {
        CommandHistory h = new CommandHistory(10, new NoopCommandHistoryStorage());
        h.push("one");
        h.push("two");
        h.push("three");

        h.beginNavigation("tw");
        assertEquals(Optional.of("three"), h.up());
        assertEquals(Optional.of("two"), h.up());
        assertEquals(Optional.of("three"), h.down());
        assertEquals(Optional.of("tw"), h.down());
    }

    @Test
    void emptyHistory_upEmpty_downRestoresSnapshot() {
        CommandHistory h = new CommandHistory(10, new NoopCommandHistoryStorage());
        assertEquals(Optional.empty(), h.up());

        h.beginNavigation("typing...");
        assertEquals(Optional.of("typing..."), h.down());
    }

    /**
     * Minimal no-op storage stub so tests don't touch the filesystem.
     * Saves are ignored; reads return empty.
     */
    private static class NoopCommandHistoryStorage implements CommandHistoryStorage {
        @Override
        public Path getCommandHistoryFilePath() {
            // Return a dummy path; not used by tests
            return Path.of(System.getProperty("java.io.tmpdir"), "noop_command_history.txt");
        }

        @Override
        public Optional<List<String>> readCommandHistory() throws IOException {
            return Optional.empty();
        }

        @Override
        public Optional<List<String>> readCommandHistory(Path filePath) throws IOException {
            return Optional.empty();
        }

        @Override
        public void saveCommandHistory(List<String> oldestFirst) throws IOException {
            // no-op
        }
    }
}
