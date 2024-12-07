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

public class SellerActivity extends AppCompatActivity implements ImageAdapter.OnImageClickListener {
    RecyclerView recyclerView;
    ImageAdapter imageAdapter;
    List<String> imageUrls;


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
        // recyclerView = findViewById(R.id.recyclerView);

// Set up how the list should look (e.g., vertically).
        //recyclerView.setLayoutManager(new LinearLayoutManager(this));

// Create some data for the list.
      //  List<String> items = new ArrayList<>();
       // items.add("Item 1");
        //items.add("Item 2");
        //items.add("Item 3");
        //items.add("Item 4");

// Set the adapter to the RecyclerView.
        //MyAdapter adapter = new MyAdapter(items);
        //recyclerView.setAdapter(adapter);

        // Initialize RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Dummy list of image URLs (replace with real URLs or resources)
        imageUrls = new ArrayList<>();
        imageUrls.add("https://i.natgeofe.com/n/4f5aaece-3300-41a4-b2a8-ed2708a0a27c/domestic-dog_thumb_square.jpg?wp=1&w=136&h=136");
        imageUrls.add("https://pettownsendvet.com/wp-content/uploads/2023/01/iStock-1052880600.jpg");
        imageUrls.add("https://i.guim.co.uk/img/media/fe1e34da640c5c56ed16f76ce6f994fa9343d09d/0_174_3408_2046/master/3408.jpg?width=1200&height=1200&quality=85&auto=format&fit=crop&s=67773a9d419786091c958b2ad08eae5e");

        // Initialize Adapter with Click Listener
        imageAdapter = new ImageAdapter(this, imageUrls, this);
        recyclerView.setAdapter(imageAdapter);
    }
    @Override
    public void onImageClick(int position) {
        String imageUrl = imageUrls.get(position);
        Toast.makeText(this, "Image clicked: " + position + " - " + imageUrl, Toast.LENGTH_SHORT).show();

        // Example: Navigate to another activity or perform any action
        // Intent intent = new Intent(this, DetailActivity.class);
        // intent.putExtra("image_url", imageUrl);
        // startActivity(intent);
    }
}