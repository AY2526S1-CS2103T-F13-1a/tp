package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.tag.Tag;

/**
 * Deletes a person identified using it's displayed index from the address book.
 */
public class DeleteCommand extends Command {

    public static final String COMMAND_WORD = "delete";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the person(s) identified by the index(es) used in the displayed person list,\n"
            + "or deletes all with a specified tag\n"
            + "Parameters:\n"
            + "  INDEX [MORE_INDEXES]\n"
            + "  OR: all t/TAG (applies to displayed list)\n"
            + "Examples:\n"
            + "  " + COMMAND_WORD + " 1\n"
            + "  " + COMMAND_WORD + " 1 2 3 5\n"
            + "  " + COMMAND_WORD + " all t/friends";

    public static final String MESSAGE_DELETE_PERSON_SUCCESS = "Deleted Person: %1$s";
    public static final String MESSAGE_DELETE_PERSONS_SUCCESS = "Deleted %d People:\n%1$s";
    public static final String NO_PERSONS_FOUND_WITH_TAG = "No persons found with tag: %s";

    /**
     * Deletion modes supported by {@link DeleteCommand}:
     * - {@code BY_INDEX}: delete one or more people by their displayed indices</li>
     * - {@code BY_TAG}: delete all displayed people that contain the specified tag</li>
     */
    public enum Mode { BY_INDEX, BY_TAG };

    private final Mode mode;
    private final List<Index> targetIndexes;
    private final Tag targetTag;

    /**
     * Constructor for a delete command with a single target index to delete
     *
     * @param targetIndex index of the contact to be deleted
     */
    public DeleteCommand(Index targetIndex) {
        this.mode = Mode.BY_INDEX;
        this.targetIndexes = new ArrayList<>();
        this.targetIndexes.add(targetIndex);
        this.targetTag = null;
    }

    /**
     * Constructor for a delete command that targets more than one index
     *
     * @param targetIndexes list of indexes of contacts to be deleted
     */
    public DeleteCommand(List<Index> targetIndexes) {
        requireNonNull(targetIndexes);
        if (targetIndexes.isEmpty()) {
            throw new IllegalArgumentException("At least one index is required.");
        }

        this.mode = Mode.BY_INDEX;
        this.targetIndexes = targetIndexes;
        this.targetTag = null;
    }


    /**
     * Creates a delete command that deletes all displayed persons containing {@code targetTag}.
     *
     * @param targetTag the tag to match.
     */
    public DeleteCommand(Tag targetTag) {
        requireNonNull(targetTag);
        this.mode = Mode.BY_TAG;
        this.targetIndexes = null;
        this.targetTag = targetTag;
    }

    /**
     * Executes the delete command against the provided {@link Model}.
     *
     * Dispatches to the appropriate deletion strategy based on {@link #mode}:
     * - {@link Mode#BY_INDEX}: delete one or more people by their displayed indices
     * - {@link Mode#BY_TAG}: delete all displayed people that contain the specified tag
     *
     * @param model The model containing the filtered person list and mutation methods.
     * @return A {@link CommandResult} describing the outcome.
     * @throws CommandException If indices are invalid or no persons match the given tag.
     * @throws AssertionError If an unknown mode is encountered.
     */
    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        return switch (mode) {
            case BY_TAG -> executeByTag(model);
            case BY_INDEX -> executeByIndex(model);
            default -> throw new AssertionError("Unknown mode " + mode);
        };
    }

    /**
     * Deletes all currently displayed {@link Person}s that contain {@link #targetTag}.
     *
     * If no displayed person contains the tag, a {@link CommandException} is thrown.
     * On success, all matching persons are removed from the model and a summary
     * listing the deleted persons is returned.
     *
     * @param model The model providing access to the filtered list and delete operation.
     * @return A {@link CommandResult} indicating how many persons were deleted and listing them.
     * @throws CommandException If no persons with the specified tag are found in the displayed list.
     */
    private CommandResult executeByTag(Model model) throws CommandException {
        List<Person> visible = model.getFilteredPersonList();
        List<Person> matches = new ArrayList<>();
        for (Person p : visible) {
            if (p.getTags().contains(targetTag)) {
                matches.add(p);
            }
        }

        if (matches.isEmpty()) {
            throw new CommandException(String.format(NO_PERSONS_FOUND_WITH_TAG, targetTag.tagName));
        }

        for (Person p : matches) {
            model.deletePerson(p);
        }

        String matchingList = matches.stream()
                .map(Messages::format)
                .collect(Collectors.joining("\n"));
        return new CommandResult(String.format(MESSAGE_DELETE_PERSONS_SUCCESS, matches.size(), matchingList));
    }

    /**
     * Deletes one or more {@link Person}s identified by {@link #targetIndexes} in the currently displayed list.
     *
     * Validation: All indices must refer to existing entries in the displayed list;
     * otherwise a {@link CommandException} is thrown.
     * Deletion semantics:
     * - Indices are processed in descending order to avoid shifting issues.
     * - Duplicates are ignored via {@code distinct()} to prevent double-deletes.
     * Result:
     * - Single deletion returns {@link #MESSAGE_DELETE_PERSON_SUCCESS} with the person formatted.
     * - Multiple deletions return {@link #MESSAGE_DELETE_PERSONS_SUCCESS} with a newline-joined list.
     *
     * @param model The model providing the displayed list and delete operation.
     * @return A {@link CommandResult} describing which person(s) were deleted.
     * @throws CommandException If any provided index is invalid for the displayed list.
     */
    private CommandResult executeByIndex(Model model) throws CommandException {
        List<Person> lastShownList = model.getFilteredPersonList();

        for (Index id : targetIndexes) {
            if (id.getZeroBased() >= lastShownList.size()) {
                throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
            }
        }

        List<Person> toDelete = targetIndexes.stream()
                .sorted((a, b) -> Integer.compare(b.getZeroBased(), a.getZeroBased()))
                .map(i -> lastShownList.get(i.getZeroBased()))
                .distinct()
                .toList();

        for (Person p : toDelete) {
            model.deletePerson(p);
        }

        if (toDelete.size() == 1) {
            return new CommandResult(String.format(MESSAGE_DELETE_PERSON_SUCCESS,
                    Messages.format(toDelete.get(0))));
        }

        String deletedPersons = toDelete.stream()
                .map(Messages::format)
                .collect(Collectors.joining("\n"));
        return new CommandResult(String.format(MESSAGE_DELETE_PERSONS_SUCCESS, toDelete.size(), deletedPersons));
    }

    @Override
    public boolean equals(Object other) {

        if (other == this) {
            return true;
        }

        if (!(other instanceof DeleteCommand)) {
            return false;
        }

        DeleteCommand o = (DeleteCommand) other;
        if (mode != o.mode) {
            return false;
        }
        return mode == Mode.BY_INDEX
                ? targetIndexes.equals(o.targetIndexes)
                : targetTag.equals(o.targetTag);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("mode", mode)
                .add("targetIndexes", targetIndexes)
                .add("targetTag", targetTag)
                .toString();
    }
}
