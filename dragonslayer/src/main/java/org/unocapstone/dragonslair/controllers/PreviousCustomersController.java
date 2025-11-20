package org.unocapstone.dragonslair.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

import org.unocapstone.dragonslair.Log;
import org.unocapstone.dragonslair.ui.AlertBox;

// adds support for JavaFX to auto-update with each change
import javafx.beans.property.*;

public class PreviousCustomersController implements Initializable {
    
    @FXML ToggleButton      showActiveButton;
    @FXML ComboBox<String>  titleComboBox;           // String for title names
    @FXML ComboBox<String>  customerTitleBox;        // String for customer names
    @FXML Button            clearButton;
    @FXML Button            removeRelationshipButton;
    @FXML Button            reactivateRelationshipButton;
    @FXML Button            closeButton;
    
    // Uses CustomerTitleRecord, which is defined below to cut down code bloat
    @FXML TableView<CustomerTitleRecord>            customerTitleTable;
    @FXML TableColumn<CustomerTitleRecord, String>  customerColumn;
    @FXML TableColumn<CustomerTitleRecord, String>  titleColumn;
    @FXML TableColumn<CustomerTitleRecord, String>  dateAddedColumn;
    @FXML TableColumn<CustomerTitleRecord, String>  dateRemovedColumn;
    @FXML TableColumn<CustomerTitleRecord, String>  statusColumn;
    
    @FXML Label statusLabel;
    @FXML Label recordStatusLabel;
    
    private Connection conn;
    private ObservableList<CustomerTitleRecord> allRecords = FXCollections.observableArrayList();
    
    /**
     * Initialize tables to display CustomerTitle data
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Set up table columns to extract data from CustomerTitleRecord
        // SimpleStringProperty ensures that UI data is updated automatically
        customerColumn.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getCustomerName()));
        titleColumn.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getTitleName()));
        dateAddedColumn.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getDateAddedStr()));
        dateRemovedColumn.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getDateRemovedStr()));
        statusColumn.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getStatus()));
    }
    
    /**
     * Sets the database connection
     * @param conn the database connection
     */
    public void setConnection(Connection conn) {
        this.conn = conn;
        // once connection is received, load data
        loadData();
    }
    
    /**
     * Loads all customer-title relationships from the database
     */
    private void loadData() {
        allRecords.clear();
        
        String sql = """
            SELECT ct.CustomerID, ct.TitleID, ct.DateAdded, ct.DateRemoved,
                   c.FirstName, c.LastName, t.Title
            FROM CustomerTitles ct
            JOIN Customers c ON ct.CustomerID = c.CustomerID
            JOIN Titles t ON ct.TitleID = t.TitleID
            ORDER BY ct.DateAdded DESC
            """;
        
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            // reads from an entire entry
            while (rs.next()) {
                int customerId = rs.getInt("CustomerID");
                int titleId = rs.getInt("TitleID");
                Date dateAdded = rs.getDate("DateAdded");
                Date dateRemoved = rs.getDate("DateRemoved");
                String firstName = rs.getString("FirstName");
                String lastName = rs.getString("LastName");
                String title = rs.getString("Title");
                
                String customerName = firstName + " " + lastName;
                
                CustomerTitleRecord record = new CustomerTitleRecord(
                    customerId, titleId, customerName, title, dateAdded, dateRemoved
                );
                allRecords.add(record);
            }
            
            statusLabel.setText("Data loaded successfully");
            
        } catch (SQLException e) {
            Log.LogEvent("SQL Exception - Load CustomerTitles", e.getMessage());
            e.printStackTrace();
            statusLabel.setText("Error loading data");
            AlertBox.display("Database Error", "Failed to load customer-title relationships: " + e.getMessage());
        }
    }
    
    @FXML
    private void toggleActiveFilter() {
        // TODO: Filter active/inactive
    }
    
    @FXML
    private void applyFilters() {
        // TODO: Apply combo box filters
    }
    
    @FXML
    private void clearFilters() {
        // TODO: Clear all filters
    }
    
    @FXML
    private void removeRelationship() {
        // TODO: Set DateRemoved
    }
    
    @FXML
    private void reactivateRelationship() {
        // TODO: Clear DateRemoved
    }
    
    @FXML
    private void closeWindow() {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    // Inner class
    public static class CustomerTitleRecord {
        private final int customerId;
        private final int titleId;
        private final String customerName;
        private final String titleName;
        private final Date dateAdded;
        private final Date dateRemoved;
        
        public CustomerTitleRecord(int customerId, int titleId, String customerName, 
                                  String titleName, Date dateAdded, Date dateRemoved) {
            this.customerId = customerId;
            this.titleId = titleId;
            this.customerName = customerName;
            this.titleName = titleName;
            this.dateAdded = dateAdded;
            this.dateRemoved = dateRemoved;
        }
        
        public int getCustomerId() { return customerId; }
        public int getTitleId() { return titleId; }
        public String getCustomerName() { return customerName; }
        public String getTitleName() { return titleName; }
        public String getDateAddedStr() { return dateAdded != null ? dateAdded.toString() : ""; }
        public String getDateRemovedStr() { return dateRemoved != null ? dateRemoved.toString() : ""; }
        public boolean isActive() { return dateRemoved == null; }
        public String getStatus() { return isActive() ? "Active" : "Inactive"; }
    }
}
