package com.example.projectquiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.ColorSpace;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class QuestionPage extends AppCompatActivity {

    public static ArrayList<modelClass> listOfQuestion = new ArrayList<>();
    public DatabaseReference databaseReference;
    public modelClass model2;
    public TextView question, optionA, optionB, optionC, optionD , submitbtn , backbtn ;
    public CardView cardOA, cardOB, cardOC, cardOD;
    public LinearLayout nextButton;
    int index = 0, correctCount = 0, wrongCount = 0;
    private static final int LENGTH_LONG = 500000;
    String userEnteredEmail ;


    // setting the timer for the question page
    // Time set here is 10 min , to increase the time the value should be increase like ( minute , second , millisecond ) 10*60*100
    private static final long START_TIME_IN_MILLIS = 60000;
    ProgressBar timerProgress;
    TextView timerText;
    public CountDownTimer countDownTimer;
    public  boolean timerRunning;
    private long timeLeftInMillis = START_TIME_IN_MILLIS;


    // on create function for the questionPage
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_page);

        // calling the hooks for the page
        Hooks();

        // calling for the database reference for the child QuestionList
        databaseReference = FirebaseDatabase.getInstance().getReference().child("QuestionList");

        // Async function for fetching data from the database
        readData(new FirebaseCallback() {
            @Override
            public void onCallback(List<modelClass> list) {
                System.out.println(list + "\n i was here \n");

            }
        });
        submitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertShow();
            }
        });

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backAlert();
            }
        });

        initialize();
    }




    // firebase call back function for getting the data from the database and storing
    private  void readData(FirebaseCallback firebaseCallback){

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listOfQuestion.clear();
                index = 0;
                wrongCount=0;
                correctCount = 0;

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    modelClass model1 = dataSnapshot.getValue(modelClass.class);
                    listOfQuestion.add(model1);
                }
                firebaseCallback.onCallback(listOfQuestion);
                //Collections.shuffle(listOfQuestion);
                model2 = listOfQuestion.get(index);
                try{
                    setAllData();
                }catch (Exception e){
                    Toast.makeText(QuestionPage.this, ""+e, Toast.LENGTH_LONG).show();
                }
                cardOA.setBackgroundColor(getResources().getColor(R.color.white));
                cardOB.setBackgroundColor(getResources().getColor(R.color.white));
                cardOC.setBackgroundColor(getResources().getColor(R.color.white));
                cardOD.setBackgroundColor(getResources().getColor(R.color.white));
              }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(QuestionPage.this, "" + error , Toast.LENGTH_LONG).show();
            }
        });
    }

    // firebase callback function
    interface  FirebaseCallback{
        void onCallback(List<modelClass> list);
    }

   // for timer function
   private void initialize() {
        timerText = (TextView) findViewById(R.id.count_down);
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                 timeLeftInMillis = millisUntilFinished;
                 updateCountDownText();
            }

            @Override
            public void onFinish() {
                timerRunning = false;
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                pauseTimer();


            }
        }.start();

        timerRunning = true;

   }

    // updating the count down
    private void updateCountDownText() {
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;

        String timeLeftFormatted = String.format(Locale.getDefault(),"%02d:%02d" , minutes, seconds);
        timerText.setText(timeLeftFormatted);

    }



    // call to set all data
    private void setAllData() {
        question.setText(model2.getQuestion());
        optionA.setText(model2.getOptA());
        optionB.setText(model2.getOptB());
        optionC.setText(model2.getOptC());
        optionD.setText(model2.getOptD());
    }

    // hook section to get the data
    private void Hooks() {
        question = findViewById(R.id.question);
        optionA = findViewById(R.id.optionA);
        optionB = findViewById(R.id.optionB);
        optionC = findViewById(R.id.optionC);
        optionD = findViewById(R.id.optionD);
        cardOA = findViewById(R.id.cardOptionA);
        cardOB = findViewById(R.id.cardOptionB);
        cardOC = findViewById(R.id.cardOptionC);
        cardOD = findViewById(R.id.cardOptionD);
        nextButton = findViewById(R.id.nextButton);
        submitbtn = findViewById(R.id.exitButton);
        backbtn = findViewById(R.id.backArrow);
    }

    // final won activity details sending page
    private void GameWon() {
        userEnteredEmail = getIntent().getStringExtra("email");
        System.out.println("upar wala question page pe = " + userEnteredEmail);
        String set = getIntent().getStringExtra("set");
        Intent intent = new Intent(QuestionPage.this, WonActivity.class);
        intent.putExtra("correct",correctCount);
        intent.putExtra("wrong",wrongCount);
        intent.putExtra("total",listOfQuestion.size());
        intent.putExtra("email",userEnteredEmail);
        intent.putExtra("set",set);
        startActivity(intent);
    }

    public void correct(CardView card) {
        card.setBackgroundColor(getResources().getColor(R.color.green));
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(index<listOfQuestion.size()-1){
                    index++;
                    model2 = listOfQuestion.get(index);
                    enableButton();
                    resetColor();
                    setAllData();
                }
                else{
                    alertShow();
                }
            }
        });
    }

    public void wrong(CardView card) {
        card.setBackgroundColor(getResources().getColor(R.color.red));
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (index < listOfQuestion.size() - 1) {
                    index++;
                    model2 = listOfQuestion.get(index);
                    enableButton();
                    resetColor();
                    setAllData();
                } else {
                    alertShow();
                }
            }
        });
    }

    public void alertShow(){
        AlertDialog.Builder myAlertBuilder =  new AlertDialog.Builder(QuestionPage.this).setCancelable(false);
        myAlertBuilder.setTitle("");
        myAlertBuilder.setMessage("Do you want to submit ?");
        myAlertBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                try {
                    if ( timerRunning == false){
                        pauseTimer();
                    }
                    GameWon();
                    getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                }
                catch (Exception e){
                    Toast.makeText(QuestionPage.this, ""+e, Toast.LENGTH_LONG).show();
                }
            }
        });

        myAlertBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        myAlertBuilder.show();
    }



    // On pressing back this alert will be shown
    public void backAlert(){
        AlertDialog.Builder myAlertBuilder =  new AlertDialog.Builder(QuestionPage.this).setCancelable(false);
        myAlertBuilder.setTitle("");
        myAlertBuilder.setMessage("Do you want to exit ?");
        myAlertBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                try {
                    GameWon();
                    getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                }
                catch (Exception e){
                    Toast.makeText(QuestionPage.this, ""+e, Toast.LENGTH_LONG).show();
                }
            }
        });

        myAlertBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            }
        });
        myAlertBuilder.show();
    }

    // for pausing the timer after the submission of the test
    private void pauseTimer() {
         //timerAlertShow();
         countDownTimer.cancel();
         timerRunning = false;
         timerAlertShow();
    }

    private void timerAlertShow() {
            AlertDialog.Builder myAlertBuilder =  new AlertDialog.Builder(QuestionPage.this).setCancelable(false);
            myAlertBuilder.setTitle("");

            myAlertBuilder.setMessage("TimeOut!! Submit the Test !");
            myAlertBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    try {
                        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        GameWon();
                    }
                    catch (Exception e){
                        Toast.makeText(QuestionPage.this, ""+e, Toast.LENGTH_LONG).show();
                    }
                }
            });


        myAlertBuilder.show();

    }

    public void enableButton() {
        cardOA.setClickable(true);
        cardOB.setClickable(true);
        cardOC.setClickable(true);
        cardOD.setClickable(true);
    }

    public void disableButton() {
        cardOA.setClickable(false);
        cardOB.setClickable(false);
        cardOC.setClickable(false);
        cardOD.setClickable(false);
    }
    public void resetColor(){
        cardOA.setBackgroundColor(getResources().getColor(R.color.white));
        cardOB.setBackgroundColor(getResources().getColor(R.color.white));
        cardOC.setBackgroundColor(getResources().getColor(R.color.white));
        cardOD.setBackgroundColor(getResources().getColor(R.color.white));
    }

    public void optionAClick(View view) {
        disableButton();
        if(model2.getOptA().equals(model2.getAnswer())){
            correctCount++;
            cardOA.setBackgroundColor(getResources().getColor(R.color.green));
            if(index < listOfQuestion.size()){
                correct(cardOA);
                }
        }
        else{
            wrongCount++;
            wrong(cardOA);
        }
    }

    public void optionBClick(View view) {
        disableButton();
        if(model2.getOptB().equals(model2.getAnswer())){
            correctCount++;
            cardOB.setBackgroundColor(getResources().getColor(R.color.green));
            if(index < listOfQuestion.size()){
                correct(cardOB);
            }
        }
        else{
            wrongCount++;
            wrong(cardOB);
        }
    }
    public void optionCClick(View view) {
        disableButton();
        if(model2.getOptC().equals(model2.getAnswer())){
            correctCount++;
            cardOC.setBackgroundColor(getResources().getColor(R.color.green));
            if(index < listOfQuestion.size()){
                correct(cardOC);
            }
        }
        else{
            wrongCount++;
            wrong(cardOC);
        }
    }
    public void optionDClick(View view) {
        disableButton();
        if(model2.getOptD().equals(model2.getAnswer())){
            correctCount++;
            cardOD.setBackgroundColor(getResources().getColor(R.color.green));
            if(index < listOfQuestion.size()){
                correct(cardOD);
            }
        }
        else{
            wrongCount++;
            wrong(cardOD);
        }
    }


    // When mobile's back button is pressed at the time of test then it will show a alert box on clicking yes it will redirect user
    // to dashboard on clicking no it will remain on same page

    @Override
    public void onBackPressed()
    {
        backAlert();
   }
}

