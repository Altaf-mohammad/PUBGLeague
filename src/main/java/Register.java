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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    Button regloginbtn,reg_btn;
    private EditText reg_display_name,reg_num,reg_email,reg_password,reg_confirm_password;
    private ProgressDialog mRegProgress;
    private FirebaseAuth mAuth;
    String current_id=" ";
    private FirebaseFirestore mFirestore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
        mRegProgress = new ProgressDialog(this);
        reg_display_name = findViewById(R.id.reg_display_name);
        reg_num = findViewById(R.id.reg_num);
        reg_email = findViewById(R.id.reg_email);
        reg_password = findViewById(R.id.reg_password);
        reg_btn = findViewById(R.id.reg_btn);
        reg_confirm_password = findViewById(R.id.reg_confirm_password);

        regloginbtn = (Button)findViewById(R.id.regloginbtn);
        regloginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent regtologin = new Intent(Register.this,Login.class);
                startActivity(regtologin);
            }
        });


        reg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String name = reg_display_name.getText().toString();
                final String number = reg_num.getText().toString();
                final String gmail = reg_email.getText().toString();
                String password = reg_password.getText().toString();
                String ConfirmPass = reg_confirm_password.getText().toString();

                if(!TextUtils.isEmpty(name) && !TextUtils.isEmpty(number) & !TextUtils.isEmpty(gmail)
                        & !TextUtils.isEmpty(password) & !TextUtils.isEmpty(ConfirmPass)){

                    if(password.equals(ConfirmPass)){


                        mAuth.createUserWithEmailAndPassword(gmail, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if(task.isSuccessful()){

                                    mRegProgress.setTitle("Registering User");
                                    mRegProgress.setMessage("Please wait while we create your account !");
                                    mRegProgress.setCanceledOnTouchOutside(false);
                                    mRegProgress.show();

                                    current_id          = mAuth.getCurrentUser().getUid();
                                    final FieldValue timestamp = FieldValue.serverTimestamp();
                                    String token_id = FirebaseInstanceId.getInstance().getToken();


                                    Map<String, Object> tokenMap = new HashMap<>();
                                    tokenMap.put("email",gmail);
                                    tokenMap.put("name",name);
                                    tokenMap.put("timestamp",timestamp);
                                    tokenMap.put("userid",current_id);
                                    tokenMap.put("number",number);
                                    tokenMap.put("wallet",0);
                                    tokenMap.put("tokenid",token_id);

                                    mFirestore.collection("users").document(current_id).set(tokenMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                            mRegProgress.dismiss();
                                            sendToMain();

                                        }
                                    });

                                } else {

                                    String errorMessage = task.getException().getMessage();
                                    Toast.makeText(Register.this, "Error : " + errorMessage, Toast.LENGTH_LONG).show();

                                }


                            }
                        });

                    } else {

                        Toast.makeText(Register.this, "Confirm Password and Password Field doesn't match.", Toast.LENGTH_LONG).show();

                    }
                }


            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){

            sendToMain();

        }

    }

    private void sendToMain() {

        Intent mainIntent = new Intent(Register.this, MainActivity.class);
        startActivity(mainIntent);
        finish();

    }
}