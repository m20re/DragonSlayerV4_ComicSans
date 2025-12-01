package org.unocapstone.dragonslair.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
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
    private FilteredList<CustomerTitleRecord> filteredRecords;

    // To enable jumping to specific customer/title from main GUI
    private Integer preselectedCustomerId = null;
    private Integer preselectedTitleId = null;
    
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

        // "p -> true" means "show everything" by default
        filteredRecords = new FilteredList<>(allRecords, p -> true);

        // Bind the table to the filtered list 
        // meaning that changing filters will auto-update the table
        customerTitleTable.setItems(filteredRecords);
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
     * Preselects a customer to filter by when opening this window from main GUI
     * Call this BEFORE setConnection() to have it auto-filter on load
     * @param customerId The ID of the customer to jump to
     */
    public void setPreselectedCustomer(int customerId) {
        this.preselectedCustomerId = customerId;
    }

    /**
     * Preselects a title to filter by when opening this window from main GUI
     * Call this BEFORE setConnection() to have it auto-filter on load
     * @param titleId The ID of the title to jump to
     */
    public void setPreselectedTitle(int titleId) {
        this.preselectedTitleId = titleId;
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

            populateComboBoxes();

            // Apply preselected filters if they were set from main GUI
            applyPreselectedFilters();
            statusLabel.setText("Data loaded successfully!");
            recordStatusLabel.setText(allRecords.size() + " Records");
            
        } catch (SQLException e) {
            Log.LogEvent("SQL Exception - Load CustomerTitles", e.getMessage());
            e.printStackTrace();
            statusLabel.setText("Error loading data");
            AlertBox.display("Database Error", "Failed to load customer-title relationships: " + e.getMessage());
        }
    }
    
    /**
     * Populates the title and customer combo boxes (filter boxes) with unique values from the loaded data
     * Uses Allrecords to do so.
     */
    private void populateComboBoxes() {
        // Create lists to hold EACH unique customers and titles
        ObservableList<String> uniqueCustomers = FXCollections.observableArrayList();
        ObservableList<String> uniqueTitles = FXCollections.observableArrayList();

        for (CustomerTitleRecord record : allRecords) {
            if (!uniqueCustomers.contains(record.getCustomerName())) {
                uniqueCustomers.add(record.getCustomerName());
            }
            if (!uniqueTitles.contains(record.getTitleName())) {
                uniqueTitles.add(record.getTitleName());
            }
        }

        // Sort alphabetically and then populates
        uniqueCustomers.sort(String::compareTo);
        uniqueTitles.sort(String::compareTo);
        customerTitleBox.setItems(uniqueCustomers);
        titleComboBox.setItems(uniqueTitles);
    }

    /**
     * Applies the filters if selected from the main GUI
     */
    private void applyPreselectedFilters() {
        if (preselectedCustomerId != null) {
            // Find the customer name that matches this ID (might be slow)
            for (CustomerTitleRecord record : allRecords) {
                if (record.getCustomerId() == preselectedCustomerId) {
                    // Set the combo box to this customer's name and stop the search
                    customerTitleBox.setValue(record.getCustomerName());
                    break;
                }
            }
        }

        if (preselectedTitleId != null) {
            // Find the title name that matches this ID
            for (CustomerTitleRecord record : allRecords) {
                if (record.getTitleId() == preselectedTitleId) {
                    titleComboBox.setValue(record.getTitleName());
                    break;
                }
            }
        }

        if (preselectedCustomerId != null || preselectedTitleId != null) {
            applyFilters();
        }
    }

    /**
     * Toggles between showing all records vs only active records
     * Called when the "Show Active Customers" toggle button is clicked
     */
    @FXML
    private void toggleActiveFilter() {
        applyFilters();
    }

    /**
     * Applies all active filters (customer, title, and active status)
     * updates Filtered records
     */
    @FXML
    private void applyFilters() {
        // Set a new predicate (filter function) on the filtered list
        // This function is called for EACH record to decide if it should be shown
        filteredRecords.setPredicate(record -> {
            // Get the selected values from the combo boxes
            String selectedCustomer = customerTitleBox.getValue();
            String selectedTitle = titleComboBox.getValue();
            boolean showActiveOnly = showActiveButton.isSelected();

            // Filter by customer if one is selected
            // If a customer is selected AND this record doesn't match, hide it (return false)
            if (selectedCustomer != null && !selectedCustomer.isEmpty()) {
                if (!record.getCustomerName().equals(selectedCustomer)) {
                    return false;
                }
            }

            // Filter by title if one is selected
            // If a title is selected AND this record doesn't match, hide it (return false)
            if (selectedTitle != null && !selectedTitle.isEmpty()) {
                if (!record.getTitleName().equals(selectedTitle)) {
                    return false;
                }
            }

            // Filter by active status if toggle is on
            // If toggle is on AND this record is inactive, hide it (return false)
            if (showActiveOnly && !record.isActive()) {
                return false;
            }

            // If we got here, the record passed all filters - show it!
            return true;
        });

        // Update the record count label to show filtered results
        recordStatusLabel.setText(filteredRecords.size() + " Records (filtered from " + allRecords.size() + ")");
    }

    /**
     * Clears all filters and shows all records again
     */
    @FXML
    private void clearFilters() {
        customerTitleBox.setValue(null);
        titleComboBox.setValue(null);

        // Turn off the active-only toggle
        showActiveButton.setSelected(false);
        filteredRecords.setPredicate(p -> true);
        recordStatusLabel.setText(allRecords.size() + " Records");
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
