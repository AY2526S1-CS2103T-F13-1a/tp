package seedu.address.storage;

//import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class CommandHistoryTest {

    @TempDir Path tmp;

    @Test
    void pushTrimsIgnoresBlankAndNoConsecutiveDup() throws Exception {
        Path store = tmp.resolve("hist.txt");
        CommandHistory h = new CommandHistory(10, store);

        h.push("  ls  ");
        h.push("ls");
        h.push("   ");
        h.push("cd src");

        h.beginNavigation("");
        assertEquals(Optional.of("cd src"), h.up());
        assertEquals(Optional.of("ls"), h.up());

        var lines = Files.readAllLines(store); // oldest->newest in file
        assertEquals(2, lines.size());
        assertEquals("ls", lines.get(0));
        assertEquals("cd src", lines.get(1));
    }

    @Test
    void sizeCap_evictsOldest() {
        CommandHistory h = new CommandHistory(3, null);
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
        CommandHistory h = new CommandHistory(10, null);
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
        CommandHistory h = new CommandHistory(10, null);
        assertEquals(Optional.empty(), h.up());

        h.beginNavigation("typing...");
        assertEquals(Optional.of("typing..."), h.down());
    }
}
