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
    }
    protected void fillDatabase()
    {
        //build sellers table
        databaseReference.child("sellers").push().setValue(new SellerDB("Omri", "Carmeli", "carmeli.omri@gmail.com"))
            .addOnCompleteListener(task -> {
                if (!task.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "Failed to add data.", Toast.LENGTH_SHORT).show();
                }
            });
        databaseReference.child("sellers").push().setValue(new SellerDB("Lando", "Norris", "lando.norris@gmail.com"))
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Toast.makeText(MainActivity.this, "Failed to add data.", Toast.LENGTH_SHORT).show();
                    }
                });


        //build the products table
        databaseReference.child("products").push().setValue(new ProductDB("Milk Chocolate", "Sweets", "https://lh3.googleusercontent.com/pw/AP1GczNMzQkM29JjvVvlql8cUmwk_CQqLO73OBflrq4pv-poeLyaXCHLcFXj2tHZvL3SjGVZnY-gH4cBzLvMSR0u1v4hDlSQoJej5Wcvm_41Ra5Y1rMXFjslPz1_IKBm9oW05rFnrwnFS-zeN5wrFutxnGjBSg=w3039-h456-s-no-gm?authuser=0", 10))
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Toast.makeText(MainActivity.this, "Failed to add data.", Toast.LENGTH_SHORT).show();
                    }
                });
        databaseReference.child("products").push().setValue(new ProductDB("Chocolate Chips Cookies", "Sweets", "https://lh3.googleusercontent.com/pw/AP1GczOnTap2zVWD-Wgav7kKm-rYjGk6eZWHQLn7MIHFAvkr0xAybAfBlLoNPJxlsqPz6OWBf51Z6znKFPOVSp1FXEmpLeZhD-uU3ZZDcvUk4uBrOvrgnRkaqRXzgSNknrunKwkfTorjm9PZMEp0SBwbRSTRFg=w1319-h198-s-no-gm?authuser=0", 30))
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Toast.makeText(MainActivity.this, "Failed to add data.", Toast.LENGTH_SHORT).show();
                    }
                });
        databaseReference.child("products").push().setValue(new ProductDB("Dark Chocolate", "Sweets", "https://lh3.googleusercontent.com/pw/AP1GczMnTg-5WcCw6qBVUuS-4Yl2fLtYwEhBAwtU-zQ8FdTHU8wPOnYfJHO1Agpaa9ih2Jz5TWsKbhRnViALX7rz15vhcM3n2cEa0xp81DTKdqeNtG5u4WJnXFjxd2UjxV8bcTsdVcJUu4GEkgRTXKelK1lB3g=w3039-h460-s-no-gm?authuser=0", 12))
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Toast.makeText(MainActivity.this, "Failed to add data.", Toast.LENGTH_SHORT).show();
                    }
                });
        databaseReference.child("products").push().setValue(new ProductDB("Mars Bar", "Sweets", "https://lh3.googleusercontent.com/pw/AP1GczNW1R4QGebdXAF3-BEeSlzK1hgwdiVCMAEPFqFfejprVsKWcrWt-v27WUNfdY8e7kMJA3tzjfQRaTpqbIhAEl5mXxEzRb0pa6BVRChqeBakPE4pr9dgbuwQxlIusWQfSqs9usweFgm-i5SytFpVm5UIkQ=w3039-h456-s-no-gm?authuser=0", 4))
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Toast.makeText(MainActivity.this, "Failed to add data.", Toast.LENGTH_SHORT).show();
                    }
                });
        databaseReference.child("products").push().setValue(new ProductDB("Rainbow Blast Lollipop", "Sweets", "https://lh3.googleusercontent.com/pw/AP1GczMNRGREl-V2Jzvg9QKoIp2NxKbvWXLWYnhlhnutG1Z_K_azWMkSohORe_QdpZu9x-XKPoevy03X7Bhhl75iQFFSbkjPPZDWwym94iaO9CSkOj0YPZoeE2hbvGGFSqh8LytVQPJKzYqywjB24SpX99XxZw=w3039-h459-s-no-gm?authuser=0", 10))
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Toast.makeText(MainActivity.this, "Failed to add data.", Toast.LENGTH_SHORT).show();
                    }
                });
        databaseReference.child("products").push().setValue(new ProductDB("Snickers Bar", "Sweets", "https://lh3.googleusercontent.com/pw/AP1GczOi1TwFRaANT30e-eczH2Sw7rViW_TDX1W7umgCCb36MxYiAJ90tXx6Fe_CsbrK04EcHnccmfM-f4vp55C-JzndSsY7pneM7MPM8gR8ub9BQvoFqYVNbHnLV6xkT7hlgQIs_XMDLbfFKnQqBPgYVMMCLA=w3039-h456-s-no-gm?authuser=0", 4))
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Toast.makeText(MainActivity.this, "Failed to add data.", Toast.LENGTH_SHORT).show();
                    }
                });


        databaseReference.child("products").push().setValue(new ProductDB("Milk", "Dairy", "https://lh3.googleusercontent.com/pw/AP1GczPOduqQn7yeoYc7kkYnudrgQPj65kCd-wuYPPb4c0TxZ6mvf_p6TIOAA9sDKYM3DuRaF0MsEnHPK0bXfZ--9u19jdA2pJkONSPTe7dxytE6DcColcnY0aVyxvjO9z9QHbIUpSwu84FqDjRINUvwKiZBXg=w3039-h456-s-no-gm?authuser=0", 5))
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Toast.makeText(MainActivity.this, "Failed to add data.", Toast.LENGTH_SHORT).show();
                    }
                });
        databaseReference.child("products").push().setValue(new ProductDB("Camembert Cheese", "Dairy", "https://lh3.googleusercontent.com/pw/AP1GczNEN8fQlLB4HfMCAbTltXce54HN9sImxV8ADgTKeI5pbD0r39-uknqenccg2RRG4y_pvJ0QzygMqAex6OjdbkHAuwN_tT_Na8i5MBjxmXRIqW4jxzzJxvOupPcxjPz8S1SFHG-rj94eA8XbGEcvV_dMDA=w3039-h456-s-no-gm?authuser=0", 50))
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Toast.makeText(MainActivity.this, "Failed to add data.", Toast.LENGTH_SHORT).show();
                    }
                });
        databaseReference.child("products").push().setValue(new ProductDB("Low Fat Milk", "Dairy", "https://lh3.googleusercontent.com/pw/AP1GczMN8EOR4uhq8vihWPMmQ1RAYs_7eRLRmMnBuotRwpFae8kxVscQ9AbPJSHXTP389-8G9s3OQ8pgofxpaBEDbt8P14lB1QVqLLMvrui2QDmnFdyOSTCm2uD0ZIvzhLO45tKWyvwfB739KFXNGw0jbvZX6g=w3039-h456-s-no-gm?authuser=0", 7))
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Toast.makeText(MainActivity.this, "Failed to add data.", Toast.LENGTH_SHORT).show();
                    }
                });
        databaseReference.child("products").push().setValue(new ProductDB("Strawberry Yogurt", "Dairy", "https://lh3.googleusercontent.com/pw/AP1GczPEVsyYVJLvu8-d69YzWvmO-y5IwGifrnE3bZ0O63R_z1gtyV1SHs3LBTw6gKIGGoZej8x2pbJVLO_Xx81SdI_lzIGYafSDPYwr43-GWeMnRLXWhcQpryyJss3aYW44oEpYjNyJAuD39pJf7TmkzMw8EA=w3039-h456-s-no-gm?authuser=0", 10))
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Toast.makeText(MainActivity.this, "Failed to add data.", Toast.LENGTH_SHORT).show();
                    }
                });
        databaseReference.child("products").push().setValue(new ProductDB("Swiss Cheese", "Dairy", "https://lh3.googleusercontent.com/pw/AP1GczMK_W2oIvlnBxQrP94T9dZtmcfkbB_cfDOt2vBdrGa8sjzmE3MY8B0c2GvwEA3HiFJY1StJsQRumV53J4rI-7zrOylAplOvdF6xwMrEe_IlrzalTmWH9An1tCSdzDN0-aW8dWSQuDqeT-nZob-Sy5VVmg=w3039-h456-s-no-gm?authuser=0", 20))
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Toast.makeText(MainActivity.this, "Failed to add data.", Toast.LENGTH_SHORT).show();
                    }
                });
        databaseReference.child("products").push().setValue(new ProductDB("Yogurt", "Dairy", "https://lh3.googleusercontent.com/pw/AP1GczMLjKD8H0Ed7KzY00NI4Rt39Iy1x05MToaULEXLU_DCsYdMJnNV8A702K0MEXOYwuMP4f_luGyhovdb_bQvLet5peQQkpmFDaBMQ6arthGIqiY60BhAVWprVcD_OAnsBCHDbBjQC2LC2lIvd-h5e5o_vg=w3039-h456-s-no-gm?authuser=0", 5))
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Toast.makeText(MainActivity.this, "Failed to add data.", Toast.LENGTH_SHORT).show();
                    }
                });


        databaseReference.child("products").push().setValue(new ProductDB("Carrots", "Vegetables", "https://lh3.googleusercontent.com/pw/AP1GczOP9IAsrlHBV5Wknm4OuCcJeJuP7yOfL3VcyQiCP86sl13YlOh0g4hh8TT643FksvIk8DY7wQnAt1hUWxW_qt9nfXGEJhbf2PuIY56_QlaNoqaRykamZo_WWyyHyALYK9mSr7OBd8HWwFXxFTTJrAb6xQ=w1319-h198-s-no-gm?authuser=0", 20))
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Toast.makeText(MainActivity.this, "Failed to add data.", Toast.LENGTH_SHORT).show();
                    }
                });
        databaseReference.child("products").push().setValue(new ProductDB("Cabbage", "Vegetables", "https://lh3.googleusercontent.com/pw/AP1GczPEdhn80TAM9PKYnnRAQNdQq2UmnL70bidPFNS3U8InX2mD2x2e7I1OSW_daCKHyIL1TQfAKK3iWqT6OyQSlSsP9WvIuN7oAgKY2uzjd_8IGmMjD9P1C17YUkJHwa42_l4iWPlq7iYo7N7OZEx9JKKOlg=w3039-h456-s-no-gm?authuser=0", 30))
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Toast.makeText(MainActivity.this, "Failed to add data.", Toast.LENGTH_SHORT).show();
                    }
                });
        databaseReference.child("products").push().setValue(new ProductDB("Cucumbers", "Vegetables", "https://lh3.googleusercontent.com/pw/AP1GczPHSrk2VBzpjO_l6OK_Z1IZF0CTrGqpQ9mMed48XvyeAEwl04k8feQZ3KFyQ_-WmsHWh6T6xn__KnIDFqe7m7qiQhBJnOnFnfeOPypbO7K084ZJzt07igaFnXOTlYFdZ_dETy7nY4OXscvukRGcXiQ8bg=w3039-h456-s-no-gm?authuser=0", 15))
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Toast.makeText(MainActivity.this, "Failed to add data.", Toast.LENGTH_SHORT).show();
                    }
                });
        databaseReference.child("products").push().setValue(new ProductDB("Lettuce Hearts", "Vegetables", "https://lh3.googleusercontent.com/pw/AP1GczO3TUc5Sl6nY9VLCYbOYc5NJllmpX0GciTV9VPwJCu0E_YhoMLUNTfbrtV5rHyxaLp90XHIPYPQsut-pEG_bP6zKzhnkPFDI2q08FDDucMpRIGZrZ_psHX145a0yyAEzli6ffkJotKgR9_DUe2YAecccQ=w3039-h456-s-no-gm?authuser=0", 25))
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Toast.makeText(MainActivity.this, "Failed to add data.", Toast.LENGTH_SHORT).show();
                    }
                });
        databaseReference.child("products").push().setValue(new ProductDB("Potatoes", "Vegetables", "https://lh3.googleusercontent.com/pw/AP1GczMcuGKQMj0WGdsCwLoSRemMv-cs6pVs5PkpPT4wqpkb9U6AhfoZNQmresA0u38POII75B1lgzbWuqIZOy228DR8YHqMcbGNauKKeME7CpLaQAICMHbuIw3tdGMwz2Er_veB2-VrhQjRSrRPW7qGFp3etw=w3039-h456-s-no-gm?authuser=0", 15))
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Toast.makeText(MainActivity.this, "Failed to add data.", Toast.LENGTH_SHORT).show();
                    }
                });
        databaseReference.child("products").push().setValue(new ProductDB("Tomatoes", "Vegetables", "https://lh3.googleusercontent.com/pw/AP1GczPBx7-Hi8GOppLoDYZbU6Y8Ne3gcK1lGJBuGe9decfauB_Iu8uAq7x7mx9XFWx_ooPcRl-uYKBqQQW0QQGl2Pfao70bT5Y27feJQxgWpenEJcN6KvG2kCHCtg-1SgAv_tp1fuy_8fMneGiEsgMwgJzOMQ=w3039-h456-s-no-gm?authuser=0", 20))
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Toast.makeText(MainActivity.this, "Failed to add data.", Toast.LENGTH_SHORT).show();
                    }
                });


        databaseReference.child("products").push().setValue(new ProductDB("Broom", "Misc", "https://lh3.googleusercontent.com/pw/AP1GczOTRqHPWfWUQQllGJtNzCDsUZRd_UvVxVUsn1VYMFlZtm75ug6iR4Zn28lkDGBZfl7n3-lkOqWjEM-bHgZgO4QekrV_7NYcHpVtHPQRSmVi2UNyZj92Kshf-7IhmV7C4qRH0ouQqye7Rb3Wl-qRWkJpvg=w1319-h198-s-no-gm?authuser=0", 12))
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Toast.makeText(MainActivity.this, "Failed to add data.", Toast.LENGTH_SHORT).show();
                    }
                });
        databaseReference.child("products").push().setValue(new ProductDB("Dishes Cleaning Liquid", "Misc", "https://lh3.googleusercontent.com/pw/AP1GczPrIeNcmFloTpfIlfgnaiWDwyrQfEL0_17hOyCtmnzQgSg9ldOaKUCMlLexM7gtRDT65lMkKXcl3o17u9NHj4vwHXowClcq3ueFw0L9cUzQ27Nvn_071cT0BCGXS04gy2XlNh2NmJsi4dHUYL6mmu8VoA=w3039-h456-s-no-gm?authuser=0", 18))
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Toast.makeText(MainActivity.this, "Failed to add data.", Toast.LENGTH_SHORT).show();
                    }
                });
        databaseReference.child("products").push().setValue(new ProductDB("Plastic Chair", "Misc", "https://lh3.googleusercontent.com/pw/AP1GczPvstmC1fEym3OnWIrBJrTWLWBYu8k0S1erCEHpPrSArAzZx6QW_sVNxuHZc8YHV3HXzQVW7tjrRl7pc6Gnc1fZSiTNN2qSx0VAdDeHu2rwdlLZwD8s408eshNXAiD3BwObviVSo2buI1PJCqP2f52J-g=w3039-h456-s-no-gm?authuser=0", 35))
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Toast.makeText(MainActivity.this, "Failed to add data.", Toast.LENGTH_SHORT).show();
                    }
                });
        databaseReference.child("products").push().setValue(new ProductDB("Sandwich Bags", "Misc", "https://lh3.googleusercontent.com/pw/AP1GczPAynIu3IxWKjH3tTR8b2g35VoHcjeNu9i6XcqWZb7ZhlTpzEi8LTQN7oPcG87-UTxVwMre_Q1meCWEbAg6T8HLNlp1tcUTKFU7zBWlSG_6J9lL1QTb6FxKzP0v-VP0UoyDoh_-T4YX2Q0exKojK_aTcA=w3039-h456-s-no-gm?authuser=0", 15))
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Toast.makeText(MainActivity.this, "Failed to add data.", Toast.LENGTH_SHORT).show();
                    }
                });
        databaseReference.child("products").push().setValue(new ProductDB("Shampoo", "Misc", "https://lh3.googleusercontent.com/pw/AP1GczOYVBQedDdA8Q1J66YpaFCGcH9OHoUjTYXl7igxhsbO662_a8HebDI09mLJIQ9vX6cpidsR9t8EnCXxKEpqVc5tFQLH-ydECE0ZdrPM0kg73z9f2pWAth6vBtCpPSfQWh8-2XnEQTtme2tgzwYuYTdzCw=w3039-h456-s-no-gm?authuser=0", 17))
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Toast.makeText(MainActivity.this, "Failed to add data.", Toast.LENGTH_SHORT).show();
                    }
                });
        databaseReference.child("products").push().setValue(new ProductDB("Soap", "Misc", "https://lh3.googleusercontent.com/pw/AP1GczOC7bkmImeUziqhhsUWD4RbBrnMLNddkejpKHb05z8MofJg8gOIMPYfncTqp83W9TE597Le7SmbcxUD-o6-Q5ChDQrvZpmUUEWNxc742YJlHEBn1NDnQ4LqtzDFh0k9o-yjfHsc_llouPAKDHyqFyAGfA=w3039-h456-s-no-gm?authuser=0", 10))
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Toast.makeText(MainActivity.this, "Failed to add data.", Toast.LENGTH_SHORT).show();
                    }
                });

    }

}