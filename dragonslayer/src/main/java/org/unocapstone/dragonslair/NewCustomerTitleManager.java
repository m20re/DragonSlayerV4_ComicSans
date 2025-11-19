package org.unocapstone.dragonslair;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Date;


public class NewCustomerTitleManager {

    /**
     * Upon successful creation of an Customer to title order OR attaching a customer to a title,
     * add the relationship to the CustomerTitle Junction table.
     * @param conn:       Connection - The current database session
     * @param customerID: int - The unique ID with the associated customer
     * @param titleID:    int - The unique ID with the associated title.
     * 
     * @return: True or false, depending on whether the database updated.
     */
    public static boolean handleNewCustomerTitleOrder(Connection conn, int customerID, int titleID) {
        PreparedStatement ppst = null;
        String sql = "INSERT INTO CustomerTitles (CustomerID, TitleID, DateAdded, DateRemoved) VALUES (?, ?, ?, ?)";
        
        try {
            ppst = conn.prepareStatement(sql);
            ppst.setInt(1, customerID);
            ppst.setInt(2, titleID);
            ppst.setDate(3, new Date(System.currentTimeMillis())); // Current date
            ppst.setDate(4, null); // Default to NULL for new relationships

            int rowsAffected = ppst.executeUpdate();
            ppst.close();
            
            return rowsAffected > 0;
        } catch (SQLException e) {
            Log.LogEvent("SQL Exception - CustomerTitles", e.getMessage());
            e.printStackTrace();
            return false;
        }
    } 
}
