package com.example.projectquiz;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Dashboard extends AppCompatActivity {
    public CardView profile;
    public CardView performance;
    public CardView test;
    public CardView notes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        profile = findViewById(R.id.userProfile);

        Intent getEmailIntent = getIntent();
        String userEnteredEmail = getEmailIntent.getStringExtra("email");
        System.out.println("i am in dashboard " + userEnteredEmail);


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("user");
        Query checkUser = reference.orderByChild("pass_email").equalTo((userEnteredEmail));

        // now this part is fetch the data
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // fetching individual details
                    String Uname = dataSnapshot.child(userEnteredEmail).child("name").getValue(String.class);
                    System.out.println(" "+ Uname);
//                    String Uemail = dataSnapshot.child(userEnteredEmail).child("email").getValue(String.class);
//                    String Umobile = dataSnapshot.child(userEnteredEmail).child("mobile").getValue(String.class);
                        TextView dashboardUsername = findViewById(R.id.DashboardUsername);
                        dashboardUsername.setText(Uname);
                    dashboardUsername.setTextColor(Color.parseColor("#000000"));

                }

                else {
                    Toast.makeText(Dashboard.this, "Not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // error message when database not found

                Toast.makeText(Dashboard.this, "DataBase Error", Toast.LENGTH_LONG).show();
            }
        });



        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Dashboard.this, UserProfile.class);
                intent.putExtra("email", userEnteredEmail);
                startActivity(intent);
            }
        });


        performance = findViewById(R.id.userPerformance);
        performance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Dashboard.this, Performance.class);
                intent.putExtra("email", userEnteredEmail);
                startActivity(intent);
            }
        });

        test = findViewById(R.id.userTest);
        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Dashboard.this, QuestionSet.class);
                intent.putExtra("email", userEnteredEmail);
                startActivity(intent);
            }
        });

        notes = findViewById(R.id.Notes);
        notes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Dashboard.this, NotesPage.class);
                startActivity(intent);
            }
        });
    }
}