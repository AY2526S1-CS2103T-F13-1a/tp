package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ORDER;

import java.util.Comparator;

import seedu.address.model.Model;
import seedu.address.model.person.Person;

/**
 * Sorts the contact list by closeness rating, in either ascending or descending order.
 */
public class SortByClosenessCommand extends Command {
    public static final String COMMAND_WORD = "sort";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Sorts the contact list by closeness rating.\n"
            + "Parameters: " + PREFIX_ORDER + "{asc|desc}\n"
            + "Example: " + COMMAND_WORD + " " + PREFIX_ORDER + "desc";

    public static final String MESSAGE_SUCCESS_ASC = "Sorted contact list by closeness (ascending)";
    public static final String MESSAGE_SUCCESS_DESC = "Sorted contact list by closeness (descending)";
    public static final String MESSAGE_INVALID_ORDER = "Invalid sort order. Must be 'asc' or 'desc'.";

    /**
     * This enumeration represents the possible orders in which to sort the contact list.
     */
    public enum SortOrder {
        ASCENDING,
        DESCENDING
    }

    private final SortOrder sortOrder;
    private final Comparator<Person> comparator;

    /**
     * Creates a SortByClosenessCommand to sort the contact list in the specified order.
     * @param sortOrder
     */
    public SortByClosenessCommand(SortOrder sortOrder) {
        requireNonNull(sortOrder);
        this.sortOrder = sortOrder;
        if (sortOrder == SortOrder.ASCENDING) {
            this.comparator = Comparator.comparingInt(person -> person.getCloseness().closenessLevel);
        } else {
            this.comparator = Comparator.comparingInt((Person person) -> person.getCloseness().closenessLevel)
                    .reversed();
        }
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateSortComparator(this.comparator);
        return new CommandResult(sortOrder == SortOrder.ASCENDING ? MESSAGE_SUCCESS_ASC : MESSAGE_SUCCESS_DESC);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof SortByClosenessCommand)) {
            return false;
        }
        SortByClosenessCommand otherCommand = (SortByClosenessCommand) other;
        return sortOrder == otherCommand.sortOrder;
    }
}
