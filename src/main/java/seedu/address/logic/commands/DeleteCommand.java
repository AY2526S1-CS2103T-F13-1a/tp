package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.tag.Tag;

/**
 * Deletes person(s) by displayed indices or by tag(s) from the displayed list.
 */
public class DeleteCommand extends Command {

    public static final String COMMAND_WORD = "delete";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the person(s) identified by the index(es) used in the displayed person list,\n"
            + "or deletes all with one or more specified tags (union of matches).\n"
            + "Parameters:\n"
            + "  INDEX [MORE_INDEXES]\n"
            + "  OR: all t/TAG [t/TAG]... (applies to displayed list)\n"
            + "Examples:\n"
            + "  " + COMMAND_WORD + " 1\n"
            + "  " + COMMAND_WORD + " 1 2 3 5\n"
            + "  " + COMMAND_WORD + " all t/friends\n"
            + "  " + COMMAND_WORD + " all t/superhero t/noisy";

    public static final String MESSAGE_DELETE_PERSON_SUCCESS = "Deleted Person: %1$s";
    public static final String MESSAGE_DELETE_PERSONS_SUCCESS = "Deleted %d People:\n%2$s";
    public static final String NO_PERSONS_FOUND_WITH_TAGS = "No persons found with tag(s): %s";

    /**
     * Deletion modes for DeleteCommand
     */
    public enum Mode { BY_INDEX, BY_TAGS }

    private final Mode mode;
    private final List<Index> targetIndexes;
    private final Set<Tag> targetTags;

    /**
     * Creates a delete command that removes a single person by displayed index.
     *
     * @param targetIndex index of the person to delete (1-based in CLI, zero-based stored)
     * @throws NullPointerException if {@code targetIndex} is null
     */
    public DeleteCommand(Index targetIndex) {
        this.mode = Mode.BY_INDEX;
        this.targetIndexes = new ArrayList<>();
        this.targetIndexes.add(targetIndex);
        this.targetTags = null;
    }

    /**
     * Creates a delete command that removes multiple persons by their displayed indices.
     * Duplicate indices are allowed at construction time; they will be de-duplicated at execution.
     *
     * @param targetIndexes list of indices of persons to delete
     * @throws NullPointerException if {@code targetIndexes} is null
     * @throws IllegalArgumentException if {@code targetIndexes} is empty
     */
    public DeleteCommand(List<Index> targetIndexes) {
        requireNonNull(targetIndexes);
        if (targetIndexes.isEmpty()) {
            throw new IllegalArgumentException("At least one index is required.");
        }
        this.mode = Mode.BY_INDEX;
        this.targetIndexes = targetIndexes;
        this.targetTags = null;
    }

    /**
     * Creates a delete command that removes all displayed persons having
     * <em>any</em> of the specified tags (union semantics).
     * The provided set’s iteration order is preserved for user-facing messages.
     *
     * @param targetTags set of tags to match
     * @throws NullPointerException if {@code targetTags} is null
     * @throws IllegalArgumentException if {@code targetTags} is empty
     */
    public DeleteCommand(Set<Tag> targetTags) {
        requireNonNull(targetTags);
        if (targetTags.isEmpty()) {
            throw new IllegalArgumentException("At least one tag is required.");
        }
        this.mode = Mode.BY_TAGS;
        this.targetIndexes = null;
        // preserve insertion order for predictable error strings
        this.targetTags = new LinkedHashSet<>(targetTags);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        return switch (mode) {
        case BY_INDEX -> executeByIndex(model);
        case BY_TAGS -> executeByTags(model);
        };
    }

    private CommandResult executeByTags(Model model) throws CommandException {
        List<Person> visible = model.getSortedPersonList();
        List<Person> matches = collectPersonsByTags(visible, targetTags);
        if (matches.isEmpty()) {
            throw new CommandException(tagsNotFoundMessage(targetTags));
        }
        deletePersons(model, matches);
        return successListResult(matches);
    }

    private static List<Person> collectPersonsByTags(List<Person> visible, Set<Tag> tags) {
        List<Person> matches = new ArrayList<>();
        for (Person p : visible) {
            if (hasAnyTag(p, tags)) {
                matches.add(p);
            }
        }
        return matches;
    }

    private static boolean hasAnyTag(Person p, Set<Tag> tags) {
        for (Tag t : tags) {
            if (p.getTags().contains(t)) {
                return true;
            }
        }
        return false;
    }

    private static String tagsNotFoundMessage(Set<Tag> tags) {
        String joined = tags.stream().map(t -> t.tagName).collect(Collectors.joining(", "));
        return String.format(NO_PERSONS_FOUND_WITH_TAGS, joined);
    }

    private CommandResult executeByIndex(Model model) throws CommandException {
        List<Person> visible = model.getSortedPersonList();
        validateIndices(visible.size(), targetIndexes);
        List<Person> toDelete = collectPersonsByIndices(visible, targetIndexes);
        deletePersons(model, toDelete);
        return successListResult(toDelete);
    }

    private static void validateIndices(int size, List<Index> indexes) throws CommandException {
        for (Index id : indexes) {
            if (id.getZeroBased() >= size) {
                throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
            }
        }
    }

    private static List<Person> collectPersonsByIndices(List<Person> visible, List<Index> indexes) {
        return indexes.stream()
                .sorted((a, b) -> Integer.compare(b.getZeroBased(), a.getZeroBased()))
                .map(i -> visible.get(i.getZeroBased()))
                .distinct()
                .toList();
    }

    private static void deletePersons(Model model, List<Person> people) {
        for (Person p : people) {
            model.deletePerson(p);
        }
    }

    private static CommandResult successListResult(List<Person> people) {
        if (people.size() == 1) {
            return new CommandResult(String.format(
                    MESSAGE_DELETE_PERSON_SUCCESS, Messages.format(people.get(0))));
        }
        String list = people.stream().map(Messages::format).collect(Collectors.joining("\n"));
        return new CommandResult(String.format(MESSAGE_DELETE_PERSONS_SUCCESS, people.size(), list));
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
                : targetTags.equals(o.targetTags);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("mode", mode)
                .add("targetIndexes", targetIndexes)
                .add("targetTags", targetTags)
                .toString();
    }
}
