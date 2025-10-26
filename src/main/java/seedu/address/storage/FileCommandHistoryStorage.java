package seedu.address.storage;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Optional;


/**
 * A file-backed implementation of {@link CommandHistoryStorage}
 */
public class FileCommandHistoryStorage implements CommandHistoryStorage {
    private final Path filePath;

    public FileCommandHistoryStorage(Path filePath) {
        this.filePath = requireNonNull(filePath);
    }

    @Override
    public Path getCommandHistoryFilePath() {
        return filePath;
    }

    @Override
    public Optional<List<String>> readCommandHistory() throws IOException {
        return readCommandHistory(filePath);
    }

    @Override
    public Optional<List<String>> readCommandHistory(Path path) throws IOException {
        requireNonNull(path);
        if (!Files.exists(path)) {
            return Optional.empty();
        }
        List<String> lines = Files.readAllLines(path);
        return Optional.of(lines);
    }

    @Override
    public void saveCommandHistory(List<String> oldestFirst) throws IOException {
        requireNonNull(oldestFirst);
        Files.createDirectories(filePath.getParent());
        Files.write(filePath, oldestFirst,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING);
    }
}
