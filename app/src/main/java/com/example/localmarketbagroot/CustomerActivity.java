package com.example.localmarketbagroot;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;



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
        Button button2 = findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CustomerActivity.this,SellerActivity.class);
                intent.putExtra("USERNAME",username);
                startActivity(intent);
            }
        });

        Button dairyButton = findViewById(R.id.dairy);
        dairyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CustomerActivity.this,PurchaseActivity.class);
                intent.putExtra("USERNAME",username);
                intent.putExtra("CATEGORY","Dairy");
                startActivity(intent);
            }
        });
        Button fruitsAndVegtablesButton = findViewById(R.id.fruitsandvegetables);
        fruitsAndVegtablesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CustomerActivity.this,PurchaseActivity.class);
                intent.putExtra("USERNAME",username);
                intent.putExtra("CATEGORY","FruitsVegetables");
                startActivity(intent);
            }
        });
        Button miscButton = findViewById(R.id.misc);
        miscButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CustomerActivity.this,PurchaseActivity.class);
                intent.putExtra("USERNAME",username);
                intent.putExtra("CATEGORY","Misc");
                startActivity(intent);
            }
        });
        Button sweetsButton = findViewById(R.id.sweets);
        sweetsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CustomerActivity.this,PurchaseActivity.class);
                intent.putExtra("USERNAME",username);
                intent.putExtra("CATEGORY","Sweets");
                startActivity(intent);
            }
        });
    }



}