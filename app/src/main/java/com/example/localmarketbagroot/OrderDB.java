package com.example.localmarketbagroot;

public class OrderDB {
    private String customerName;
    private String itemName;
    private int amount;

    public OrderDB() {
        // Default constructor required for calls to DataSnapshot.getValue(SellerDB.class)
    }

    public OrderDB(String customerName, String itemName, int amount) {
        this.customerName = customerName;
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
}
