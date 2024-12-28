package com.example.localmarketbagroot;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SellerActivity extends AppCompatActivity implements ImageAdapter.OnImageClickListener {

    ImageAdapter imageAdapter;
    List<String> imageUrls;
    private DatabaseReference databaseReference;


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
//        imageUrls.add("https://lh3.googleusercontent.com/pw/AP1GczOTRqHPWfWUQQllGJtNzCDsUZRd_UvVxVUsn1VYMFlZtm75ug6iR4Zn28lkDGBZfl7n3-lkOqWjEM-bHgZgO4QekrV_7NYcHpVtHPQRSmVi2UNyZj92Kshf-7IhmV7C4qRH0ouQqye7Rb3Wl-qRWkJpvg=w3039-h456-s-no-gm?authuser=0");
//        imageUrls.add("https://lh3.googleusercontent.com/pw/AP1GczPEdhn80TAM9PKYnnRAQNdQq2UmnL70bidPFNS3U8InX2mD2x2e7I1OSW_daCKHyIL1TQfAKK3iWqT6OyQSlSsP9WvIuN7oAgKY2uzjd_8IGmMjD9P1C17YUkJHwa42_l4iWPlq7iYo7N7OZEx9JKKOlg=w1319-h198-s-no-gm?authuser=0");
//        imageUrls.add("https://i.guim.co.uk/img/media/fe1e34da640c5c56ed16f76ce6f994fa9343d09d/0_174_3408_2046/master/3408.jpg?width=1200&height=1200&quality=85&auto=format&fit=crop&s=67773a9d419786091c958b2ad08eae5e");

        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("products").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ProductDB product = snapshot.getValue(ProductDB.class);
                    imageUrls.add(product.getUrl());
                    Log.e("Edwa", "************** got url*********: " + product.getUrl());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("FirebaseData", "Error: " + databaseError.getMessage());
            }
        });


        // Initialize Adapter with Click Listener
        imageAdapter = new ImageAdapter(this, imageUrls, this);
        recyclerView.setAdapter(imageAdapter);
        Log.e("dwasdw", "added image urls");
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