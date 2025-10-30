package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.FileUtil;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Address;
import seedu.address.model.person.Closeness;
import seedu.address.model.person.Email;
import seedu.address.model.person.Handle;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.tag.Tag;

/**
 * Adds or updates the profile picture of an existing person.
 */
public class AddProfilePicCommand extends Command {

    public static final String COMMAND_WORD = "addProfilePic";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds/updates the profile picture of the person "
            + "identified by the index number used in the displayed person list.\n"
            + "Only .png image files are supported.\n"
            + "Example (Upload a local image file, from your directory): " + COMMAND_WORD
            + " 1 pp/~/Downloads/myphoto.png\n"
            + "Example (Upload an existing picture): " + COMMAND_WORD + " 1 pp/johndoe.png\n";

    public static final String MESSAGE_SUCCESS = "Updated profile picture for: %1$s";

    private final Index index;
    private final String profilePicture;

    /**
     * Creates an AddProfilePicCommand to add/update the profile picture of the person at the given index.
     * @param index the index of the person in the displayed person list.
     * @param profilePicture the path to the profile picture.
     */
    public AddProfilePicCommand(Index index, String profilePicture) {
        requireNonNull(index);
        this.index = index;
        this.profilePicture = profilePicture == null ? "" : profilePicture;
    }

    /**
     * Executes the AddProfilePicCommand and returns the result message.
     * @param model {@code Model} which the command should operate on.
     * @return feedback message of the operation result for display
     * @throws CommandException if an error occurs during execution.
     */
    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getSortedPersonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToEdit = lastShownList.get(index.getZeroBased());

        Name name = personToEdit.getName();
        Phone phone = personToEdit.getPhone();
        Email email = personToEdit.getEmail();
        Address address = personToEdit.getAddress();
        java.util.Set<Tag> tags = personToEdit.getTags();
        Handle handle = personToEdit.getHandle();
        Closeness closeness = personToEdit.getCloseness();

        String finalProfilePicture = profilePicture;
        if (!profilePicture.isEmpty() && (profilePicture.contains("/") || profilePicture.contains("\\"))) {
            try {
                finalProfilePicture = copyLocalImageToImagesDirectory(profilePicture);
            } catch (IOException e) {
                throw new CommandException("Failed to copy image file: " + e.getMessage());
            } catch (IllegalArgumentException e) {
                throw new CommandException("Invalid filename: " + e.getMessage());
            }
        }

        if (!finalProfilePicture.isEmpty() && !isPngFileName(finalProfilePicture)) {
            throw new CommandException("Only .png images are supported.");
        }

        Person edited = finalProfilePicture.isEmpty()
                ? new Person(name, phone, email, address, tags, handle, closeness)
                : new Person(name, phone, email, address, tags, handle, finalProfilePicture, closeness);

        model.setPerson(personToEdit, edited);

        return new CommandResult(String.format(MESSAGE_SUCCESS, edited.getName().fullName));
    }

    /**
     * Copies a local image file to the docs/images directory and returns the filename.
     * @param localImagePath The path to the local image file
     * @return The filename of the copied image in the docs/images directory
     * @throws IOException if the copy operation fails
     */
    private String copyLocalImageToImagesDirectory(String localImagePath) throws IOException {

        Path sourcePath = Paths.get(localImagePath);
        String originalFileName = sourcePath.getFileName().toString();
        if (!isPngFileName(originalFileName)) {
            throw new IllegalArgumentException("Only .png files are supported.");
        }
        String fileName = generateUniqueFilename(originalFileName);

        Path imagesDir = Paths.get(System.getProperty("user.dir"), "docs", "images");
        Path destinationPath = imagesDir.resolve(fileName);

        FileUtil.copyFile(sourcePath, destinationPath);
        return fileName;
    }

    /**
     * Generates a unique filename in the docs/images directory to avoid conflicts.
     * If the original filename already exists, appends a number to make it unique.
     * This method is used in copyLocalImageToImagesDirectory to avoid conflicts.
     * @param originalFileName The original filename
     * @return A unique filename that doesn't exist in docs/images
     */
    private String generateUniqueFilename(String originalFileName) {
        String fileName = originalFileName;

        Path imagesDir = Paths.get(System.getProperty("user.dir"), "docs", "images");
        Path destinationPath = imagesDir.resolve(fileName);

        if (destinationPath.toFile().exists()) {
            throw new IllegalArgumentException("File already exists: " + originalFileName
                + " in the docs/images directory. Please add with this command instead: \n"
                + "addProfilePic <index> pp/<image name> \n"
                + "Example: addProfilePic 1 pp/example.png");
        }
        return fileName;
    }

    private boolean isPngFileName(String fileName) {
        return fileName != null && fileName.toLowerCase().endsWith(".png");
    }

    /**
     * Returns true if both AddProfilePicCommands have the same index and profile picture.
     * @param other the other object to compare with.
     * @return true if both AddProfilePicCommands have the same index and profile picture, false otherwise.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof AddProfilePicCommand)) {
            return false;
        }
        AddProfilePicCommand o = (AddProfilePicCommand) other;
        return index.equals(o.index)
                && profilePicture.equals(o.profilePicture);
    }
}


