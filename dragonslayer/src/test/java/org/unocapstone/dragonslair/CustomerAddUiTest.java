package org.unocapstone.dragonslair;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.testfx.api.FxRobot;

public class CustomerAddUiTest extends BaseFxUiTest {
    @Test
    void addCustomer_oneTime(FxRobot robot) {
        /* Makes sure the customer count is also correct */
        int before = controller.getCustomers().size();
        openAddNewCustomer(robot);
        createNewCustomer(robot, "test", "john", "402-672-6969");

        assertEquals(before + 1, controller.getCustomers().size());
    }
}
