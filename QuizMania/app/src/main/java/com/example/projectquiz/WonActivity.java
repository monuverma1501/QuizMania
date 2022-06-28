package com.example.projectquiz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import java.util.Locale;

public class WonActivity extends AppCompatActivity {
    CircularProgressBar circularProgressBar;
    TextView resulttext;
    int correct,wrong,total;
    private static final int LENGTH_LONG = 500000;

    //Getting data from previous activity using  intent
    String userEnteredEmail ;
    FirebaseDatabase rootnode ;
    DatabaseReference reference;

    LinearLayout buttonShare;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_won);

        userEnteredEmail  = getIntent().getStringExtra("email");
        String set = getIntent().getStringExtra("set");
       // System.out.println(""+ set +"\n  "+userEnteredEmail);
        correct=getIntent().getIntExtra("correct", 0);
        wrong=getIntent().getIntExtra("wrong", 0);
        total = getIntent().getIntExtra("total",0);

        circularProgressBar=findViewById(R.id.circularProgressBar);
        resulttext=findViewById(R.id.resulttext);
        buttonShare = findViewById(R.id.buttonShare);



        //updating data in firebase database

        rootnode = FirebaseDatabase.getInstance();
        reference = rootnode.getReference("Performance").child(userEnteredEmail);
        System.out.println(""+ set +"\n  "+userEnteredEmail);
        String correctCount = Integer.toString(correct);
        String wrongCount = Integer.toString(wrong);
        String totalCount = Integer.toString(total);
        String passEmail = getIntent().getStringExtra("email");
        PerformanceUpdate details = new PerformanceUpdate(correctCount,wrongCount,totalCount , passEmail);

        try {
            reference.child(set).setValue(details);
        } catch (Exception e) {
            e.printStackTrace();
        }



        //Setting data in progress bar

        resulttext.setText(correct+"/"+(total));
        circularProgressBar.setProgress(correct);
        // Share button function

        buttonShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My application name");
                    String shareMessage= "\ni got "+correct+" out of "+total+" You can also try";
                    shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID +"\n\n";
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    startActivity(Intent.createChooser(shareIntent, "choose one"));
                } catch(Exception e) {
                    //e.toString();
                }

            }
        });


    }

    // This function is to avoid going to the previous activity when mobile's back button is pressed
    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(WonActivity.this,Dashboard.class);
        intent.putExtra("email",userEnteredEmail);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }
}