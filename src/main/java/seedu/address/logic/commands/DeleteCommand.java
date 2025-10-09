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


    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        switch (mode) {
        case BY_TAG -> {

            // fill matches list with persons with target tag
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

            // iterate through list of matches and delete each match
            for (Person p : matches) {
                model.deletePerson(p);
            }

            // print success message with list of contacts deleted
            String matchingList = matches.stream().map(Messages::format).collect(Collectors.joining("\n"));
            return new CommandResult(String.format(MESSAGE_DELETE_PERSONS_SUCCESS, matches.size(), matchingList));
        }
        case BY_INDEX -> {
            List<Person> lastShownList = model.getFilteredPersonList();

            for (Index id : targetIndexes) {
                if (id.getZeroBased() >= lastShownList.size()) {
                    throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
                }
            }

            // remove dupes
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
            String deletedPersons = toDelete.stream().map(Messages :: format)
                    .collect(Collectors.joining("\n"));
            return new CommandResult(String.format(MESSAGE_DELETE_PERSONS_SUCCESS, toDelete.size(), deletedPersons));
        }
        default -> {
            throw new AssertionError("Unknown mode" + mode);
        }
        }
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
