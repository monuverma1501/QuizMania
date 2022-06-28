package com.example.projectquiz;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import java.util.regex.Pattern;

public class ForgotPass extends AppCompatActivity {
    // variable section that is to be used in this page
    public TextView emailEntered;
    public Button otpButton;
    public ProgressBar progressBar;
    public ProgressDialog progressDialog;

    // setting error timing
    private static final int LENGTH_LONG = 500000;
    // Data base reference links
    public FirebaseAuth fauth;

    // verifying the email id with Regex Patter .. Below is the Pattern
    public static final Pattern EMAIL_ADDRESS = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "("+
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{1,25}" +
                    ")+"
    );


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpass);

        // setting the title for the activity
       // getSupportActionBar().setTitle("Forgot Password");

        // setting up the hooks for the pages
        Hook();

        // setting onClick listner even for otpButton
        otpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // getting the email entered by the user
                String i_email = emailEntered.getText().toString();

                // checking for the validation of the input
                if ( !validateEmail() ){
                    return ;
                }
                else {
                    setPassword(i_email);
                }
            }
        });
    }

    // creating method for crating password
    private void setPassword(String i_email) {
        fauth = FirebaseAuth.getInstance();
        fauth.sendPasswordResetEmail(i_email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                // now checking
                if ( task.isSuccessful()){

                    Toast.makeText(ForgotPass.this, "Please Check your Inbox for Password Reset Link !!", Toast.LENGTH_LONG).show();

                    // intent to transfer the control from this page to main activity
                    Intent intent = new Intent(ForgotPass.this , MainActivity.class);
                    // clear stack to prevent the user coming back to this page after pressing back button after loggin out
                    //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();


                }
                else {
                    String error = task.getException().getLocalizedMessage();
                    Toast.makeText(ForgotPass.this, "Email Id not found !!!\n"+error + "\nCheck the Entered Data!!", Toast.LENGTH_LONG).show();
                }

//                progressBar.setVisibility(View.GONE);
            }
        });

    }

    private void Hook() {
        emailEntered = findViewById(R.id.idEditEmail);
        otpButton = findViewById(R.id.sentOtp);

    }

    /// REGEX PATTERNS for email and passwrod input checks

    private boolean validateEmail() {
        String emailInput = emailEntered.getText().toString();


        if (emailInput.isEmpty()) {
            emailEntered.setError("Field can't be empty");
            return false;
        }
        else if (!EMAIL_ADDRESS.matcher(emailInput).matches()) {
            emailEntered.setError("Please enter a valid email address");
            return false;
        } else {
            emailEntered.setError(null);
            return true;
        }
    }
}