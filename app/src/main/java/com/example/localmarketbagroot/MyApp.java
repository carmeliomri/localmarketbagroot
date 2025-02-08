package com.example.localmarketbagroot;

import android.app.Application;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

class CartItem {
    public int amount;
    public String itemName;

}
public class MyApp extends Application {
    private HashMap<String, CartItem> cart;

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

