package org.unocapstone.dragonslair;

/**
 * Helper class to create and display customer requests in a table
 */
public class RequestTable {

    private final int orderId;
    private String requestLastName;
    private String requestFirstName;
    private int requestQuantity;
    private int requestIssue;

    /**
     * Creates a RequestTable object based on the parameters provided
     * @param requestLastName the last name of the requesting customer
     * @param requestFirstName the first name of the requestign customer
     * @param requestQuantity the quantity of the customer's order
     */
    public RequestTable(int orderId, String requestLastName, String requestFirstName, int requestQuantity, int issueNumber){

        this.orderId = orderId;
        this.requestLastName = requestLastName;
        this.requestFirstName = requestFirstName;
        this.requestQuantity = requestQuantity;
        this.requestIssue = issueNumber;
    }

    /**
     * Gets the orderId of the person for the selected title.
     * @return the customer's orderId for the selected title.
     */
    public int getOrderId() { return orderId; }

    /**
     * Gets the last name of the customer for this object
     * @return the customer's last name
     */
    public String getRequestLastName(){ return this.requestLastName; }

    /**
     * Gets the first name of the customer for this object
     * @return the customer's first name
     */
    public String getRequestFirstName(){ return this.requestFirstName; }

    /**
     * Gets the quantity of the customer's order for this object
     * @return the quantity of the customer's order
     */
    public String getRequestQuantity(){ return String.valueOf(this.requestQuantity); }

    /**
     * Gets the issue of the title of the customer's order for this object
     * @return the issue of the title of the customer's order
     */
    public String getRequestIssue() { return String.valueOf(this.requestIssue); }

    /**
     * Sets the quantity of the customer's order for this object
     * @param quantity the quantity of the customer's order
     */
    public void setRequestQuantity(int quantity) { this.requestQuantity = quantity; }

    /**
     * Sets the issue of the title of the customer's order for this object
     * @return the issue of the title of the customer's order
     */
    public void setRequestIssue(int issue) { this.requestIssue = issue; }

    public int getIssue() { return requestIssue; }

    public int getQuantity() { return requestQuantity; }

    public boolean equals(RequestTable t)
    {
        return this.requestFirstName.equals(t.getRequestFirstName()) && this.requestLastName.equals(t.getRequestLastName())
                && this.requestIssue == t.getIssue() && this.requestQuantity == t.getQuantity();
    }
}
