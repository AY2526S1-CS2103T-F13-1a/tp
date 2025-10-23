package seedu.address.ui;

import java.util.Comparator;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import seedu.address.model.person.Person;

/**
 * An UI component that displays information of a {@code Person}.
 */
public class PersonCard extends UiPart<Region> {

    private static final String FXML = "PersonListCard.fxml";

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The issue on AddressBook level 4</a>
     */

    public final Person person;

    @FXML
    private HBox cardPane;
    @FXML
    private Label name;
    @FXML
    private Label id;
    @FXML
    private Label phone;
    @FXML
    private Label address;
    @FXML
    private Label email;
    @FXML
    private Label handle;
    @FXML
    private Label closeness;
    @FXML
    private FlowPane tags;
    @FXML
    private ImageView avatar;

    /**
     * Creates a {@code PersonCode} with the given {@code Person} and index to display.
     */
    public PersonCard(Person person, int displayedIndex) {
        super(FXML);
        this.person = person;
        id.setText(displayedIndex + ". ");
        name.setText(person.getName().fullName);
        phone.setText(person.getPhone().value);
        address.setText(person.getAddress().value);
        email.setText(person.getEmail().value);
        loadProfileImage(person.getProfilePicture());
        person.getTags().stream()
                .sorted(Comparator.comparing(tag -> tag.tagName))
                .forEach(tag -> tags.getChildren().add(new Label(tag.tagName)));
        handle.setText(person.getHandle().teleHandle);
        closeness.setText("Closeness: " + person.getCloseness().toString() + "/5");
    }

    /**
     * Loads the profile image from docs/images for the given filename, sets a default error image on failure,
     * and shows a tooltip with a user-visible error message when loading fails.
     */
    private void loadProfileImage(String profilePicturePath) {
        if (profilePicturePath == null || profilePicturePath.isEmpty()) {
            avatar.setImage(null);
            Tooltip.uninstall(avatar, null);
            return;
        }

        Image image;
        java.nio.file.Path p = java.nio.file.Paths.get(System.getProperty("user.dir"),
                "docs", "images", profilePicturePath);
        image = new Image(p.toUri().toString());
        avatar.setImage(image);
        Tooltip.uninstall(avatar, null);
    }
}
