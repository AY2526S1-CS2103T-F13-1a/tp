package seedu.address.model.person;

import java.util.Set;
import java.util.function.Predicate;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.tag.Tag;

/**
 * Tests that a {@code Person}'s {@code Tag} set contains any of the tags given.
 */
public class TagContainsKeywordPredicate implements Predicate<Person> {
    private final Set<Tag> tags;

    public TagContainsKeywordPredicate(Set<Tag> tags) {
        this.tags = tags;
    }

    @Override
    public boolean test(Person person) {
        return tags.stream()
                .anyMatch(predicateTag -> person.getTags().stream()
                        .anyMatch(personTag -> personTag.equals(predicateTag)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof TagContainsKeywordPredicate)) {
            return false;
        }

        TagContainsKeywordPredicate otherPredicate = (TagContainsKeywordPredicate) other;
        return tags.equals(otherPredicate.tags);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("tags", tags).toString();
    }
}
