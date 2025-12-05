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

    public static boolean handleDeleteCustomerTitle(Connection conn, int customerID, int titleID) {
        PreparedStatement ppst = null;
        String sql = "UPDATE CustomerTitles SET DateRemoved = ? WHERE CustomerID = ? AND TitleID = ? AND DateRemoved IS NULL";

        try {
            ppst = conn.prepareStatement(sql);
            ppst.setDate(1, new Date(System.currentTimeMillis()));
            ppst.setInt(2, customerID);
            ppst.setInt(3, titleID);

            int rowsAffected = ppst.executeUpdate();
            ppst.close();

            if (rowsAffected > 0) {
                Log.LogEvent("CustomerTitle Deactivated", 
                    "Set DateRemoved for CustomerID: " + customerID + ", " + titleID 
                );
                return true;
            } else {
                Log.LogEvent("CustomerTitle Not Found", 
                    "No relationship was found for customerID: " + customerID + ", " + titleID
                );
                return false;
            }
        } catch (SQLException e) {
            Log.LogEvent("SQL Exception - CustomerTitles Delete", e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
