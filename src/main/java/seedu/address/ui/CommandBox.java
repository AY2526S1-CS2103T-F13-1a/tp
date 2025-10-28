package seedu.address.ui;

import java.util.List;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Region;
import seedu.address.logic.Logic;
import seedu.address.logic.commands.CommandHints;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.exceptions.ParseException;


/**
 * The UI component that is responsible for receiving user command inputs.
 */
public class CommandBox extends UiPart<Region> {

    public static final String ERROR_STYLE_CLASS = "error";
    private static final String FXML = "CommandBox.fxml";

    private final CommandExecutor commandExecutor;

    private final ContextMenu suggestions = new ContextMenu();
    private int highlightIndex = -1;

    @FXML
    private TextField commandTextField;

    /**
     * Creates a {@code CommandBox} with the given {@code CommandExecutor}.
     */
    public CommandBox(CommandExecutor commandExecutor) {
        super(FXML);
        this.commandExecutor = commandExecutor;
        // calls #setStyleToDefault() whenever there is a change to the text of the command box.
        commandTextField.textProperty().addListener((unused1, unused2, unused3) -> setStyleToDefault());

        commandTextField.textProperty().addListener((obs, oldText, newText) -> {
            updateSuggestions(newText.trim());
        });

        commandTextField.addEventFilter(KeyEvent.KEY_PRESSED, this::handleKeyPressed);

        commandTextField.focusedProperty().addListener((o, was, isNow) -> {
            if (!isNow) {
                suggestions.hide();
            }
        });
    }

    public String getCommandText() {
        return commandTextField.getText();
    }
    /**
     * Sets the command box to show the requested text
     * @param txt text to be displayed
     */
    public void showText(String txt) {
        commandTextField.setText(txt);
        commandTextField.positionCaret(txt.length());
    }

    /**
     * Passes the logic object to command box and updates its contents
     * @param logic
     */
    public void installHistoryHandlers(Logic logic) {
        commandTextField.addEventFilter(KeyEvent.KEY_PRESSED, e -> {
            if (e.getCode() == KeyCode.UP || e.getCode() == KeyCode.DOWN) {
                logic.beginNavigation(commandTextField.getText()); (
                        e.getCode() == KeyCode.UP ? logic.up() : logic.down())
                        .ifPresent(s -> {
                            showText(s);
                        });
                e.consume();
            }
        });
    }
    /**
     * Handles the Enter button pressed event.
     */
    @FXML
    private void handleCommandEntered() {
        String commandText = commandTextField.getText();
        if (commandText.equals("")) {
            return;
        }

        try {
            commandExecutor.execute(commandText);
            commandTextField.setText("");
        } catch (CommandException | ParseException e) {
            setStyleToIndicateCommandFailure();
        }
    }

    /**
     * Sets the command box style to use the default style.
     */
    private void setStyleToDefault() {
        commandTextField.getStyleClass().remove(ERROR_STYLE_CLASS);
    }

    /**
     * Sets the command box style to indicate a failed command.
     */
    private void setStyleToIndicateCommandFailure() {
        ObservableList<String> styleClass = commandTextField.getStyleClass();

        if (styleClass.contains(ERROR_STYLE_CLASS)) {
            return;
        }

        styleClass.add(ERROR_STYLE_CLASS);
    }

    private void acceptSuggestion(String suggestion) {
        commandTextField.setText(suggestion + " ");
        commandTextField.positionCaret(commandTextField.getText().length());
        suggestions.hide();
    }


    private void setHighlight(int newIndex) {
        int size = suggestions.getItems().size();

        if (highlightIndex >= 0 && highlightIndex < size) {
            CustomMenuItem prevItem = (CustomMenuItem) suggestions.getItems().get(highlightIndex);
            prevItem.getContent().setStyle("");
        }

        highlightIndex = newIndex;

        if (highlightIndex >= 0 && highlightIndex < size) {
            CustomMenuItem currItem = (CustomMenuItem) suggestions.getItems().get(highlightIndex);
            currItem.getContent().setStyle("-fx-font-weight: bold;");
        }
    }

    private void handleKeyPressed(KeyEvent e) {
        if (!isMenuVisible()) {
            if (e.getCode() == KeyCode.TAB) {
                e.consume();
            }
            return;
        }
        switch (e.getCode()) {
        case TAB:
            e.consume();
            acceptHighlightedOrFirst();
            break;
        case ENTER:
            handleEnterWithMenu(e);
            break;
        case DOWN:
            e.consume();
            setHighlight((highlightIndex + 1) % suggestions.getItems().size());
            break;
        case UP:
            e.consume();
            int size = suggestions.getItems().size();
            setHighlight((highlightIndex - 1 + size) % size);
            break;
        case ESCAPE:
            e.consume();
            suggestions.hide();
            break;
        default:
        }
    }

    private void updateSuggestions(String prefix) {
        if (prefix.isEmpty()) {
            suggestions.hide();
            return;
        }
        List<String> matches = buildMatches(prefix);
        if (matches.isEmpty() || isExactSingleMatch(prefix, matches)) {
            suggestions.hide();
            return;
        }
        populateMenu(matches);
        showMenuIfNeeded();
    }

    private boolean isMenuVisible() {
        return suggestions.isShowing() && !suggestions.getItems().isEmpty();
    }

    private void handleEnterWithMenu(KeyEvent e) {
        String typed = commandTextField.getText().trim().toLowerCase();
        boolean isCommand = CommandHints.COMMANDS.stream()
                .anyMatch(c -> c.equalsIgnoreCase(typed));
        e.consume();
        if (isCommand) {
            suggestions.hide();
            handleCommandEntered();
            return;
        }
        acceptHighlightedOrFirst();
    }

    private void acceptHighlightedOrFirst() {
        if (highlightIndex < 0) {
            setHighlight(0);
        }
        CustomMenuItem item =
                (CustomMenuItem) suggestions.getItems().get(highlightIndex);
        Label label = (Label) item.getContent();
        acceptSuggestion(label.getText());
    }

    private List<String> buildMatches(String prefix) {
        String p = prefix.toLowerCase();
        return CommandHints.COMMANDS.stream()
                .filter(cmd -> cmd.toLowerCase().startsWith(p))
                .limit(8)
                .toList();
    }

    private boolean isExactSingleMatch(String prefix, List<String> matches) {
        return matches.size() == 1
                && matches.get(0).equalsIgnoreCase(prefix);
    }

    private void populateMenu(List<String> matches) {
        suggestions.getItems().clear();
        highlightIndex = -1;
        for (int i = 0; i < matches.size(); i++) {
            final int idx = i;
            String text = matches.get(i);
            Label label = new Label(text);
            label.setMaxWidth(Double.MAX_VALUE);
            CustomMenuItem item = new CustomMenuItem(label, true);
            item.setOnAction(ev -> acceptSuggestion(text));
            label.setOnMouseEntered(ev -> setHighlight(idx));
            suggestions.getItems().add(item);
        }
    }

    private void showMenuIfNeeded() {
        if (!suggestions.isShowing()) {
            suggestions.show(commandTextField, Side.BOTTOM, 0, 0);
        }
    }


    /**
     * Represents a function that can execute commands.
     */
    @FunctionalInterface
    public interface CommandExecutor {
        /**
         * Executes the command and returns the result.
         *
         * @see seedu.address.logic.Logic#execute(String)
         */
        CommandResult execute(String commandText) throws CommandException, ParseException;
    }

}
