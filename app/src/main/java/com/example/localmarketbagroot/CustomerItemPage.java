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
//show the single item with price, url and image
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

        databaseReference = FirebaseDatabase.getInstance().getReference("products");//go to the products table in DB
        databaseReference.orderByChild("url").equalTo(url).addListenerForSingleValueEvent(new ValueEventListener() {//use url to query the item
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {//when query result returned
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ProductDB product = snapshot.getValue(ProductDB.class);
                    TextView textView = findViewById(R.id.itemPriceText);//show price from db
                    textView.setText(String.valueOf(product.getPrice()));
                    TextView textView2 = findViewById(R.id.itemNameText);//show item name from db
                    textView2.setText(product.getName());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {//error
                Log.e("FirebaseData", "Error: " + databaseError.getMessage());
            }
        });
        Glide.with(this)
                .load(url) //show items image using glide and url
                .into(imageView);
        Button addToCart = findViewById(R.id.addToCartButton);
        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {//clicked on add to cart
                TextView textView = findViewById(R.id.ammountText);
                int amount = Integer.parseInt(textView.getText().toString());//take amount from what user entered
                TextView textView2 = findViewById(R.id.itemNameText);//take item name
                String itemName = textView2.getText().toString();
                MyApp app = (MyApp) getApplicationContext();//send to cart (global) which is located in application class
                try {
                    app.setAmount(intent.getExtras().getString("URL"), itemName, amount);//put into map
                    Toast.makeText(CustomerItemPage.this, "added "+amount+" to cart",Toast.LENGTH_SHORT).show();
                    String username = intent.getStringExtra("USERNAME");
                    Intent intent2 = new Intent(CustomerItemPage.this,CustomerActivity.class);//after added send back to customer homepage
                    intent2.putExtra("USERNAME",username);
                    startActivity(intent2);
                } catch(Exception e){
                    Toast.makeText(CustomerItemPage.this, "Amount must be a number!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}