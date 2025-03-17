package com.example.localmarketbagroot;

public class OrderDB {
    private String customerName;
    private String itemName;
    private int amount;
    private String address;

    public OrderDB() {
        // Default constructor required for calls to DataSnapshot.getValue(SellerDB.class)
    }
//define order table in database
    public OrderDB(String customerName, String address, String itemName, int amount) {
        this.customerName = customerName;
        this.address = address;
        this.itemName = itemName;
        this.amount = amount;
    }

    // Getters and setters
    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
    public String getAddress() {
        return address;

    }
    public void setAddress (String address) {
        this.address = address;
    }
}
