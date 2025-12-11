package org.unocapstone.dragonslair.ui;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * ConfirmBox to display a window and get a yes or no answer from the user.
 *
 */
public class ConfirmBox {

    static boolean answer;

    /**
     * Displays the title as the window title, and the message as text inside
     * the window. Prompts the users for yes or no confirmation and returns
     * their answer.
     * @param title The title for the window
     * @param message  The message to display in the window
     * @return True or false depending on the user's selection
     */
    public static boolean display(String title, String message) {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(250);
        Label label = new Label();
        label.setText(message);

        Button yesButton = new Button("Yes");
        yesButton.setId("yesButton");

        Button noButton = new Button("No");
        noButton.setId("noButton");

        yesButton.setOnAction(e -> {
            answer = true;
            window.close();
        });

        noButton.setOnAction(e -> {
            answer = false;
            window.close();
        });

        HBox buttonsBox = new HBox(10);
        buttonsBox.getChildren().addAll(yesButton, noButton);
        buttonsBox.setAlignment(Pos.CENTER);

        VBox layout = new VBox(10);
        layout.getChildren().addAll(label, buttonsBox);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();

        return answer;
    }

    /**
     * Displays a password-protected confirmation dialog.
     * Prompts the user for a password to confirm the action.
     * @param title The title for the window
     * @param message The message to display in the window
     * @param requiredPassword The password that must be entered to confirm
     * @return True if the user entered the correct password, false otherwise
     */
    public static boolean displayWithPassword(String title, String message, String requiredPassword) {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(250);
        
        Label label = new Label();
        label.setText(message);
        
        Label passwordLabel = new Label("Enter Password:");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        
        Button yesButton = new Button("Confirm");
        yesButton.setId("yesButton");
        
        Button noButton = new Button("Cancel");
        noButton.setId("noButton");
        
        answer = false; // Default to false
        
        yesButton.setOnAction(e -> {
            if (passwordField.getText().equals(requiredPassword)) {
                answer = true;
            } else {
                answer = false;
            }
            window.close();
        });
        
        noButton.setOnAction(e -> {
            answer = false;
            window.close();
        });
        
        HBox buttonsBox = new HBox(10);
        buttonsBox.getChildren().addAll(yesButton, noButton);
        buttonsBox.setAlignment(Pos.CENTER);
        
        VBox passwordBox = new VBox(5);
        passwordBox.getChildren().addAll(passwordLabel, passwordField);
        
        VBox layout = new VBox(10);
        layout.getChildren().addAll(label, passwordBox, buttonsBox);
        layout.setAlignment(Pos.CENTER);
        
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
        
        return answer;
    }

}

