package com.example.localmarketbagroot;

import android.app.Application;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

class CartItem {//define cart item
    public int amount;
    public String itemName;

}
public class MyApp extends Application {//create application class (global) that inherits from application (android class) in order to put a private variable of cart
    private HashMap<String, CartItem> cart;//private variable of cart (global). take url, give amount and name (cart item class)

    @Override
    public void onCreate() {
        super.onCreate();
        // Initialize your variables
        cart = new HashMap<String, CartItem>();
    }
    public HashMap<String, CartItem> getCart(){
        return cart;
    }

    public int getAmount(String URL) {
        CartItem item = cart.get(URL);
        if (item == null)
        {
            return 0;
        }
        return item.amount;
    }

    public void setAmount(String URL, String itemName, int amount) {
        CartItem item = new CartItem();
        item.amount = amount;
        item.itemName = itemName;
        cart.put(URL,item);
    }
}

