package com.example.localmarketbagroot;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
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



public class PurchaseActivity extends AppCompatActivity implements ItemsImageAdapter.OnImageClickListener{
    ItemsImageAdapter imageAdapter;
    List<String> imageUrls;
    private DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_purchase);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        String category = this.getIntent().getExtras().getString("CATEGORY");
        if (category.equals("Dairy")) {
            ImageView imageView = findViewById(R.id.catTitleImage);
            imageView.setImageResource(R.drawable.dairy);
        }
        else if (category.equals("Misc")) {
            ImageView imageView = findViewById(R.id.catTitleImage);
            imageView.setImageResource(R.drawable.misc);
        }
        else if (category.equals("FruitsVegetables")) {
            ImageView imageView = findViewById(R.id.catTitleImage);
            imageView.setImageResource(R.drawable.vegtables);
        }
        else if (category.equals("Sweets")) {
            ImageView imageView = findViewById(R.id.catTitleImage);
            imageView.setImageResource(R.drawable.sweets);
        }
        RecyclerView recyclerView = findViewById(R.id.recyclerViewItemsPic);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        imageUrls = new ArrayList<>();
        imageAdapter = new ItemsImageAdapter(this, imageUrls,this);
        databaseReference = FirebaseDatabase.getInstance().getReference("products");
        databaseReference.orderByChild("category").equalTo(category).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ProductDB product = snapshot.getValue(ProductDB.class);
                    imageUrls.add(product.getUrl());
                }
                recyclerView.setAdapter(imageAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("FirebaseData", "Error: " + databaseError.getMessage());
            }
        });

    }


    @Override
    public void onImageClick(int position) {
        Intent intent1 = this.getIntent();
        String username = intent1.getStringExtra("USERNAME");
        String imageUrl = imageUrls.get(position);
        Intent intent = new Intent(PurchaseActivity.this,CustomerItemPage.class);
        intent.putExtra("URL",imageUrl);
        intent.putExtra("POSITION",position);
        intent.putExtra("USERNAME",username);
        startActivity(intent);

        // Example: Navigate to another activity or perform any action
        // Intent intent = new Intent(this, DetailActivity.class);
        // intent.putExtra("image_url", imageUrl);
        // startActivity(intent);
    }
}