package com.example.localmarketbagroot;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SellerActivity extends AppCompatActivity implements ImageAdapter.OnImageClickListener {

    ImageAdapter imageAdapter;
    List<String> imageUrls;
    private DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_seller);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;

        });



        // Initialize RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerViewPic);//create recycler view where all items appear from database.
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // create a list of all products's images from the database
        imageUrls = new ArrayList<>();//new list
        imageAdapter = new ImageAdapter(this, imageUrls, this);//new adapter for database

        databaseReference = FirebaseDatabase.getInstance().getReference();//database
        databaseReference.child("products").addValueEventListener(new ValueEventListener() {// actually query the products table from the database
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {//when database result returned
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ProductDB product = snapshot.getValue(ProductDB.class);
                    imageUrls.add(product.getUrl());//add each prodcut to the LIST (not recycler view yet)
                }
                recyclerView.setAdapter(imageAdapter);//give the list to the recycler view
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {//when query failed
                Log.e("FirebaseData", "Error: " + databaseError.getMessage());
            }
        });
        Button signoutButton = findViewById(R.id.signoutButton);//sign out button
        signoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(SellerActivity.this, "Signed Out Successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SellerActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });




    }
    @Override
    public void onImageClick(int position) {//seller clicked on a product --> go to update item screen
        String imageUrl = imageUrls.get(position);
        Intent intent = new Intent(SellerActivity.this,SellerUpdateItem.class);
        intent.putExtra("URL",imageUrl);//gives images url to the next activity to show the image on the top of the next screen. using glide.
        intent.putExtra("POSITION",position);//position in the list.
        startActivity(intent);
    }

}