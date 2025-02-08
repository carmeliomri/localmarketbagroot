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

public class CustomerItemPage extends AppCompatActivity {
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_customer_item_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        String url = this.getIntent().getExtras().getString("URL");
        ImageView imageView = findViewById(R.id.imageViewItem);
        Intent intent = this.getIntent();

        databaseReference = FirebaseDatabase.getInstance().getReference("products");
        databaseReference.orderByChild("url").equalTo(url).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ProductDB product = snapshot.getValue(ProductDB.class);
                    TextView textView = findViewById(R.id.itemPriceText);
                    textView.setText(String.valueOf(product.getPrice()));
                    TextView textView2 = findViewById(R.id.itemNameText);
                    textView2.setText(product.getName());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("FirebaseData", "Error: " + databaseError.getMessage());
            }
        });
        // Use Glide to load the image into the ImageView
        Glide.with(this)
                .load(url) // Replace with the actual image URL)) // The image URL
                //.placeholder(R.drawable.placeholder) // Optional placeholder while loading
                //.error(R.drawable.error_image) // Optional error image if loading fails
                .into(imageView);
        Button addToCart = findViewById(R.id.addToCartButton);
        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView textView = findViewById(R.id.ammountText);
                int amount = Integer.parseInt(textView.getText().toString());
                TextView textView2 = findViewById(R.id.itemNameText);
                String itemName = textView2.getText().toString();
                MyApp app = (MyApp) getApplicationContext();
                try {
                    app.setAmount(intent.getExtras().getString("URL"), itemName, amount);//put into map
                    Toast.makeText(CustomerItemPage.this, "added "+amount+" to cart",Toast.LENGTH_SHORT).show();
                    String username = intent.getStringExtra("USERNAME");
                    Intent intent2 = new Intent(CustomerItemPage.this,CustomerActivity.class);
                    intent2.putExtra("USERNAME",username);
                    startActivity(intent2);
                } catch(Exception e){
                    Toast.makeText(CustomerItemPage.this, "Amount must be a number!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}