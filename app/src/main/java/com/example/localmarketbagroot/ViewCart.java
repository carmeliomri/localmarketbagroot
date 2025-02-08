package com.example.localmarketbagroot;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ViewCart extends AppCompatActivity {
    private DatabaseReference productsDatabase;
    private DatabaseReference ordersDatabase;
    private int totalPrice = 0;
    private String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_cart);
        Intent intent = getIntent();
        username = intent.getStringExtra("USERNAME");


        // Initialize RecyclerView
        RecyclerView recyclerView = findViewById(R.id.cartRecyclerView);

        // Create a list of DataModel objects

// Iterate through HashMap
        MyApp app = (MyApp) getApplicationContext();
        List<CartDataModel> dataList = new ArrayList<>();
        HashMap<String, CartItem> cart = app.getCart();
        for (Map.Entry<String, CartItem> entry : cart.entrySet()) {
            String url = entry.getKey();
            CartItem item = entry.getValue();
            int amount = item.amount;
            dataList.add(new CartDataModel(Integer.toString(amount), url));
            productsDatabase = FirebaseDatabase.getInstance().getReference("products");
            productsDatabase.orderByChild("url").equalTo(url).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        ProductDB product = snapshot.getValue(ProductDB.class);
                        totalPrice+= product.getPrice()*amount;
                        TextView textView2 = findViewById(R.id.finalPriceText);
                        textView2.setText(String.valueOf(totalPrice));
                    }
                }


                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.e("FirebaseData", "Error: " + databaseError.getMessage());
                }

            });

        }

        ordersDatabase = FirebaseDatabase.getInstance().getReference("orders");
        Button checkoutBtn = findViewById(R.id.checkoutBtn);
        checkoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyApp app = (MyApp) getApplicationContext();
                List<CartDataModel> dataList = new ArrayList<>();
                HashMap<String, CartItem> cart = app.getCart();
                for (Map.Entry<String, CartItem> entry : cart.entrySet()) {
                    String url = entry.getKey();
                    CartItem item = entry.getValue();
                    int amount = item.amount;
                    String name = item.itemName;
                    ordersDatabase.push().setValue(new OrderDB(username, name, amount));
                }
                Toast.makeText(ViewCart.this, "Order Placed", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ViewCart.this, CustomerActivity.class);
                startActivity(intent);
            }
        });

        // Set adapter
        CartAdapter adapter = new CartAdapter(this,dataList);
        recyclerView.setAdapter(adapter);
    }
}