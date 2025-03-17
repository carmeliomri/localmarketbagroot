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

//shows cart items and price
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
        RecyclerView recyclerView = findViewById(R.id.cartRecyclerView);
        // Iterate through HashMap
        MyApp app = (MyApp) getApplicationContext();//get the application class (global)
        List<CartDataModel> dataList = new ArrayList<>();
        HashMap<String, CartItem> cart = app.getCart();//get the cart from application class (global)
        for (Map.Entry<String, CartItem> entry : cart.entrySet()) {//for every item on the cart
            String url = entry.getKey();
            CartItem item = entry.getValue();
            int amount = item.amount;
            dataList.add(new CartDataModel(Integer.toString(amount), url));
            productsDatabase = FirebaseDatabase.getInstance().getReference("products");//go to the products table of the DB and find the item
            productsDatabase.orderByChild("url").equalTo(url).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {//add item's price to total price
                        ProductDB product = snapshot.getValue(ProductDB.class);
                        totalPrice+= product.getPrice()*amount;//update total price
                        TextView textView2 = findViewById(R.id.finalPriceText);
                        textView2.setText(String.valueOf(totalPrice));//show total price
                    }
                }


                @Override
                public void onCancelled(DatabaseError databaseError) {//error
                    Log.e("FirebaseData", "Error: " + databaseError.getMessage());
                }

            });

        }

        Button checkoutBtn = findViewById(R.id.checkoutBtn);
        checkoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewCart.this, DeliveryAddressMap.class);
                intent.putExtra("USERNAME",username);
                startActivity(intent);
            }
        });

        // Set adapter
        CartAdapter adapter = new CartAdapter(this,dataList);
        recyclerView.setAdapter(adapter);
    }
}