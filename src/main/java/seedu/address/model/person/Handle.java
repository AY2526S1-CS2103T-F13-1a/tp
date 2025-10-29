package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;
/**
 * Represents a Person's telegram handle in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidHandle(String)}
 */
public class Handle {
    public static final String MESSAGE_CONSTRAINTS =
            "Telegram handles must start with '@'. "
                    + "It should only contain alphanumeric characters and underscores,"
                  + "though the first character must be an alphabet."
                    + "It must be between 5 and 32 characters long "
                    + "and it should not be blank";
    /*
     * The first character of the address must be a @,
     * Minimum length of 5 characters, maximum of 32
     * Telegram handle can only contain underscores and alphanumeric numbers
     */
    public static final String VALIDATION_REGEX = "^@[A-Za-z][A-Za-z0-9_]{4,31}$";
    public final String teleHandle;
    /**
     * Constructs a {@code Handle}.
     *
     * @param handle A valid name.
     */
    public Handle(String handle) {
        requireNonNull(handle);
        checkArgument(isValidHandle(handle), MESSAGE_CONSTRAINTS);
        this.teleHandle = handle;
    }

    /**
     * Returns true if the given telegram handle is valid
     */
    public static boolean isValidHandle(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return teleHandle;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof Handle)) {
            return false;
        }

        Handle otherHandle = (Handle) other;
        return teleHandle.equals(otherHandle.teleHandle);
    }
    @Override
    public int hashCode() {
        return teleHandle.hashCode();
    }

}
