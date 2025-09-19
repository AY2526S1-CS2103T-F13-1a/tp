package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;

import org.junit.jupiter.api.Test;

import seedu.address.model.Model;

public class RemarkCommandTest {

    private Model model;
    private Model expectedModel;
    private String MESSAGE_NOT_IMPLEMENTED_YET = "This method is not implemented yet";

    @Test
    public void execute() {
        assertCommandFailure(new RemarkCommand(), model, MESSAGE_NOT_IMPLEMENTED_YET);
    }
}
