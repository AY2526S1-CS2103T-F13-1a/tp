package seedu.address.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import seedu.address.commons.exceptions.DataLoadingException;


/**
 * Represents a storage for commands, in a list of strings
 */
public interface CommandHistoryStorage {
    /**
     * Returns the file path of the CommandHistory data file
     */
    Path getCommandHistoryFilePath();
    /**
     * Returns CommandHistory
     * Returns Optional.empty(){} if file is not found
     * @throws DataLoadingException
     */
    Optional<List<String>> readCommandHistory() throws IOException;
    /**
     * Returns CommandHistory
     * Returns Optional.empty(){} if file is not found
     * @throws DataLoadingException
     */
    Optional<List<String>> readCommandHistory(Path filePath) throws IOException;
    /**
     * Saves the list of commands into the storage
     * @param oldestFirst
     * @throws IOException
     */
    void saveCommandHistory(List<String> oldestFirst) throws IOException;

}
