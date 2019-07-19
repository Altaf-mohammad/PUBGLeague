package com.league.pubgleague;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class BattleDetails extends AppCompatActivity {

    TextView battletitle, battletime, battleversion, battlemap, battletype, battlefee, battlekill, battlewinprizes, battleroomdetails;
    Button battlejoinbtn;
    private Toolbar mToolbar;
    private ProgressDialog mLoginProgress;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    String documentid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battle_details);

        mToolbar = (Toolbar) findViewById(R.id.battle_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Battle Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        battletitle = (TextView) findViewById(R.id.battletitle);
        battletime = (TextView) findViewById(R.id.battletime);
        battleversion = (TextView) findViewById(R.id.battleversion);
        battlemap = (TextView) findViewById(R.id.battlemap);
        battletype = (TextView) findViewById(R.id.battletype);
        battlefee = (TextView) findViewById(R.id.battlefee);
        battlekill = (TextView) findViewById(R.id.battlekill);
        battlewinprizes = (TextView) findViewById(R.id.battlewinprizes);
        battleroomdetails = (TextView) findViewById(R.id.battleroomdetails);
        battlejoinbtn = (Button) findViewById(R.id.battlejoinbtn);


        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        mLoginProgress = new ProgressDialog(this);
        final String currentUserId = firebaseAuth.getCurrentUser().getUid();
        documentid = getIntent().getStringExtra("doc_id");
        battlejoinbtn = (Button) findViewById(R.id.battlejoinbtn);


        firebaseFirestore.collection("match").document(documentid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                String w1 = task.getResult().getString("w1");
                String w2 = task.getResult().getString("w2");
                String w3 = task.getResult().getString("w3");
                String name = task.getResult().getString("name");
                String type = task.getResult().getString("type");
                String map = task.getResult().getString("map");
                final String limit = task.getResult().getString("limit");
                final String fee = task.getResult().getString("fee");
                String kill = task.getResult().getString("kills");
                String time = task.getResult().getString("time");
                final String roomid = task.getResult().getString("id");
                final String roompass = task.getResult().getString("pass");

                battletitle.setText(name);
                battlemap.setText("Map : " + map);
                battletype.setText("Type : " + type);
                battletime.setText(time);
                battlekill.setText("Per Kill : ₹" + kill);
                battlefee.setText("Entry Fee : ₹" + fee);
                battlewinprizes.setText("1st : ₹" + w1 + "\n2nd : ₹" + w2 + "\n3rd : ₹" + w3);


                //Get status
                firebaseFirestore.collection("match/" + documentid + "/players").document(currentUserId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {

                        if (documentSnapshot.exists()) {

                            battlejoinbtn.setText("Joined");
                            battlejoinbtn.setEnabled(false);
                            battlejoinbtn.setBackgroundResource(R.color.disable);
                            battlejoinbtn.setTextColor(Color.parseColor("#FFFFFF"));
                            battleroomdetails.setText("Room Id : " + roomid + "\nPassword : " + roompass);

                        } else {

                            battlejoinbtn.setText("Join");
                            battlejoinbtn.setEnabled(true);
                            battlejoinbtn.setBackgroundResource(R.color.PUBGColor);
                        }

                    }
                });

                firebaseFirestore.collection("match/" + documentid + "/players").addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(final QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                        if (!documentSnapshots.isEmpty()) {

                            final int count = documentSnapshots.size();
                            final long vl = Long.valueOf(limit);

                                battlejoinbtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(final View v) {



                                        if (count < vl){

                                        mLoginProgress.setTitle("Joining in");
                                        mLoginProgress.setMessage("Please wait while wallet transaction is completed.");
                                        mLoginProgress.setCanceledOnTouchOutside(false);
                                        mLoginProgress.show();

                                        firebaseFirestore.collection("users").document(currentUserId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                                final String username = task.getResult().getString("name");
                                                final Long walletmoney = task.getResult().getLong("wallet");
                                                long finalfee = Long.parseLong(fee);
                                                if (walletmoney >= finalfee) {
                                                    final long charges = walletmoney - finalfee;

                                                    firebaseFirestore.collection("match/" + documentid + "/players").document(currentUserId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                            if (!task.getResult().exists()) {

                                                                final Map<String, Object> likesMap = new HashMap<>();
                                                                likesMap.put("name", username);
                                                                likesMap.put("timestamp", FieldValue.serverTimestamp());

                                                                firebaseFirestore.collection("match/" + documentid + "/players").document(currentUserId).set(likesMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {


                                                                        Map<String, Object> walletmap = new HashMap<>();
                                                                        walletmap.put("wallet", charges);

                                                                        firebaseFirestore.collection("users").document(currentUserId).update(walletmap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                            @Override
                                                                            public void onComplete(@NonNull Task<Void> task) {

                                                                                mLoginProgress.dismiss();

                                                                            }
                                                                        });

                                                                    }
                                                                });


                                                            } else {

                                                                firebaseFirestore.collection("match/" + documentid + "/players").document(currentUserId).delete();

                                                            }
                                                        }
                                                    });

                                                } else {
                                                    mLoginProgress.dismiss();
                                                    Toast.makeText(BattleDetails.this, "You didn't have enough amount to join in", Toast.LENGTH_LONG).show();
                                                }


                                            }
                                        });

                                        }else{

                                            Toast.makeText(BattleDetails.this,"Players are filled, Join in next battle.",Toast.LENGTH_LONG).show();

                                        }
                                    }
                                });




                        }
                    }

                });


            }
        });
    }
}
