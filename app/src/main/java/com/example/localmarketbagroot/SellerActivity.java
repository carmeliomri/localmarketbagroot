package com.example.localmarketbagroot;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class SellerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_seller);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;

        });

        LinearLayout layout = findViewById(R.id.linear_Layout);
        Button newBtn = new Button(this);
        newBtn.setText("New Button");
        layout.addView(newBtn);
        newBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SellerActivity.this, "Button Clicked", Toast.LENGTH_SHORT).show();
            }
        });
        RecyclerView recyclerView = findViewById(R.id.recyclerView);

// Set up how the list should look (e.g., vertically).
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

// Create some data for the list.
        List<String> items = new ArrayList<>();
        items.add("Item 1");
        items.add("Item 2");
        items.add("Item 3");
        items.add("Item 4");

// Set the adapter to the RecyclerView.
        MyAdapter adapter = new MyAdapter(items);
        recyclerView.setAdapter(adapter);



    }
}