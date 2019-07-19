package com.league.pubgleague;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class Login extends AppCompatActivity {

    Button logregbtn;
    private Button mLogin_btn;
    EditText mLoginEmail,mLoginPassword;

    private ProgressDialog mLoginProgress;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();
        mLoginProgress = new ProgressDialog(this);
        logregbtn = (Button)findViewById(R.id.logregbtn);
        logregbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent logintoreg = new Intent(Login.this,Register.class);
                startActivity(logintoreg);
            }
        });


        mLoginEmail = (EditText) findViewById(R.id.log_email);
        mLoginPassword = (EditText) findViewById(R.id.log_password);
        mLogin_btn = (Button) findViewById(R.id.log_btn);

        mLogin_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = mLoginEmail.getText().toString();
                String password = mLoginPassword.getText().toString();

                if(!TextUtils.isEmpty(email) || !TextUtils.isEmpty(password)){

                    mLoginProgress.setTitle("Logging In");
                    mLoginProgress.setMessage("Please wait while we check your credentials.");
                    mLoginProgress.setCanceledOnTouchOutside(false);
                    mLoginProgress.show();

                    loginUser(email, password);

                }

            }
        });


    }



    private void loginUser(String email, String password) {


        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){

                   mLoginProgress.dismiss();



                            Intent mainIntent = new Intent(Login.this, MainActivity.class);
                            mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(mainIntent);
                            finish();




                } else {

                    mLoginProgress.hide();

                    String task_result = task.getException().getMessage().toString();

                    Toast.makeText(Login.this, "Error : " + task_result, Toast.LENGTH_LONG).show();

                }

            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(!(currentUser == null)){

            sendToLogin();

        }
    }

    private void sendToLogin() {

        Intent loginIntent = new Intent(Login.this, MainActivity.class);
        startActivity(loginIntent);
        finish();

    }
}
