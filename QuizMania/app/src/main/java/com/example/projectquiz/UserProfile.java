package com.example.projectquiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class UserProfile extends AppCompatActivity {

    // creating a variable for our
    private static final int LENGTH_LONG = 500000;
    // Database Reference for Firebase.

    DatabaseReference databaseReference;

    // variable references
    TextInputEditText profileName, profileEmail, profileMobile;
    TextView nameUser, nameDp;
    String userEnteredEmail;
    public ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        // Getting id or email which have been logged in
        Intent getEmailIntent = getIntent();
        userEnteredEmail = getEmailIntent.getStringExtra("email");

        // creating data base reference
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("user");
        Query checkUser = reference.orderByChild("pass_email").equalTo((userEnteredEmail));

        // now this part is fetch the data
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // fetching individual details
                    String Uname = dataSnapshot.child(userEnteredEmail).child("name").getValue(String.class);
                    String Uemail = dataSnapshot.child(userEnteredEmail).child("email").getValue(String.class);
                    String Umobile = dataSnapshot.child(userEnteredEmail).child("mobile").getValue(String.class);


                    // setting the values to the front end
                    nameUser = (TextView) findViewById(R.id.userName);
                    nameDp = (TextView) findViewById(R.id.userNameSmall);
                    profileName = (TextInputEditText) findViewById(R.id.fullNameProfile);
                    profileEmail = (TextInputEditText)findViewById(R.id.emailProfile);
                    profileMobile = (TextInputEditText)findViewById(R.id.mobileProfile);

                    // setting the data
                    nameUser.setText(Uname);
                    nameDp.setText(Uemail);
                    profileEmail.setText(Uemail);
                    profileMobile.setText(Umobile);
                    profileName.setText(Uname);

                    //Editing of text disabled here

                    profileName.setEnabled(false);
                    profileMobile.setEnabled(false);
                    profileEmail.setEnabled(false);

                    //Text color changed back to black from grey
                    nameUser.setTypeface(null, Typeface.BOLD);
                    profileName.setTextColor(Color.parseColor("#000000"));
                    profileEmail.setTextColor(Color.parseColor("#000000"));
                    profileMobile.setTextColor(Color.parseColor("#000000"));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // error message when database not found
                progressBar.setVisibility(View.GONE);
                Toast.makeText(UserProfile.this, "DataBase Error", Toast.LENGTH_LONG).show();
            }
        });
        MaterialCardView score,rank;
        score = findViewById(R.id.scoreInProfile);
        rank = findViewById(R.id.rankInProfile);
        score.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(UserProfile.this, Performance.class);
                        intent.putExtra("email", userEnteredEmail);
                        startActivity(intent);
                    }
                });

        rank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserProfile.this, Performance.class);
                intent.putExtra("email", userEnteredEmail);
                startActivity(intent);
            }
        });


    }

}


