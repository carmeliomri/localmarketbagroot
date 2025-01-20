package com.example.localmarketbagroot;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth auth;
    GoogleSignInClient googleSignInClient;
    ShapeableImageView imageView;
    TextView name, mail;
    private DatabaseReference databaseReference;

    private final ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if(result.getResultCode() == RESULT_OK) {
                Task<GoogleSignInAccount> accountTask = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                try {
                    GoogleSignInAccount signInAccount = accountTask.getResult(ApiException.class);
                    AuthCredential authCredential = GoogleAuthProvider.getCredential(signInAccount.getIdToken(), null);
                    auth.signInWithCredential(authCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()) {
                                auth = FirebaseAuth.getInstance();
                                String email = auth.getCurrentUser().getEmail();
                                Glide.with(MainActivity.this).load(Objects.requireNonNull(auth.getCurrentUser()).getPhotoUrl()).into(imageView);
                                name.setText(auth.getCurrentUser().getDisplayName());
                                mail.setText(auth.getCurrentUser().getEmail());
                                Toast.makeText(MainActivity.this, "Signed In Successfully", Toast.LENGTH_SHORT).show();
                                databaseReference = FirebaseDatabase.getInstance().getReference("sellers");
                                databaseReference.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.getChildrenCount()== 0)
                                        {
                                            //regular customer
                                            Intent intent = new Intent(MainActivity.this,CustomerActivity.class);
                                            intent.putExtra("USERNAME",auth.getCurrentUser().getDisplayName());
                                            startActivity(intent);
                                        }
                                        else {
                                            //seller
                                            Intent intent = new Intent(MainActivity.this,SellerActivity.class);
                                            intent.putExtra("USERNAME",auth.getCurrentUser().getDisplayName());
                                            startActivity(intent);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        Log.e("FirebaseData", "Error: " + databaseError.getMessage());
                                    }
                                });
                            } else {
                                Toast.makeText(MainActivity.this, "Failed to sign in: "+ task.getException(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } catch (ApiException e) {
                    e.printStackTrace();
                }
            }
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(this);
        imageView = findViewById(R.id.profileImage);
        name = findViewById(R.id.nameTV);
        mail = findViewById(R.id.mailTV);
        GoogleSignInOptions options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.client_id)).requestEmail().build();
        googleSignInClient = GoogleSignIn.getClient(MainActivity.this, options);
        auth = FirebaseAuth.getInstance();
        SignInButton signInButton = findViewById(R.id.signIn);
        signInButton.setOnClickListener (new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                   Intent intent = googleSignInClient.getSignInIntent();
                   activityResultLauncher.launch(intent);
            }
        });
        databaseReference = FirebaseDatabase.getInstance().getReference();
        //fillDatabase();


//        databaseReference.child("users").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    SellerDB sellerDB = snapshot.getValue(SellerDB.class);
//                    Log.d("FirebaseData", "SellerDB: " + sellerDB.getFirstName());
//                    Toast.makeText(MainActivity.this, "First name: "+ sellerDB.getFirstName(), Toast.LENGTH_SHORT).show();
//                }
//            }

//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                Log.e("FirebaseData", "Error: " + databaseError.getMessage());
//            }
//        });



    }
    protected void fillDatabase()
    {
        //build sellers table
        databaseReference.child("sellers").push().setValue(new SellerDB("Omri", "Carmeli", "carmeli.omri@gmail.com"))
            .addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "Data added successfully!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Failed to add data.", Toast.LENGTH_SHORT).show();
                }
            });
        databaseReference.child("sellers").push().setValue(new SellerDB("Lando", "Norris", "lando.norris@gmail.com"))
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(MainActivity.this, "Data added successfully!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Failed to add data.", Toast.LENGTH_SHORT).show();
                    }
                });
        //build the products table
        databaseReference.child("products").push().setValue(new ProductDB("Milk Chocolate", "Sweets", "https://lh3.googleusercontent.com/pw/AP1GczNMzQkM29JjvVvlql8cUmwk_CQqLO73OBflrq4pv-poeLyaXCHLcFXj2tHZvL3SjGVZnY-gH4cBzLvMSR0u1v4hDlSQoJej5Wcvm_41Ra5Y1rMXFjslPz1_IKBm9oW05rFnrwnFS-zeN5wrFutxnGjBSg=w3039-h456-s-no-gm?authuser=0", 10))
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(MainActivity.this, "Data added successfully!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Failed to add data.", Toast.LENGTH_SHORT).show();
                    }
                });
        databaseReference.child("products").push().setValue(new ProductDB("Milk", "Dairy", "https://lh3.googleusercontent.com/pw/AP1GczPOduqQn7yeoYc7kkYnudrgQPj65kCd-wuYPPb4c0TxZ6mvf_p6TIOAA9sDKYM3DuRaF0MsEnHPK0bXfZ--9u19jdA2pJkONSPTe7dxytE6DcColcnY0aVyxvjO9z9QHbIUpSwu84FqDjRINUvwKiZBXg=w3039-h456-s-no-gm?authuser=0", 5))
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(MainActivity.this, "Data added successfully!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Failed to add data.", Toast.LENGTH_SHORT).show();
                    }
                });
        databaseReference.child("products").push().setValue(new ProductDB("Carrots", "Vegetables", "https://lh3.googleusercontent.com/pw/AP1GczOP9IAsrlHBV5Wknm4OuCcJeJuP7yOfL3VcyQiCP86sl13YlOh0g4hh8TT643FksvIk8DY7wQnAt1hUWxW_qt9nfXGEJhbf2PuIY56_QlaNoqaRykamZo_WWyyHyALYK9mSr7OBd8HWwFXxFTTJrAb6xQ=w1319-h198-s-no-gm?authuser=0", 20))
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(MainActivity.this, "Data added successfully!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Failed to add data.", Toast.LENGTH_SHORT).show();
                    }
                });
        databaseReference.child("products").push().setValue(new ProductDB("Choclate Chips Cookies", "Sweets", "https://lh3.googleusercontent.com/pw/AP1GczOnTap2zVWD-Wgav7kKm-rYjGk6eZWHQLn7MIHFAvkr0xAybAfBlLoNPJxlsqPz6OWBf51Z6znKFPOVSp1FXEmpLeZhD-uU3ZZDcvUk4uBrOvrgnRkaqRXzgSNknrunKwkfTorjm9PZMEp0SBwbRSTRFg=w1319-h198-s-no-gm?authuser=0", 30))
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(MainActivity.this, "Data added successfully!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Failed to add data.", Toast.LENGTH_SHORT).show();
                    }
                });
        databaseReference.child("products").push().setValue(new ProductDB("Broom", "Misc", "https://lh3.googleusercontent.com/pw/AP1GczOTRqHPWfWUQQllGJtNzCDsUZRd_UvVxVUsn1VYMFlZtm75ug6iR4Zn28lkDGBZfl7n3-lkOqWjEM-bHgZgO4QekrV_7NYcHpVtHPQRSmVi2UNyZj92Kshf-7IhmV7C4qRH0ouQqye7Rb3Wl-qRWkJpvg=w1319-h198-s-no-gm?authuser=0", 12))
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(MainActivity.this, "Data added successfully!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Failed to add data.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}