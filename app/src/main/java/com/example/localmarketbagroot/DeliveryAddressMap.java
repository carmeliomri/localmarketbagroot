package com.example.localmarketbagroot;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
//show map checkout screen

public class DeliveryAddressMap extends AppCompatActivity implements OnMapReadyCallback {
    private DatabaseReference ordersDatabase;
    private String username;
    private GoogleMap mMap;
    private Geocoder mGeocoder;
    private String mAddress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_delivery_address_map);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // Initialize the Map Fragment and set the callback
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Button showMap = findViewById(R.id.showMap);
        showMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView textView = findViewById(R.id.deliveryAddressText);//take address user typed
                 mAddress = textView.getText().toString();
                try {//try to convert adrdress to corodinates
                    List<Address> addressList = mGeocoder.getFromLocationName(mAddress, 1);//actually try
                    if (addressList != null && addressList.size() > 0) {//if successfully converted to cordinates
                        Address location = addressList.get(0);//take the first option (0)
                        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());//actually convert to lat and long

                        // Add a marker on the map and move the camera
                        mMap.addMarker(new MarkerOptions().position(latLng).title("Marker at: " + mAddress));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15)); // Zoom level 15
                    } else {//failed to convert the address to cordiantes. user error
                        Toast.makeText(DeliveryAddressMap.this, "Address not found", Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(DeliveryAddressMap.this, "Geocoder service is not available", Toast.LENGTH_SHORT).show();
                }
            }
        });
        Intent intent = getIntent();
        username = intent.getStringExtra("USERNAME");
        ordersDatabase = FirebaseDatabase.getInstance().getReference("orders");
        Button submitOrderBtn = findViewById(R.id.submitOrderBtn);
        submitOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {//when submit order
                if (mAddress == null) {//no address entered, user error
                    Toast.makeText(DeliveryAddressMap.this, "Please enter an address", Toast.LENGTH_SHORT).show();
                    return;
                }
                MyApp app = (MyApp) getApplicationContext();//go to application class (global)
                List<CartDataModel> dataList = new ArrayList<>();
                HashMap<String, CartItem> cart = app.getCart();//get cart from application class (global)
                for (Map.Entry<String, CartItem> entry : cart.entrySet()) {//for every item in cart
                    String url = entry.getKey();
                    CartItem item = entry.getValue();
                    int amount = item.amount;
                    String name = item.itemName;
                    ordersDatabase.push().setValue(new OrderDB(username, mAddress, name, amount));//push the username, address, item name and amount to the DB. ENTER FINAL ORDER TO DB
                }
                Toast.makeText(DeliveryAddressMap.this, "Order Placed", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(DeliveryAddressMap.this, CustomerActivity.class);
                startActivity(intent);
            }
        });

    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mGeocoder = new Geocoder(this);
    }
}