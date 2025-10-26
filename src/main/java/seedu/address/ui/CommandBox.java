package seedu.address.ui;


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
import seedu.address.logic.commands.CommandHints;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.storage.CommandHistory;


/**
 * The UI component that is responsible for receiving user command inputs.
 */
public class CommandBox extends UiPart<Region> {

    public static final String ERROR_STYLE_CLASS = "error";
    private static final String FXML = "CommandBox.fxml";

    private final CommandExecutor commandExecutor;
    private final CommandHistory history;

    private final ContextMenu suggestions = new ContextMenu();
    private int highlightIndex = -1;

    @FXML
    private TextField commandTextField;

    /**
     * Creates a {@code CommandBox} with the given {@code CommandExecutor}.
     */
    public CommandBox(CommandExecutor commandExecutor, CommandHistory history) {
        super(FXML);
        this.commandExecutor = commandExecutor;
        this.history = history;
        // calls #setStyleToDefault() whenever there is a change to the text of the command box.
        commandTextField.textProperty().addListener((unused1, unused2, unused3) -> setStyleToDefault());

        commandTextField.setOnKeyPressed(this::handleHistoryKeys);

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

    private void handleHistoryKeys(javafx.scene.input.KeyEvent ev) {
        switch (ev.getCode()) {
        case UP -> {
            navigateHistory(true);
            ev.consume();
        }
        case DOWN -> {
            navigateHistory(false);
            ev.consume();
        }
        default -> {
            // ignore other keys pressed by the user
        }
        }
    }
    private void navigateHistory(boolean older) {
        String current = commandTextField.getText();
        history.beginNavigation(current); (
                older ? history.up() : history.down()).ifPresent(this::applyHistory);
    }
    /**
     * Sets the command box to show the requested text
     * @param txt text to be displayed
     */
    private void applyHistory(String txt) {
        commandTextField.setText(txt);
        commandTextField.positionCaret(txt.length());
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
            history.push(commandText);
            commandExecutor.execute(commandText);
            commandTextField.setText("");
            history.resetNav();
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

    private void updateSuggestions(String prefix) {
        if (prefix.isEmpty()) {
            suggestions.hide();
            return;
        }
        var matches = CommandHints.COMMANDS.stream()
                .filter(cmd -> cmd.startsWith(prefix.toLowerCase()))
                .limit(8)
                .toList();
        if (matches.isEmpty()) {
            suggestions.hide();
            return;
        }
        suggestions.getItems().clear();
        highlightIndex = -1;
        for (int i = 0; i < matches.size(); i++) {
            final int index = i;
            String text = matches.get(i);

            Label label = new Label(text);
            label.setMaxWidth(Double.MAX_VALUE);
            var item = new CustomMenuItem(label, true);

            item.setOnAction(e -> acceptSuggestion(text));
            label.setOnMouseEntered(e -> setHighlight(index));
            suggestions.getItems().add(item);
        }
        if (!suggestions.isShowing()) {
            suggestions.show(commandTextField, Side.BOTTOM, 0, 0);
        }
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
        if (!suggestions.isShowing() || suggestions.getItems().isEmpty()) {
            if (e.getCode() == KeyCode.TAB) {
                e.consume();
            }
            return;
        }

        switch (e.getCode()) {
        case TAB:
        case ENTER:
            e.consume();
            if (highlightIndex < 0) {
                setHighlight(0);
            }
            String chosen = ((Label) ((CustomMenuItem) suggestions.getItems().get(highlightIndex)).getContent())
                    .getText();
            acceptSuggestion(chosen);
            break;
        case DOWN:
            e.consume();
            int next = (highlightIndex + 1) % suggestions.getItems().size();
            setHighlight(next);
            break;
        case UP:
            e.consume();
            int prev = (highlightIndex - 1 + suggestions.getItems().size()) % suggestions.getItems().size();
            setHighlight(prev);
            break;
        case ESCAPE:
            e.consume();
            suggestions.hide();
            break;
        default:
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
