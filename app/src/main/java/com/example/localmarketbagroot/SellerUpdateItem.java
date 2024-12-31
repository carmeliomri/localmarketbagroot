package com.example.localmarketbagroot;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class SellerUpdateItem extends AppCompatActivity {
    private DatabaseReference databaseReference;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_seller_update_item);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            Toast.makeText(SellerUpdateItem.this, this.getIntent().getExtras().getInt("POSITION")+ "*******" +this.getIntent().getExtras().getString("URL"), Toast.LENGTH_SHORT).show();
            // Reference to the ImageView
            ImageView imageView = findViewById(R.id.imageView);
            url = this.getIntent().getExtras().getString("URL");

            // Use Glide to load the image into the ImageView
            Glide.with(this)
                    .load(url) // The image URL
                    //.placeholder(R.drawable.placeholder) // Optional placeholder while loading
                    //.error(R.drawable.error_image) // Optional error image if loading fails
                    .into(imageView);
            databaseReference = FirebaseDatabase.getInstance().getReference("products");
            databaseReference.orderByChild("url").equalTo(url).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        ProductDB product = snapshot.getValue(ProductDB.class);
                        TextView textView = findViewById(R.id.priceText);
                        textView.setText(String.valueOf(product.getPrice()));
                        TextView textView2 = findViewById(R.id.imageURLtext);
                        textView2.setText(product.getUrl());
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.e("FirebaseData", "Error: " + databaseError.getMessage());
                }
            });
            return insets;
        });

        Button updateButton = findViewById(R.id.updateButton);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView textView = findViewById(R.id.priceText);
                TextView textView2 = findViewById(R.id.imageURLtext);
                String newPrice = textView.getText().toString();
                String newUrl = textView2.getText().toString();
                Map<String, Object> updates = new HashMap<>();
                updates.put("price",Integer.parseInt(newPrice));
                updates.put("url",newUrl);

                databaseReference = FirebaseDatabase.getInstance().getReference("products");
                databaseReference.orderByChild("url").equalTo(url).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            ProductDB product = snapshot.getValue(ProductDB.class);
                           snapshot.getRef().updateChildren(updates);

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e("FirebaseData", "Error: " + databaseError.getMessage());
                    }
                });
                Intent intent = new Intent(SellerUpdateItem.this, SellerActivity.class);
                startActivity(intent);
            }
        });

    }
}