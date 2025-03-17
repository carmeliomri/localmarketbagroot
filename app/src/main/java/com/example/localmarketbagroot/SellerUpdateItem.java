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
            ImageView imageView = findViewById(R.id.imageView);//image view to show the image
            url = this.getIntent().getExtras().getString("URL");//show the image recieved from intent extra data from prev screen

            Glide.with(this)//use glide to show the pic
                    .load(url)
                    .into(imageView);
            databaseReference = FirebaseDatabase.getInstance().getReference("products");//serach the item's URL in products table in the database in order to present the price and url
            databaseReference.orderByChild("url").equalTo(url).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {//when database result returned
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        ProductDB product = snapshot.getValue(ProductDB.class);
                        TextView textView = findViewById(R.id.priceText);//show the price
                        textView.setText(String.valueOf(product.getPrice()));
                        TextView textView2 = findViewById(R.id.imageURLtext);//show the url
                        textView2.setText(product.getUrl());
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {//error
                    Log.e("FirebaseData", "Error: " + databaseError.getMessage());
                }
            });
            return insets;
        });

        Button updateButton = findViewById(R.id.updateButton);//changed data, wants to update the DB
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView textView = findViewById(R.id.priceText);//take new price
                TextView textView2 = findViewById(R.id.imageURLtext);//take new URL
                String newPrice = textView.getText().toString();
                String newUrl = textView2.getText().toString();
                Map<String, Object> updates = new HashMap<>();//create a map to update the database (only map varibale can be updated)
                updates.put("price",Integer.parseInt(newPrice));//change the price in the db
                updates.put("url",newUrl);//change the url in the db

                databaseReference = FirebaseDatabase.getInstance().getReference("products");//go to products table in the database
                databaseReference.orderByChild("url").equalTo(url).addListenerForSingleValueEvent(new ValueEventListener() {//pull the record of the specifc product
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            ProductDB product = snapshot.getValue(ProductDB.class);
                           snapshot.getRef().updateChildren(updates);//push the record of the specific product. actually updates the DB

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e("FirebaseData", "Error: " + databaseError.getMessage());//error
                    }
                });
                Intent intent = new Intent(SellerUpdateItem.this, SellerActivity.class);//after updated, send back to sellers home screen
                startActivity(intent);
            }
        });

    }
}