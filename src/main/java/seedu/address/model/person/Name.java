package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Person's name in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidName(String)}
 */
public class Name {

    public static final String MESSAGE_CONSTRAINTS =
            "Names should only contain letters (including accents and non-English characters), "
                    + "\n numbers, spaces, hyphens (-), apostrophes (' or ’), and periods (.). "
                    + "They must begin and end with a letter,"
                    + "\n and cannot contain special symbols like @ or #.";

    /*
     * The first character of the address must not be a whitespace,
     * otherwise " " (a blank string) becomes a valid input.
     * The name can contain hyphens or slashes
     */
    private static final String VALIDATION_REGEX =
            "^(?=.{1,80}$)[\\p{L}\\p{M}\\p{N}]+(?:[\\p{Zs}\\-.'’][\\p{L}\\p{M}\\p{N}]+)*$";

    public final String fullName;

    /**
     * Constructs a {@code Name}.
     *
     * @param name A valid name.
     */
    public Name(String name) {
        requireNonNull(name);
        String normalized = name.trim().replaceAll("\\p{Zs}+", " ");
        checkArgument(isValidName(normalized), MESSAGE_CONSTRAINTS);
        fullName = normalized;
    }

    /**
     * Returns true if a given string is a valid name.
     */
    public static boolean isValidName(String test) {
        return test.matches(VALIDATION_REGEX);
    }


    @Override
    public String toString() {
        return fullName;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Name)) {
            return false;
        }

        Name otherName = (Name) other;
        return fullName.equals(otherName.fullName);
    }

    @Override
    public int hashCode() {
        return fullName.hashCode();
    }

}
