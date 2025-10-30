package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.person.TagContainsKeywordPredicate;

/**
 * Filters and lists persons in address book who have a tag matching the keyword.
 * Keyword matching is case-insensitive.
 */
public class FilterCommand extends Command {
    public static final String COMMAND_WORD = "filter";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Filters and lists all persons who have "
            + "at least one of the specified tags (case-insensitive) \n"
            + "Parameters: t/KEYWORD [t/MORE_KEYWORDS]... \n"
            + "Example: " + COMMAND_WORD + " t/friends t/colleagues";

    private final TagContainsKeywordPredicate predicate;

    public FilterCommand(TagContainsKeywordPredicate predicate) {
        this.predicate = predicate;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredPersonList(predicate);
        return new CommandResult(
                String.format(Messages.MESSAGE_PERSONS_LISTED_OVERVIEW, model.getFilteredPersonList().size()));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof FilterCommand)) {
            return false;
        }

        FilterCommand otherFilterCommand = (FilterCommand) other;
        return predicate.equals(otherFilterCommand.predicate);
    }

    @Override
    public String toString() {
        return new seedu.address.commons.util.ToStringBuilder(this)
                .add("predicate", predicate)
                .toString();
    }
}
