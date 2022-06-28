package com.example.projectquiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Performance extends AppCompatActivity {
    // variable for the using in the page
    public TextView totalScore, totalQuestion , testCount , totalRank;
    public FirebaseDatabase firebaseDatabase;
    public DatabaseReference databaseReference;
    long  totalCorrect = 0, totalWrong = 0, totalAttempt = 0, countOfQuestion = 0 ;
    double rank = 0.0;
    private static final int LENGTH_LONG = 500000;


    // crating global user email id variable
    public String userEnteredEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_performance);

        // getting the email id from the intent
        userEnteredEmail = getIntent().getStringExtra("email");
        System.out.println("performance page pe = " + userEnteredEmail);

        // creating hook for getting the values from the front end
        Hook();

        // database reference for connecting to the database and fetching the details from it ( Performance )
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Performance");

        // now this part is fetch the data
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                // outer layer object reference for the traversal in performance tree
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    // checking for the same email id on which task is goin on
                    if (dataSnapshot.getKey().toString().equals(userEnteredEmail)) {
                        // if data matches we again traverse to the data object of the sub part
                        for (DataSnapshot snap : dataSnapshot.getChildren()) {
                            System.out.println("I m the key = " + snap.getKey().toString() + "\nI m value = " + snap.getValue());
                            for (DataSnapshot sn : snap.getChildren()) {
                                if (sn.getKey().toString().equals("correct"))
                                    totalCorrect += Integer.parseInt(sn.getValue().toString());
                                else if (sn.getKey().toString().equals("wrong"))
                                    totalWrong += Integer.parseInt(sn.getValue().toString());
                                else if (sn.getKey().toString().equals("total"))
                                    totalAttempt += Integer.parseInt(sn.getValue().toString());
                            }  // for loop of sn
                            countOfQuestion = (int) dataSnapshot.getChildrenCount();
                            rank = (totalCorrect *100)/totalAttempt;

                        } // for loop of snap

                    }// end of if

                    else
                        System.out.println("I Can't found");

                    // just for printing purpose
                    System.out.println("Total questions attempted = " + dataSnapshot.getChildrenCount() + "\nTotal Correct = " + totalCorrect +
                            "\n" + "Total incorrect = " + totalWrong + "\nTotal Attemp = " + totalAttempt);
                    
                    setData();

                } // end for loop datasnapshot
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // error message when database not found

                Toast.makeText(Performance.this, "DataBase Error", Toast.LENGTH_LONG).show();
            }
        });

    }


    // hooks function
    private void Hook() {
        totalScore = (TextView) findViewById(R.id.finalScore);
        testCount = (TextView) findViewById(R.id.totalTestCount);
        totalQuestion = (TextView) findViewById(R.id.totalQuestion);
        totalRank = (TextView) findViewById(R.id.finalRank);
    }

    // set data function
    private void setData() {
        totalQuestion.setText(String.valueOf(totalAttempt));
        totalScore.setText(String.valueOf(totalCorrect));
        testCount.setText(String.valueOf(countOfQuestion));
        totalRank.setText(String.valueOf("#"+rank));


    }
}