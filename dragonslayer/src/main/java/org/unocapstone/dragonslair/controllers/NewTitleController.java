package org.unocapstone.dragonslair.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.sql.*;

import org.unocapstone.dragonslair.Log;

/**
 * This Controller controls the New Title window. It allows the window
 * to get the text that is entered in the fields and save it in the
 * database.
 */
public class NewTitleController{

    public boolean titleWasAdded = false;
    
    private Connection conn;
    int rowsAffected = 0;

    @FXML private Button addTitleButton;

    @FXML private TextField newTitleTitle;
    @FXML private TextField newTitlePrice;
    @FXML private TextField newTitleNotes;
    @FXML private TextField newTitleProductId;

    @FXML private Text priceValidText;

    /**
     * Creates a title based off of the text fields and adds it
     * to the database
     * @param event Event that triggered this method
     */
    @FXML
    void addTitle(ActionEvent event) {
        String title = newTitleTitle.getText();
        String notes = newTitleNotes.getText();
        String productId = newTitleProductId.getText();
        Date dateCreated = new Date(System.currentTimeMillis());

        if(isValidPrice(newTitlePrice.getText())) {
            String price = newTitlePrice.getText();

            Statement get = null;
            PreparedStatement insert = null;
            String sql = "INSERT INTO Titles (TITLE, PRICE, NOTES, PRODUCTID, DATECREATED) VALUES (?, ?, ?, ?, ?)";

            try {
                get = conn.createStatement();
                ResultSet result = get.executeQuery("SELECT TITLE FROM TITLES");
                while (result.next()) {
                    String testTitle = result.getString("TITLE");
                    if (testTitle.equals(title)) {
                        Alert alert = new Alert(Alert.AlertType.WARNING, "Cannot create multiple Titles with exactly the same name.", ButtonType.OK);
                        alert.setTitle("Duplicate Title Entry");
                        alert.setHeaderText("");
                        alert.show();
                        return;
                    }
                    else if (title == "")
                    {
                        Alert alert = new Alert(Alert.AlertType.WARNING, "Please enter a title for this publication!", ButtonType.OK);
                        alert.setTitle("No title entered");
                        alert.setHeaderText("");
                        alert.show();
                        return;
                    }
                }

                insert = conn.prepareStatement(sql);
                insert.setString(1, title);
                insert.setObject(2, dollarsToCents(price), Types.INTEGER);
                insert.setString(3, notes);
                insert.setString(4, productId);
                insert.setDate(5, dateCreated);
                rowsAffected = insert.executeUpdate();

                insert.close();
                Log.LogEvent("Added Title", "Added Title - Title: " + title + " - Price: " + price + " - Notes: " + notes + " - Product ID: " + productId + " - Date Created: " + dateCreated);
            } catch (SQLException sqlExcept) {
                Log.LogEvent("SQL Exception", sqlExcept.getMessage());
                Alert alert = new Alert(Alert.AlertType.ERROR, "Database error. This is either a bug, or you messed with the DragonSlayer/derbyDB folder.", ButtonType.OK);
                alert.setTitle("Database Error");
                alert.setHeaderText("");
                alert.show();
            }

            titleWasAdded = true;
            Stage window = (Stage) addTitleButton.getScene().getWindow();
            window.close();
        }
    }

    /**
     * Checks to see if a price String is in the valid format
     * @param priceDollars The String to test
     * @return True if the String is a valid format, false otherwise
     */
    private boolean isValidPrice(String priceDollars) {
        if (priceDollars.equals("") || priceDollars.matches("^[0-9]{1,3}(?:,?[0-9]{3})*\\.[0-9]{2}$") ) {
            return true;
        } else {
            priceValidText.setVisible(true);
            return false;
        }
    }

    /**
     * Converts a string in the format of xxx,xxx.xx to an integer
     * @param priceDollars The price in dollars to be converted
     * @return An integer representing the number of cents
     */
    private String dollarsToCents(String priceDollars) {
        if (priceDollars.isBlank()) {
            return null;
        }
        priceDollars = priceDollars.replace(".", "");
        priceDollars = priceDollars.replaceAll(",", "");
        return priceDollars;
    }

    /**
     * Sets this database connection for this controller
     * @param conn Connection to set for this controller
     */
    public void setConnection(Connection conn) {
        this.conn = conn;
    }
}
