package org.unocapstone.dragonslair;

import static org.unocapstone.dragonslair.Helpers.generateRandomName;
import static org.unocapstone.dragonslair.Helpers.generateRandomPhoneNumber;

import org.junit.jupiter.api.Test;
import org.testfx.api.FxRobot;

public class CustomerEditUiTest extends BaseFxUiTest {
    @Test
    void editCustomer_oneTime(FxRobot robot) {
        /* Adds a customer again */
        String f_name = generateRandomName();
        openAddNewCustomer(robot);
        createNewCustomer(robot, f_name, "editTest", generateRandomPhoneNumber());
    }
}
