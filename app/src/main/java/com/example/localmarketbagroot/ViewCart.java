package com.example.localmarketbagroot;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewCart extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_cart);

        // Initialize RecyclerView
        RecyclerView recyclerView = findViewById(R.id.cartRecyclerView);

        // Create a list of DataModel objects

// Iterate through HashMap
        MyApp app = (MyApp) getApplicationContext();
        List<CartDataModel> dataList = new ArrayList<>();
        HashMap<String, String> cart = app.getCart();
        Toast.makeText(this, "map size:"+cart.size(), Toast.LENGTH_SHORT).show();
        for (Map.Entry<String, String> entry : cart.entrySet()) {
            String url = entry.getKey();
            String ammount = entry.getValue();
            dataList.add(new CartDataModel(ammount, url));
        }
        // Set adapter
        CartAdapter adapter = new CartAdapter(this,dataList);
        recyclerView.setAdapter(adapter);
    }
}