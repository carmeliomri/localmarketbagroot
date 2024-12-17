package com.example.localmarketbagroot;

import android.app.Application;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

public class MyApp extends Application {
    private HashMap<String, String> cart;

    @Override
    public void onCreate() {
        super.onCreate();
        // Initialize your variables
        cart = new HashMap<String, String>();
    }
    public HashMap<String, String> getCart(){
        Log.e("sws","**************************size: "+ cart.size());
        return cart;
    }

    public String getAmmount(String URL) {
        String ammount = cart.get(URL);
        if (ammount == null)
        {
            return "0";
        }
        return ammount;
    }

    public void setAmmount(String URL, String ammount) {
        cart.put(URL,ammount);
        Log.e("sws","**************************size: "+ cart.size());
    }
}

