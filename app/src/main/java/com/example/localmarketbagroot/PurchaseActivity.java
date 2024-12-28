package com.example.localmarketbagroot;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;



public class PurchaseActivity extends AppCompatActivity implements ItemsImageAdapter.OnImageClickListener{
    ItemsImageAdapter imageAdapter;
    List<String> imageUrls;

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
        RecyclerView recyclerView = findViewById(R.id.recyclerViewItemsPic);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        imageUrls = new ArrayList<>();
        imageUrls.add("https://lh3.googleusercontent.com/pw/AP1GczP2oJGP7tGn6_070_wvyRdAYqVAXqELzz83m8CL35TJfDaCSJi8tLLlm4flxNvbRSmaMIov7LNMgODnAXAYtXoQ7hVvSol-pi1yQmQkjVKy-CDXhubbvnDNblis32x0DWOBeBqoBVWI6CMGEUr5nnIiIA=w500-h100-s-no-gm?authuser=0");
        imageUrls.add("https://lh3.googleusercontent.com/pw/AP1GczOkUaGB-Kj4DnyrKBWeU6lICSTVDn2rw1YqQxwpvykmAnM68xA1dyolBkgTPkW5ZzqIdS1_dWlhEB-_5apE26ySQHOSwOV7h80oqrR9GMiKVTAw3bbvONJVp7rsHHNMbB3aIPl2ntM79F1n9P9SU2N-bw=w966-h1288-s-no-gm?authuser=0");
        imageUrls.add("https://lh3.googleusercontent.com/pw/AP1GczOKa7FIaAc7JKq3z6XlUeShgle1FJSzL1PM-y-eSXMvFgv6ip238HMULEt61_OXQkjvHqxzAiiv9-Hd27ZY2HVv9osPke_4h8WxXswG7v1lkHk8xgzQdRAFM9AsiZNkPKTDj8kx6Qe7R8KZ_lqm01DCCw=w955-h1271-s-no-gm?authuser=0");
        imageUrls.add("https://i.guim.co.uk/img/media/fe1e34da640c5c56ed16f76ce6f994fa9343d09d/0_174_3408_2046/master/3408.jpg?width=1200&height=1200&quality=85&auto=format&fit=crop&s=67773a9d419786091c958b2ad08eae5e");
        imageUrls.add("https://lh3.googleusercontent.com/pw/AP1GczNEN8fQlLB4HfMCAbTltXce54HN9sImxV8ADgTKeI5pbD0r39-uknqenccg2RRG4y_pvJ0QzygMqAex6OjdbkHAuwN_tT_Na8i5MBjxmXRIqW4jxzzJxvOupPcxjPz8S1SFHG-rj94eA8XbGEcvV_dMDA=w1319-h198-s-no-gm?authuser=0");
        imageUrls.add("https://lh3.googleusercontent.com/pw/AP1GczOP9IAsrlHBV5Wknm4OuCcJeJuP7yOfL3VcyQiCP86sl13YlOh0g4hh8TT643FksvIk8DY7wQnAt1hUWxW_qt9nfXGEJhbf2PuIY56_QlaNoqaRykamZo_WWyyHyALYK9mSr7OBd8HWwFXxFTTJrAb6xQ=w1319-h198-s-no-gm?authuser=0");
                                                            
        // Initialize Adapter with Click Listener
        imageAdapter = new ItemsImageAdapter(this, imageUrls, this);
        recyclerView.setAdapter(imageAdapter);
    }
    @Override
    public void onImageClick(int position) {
        String imageUrl = imageUrls.get(position);
        Intent intent = new Intent(PurchaseActivity.this,CustomerItemPage.class);
        intent.putExtra("URL",imageUrl);
        intent.putExtra("POSITION",position);
        startActivity(intent);

        // Example: Navigate to another activity or perform any action
        // Intent intent = new Intent(this, DetailActivity.class);
        // intent.putExtra("image_url", imageUrl);
        // startActivity(intent);
    }
}