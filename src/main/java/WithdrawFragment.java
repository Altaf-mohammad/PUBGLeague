package com.league.pubgleague;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class WithdrawFragment extends Fragment {


    public WithdrawFragment() {
        // Required empty public constructor
    }

    EditText withdrawalphnum,withdrawalamount;
    Button withdrawbtn;
    private ProgressDialog mRegProgress;
    private FirebaseAuth mAuth;
    String current_id=" ";
    private FirebaseFirestore mFirestore;
    TextView withdrawalstatus,withdrawwallet;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_withdraw, container, false);

        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
        mRegProgress = new ProgressDialog(getContext());
        current_id          = mAuth.getCurrentUser().getUid();

        withdrawalphnum = (EditText)v.findViewById(R.id.withdrawalphnum);
        withdrawalamount = (EditText)v.findViewById(R.id.withdrawalamount);
        withdrawalstatus = (TextView)v.findViewById(R.id.withdrawalstatus);
        withdrawwallet = (TextView)v.findViewById(R.id.withdrawwallet);
        withdrawbtn = (Button)v.findViewById(R.id.withdrawbtn);


        mFirestore.collection("users").document(current_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if(task.isSuccessful()){

                    if(task.getResult().exists()){


                        String number = task.getResult().getString("number");
                        final Long wallet = task.getResult().getLong("wallet");

                        withdrawalphnum.setText(number);
                        withdrawwallet.setText("Wallet Amount : ₹"+wallet);



                        withdrawbtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                final Long amount = Long.valueOf(withdrawalamount.getText().toString());
                                final String PayTmNum = withdrawalphnum.getText().toString();

                                if(amount != 0 &&  !TextUtils.isEmpty(PayTmNum)) {

                                    if (amount <= wallet){

                                        final long finalamount = wallet - amount;

                                        mRegProgress.setTitle("Requesting your Withdrawal");
                                        mRegProgress.setMessage("Please wait while we send withdrawal request !");
                                        mRegProgress.setCanceledOnTouchOutside(false);
                                        mRegProgress.show();

                                        current_id          = mAuth.getCurrentUser().getUid();
                                        final FieldValue timestamp = FieldValue.serverTimestamp();


                                        Map<String, Object> tokenMap = new HashMap<>();
                                        tokenMap.put("amount",amount);
                                        tokenMap.put("status","sent");
                                        tokenMap.put("paytm",PayTmNum);
                                        tokenMap.put("timestamp",timestamp);
                                        tokenMap.put("userid",current_id);

                                        mFirestore.collection("withdrawal").document(current_id).set(tokenMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){


                                                    Map<String, Object> walletmap = new HashMap<>();
                                                    walletmap.put("wallet",finalamount);

                                                    mFirestore.collection("users").document(current_id).update(walletmap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {

                                                            withdrawalstatus.setText("Withdrawal request sent. amount will be added to wallet within 12hrs");

                                                            withdrawalamount.setText(" ");
                                                            withdrawalphnum.setText(" ");

                                                            mRegProgress.dismiss();

                                                        }
                                                    });



                                                } else {

                                                    Toast.makeText(getContext(), "There was some error in sending withdrawal request.", Toast.LENGTH_LONG).show();

                                                }

                                            }
                                        });

                                    }else{
                                        Toast.makeText(getContext(), "You didn't have enough amount in wallet.", Toast.LENGTH_LONG).show();
                                    }



                                }else{
                                    Toast.makeText(getContext(), "Please fill all details.", Toast.LENGTH_LONG).show();
                                }


                            }
                        });



                    }

                } else {

                    String error = task.getException().getMessage();
                    Toast.makeText(getContext(), "(FIRESTORE Retrieve Error) : " + error, Toast.LENGTH_LONG).show();

                }


            }
        });






        return  v;
    }

}
