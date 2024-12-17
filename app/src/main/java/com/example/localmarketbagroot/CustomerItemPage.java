package com.example.localmarketbagroot;

import android.content.Intent;
import android.os.Bundle;
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

public class CustomerItemPage extends AppCompatActivity {

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
        ImageView imageView = findViewById(R.id.imageViewItem);
        Intent intent = this.getIntent();


        // Use Glide to load the image into the ImageView
        Glide.with(this)
                .load(this.getIntent().getExtras().getString("URL")) // The image URL
                //.placeholder(R.drawable.placeholder) // Optional placeholder while loading
                //.error(R.drawable.error_image) // Optional error image if loading fails
                .into(imageView);
        Button addToCart = findViewById(R.id.addToCartButton);
        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView textView = findViewById(R.id.ammountText);
                String amount = textView.getText().toString();
                MyApp app = (MyApp) getApplicationContext();
                try {
                    app.setAmmount(intent.getExtras().getString("URL"), amount);//put into map
                    Toast.makeText(CustomerItemPage.this, "added "+amount+" to cart",Toast.LENGTH_SHORT).show();
                    Intent intent2 = new Intent(CustomerItemPage.this,CustomerActivity.class);
                    startActivity(intent2);
                } catch(Exception e){
                    Toast.makeText(CustomerItemPage.this, "Amount must be a number!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}