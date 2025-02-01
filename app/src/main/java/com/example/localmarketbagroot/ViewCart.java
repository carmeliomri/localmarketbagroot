package com.example.localmarketbagroot;

import android.os.Bundle;
import android.util.Log;
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
    private DatabaseReference databaseReference;
    private int totalPrice = 0;
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
        for (Map.Entry<String, String> entry : cart.entrySet()) {
            String url = entry.getKey();
            String ammount = entry.getValue();
            dataList.add(new CartDataModel(ammount, url));

            databaseReference = FirebaseDatabase.getInstance().getReference("products");
            databaseReference.orderByChild("url").equalTo(url).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        ProductDB product = snapshot.getValue(ProductDB.class);
                        totalPrice+= product.getPrice()*Integer.parseInt(ammount);
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

        // Set adapter
        CartAdapter adapter = new CartAdapter(this,dataList);
        recyclerView.setAdapter(adapter);
    }
}