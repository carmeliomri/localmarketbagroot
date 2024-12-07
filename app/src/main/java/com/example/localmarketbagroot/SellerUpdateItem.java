package com.example.localmarketbagroot;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;

public class SellerUpdateItem extends AppCompatActivity {

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

            // URL of the image to load
            String imageUrl = "https://via.placeholder.com/600";

            // Use Glide to load the image into the ImageView
            Glide.with(this)
                    .load(this.getIntent().getExtras().getString("URL")) // The image URL
                    //.placeholder(R.drawable.placeholder) // Optional placeholder while loading
                    //.error(R.drawable.error_image) // Optional error image if loading fails
                    .into(imageView);

            return insets;
        });
    }
}