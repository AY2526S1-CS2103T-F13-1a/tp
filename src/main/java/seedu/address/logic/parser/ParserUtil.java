package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Address;
import seedu.address.model.person.Closeness;
import seedu.address.model.person.Email;
import seedu.address.model.person.Handle;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;
import seedu.address.model.tag.Tag;

/**
 * Contains utility methods used for parsing strings in the various *Parser classes.
 */
public class ParserUtil {

    public static final String MESSAGE_INVALID_INDEX = "The person index is not a non-zero unsigned integer.";


    /**
     * Parses {@code oneBasedIndex} into an {@code Index} and returns it. Leading and trailing whitespaces will be
     * trimmed.
     * @throws ParseException if the specified index is invalid (not non-zero unsigned integer).
     */
    public static Index parseIndex(String oneBasedIndex) throws ParseException {
        String s = oneBasedIndex == null ? "" : oneBasedIndex.trim();
        if (s.isEmpty()) {
            throw new ParseException(MESSAGE_INVALID_INDEX);
        }

        try {
            int value = Integer.parseInt(s);
            if (value <= 0) {
                throw new ParseException(seedu.address.logic.Messages.MESSAGE_NEGATIVE_PERSON_DISPLAYED_INDEX);
            }
            return Index.fromOneBased(value);
        } catch (NumberFormatException ex) {
            throw new ParseException(MESSAGE_INVALID_INDEX);
        }
    }

    /**
     * Parses a {@code String name} into a {@code Name}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code name} is invalid.
     */
    public static Name parseName(String name) throws ParseException {
        requireNonNull(name);
        String formattedName = normaliseAndFormat(name);
        if (!Name.isValidName(formattedName)) {
            throw new ParseException(Name.MESSAGE_CONSTRAINTS);
        }
        return new Name(formattedName);
    }

    private static String normaliseAndFormat(String name) {
        if (name == null || name.isBlank()) {
            return "";
        }
        String normalised = name.trim().replaceAll("\\s+", " ");
        String[] words = normalised.split(" ");
        for (int i = 0; i < words.length; i++) {
            words[i] = words[i].substring(0, 1).toUpperCase()
                        + words[i].substring(1).toLowerCase();
        }
        return String.join(" ", words);
    }

    /**
     * Parses a {@code String phone} into a {@code Phone}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code phone} is invalid.
     */
    public static Phone parsePhone(String phone) throws ParseException {
        requireNonNull(phone);
        String trimmedPhone = phone.trim();
        if (!Phone.isValidPhone(trimmedPhone)) {
            throw new ParseException(Phone.MESSAGE_CONSTRAINTS);
        }
        return new Phone(trimmedPhone);
    }

    /**
     * Parses a {@code String address} into an {@code Address}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code address} is invalid.
     */
    public static Address parseAddress(String address) throws ParseException {
        requireNonNull(address);
        String trimmedAddress = address.trim();
        if (!Address.isValidAddress(trimmedAddress)) {
            throw new ParseException(Address.MESSAGE_CONSTRAINTS);
        }
        return new Address(trimmedAddress);
    }

    /**
     * Parses a {@code String email} into an {@code Email}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code email} is invalid.
     */
    public static Email parseEmail(String email) throws ParseException {
        requireNonNull(email);
        String trimmedEmail = email.trim().toLowerCase();
        int atIndex = trimmedEmail.indexOf('@');
        if (atIndex == -1) {
            throw new ParseException(Email.MESSAGE_CONSTRAINTS);
        }
        String localPart = trimmedEmail.substring(0, atIndex);
        String domainPart = trimmedEmail.substring(atIndex + 1);
        int plusIndex = localPart.indexOf('+');
        if (plusIndex != -1) {
            localPart = localPart.substring(0, plusIndex);
        }
        localPart = localPart.replace(".", "");
        String normalisedEmail = localPart + "@" + domainPart;
        if (!Email.isValidEmail(normalisedEmail)) {
            throw new ParseException(Email.MESSAGE_CONSTRAINTS);
        }
        return new Email(normalisedEmail);
    }

    /**
     * Parses a {@code String tag} into a {@code Tag}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code tag} is invalid.
     */
    public static Tag parseTag(String tag) throws ParseException {
        requireNonNull(tag);
        String trimmedTag = tag.trim();
        if (trimmedTag.length() > 15) {
            throw new ParseException("Tag names should be at most 15 characters long");
        }
        if (!Tag.isValidTagName(trimmedTag)) {
            throw new ParseException(Tag.MESSAGE_CONSTRAINTS);
        }
        return new Tag(trimmedTag);
    }

    /**
     * Parses {@code Collection<String> tags} into a {@code Set<Tag>}.
     */
    public static Set<Tag> parseTags(Collection<String> tags) throws ParseException {
        requireNonNull(tags);
        final Set<Tag> tagSet = new HashSet<>();
        for (String tagName : tags) {
            tagSet.add(parseTag(tagName));
        }
        return tagSet;
    }

    /**
     * Parses a {@code String handle} into a {@code Handle}
     * Leading and trailing whitespaces will be trimmed
     * @throws ParseException if the given {@code String handle} is invalid
     */
    public static Handle parseHandle(String handle) throws ParseException {
        requireNonNull(handle);
        String trimmedHandle = handle.trim().toLowerCase(); // trims the trailing white space
        // tells the user she/he entered the wrong input
        if (!Handle.isValidHandle(trimmedHandle)) {
            throw new ParseException(Handle.MESSAGE_CONSTRAINTS);
        }
        return new Handle(trimmedHandle);
    }

    /**
     * Parses a {@code String closeness} into a {@code Closeness}
     * Leading and trailing whitespaces will be trimmed
     * @throws ParseException if the given {@code String closeness} is invalid
     */
    public static Closeness parseCloseness(String closeness) throws ParseException {
        requireNonNull(closeness);
        String trimmedCloseness = closeness.trim();
        if (!Closeness.isValidCloseness(trimmedCloseness)) {
            throw new ParseException(Closeness.MESSAGE_CONSTRAINTS);
        }
        return new Closeness(trimmedCloseness);
    }
}
