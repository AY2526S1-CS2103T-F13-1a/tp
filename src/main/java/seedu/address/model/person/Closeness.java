package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Person's closeness level in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidCloseness(String)}
 */
public class Closeness {
    public static final String MESSAGE_CONSTRAINTS =
            "Closeness rating should be an integer between 1 and 5 inclusive.";
    public static final String VALIDATION_REGEX = "^[1-5]$";
    public final int closenessLevel;

    /**
     * Constructs a {@code Closeness}.
     *
     * @param closeness A valid closeness level.
     */
    public Closeness(String closeness) {
        requireNonNull(closeness);
        checkArgument(isValidCloseness(closeness), MESSAGE_CONSTRAINTS);
        this.closenessLevel = Integer.parseInt(closeness);
    }

    /**
     * Returns true if the given closeness level is valid
     */
    public static boolean isValidCloseness(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return String.valueOf(closenessLevel);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof Closeness)) {
            return false;
        }

        Closeness otherCloseness = (Closeness) other;
        return closenessLevel == otherCloseness.closenessLevel;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(closenessLevel);
    }
}
