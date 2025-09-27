package org.unocapstone.dragonslair.controllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

import org.unocapstone.dragonslair.FxUtilTest;
import org.unocapstone.dragonslair.Log;
import org.unocapstone.dragonslair.Title;

/**
 * This Controller controls the New Order window. It allows the window
 * to get the text that is entered in the fields and save it in the
 * database.
 */
public class NewOrderController implements Initializable{

    public boolean orderWasAdded = false;
    public int lastTitleAdded;
    private Connection conn;
    private int customerId;
    private String customer;

    @FXML private Button addOrderButton;
    @FXML private ComboBox<String> setTitle;
    @FXML private TextField setQuantity;
    @FXML private TextField setIssue;

    @FXML private Text orderTitleErrorText;
    @FXML private Text orderQuantityErrorText;

    private ObservableList<Title> titles  = FXCollections.observableArrayList();
    private ObservableList<String> titlesStr  = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setTitle.focusedProperty().addListener((obs, oldval, newval) -> {
            System.out.println("Selecting all New Order Title Text");
            // setTitle.getEditor().selectAll();

            Platform.runLater(() -> {
                if ((setTitle.getEditor().isFocused() || setTitle.isFocused()) && !setTitle.getEditor().getText().isEmpty()) {
                    setTitle.getEditor().selectAll();
                }
            });
        });
    }

    /**
     * Creates an order based on the fields and ComboBox and adds it
     * to the database
     * @param event Event that triggered this method
     */
    @FXML
    void newOrder(ActionEvent event) {
        PreparedStatement s = null;
        String sql = "INSERT INTO Orders (customerId, titleId, quantity, issue) VALUES (?, ?, ?, ?)";
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
            String issue = setIssue.getText();
            if (issue.isBlank()) {
                issue = null;
            }
            String quantity = setQuantity.getText();
            int customerId = this.customerId;
            Statement get = null;

            try {
                get = conn.createStatement();
                ResultSet result = get.executeQuery("SELECT * FROM ORDERS");
                while (result.next()) {
                    Integer testTitle = result.getInt("TITLEID");
                    Integer testCust = result.getInt("CUSTOMERID");
                    if (testTitle == titleID && testCust == customerId)
                    {
                        String testIssue = result.getString("ISSUE");
                        if ((testIssue == null && issue == null) ||
                            (testIssue != null && issue != null && testIssue.equals(issue)))
                            {
                                Alert alert = new Alert(Alert.AlertType.WARNING, "Cannot create duplicate Orders. If a customer has ordered multiple issues of the same title, be sure to fill out the issue field.", ButtonType.OK);
                                alert.setTitle("Duplicate Order");
                                alert.setHeaderText("");
                                alert.show();
                                return;
                            }
                    }
                }

                s = conn.prepareStatement(sql);
                s.setString(1, Integer.toString(customerId));
                s.setString(2, Integer.toString(titleID));
                s.setString(3, quantity);
                s.setObject(4, issue == null ? issue : Integer.valueOf(setIssue.getText()), Types.INTEGER);


                int rowsAffected = s.executeUpdate();

                if (rowsAffected == 0) {
                    System.err.println("Zero rows effected on new order add, this should not happen.");
                } else if (rowsAffected > 1) {
                    // Nothing should happen here
                }
                s.close();

                orderWasAdded = true;
                lastTitleAdded = titleID;

                Log.LogEvent("New Order", "Added order - Customer: " + customer + " - Title: " + FxUtilTest.getComboBoxValue(setTitle) + " - Quantity: " + quantity + " - Issue: " + (issue == null ? null : Integer.valueOf(issue)));
            } catch (SQLException sqlExcept) {
                Log.LogEvent("SQL Exception", sqlExcept.getMessage());
                sqlExcept.printStackTrace();
            }
        }
        Stage window = (Stage) addOrderButton.getScene().getWindow();
        window.close();
    }

    /**
     * Populate the ComboBox with the titles in titlesStr, add listener to handle typing over selection
     */
    public void setNewOrder(){
        setTitle.setItems(this.titlesStr);
        setTitle.getSelectionModel().selectFirst();
        setTitle.setEditable(true);
        FxUtilTest.autoCompleteComboBoxPlus(setTitle, (typedText, itemToCompare) -> itemToCompare.toLowerCase().contains(typedText.toLowerCase()) || itemToCompare.equals(typedText));
        
    }

    /**
     * Sets the connection for this controller
     * @param conn the connection for this controller
     */
    public void setConnection(Connection conn) {
        this.conn = conn;
    }

    /**
     * Sets the customer ID for this controller
     * @param id ID of the customer to set
     */
    public void setCustomerID(int id) {
        this.customerId = id;
    }

    /**
     * Sets the customer for this controller
     * @param customer name of the customer to set
     */
    public void setCustomer(String customer) {
        this.customer = customer;
    }

    /**
     * Populates titlesStr based off of an ObservableList of Titles
     * @param getTitles ObservableList of Titles to add to titlesStr
     */
    public void populate(ObservableList<Title> getTitles){
        this.titles = getTitles;
        for(int i=0; i < titles.size(); i++){
            titlesStr.add(titles.get(i).getTitle());
        }
    }

    /**
     * Gets the choice from the ComboBox
     * @param setTitle ComboBox containing the title value
     * @return the ID of the title selected
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