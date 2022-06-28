package com.example.projectquiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class QuestionSet extends AppCompatActivity {
    public CardView set1,set2,set3,set4,set5,set6;
    private static final int LENGTH_LONG = 500000;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_set);


        Intent getEmailIntent = getIntent();
        String userEnteredEmail = getEmailIntent.getStringExtra("email");
        System.out.println("this is set wala = " + userEnteredEmail);
        set1 =  findViewById(R.id.Set1);
        set1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String set = "Set1";
                Intent intent = new Intent(QuestionSet.this,QuestionPage.class);
                intent.putExtra("email",userEnteredEmail);
                intent.putExtra("set",set);

                startActivity(intent);
            }
        });

        set2 =  findViewById(R.id.Set2);
        set2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String set = "Set2";

                Intent intent = new Intent(QuestionSet.this,QuestionPage.class);
                intent.putExtra("email",userEnteredEmail);
                intent.putExtra("set",set);
                startActivity(intent);
            }
        });

        set3 =  findViewById(R.id.Set3);
        set3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String set = "Set3";

                Intent intent = new Intent(QuestionSet.this,QuestionPage.class);
                intent.putExtra("email",userEnteredEmail);
                intent.putExtra("set",set);
                startActivity(intent);
            }
        });

        set4 =  findViewById(R.id.Set4);
        set4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String set = "Set4";

                Intent intent = new Intent(QuestionSet.this,QuestionPage.class);
                intent.putExtra("email",userEnteredEmail);
                intent.putExtra("set",set);
                startActivity(intent);
            }
        });

        set5 =  findViewById(R.id.Set5);
        set5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String set = "Set5";

                Intent intent = new Intent(QuestionSet.this,QuestionPage.class);
                intent.putExtra("email",userEnteredEmail);
                intent.putExtra("set",set);
                startActivity(intent);
            }
        });

        set6 =  findViewById(R.id.Set6);
        set6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String set = "Set6";

                Intent intent = new Intent(QuestionSet.this,QuestionPage.class);
                intent.putExtra("email",userEnteredEmail);
                intent.putExtra("set",set);
                startActivity(intent);
            }
        });
    }

}