package org.unocapstone.dragonslair;

import org.unocapstone.dragonslair.controllers.Controller;

import java.nio.file.Path;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

@ExtendWith(ApplicationExtension.class)
@Tag("ui")
/**
 * Creates an abstract class (with concrete methods) to reduce code clutter.
 * Includes UI functions to make code easier to read.
 */
abstract class BaseFxUiTest {
    /* Can only be shared with other tests */
    protected Controller controller;

    @TempDir
    Path derbyHome;

    @Start
    public void start (Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/test-sample.fxml"));
        Parent root = fxmlLoader.load();
        stage.setScene(new Scene(root));
        stage.show();
        stage.toFront();
        controller = fxmlLoader.getController();
    }

    protected static String unique(String base) {
        return base + "-" + System.nanoTime();
    }

    protected void openAddNewCustomer(FxRobot r) {
        r.clickOn("Customers");
        r.clickOn("#addCustomerButtonMain");
    }

    protected void openEditCustomer(FxRobot r) {
        r.clickOn(null);
    }

    protected void createNewCustomer(FxRobot r, String first, String last, String phone) {
        r.clickOn("#newCustomerFirstName").write(first);
        r.clickOn("#newCustomerLastName").write(last);
        r.clickOn("#newCustomerPhone").write(phone);

        r.clickOn("#addCustomerButton");
    }
}
