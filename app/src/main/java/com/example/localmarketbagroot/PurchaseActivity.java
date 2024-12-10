package com.example.localmarketbagroot;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class PurchaseActivity extends AppCompatActivity {

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
    }
}