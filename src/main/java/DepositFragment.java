package com.league.pubgleague;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
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
public class DepositFragment extends Fragment {


    public DepositFragment() {
        // Required empty public constructor
    }

    EditText depositamount,depositid,deposittime,depositphonenum;
    Button depositbtn;
    private ProgressDialog mRegProgress;
    private FirebaseAuth mAuth;
    String current_id=" ";
    private FirebaseFirestore mFirestore;
    TextView depositstatus;
    private Toolbar mToolbar;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_deposit, container, false);

        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
        mRegProgress = new ProgressDialog(getContext());

        depositamount = (EditText)v.findViewById(R.id.depositamount);
        depositid = (EditText)v.findViewById(R.id.depositid);
        depositphonenum = (EditText)v.findViewById(R.id.depositphonenum);
        deposittime = (EditText)v.findViewById(R.id.deposittime);
        depositstatus = (TextView)v.findViewById(R.id.depositstatus);
        depositbtn = (Button)v.findViewById(R.id.depositbtn);

        try {

            mFirestore.collection("deposit").document(current_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                    if(task.isSuccessful()){

                        if(task.getResult().exists()){

                            String fdepoststatus = task.getResult().getString("status");

                            if (fdepoststatus.equals("sent")){
                                depositstatus.setText("Status : Deposit request sent.");
                            }else if(fdepoststatus.equals("accept")){
                                depositstatus.setText("Status : Request Accepted, Enjoy League.");
                            }else{
                                depositstatus.setText("Status : No Deposit Yet.");
                            }

                        }

                    } else {

                        String error = task.getException().getMessage();
                        Toast.makeText(getContext(), "(FIRESTORE Retrieve Error) : " + error, Toast.LENGTH_LONG).show();

                    }


                }
            });


        }catch (Exception e){

        }

        depositbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String amount = depositamount.getText().toString();
                final String id = depositid.getText().toString();
                final String time = deposittime.getText().toString();
                final String PayTmNum = depositphonenum.getText().toString();

                if(!TextUtils.isEmpty(amount) && !TextUtils.isEmpty(id) & !TextUtils.isEmpty(time) & !TextUtils.isEmpty(PayTmNum)) {

                    mRegProgress.setTitle("Requesting your deposit");
                    mRegProgress.setMessage("Please wait while we send deposit request !");
                    mRegProgress.setCanceledOnTouchOutside(false);
                    mRegProgress.show();

                    current_id          = mAuth.getCurrentUser().getUid();
                    final FieldValue timestamp = FieldValue.serverTimestamp();


                    Map<String, Object> tokenMap = new HashMap<>();
                    tokenMap.put("amount",amount);
                    tokenMap.put("id",id);
                    tokenMap.put("time",time);
                    tokenMap.put("status","sent");
                    tokenMap.put("paytm",PayTmNum);
                    tokenMap.put("timestamp",timestamp);
                    tokenMap.put("userid",current_id);

                    mFirestore.collection("deposit").document(current_id).set(tokenMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){

                                depositstatus.setText("Deposit request sent. amount will be added to wallet within 12hrs");
                                mRegProgress.dismiss();

                            } else {

                                Toast.makeText(getContext(), "There was some error in sending deposit request.", Toast.LENGTH_LONG).show();

                            }

                        }
                    });

                }else{
                    Toast.makeText(getContext(), "Please fill all details.", Toast.LENGTH_LONG).show();
                }


            }
        });



        return  v;
    }

}
