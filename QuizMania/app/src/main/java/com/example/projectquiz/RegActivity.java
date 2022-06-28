package com.example.projectquiz;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.regex.Pattern;


public class RegActivity extends AppCompatActivity {
    public EditText username;
    public EditText email;
    public EditText mobile;
    public EditText password;
    public Button registerbtn;
    public ProgressDialog progressDialog;
    public String fromDBEmail;

    // creating instance of firebase
    FirebaseAuth authenticate;
    FirebaseUser userAuth;
    FirebaseDatabase rootnode;
    DatabaseReference reference;


    // below lines are the regex lines for pattern match which will be used while authorization
    private static final Pattern USERNAME_PATTERN = Pattern.compile(
            "^[a-z A-Z][a-z A-Z]{1,25}$"
    );

    private static final Pattern MOBILE_PATTERN = Pattern.compile(
            "^(\\+\\d{1,3}[- ]?)?\\d{10}$"
    );

    public static final Pattern EMAIL_ADDRESS = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{1,25}" +
                    ")+"
    );
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[0-9])" +         //at least 1 digit
                    "(?=.*[a-z])" +         //at least 1 lower case letter
                    "(?=.*[A-Z])" +         //at least 1 upper case letter
                    "(?=.*[a-zA-Z])" +      //any letter
                    "(?=.*[@#$%^&+=])" +    //at least 1 special character
                    "(?=\\S+$)" +           //no white spaces
                    ".{4,}" +               //at least 4 characters
                    "$");



    public TextView alreadyregister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);

        // this section is for taking the values from the frontend and then registring the user with the following data
        username = findViewById(R.id.idEditUserName);
        email = findViewById(R.id.idEditEmail);
        mobile = findViewById(R.id.idEditMobile);
        password = findViewById(R.id.idEditPassword);
        registerbtn = findViewById(R.id.idBtnRegister);
        progressDialog = new ProgressDialog(this);
        authenticate = FirebaseAuth.getInstance();
        userAuth = authenticate.getCurrentUser();

        // storing the data to the realtime database

        // below section is for the verification purpose
        registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String iemail = email.getText().toString();
                iemail = iemail.replaceAll("[$&+,:;=?@#|'<>.-^*()%!]", "");
                System.out.println("--- " + iemail);
                rootnode = FirebaseDatabase.getInstance();
                reference = rootnode.getReference("user");
                Query checkUser = reference.orderByChild("pass_email").equalTo((iemail));

                String finalEmail = iemail;
                checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (snapshot.exists()) {
                            fromDBEmail = snapshot.child(finalEmail).child("email").getValue(String.class);
                            System.out.println("i got this form db " + fromDBEmail);
                        } else {
                            System.out.println("Not exist");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                PerformAuth();
                final Handler handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //Do something after 5000ms
                        // calling the class which will take the details

                        // we will make a method call perform auth to check the authorization
                        // get all the values
                        String i_name = username.getText().toString();
                        String i_email = email.getText().toString();
                        String i_password = password.getText().toString();
                        String i_mobile = mobile.getText().toString();
                        String pass_email = i_email.replaceAll("[$&+,:;=?@#|'<>.-^*()%!]", "");


                        try {
                            if (fromDBEmail == null) {
                                UserData details = new UserData(i_name, i_email, i_password, i_mobile, pass_email);
                                try {
                                    reference.child(pass_email).setValue(details);

                                } catch (Exception e) {
                                    System.out.println(e);
                                }
                            }
                        } catch (Exception e) {
                            System.out.println(e);
                        }
                    }

                }, 3500);


                // below section checks whether the user is already have account or not
                alreadyregister = findViewById(R.id.already);
                alreadyregister.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(RegActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                });


            }
        });
    }



    // REGEX PATTERNS for email and passwrod input checks

    private boolean validateEmail() {
        String emailInput = email.getText().toString().trim();


        if (emailInput.isEmpty()) {
            email.setError("Field can't be empty");
            return false;
        } else if (!EMAIL_ADDRESS.matcher(emailInput).matches()) {
            email.setError("Please enter a valid email address");
            return false;
        } else {
            email.setError(null);
            return true;
        }
    }


    private boolean validateName() {
        String usernameInput = username.getText().toString();

        if (usernameInput.isEmpty()) {
            username.setError("Field can't be empty.");
            return false;
        } else if (!USERNAME_PATTERN.matcher(usernameInput).matches()) {
            username.setError("Enter Correct Name.");
            return false;
        } else {
            username.setError(null);
            return true;
        }
    }

    private boolean validateMobile() {
        String mobileInput = mobile.getText().toString();

        if (mobileInput.isEmpty()) {
            mobile.setError("Field can't be empty.");
            return false;
        } else if (!MOBILE_PATTERN.matcher(mobileInput).matches()) {
            mobile.setError("Invalid Mobile Number.");
            return false;
        } else {
            username.setError(null);
            return true;
        }
    }


    private boolean validatePassword() {
        String passwordInput = password.getText().toString().trim();

        if (passwordInput.isEmpty()) {
            password.setError("Field can't be empty");
            return false;
        } else if (!PASSWORD_PATTERN.matcher(passwordInput).matches()) {
            password.setError("Password should follow : " + "\n" +
                    "1) At least 1 digit" + "\n" +
                    "2) At least 1 lower case letter" + "\n" +
                    "3) At least 1 upper case letter" + "\n" +
                    "4) At least 1 special character" + "\n" +
                    "5) No white spaces" + "\n" +
                    "6) At least 6 characters" + "\n"
            );
            return false;
        } else {
            password.setError(null);
            return true;
        }
    }

    private void PerformAuth() {
        // first getting the inputs and storing it to the designated variables
        // convention used is i_variable name
        String i_email = email.getText().toString().toLowerCase(Locale.ROOT);
        String i_personName = username.getText().toString().toUpperCase(Locale.ROOT);
        String i_mobile = mobile.getText().toString();
        System.out.println(" " + i_email);

        String i_paswword = password.getText().toString();

        if (!validateName() || !validateEmail() || !validateMobile() || !validatePassword()) {
            return;
        } else {
            progressDialog.setMessage("Registration.....");
            progressDialog.setTitle("Registration");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            // initializing the variable above of authentication
            authenticate.createUserWithEmailAndPassword(i_email, i_paswword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()) {
                        progressDialog.dismiss();
                        sendUserToNextActivity();
                        Toast.makeText(RegActivity.this, "Registration Successful", Toast.LENGTH_LONG).show();
                    } else {

                        progressDialog.dismiss();
                        String error = task.getException().getMessage();

                        if ( error.contains("interrupted connection"))
                            Toast.makeText(RegActivity.this, "Please Check Your Internet Connectivity!!", Toast.LENGTH_LONG).show();

                        else if ( error.contains("unreachable host"))
                            Toast.makeText(RegActivity.this, "Server Error!!", Toast.LENGTH_LONG).show();

                        else
                            Toast.makeText(RegActivity.this, "Registration Failed!!\nEmail Id Already Exist." , Toast.LENGTH_LONG).show();

                        username.setText("");
                        email.setText("");
                        mobile.setText("");
                        password.setText("");
                        username.requestFocus();

                    }

                }
            });
        }
    }


    private void sendUserToNextActivity() {
        // diverting to the login page
        Intent intent = new Intent(RegActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}




