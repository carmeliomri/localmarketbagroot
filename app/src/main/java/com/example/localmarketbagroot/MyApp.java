package com.example.localmarketbagroot;

import android.app.Application;

import java.util.HashMap;
import java.util.Map;

public class MyApp extends Application {
    private HashMap cart;

    @Override
    public void onCreate() {
        super.onCreate();
        // Initialize your variables
        cart = new HashMap();
    }

    public int getAmmount(String URL) {
        int ammount = (int) cart.get(URL);
        return ammount;
    }

    public void setAmmount(String URL, int ammount) {
        cart.put(URL,ammount);
    }
}

