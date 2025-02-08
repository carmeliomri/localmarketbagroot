package com.example.localmarketbagroot;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class CustomerActivity extends AppCompatActivity {

    String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_customer);
        Intent intent = getIntent();
        // Extract data from the intent
        username = intent.getStringExtra("USERNAME");
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;

        });


        Button cartButton = findViewById(R.id.cartButton);
        cartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyApp app = (MyApp) getApplicationContext();
                HashMap<String, CartItem> cart = app.getCart();
                if (cart.isEmpty())
                {
                    Toast.makeText(CustomerActivity.this, "Cart is empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(CustomerActivity.this,ViewCart.class);
                intent.putExtra("USERNAME",username);
                startActivity(intent);
            }
        });
        Button signoutButton = findViewById(R.id.signoutButton);
        signoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(CustomerActivity.this, "Signed Out Successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(CustomerActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
        ImageButton vegImgButton = findViewById(R.id.fruitsandvegetablesImage);
        vegImgButton.setOnClickListener(view -> {
            Intent intent2 = new Intent(CustomerActivity.this,PurchaseActivity.class);
            intent2.putExtra("USERNAME",username);
            intent2.putExtra("CATEGORY","FruitsVegetables");
            startActivity(intent2);
        });
        ImageButton dairyImgButton = findViewById(R.id.dairyImage);
        dairyImgButton.setOnClickListener(view -> {
            Intent intent2 = new Intent(CustomerActivity.this,PurchaseActivity.class);
            intent2.putExtra("USERNAME",username);
            intent2.putExtra("CATEGORY","Dairy");
            startActivity(intent2);
        });
        ImageButton sweetsImgButton = findViewById(R.id.sweetsImage);
        sweetsImgButton.setOnClickListener(view -> {
            Intent intent2 = new Intent(CustomerActivity.this,PurchaseActivity.class);
            intent2.putExtra("USERNAME",username);
            intent2.putExtra("CATEGORY","Sweets");
            startActivity(intent2);
        });
        ImageButton miscImgButton = findViewById(R.id.miscImage);
        miscImgButton.setOnClickListener(view -> {
            Intent intent2 = new Intent(CustomerActivity.this,PurchaseActivity.class);
            intent2.putExtra("USERNAME",username);
            intent2.putExtra("CATEGORY","Misc");
            startActivity(intent2);
        });

    }



}