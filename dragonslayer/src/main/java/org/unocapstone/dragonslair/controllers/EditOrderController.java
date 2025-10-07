package org.unocapstone.dragonslair.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

import org.unocapstone.dragonslair.FxUtilTest;
import org.unocapstone.dragonslair.Log;
import org.unocapstone.dragonslair.Order;
import org.unocapstone.dragonslair.Title;

/**
 * This Controller controls the Edit Title window. It allows the window
 * to get the text that is entered in the fields and save it in the
 * database.
 */
public class EditOrderController {

    public boolean orderWasEdited = false;
    
    private Connection conn;
    Order order;
    private int customerId;
    int rowsAffected;

    @FXML private Button updateOrderButton;

    @FXML private ComboBox<String> setTitle;
    @FXML private TextField setQuantity;
    @FXML private TextField setIssue;

    @FXML private Text orderTitleErrorText;
    @FXML private Text orderQuantityErrorText;

    private String prevCustomerId;
    private String prevTitle;
    private String prevIssue;
    private String prevQuantity;

    private ObservableList<Title> titles  = FXCollections.observableArrayList();
    private ObservableList<String> titlesStr  = FXCollections.observableArrayList();

    /**
     * Updates the title based on the text entered in the text fields.
     * @param event Event that triggered the method call
     */
    @FXML
    void updateOrder(ActionEvent event) {

        PreparedStatement s = null;
        String sql = "";
        if (Integer.parseInt(prevIssue) == 0) {
            sql = """
            UPDATE Orders
            SET titleId = ?, quantity = ?, issue = ?
            WHERE customerId = ? AND titleId = ? AND quantity = ? AND issue IS NULL
            """;
        } else {
            sql = """
            UPDATE Orders
            SET titleId = ?, quantity = ?, issue = ?
            WHERE customerId = ? AND titleId = ? AND quantity = ? AND issue = ?
            """;
        }

        orderQuantityErrorText.setVisible(false);
        orderTitleErrorText.setVisible(false);

        if (getChoice(setTitle) == -1) {
            orderTitleErrorText.setVisible(true);
            return;
        }
        else if (setQuantity.getText().equals("")) {
            orderQuantityErrorText.setVisible(true);
            return;
        }
        else {
            int titleID = getChoice(setTitle);
            String issueText = setIssue.getText();
            Integer issueValue = null;
            if (issueText != null && !issueText.trim().isEmpty()) {
                try {
                    issueValue = Integer.valueOf(issueText.trim());
                } catch (NumberFormatException nfe) {
                    // invalid issue number -> show error and abort
                    orderQuantityErrorText.setVisible(true);
                    return;
                }
            }
            String quantity = setQuantity.getText();
            int customerId = this.customerId;

            try {
                s = conn.prepareStatement(sql);
                s.setInt(1, titleID);
                // quantity is expected to be numeric in DB; use setInt when possible
                try {
                    s.setInt(2, Integer.parseInt(quantity));
                } catch (NumberFormatException nfe) {
                    s.setString(2, quantity);
                }
                s.setObject(3, issueValue, Types.INTEGER);

                s.setString(4, prevCustomerId);
                s.setString(5, prevTitle);
                s.setString(6, prevQuantity);
                if (Integer.parseInt(prevIssue) != 0) {
                    s.setObject(7, prevIssue, Types.INTEGER);
                }
                rowsAffected = s.executeUpdate();
                s.close();

                if (rowsAffected > 0) {
                    orderWasEdited = true;
                    Log.LogEvent("Edited Order", "Edited order - CustomerID: " + customerId + " - Title: " + FxUtilTest.getComboBoxValue(setTitle) + " - Quantity: " + quantity + " - Issue: " 
                                            + (issueValue == null ? null : issueValue)
                                            + " - Previous Title: " + prevTitle + " - Previous Quantity: " + prevQuantity + " - Previous Issue: " + prevIssue);
                } else {
                    Log.LogEvent("Edited Order", "No rows affected when attempting to edit order - CustomerID: " + customerId);
                }
            } catch (SQLException sqlExcept) {
                Log.LogEvent("SQL Exception", sqlExcept.getMessage());
                sqlExcept.printStackTrace();
            }
        }
        Stage window = (Stage) updateOrderButton.getScene().getWindow();
        window.close();
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
     * @param order The title to set for this controller
     */
    public void setOrder(Order order) {
        this.order = order;
        this.customerId = order.getCustomerId();

        this.prevCustomerId = String.valueOf(order.getCustomerId());
        this.prevTitle =  String.valueOf(order.getTitleId());
        this.prevIssue = String.valueOf(order.getIssue());
        this.prevQuantity = String.valueOf(order.getQuantity());

        setTitle.setItems(this.titlesStr);
        setQuantity.setText(String.valueOf(order.getQuantity()));
        if (order.getIssue() > 0)
        {
            setIssue.setText(String.valueOf(order.getIssue()));
        }
        setTitle.getSelectionModel().select(order.getTitleName());
        setTitle.setEditable(true);
        FxUtilTest.autoCompleteComboBoxPlus(setTitle, (typedText, itemToCompare) -> itemToCompare.toLowerCase().contains(typedText.toLowerCase()) || itemToCompare.equals(typedText));
    }


    /**
     * Takes an observable list of titles and creates an observable list of strings with just the title name
     * @param getTitles The observable list of titles
     */
    public void populate(ObservableList<Title> getTitles){
        this.titles = getTitles;
        for(int i=0; i < titles.size(); i++){
            titlesStr.add(titles.get(i).getTitle());
        }
    }

    /**
     * Gets the user's choice from the ComboBox
     * @param setTitle The ComboBox to get the title from
     * @return The ID of the title chosen, or -1 if input value is not found
     */
    public int getChoice(ComboBox<String> setTitle ){
        String name = FxUtilTest.getComboBoxValue(setTitle);

        for(int i=0; i < titles.size(); i++){
            if (titles.get(i).getTitle().equals(name)){
                return titles.get(i).getId();
            }
        }
        return -1;
    }
}

