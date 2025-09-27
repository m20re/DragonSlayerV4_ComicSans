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
import org.unocapstone.dragonslair.Title;

/**
 * This Controller controls the Edit Title window. It allows the window
 * to get the text that is entered in the fields and save it in the
 * database.
 */
public class EditTitleController{

    public boolean titleWasEdited = false;

    private Connection conn;
    private Title title;
    int rowsAffected;
    Statement get;

    @FXML private Button updateTitleButton;

    @FXML private TextField updateTitleTitle;
    @FXML private TextField updateTitlePrice;
    @FXML private TextField updateTitleNotes;
    @FXML private TextField updateTitleProductId;

    @FXML private Text priceValidText;

    /**
     * Updates the title based on the text entered in the text fields.
     * @param event Event that triggered the method call
     */
    @FXML
    void updateTitle(ActionEvent event) {
        titleWasEdited = true;

        String titleText = updateTitleTitle.getText();
        String notes = updateTitleNotes.getText();
        String productId = updateTitleProductId.getText();

        if(isValidPrice(updateTitlePrice.getText())) {
            String price = updateTitlePrice.getText();

            get = null;
            PreparedStatement update = null;
            String sql = """
            UPDATE TITLES
            SET TITLE = ?, PRICE = ?, NOTES = ?, PRODUCTID = ?
            WHERE TITLEID = ?
            """;

            try
            {
                update = conn.prepareStatement(sql);
                update.setString(1, titleText);
                update.setObject(2, dollarsToCents(price), Types.INTEGER);
                update.setString(3, notes);
                update.setString(4, productId);
                update.setString(5, Integer.toString(title.getId()));
                rowsAffected = update.executeUpdate();

                update.close();

                Log.LogEvent("Edited Title", "Edited Title - Title: " + titleText + " - Price: " + price + " - Notes: " + notes + " - Product ID: " + productId + " - TitleID: " + title.getId());
            }
            catch (SQLException sqlExcept)
            {
                Log.LogEvent("SQL Exception", sqlExcept.getMessage());
                Alert alert = new Alert(Alert.AlertType.ERROR, "Database error. This is either a bug, or you messed with the DragonSlayer/derbyDB folder.", ButtonType.OK);
                alert.setTitle("Database Error");
                alert.setHeaderText("");
                alert.show();
            }
            Stage window = (Stage) updateTitleButton.getScene().getWindow();
            window.close();
        }
    }

    /**
     * Sets the connection for this controller
     * @param conn The connection to set for this controller
     */
    public void setConnection(Connection conn) {
        this.conn = conn;
    }

    /**
     * Sets the Title for this controller
     * @param title The title to set for this controller
     */
    public void setTitle(Title title) {
        this.title = title;
        updateTitleTitle.setText(title.getTitle());
        if (title.getPrice() > 0) {
            updateTitlePrice.setText(title.getPriceDollars());
        }
        updateTitleNotes.setText(title.getNotes());
        updateTitleProductId.setText(title.getProductId());
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
}
