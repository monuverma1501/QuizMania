package com.example.projectquiz;
//import android.graphics.Color;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Patterns;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


import java.util.Locale;
import java.util.regex.Pattern;

import javax.microedition.khronos.opengles.GL10;


public class MainActivity extends AppCompatActivity {

    // variable seciton
    public TextView reg;
    public TextView forgot;
    public Button login;
    public EditText email;
    public EditText password;

    public ProgressDialog progressDialog;

    // creating instance of firebase
    FirebaseAuth authenticate;
    FirebaseUser userAuth;

    // below lines are the regex lines for pattern match which will be used while authorization

    public static final  Pattern EMAIL_ADDRESS = Pattern.compile(
                "[a-zA-Z0-9\\+\\.\\_\\%\\+]{1,256}" +
                "\\@" +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                "("+
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                ")+"
            );
    private static final Pattern PASSWORD_PATTERN=
            Pattern.compile("^" +
                    //"(?=.*[0-9])" +         //at least 1 digit
                    //"(?=.*[a-z])" +         //at least 1 lower case letter
                    //"(?=.*[A-Z])" +         //at least 1 upper case letter
                    "(?=.*[a-zA-Z])" +      //any letter
                    "(?=.*[@#$%^&+=])" +    //at least 1 special character
                    "(?=\\S+$)" +           //no white spaces
                    ".{4,}" +               //at least 4 characters
                    "$");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);






        // this section is for taking the values from the frontend and then login the user with the following data
        email = findViewById(R.id.Email);
        password = findViewById(R.id.Password);
        login= findViewById(R.id.Login);
        progressDialog = new ProgressDialog(this);
        authenticate = FirebaseAuth.getInstance();
        userAuth = authenticate.getCurrentUser();

        reg =  findViewById(R.id.register);
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
           public void onClick(View v) {
               Intent intent = new Intent(MainActivity.this, com.example.projectquiz.RegActivity.class);
               startActivity(intent);
          }
       });

        forgot =  findViewById(R.id.forgotpassword);
      forgot.setOnClickListener(new View.OnClickListener() {
          @Override
           public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,ForgotPass.class);
               startActivity(intent);
           }
       });

        login = findViewById(R.id.Login);
       login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,Dashboard.class);
                startActivity(intent);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                performLogin();
            }
        });
    }

    /// REGEX PATTERNS for email and passwrod input checks

    private boolean validateEmail() {
        String emailInput = email.getText().toString().trim();

        if (emailInput.isEmpty()) {
            email.setError("Field can't be empty");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            email.setError("Please enter a valid email address");
            return false;
        } else {
            email.setError(null);
            return true;
        }
    }



    private boolean validatePassword() {
        String passwordInput = password.getText().toString().trim();

        if (passwordInput.isEmpty()) {
            password.setError("Field can't be empty");
            return false;
        } else {
            password.setError(null);
            return true;
        }
    }


    private void performLogin() {
        String i_email = email.getText().toString().toLowerCase(Locale.ROOT);
        String i_paswword = password.getText().toString();

        if ( !validateEmail() || !validatePassword()){
            return;
        }
        else {
//                isUser();
            progressDialog.setMessage("Login.....");
            progressDialog.setTitle("Registration");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();


            // initializing the variable above of authentication
            authenticate.signInWithEmailAndPassword(i_email , i_paswword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        progressDialog.dismiss();
                        sendUserToNextActivity();
                        Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                    } else {
                        progressDialog.dismiss();

                        String error = task.getException().getMessage().toString();

                        if ( error.contains("timeout"))
                            Toast.makeText(MainActivity.this, "Login Failed!!\nCheck your Internet Connection!!" , Toast.LENGTH_LONG).show();

                        else if ( error.contains("unreachable host"))
                                Toast.makeText(MainActivity.this, "Login Failed!!\nServer Failed to Connect!!" , Toast.LENGTH_LONG).show();


                        else {
                            Toast.makeText(MainActivity.this, "Login Failed! \nEmail or Password  incorrect..", Toast.LENGTH_SHORT).show();
                        }

                    }
                }
            });
        }
    }

    public void isUser() {
        String userEnteredEmail = email.getText().toString().trim();
        String userEnteredPassword = password.getText().toString().trim();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("user");


        Query checkUser = reference.orderByChild("mobile").equalTo((userEnteredEmail));

        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override


            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()){

                    email.setError(null);
                   //email.setErrorEnabled(false);
                    String passwordFromDB = snapshot.child("mobile").child("password").getValue(String.class);

                    if(passwordFromDB.equals(userEnteredPassword)){
                        String nameFromDB = snapshot.child("mobile").child("name").getValue(String.class);
                        String emailFromDB = snapshot.child("mobile").child("email").getValue(String.class);
                        String phoneNoFromDB = snapshot.child("mobile").child("mobile").getValue(String.class);


                        Intent intent = new Intent(MainActivity.this , Dashboard.class);

                        intent.putExtra("name",nameFromDB);
                        intent.putExtra("email",emailFromDB);
                        intent.putExtra("mobile",phoneNoFromDB);
                        intent.putExtra("password",passwordFromDB);

                        startActivity(intent);

                    }
                    else{
                        password.setError("Wrong Password");
                        password.requestFocus();
                    }

                }
                else{
                    email.setError("No such user exist");
                    email.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
    private void sendUserToNextActivity() {
        // diverting to the login page
        String userEnteredEmail = email.getText().toString();
        String userEnteredPassword = password.getText().toString().trim();
        Intent intent = new Intent(MainActivity.this, Dashboard.class);

        String changeEmail = userEnteredEmail.replaceAll("[$&+,:;=?@#|'<>.-^*()%!]", "");
        intent.putExtra("email",changeEmail);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
