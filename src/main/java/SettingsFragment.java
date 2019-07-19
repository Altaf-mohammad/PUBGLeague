package com.league.pubgleague;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;



/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment {


    public SettingsFragment() {
        // Required empty public constructor
    }
    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;
    TextView logouttext,dataname,datanumber,wallettext,amounttext,helptext;
    String user_id;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_settings, container, false);

        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        user_id = mAuth.getCurrentUser().getUid();
        wallettext = (TextView)v.findViewById(R.id.wallettext);
        logouttext = (TextView)v.findViewById(R.id.logouttext);
        helptext = (TextView)v.findViewById(R.id.Helptext);
        dataname = (TextView)v.findViewById(R.id.pubgname);
        datanumber = (TextView)v.findViewById(R.id.paytmnumber);
        amounttext = (TextView)v.findViewById(R.id.amounttext);
        logouttext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOut();
            }
        });

        wallettext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent walletIntent = new Intent(getContext(),Wallet.class);
                startActivity(walletIntent);
            }
        });


        helptext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto","www.pubgleague@gmail.com", null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "Body");
                startActivity(Intent.createChooser(emailIntent, "Send email..."));
            }
        });

        firebaseFirestore.collection("users").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if(task.isSuccessful()){

                    if(task.getResult().exists()){

                        String name = task.getResult().getString("name");
                        String number = task.getResult().getString("number");
                        Long wallet = task.getResult().getLong("wallet");



                        dataname.setText(name);
                        datanumber.setText("+91 "+number);
                        amounttext.setText("₹ "+wallet);



                    }

                } else {

                    String error = task.getException().getMessage();
                    Toast.makeText(getContext(), "(FIRESTORE Retrieve Error) : " + error, Toast.LENGTH_LONG).show();

                }


            }
        });

        return v;
    }
    private void logOut() {


        mAuth.signOut();
        sendToLogin();
    }

    private void sendToLogin() {

        Intent loginIntent = new Intent(getContext(), Login.class);
        startActivity(loginIntent);
        getActivity().finish();

    }
}
