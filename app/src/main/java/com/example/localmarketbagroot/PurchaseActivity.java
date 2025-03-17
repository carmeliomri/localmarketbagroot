package com.example.localmarketbagroot;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


//shows the items in the category
public class PurchaseActivity extends AppCompatActivity implements ItemsImageAdapter.OnImageClickListener{
    ItemsImageAdapter imageAdapter;
    List<String> imageUrls;
    private DatabaseReference databaseReference;


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


        String category = this.getIntent().getExtras().getString("CATEGORY");//get category from intents extra data
        if (category.equals("Dairy")) {
            ImageView imageView = findViewById(R.id.catTitleImage);
            imageView.setImageResource(R.drawable.dairy);//show the category image
        }
        else if (category.equals("Misc")) {
            ImageView imageView = findViewById(R.id.catTitleImage);
            imageView.setImageResource(R.drawable.misc);//show the category image
        }
        else if (category.equals("Vegetables")) {
            ImageView imageView = findViewById(R.id.catTitleImage);
            imageView.setImageResource(R.drawable.vegtables);//show the category image
        }
        else if (category.equals("Sweets")) {
            ImageView imageView = findViewById(R.id.catTitleImage);
            imageView.setImageResource(R.drawable.candy);//show the category image
        }
        RecyclerView recyclerView = findViewById(R.id.recyclerViewItemsPic);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        imageUrls = new ArrayList<>();//create a list of items in the category
        imageAdapter = new ItemsImageAdapter(this, imageUrls,this);
        databaseReference = FirebaseDatabase.getInstance().getReference("products");// go to the prodcuts table in DB
        databaseReference.orderByChild("category").equalTo(category).addListenerForSingleValueEvent(new ValueEventListener() {//query the category
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {//for every item, put URL in list
                    ProductDB product = snapshot.getValue(ProductDB.class);
                    imageUrls.add(product.getUrl());//add to list
                }
                recyclerView.setAdapter(imageAdapter);//show list using glide (in adapter)
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {//error
                Log.e("FirebaseData", "Error: " + databaseError.getMessage());
            }
        });

    }


    @Override
    public void onImageClick(int position) {//click on 1 of the items in the category
        Intent intent1 = this.getIntent();
        String username = intent1.getStringExtra("USERNAME");
        String imageUrl = imageUrls.get(position);
        Intent intent = new Intent(PurchaseActivity.this,CustomerItemPage.class);
        intent.putExtra("URL",imageUrl);//put on intents extra data for next page
        intent.putExtra("POSITION",position);//put on the intents extra data for next page (for dbg usese)
        intent.putExtra("USERNAME",username);//put on the intents extra data for next page
        startActivity(intent);//go
    }
}