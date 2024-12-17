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



        // Initialize RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerViewPic);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Dummy list of image URLs (replace with real URLs or resources)
        imageUrls = new ArrayList<>();
        imageUrls.add("https://lh3.googleusercontent.com/pw/AP1GczOTRqHPWfWUQQllGJtNzCDsUZRd_UvVxVUsn1VYMFlZtm75ug6iR4Zn28lkDGBZfl7n3-lkOqWjEM-bHgZgO4QekrV_7NYcHpVtHPQRSmVi2UNyZj92Kshf-7IhmV7C4qRH0ouQqye7Rb3Wl-qRWkJpvg=w3039-h456-s-no-gm?authuser=0");
        imageUrls.add("https://lh3.googleusercontent.com/pw/AP1GczOKa7FIaAc7JKq3z6XlUeShgle1FJSzL1PM-y-eSXMvFgv6ip238HMULEt61_OXQkjvHqxzAiiv9-Hd27ZY2HVv9osPke_4h8WxXswG7v1lkHk8xgzQdRAFM9AsiZNkPKTDj8kx6Qe7R8KZ_lqm01DCCw=w955-h1271-s-no-gm?authuser=0");
        imageUrls.add("https://i.guim.co.uk/img/media/fe1e34da640c5c56ed16f76ce6f994fa9343d09d/0_174_3408_2046/master/3408.jpg?width=1200&height=1200&quality=85&auto=format&fit=crop&s=67773a9d419786091c958b2ad08eae5e");

        // Initialize Adapter with Click Listener
        imageAdapter = new ImageAdapter(this, imageUrls, this);
        recyclerView.setAdapter(imageAdapter);
    }
    @Override
    public void onImageClick(int position) {
        String imageUrl = imageUrls.get(position);
        Intent intent = new Intent(SellerActivity.this,SellerUpdateItem.class);
        intent.putExtra("URL",imageUrl);
        intent.putExtra("POSITION",position);
        startActivity(intent);

        // Example: Navigate to another activity or perform any action
        // Intent intent = new Intent(this, DetailActivity.class);
        // intent.putExtra("image_url", imageUrl);
        // startActivity(intent);
    }
}