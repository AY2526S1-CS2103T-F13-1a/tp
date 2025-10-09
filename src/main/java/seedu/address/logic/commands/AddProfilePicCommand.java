package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
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
            + "Example: " + COMMAND_WORD + " 1 pp/johndoe.png";

    public static final String MESSAGE_SUCCESS = "Updated profile picture for: %1$s";

    private final Index index;
    private final String profilePicture;

    public AddProfilePicCommand(Index index, String profilePicture) {
        requireNonNull(index);
        this.index = index;
        this.profilePicture = profilePicture == null ? "" : profilePicture;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToEdit = lastShownList.get(index.getZeroBased());

        Name name = personToEdit.getName();
        Phone phone = personToEdit.getPhone();
        Email email = personToEdit.getEmail();
        Address address = personToEdit.getAddress();
        java.util.Set<Tag> tags = personToEdit.getTags();

        Person edited = profilePicture.isEmpty()
                ? new Person(name, phone, email, address, tags)
                : new Person(name, phone, email, address, tags, profilePicture);

        model.setPerson(personToEdit, edited);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);

        return new CommandResult(String.format(MESSAGE_SUCCESS, edited.getName().fullName));
    }

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


