package com.league.pubgleague;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ResultDetails extends AppCompatActivity {

    TextView resultmatchname,resultmatchtime,winner1,winner2,winner3,perkills,entryfee;
    private Toolbar mToolbar;
    private ProgressDialog mLoginProgress;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private RecyclerView battle_winner_list_view,battle_total_list_view;
    private List<ResultList> result_list;
    private List<TotalResultList> total_result_list;
    private ResultRecyclerAdapter resultRecyclerAdapter;
    private TotalResultRecyclerAdapter totalResultRecyclerAdapter;
    String documentid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_details);

        documentid = getIntent().getStringExtra("doc_id");

        mToolbar = (Toolbar) findViewById(R.id.result_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Battle Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        resultmatchname = (TextView)findViewById(R.id.resultmatchname);
        resultmatchtime = (TextView)findViewById(R.id.resultmatchtime);
        winner1 = (TextView)findViewById(R.id.winner1);
        winner2 = (TextView)findViewById(R.id.winner2);
        winner3 = (TextView)findViewById(R.id.winner3);
        perkills = (TextView)findViewById(R.id.perkills);
        entryfee = (TextView)findViewById(R.id.entryfee);

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        mLoginProgress = new ProgressDialog(this);
        final String currentUserId = firebaseAuth.getCurrentUser().getUid();

        result_list = new ArrayList<>();
        total_result_list = new ArrayList<>();
        battle_winner_list_view = findViewById(R.id.battle_winner_list_view);
        battle_total_list_view = findViewById(R.id.battle_total_list_view);

        resultRecyclerAdapter = new ResultRecyclerAdapter(result_list);
        battle_winner_list_view.setLayoutManager(new LinearLayoutManager(this));
        battle_winner_list_view.setAdapter(resultRecyclerAdapter);
        battle_winner_list_view.setHasFixedSize(true);

        totalResultRecyclerAdapter = new TotalResultRecyclerAdapter(total_result_list);
        battle_total_list_view.setLayoutManager(new LinearLayoutManager(this));
        battle_total_list_view.setAdapter(totalResultRecyclerAdapter);
        battle_total_list_view.setHasFixedSize(true);


        firebaseFirestore.collection("match").document(documentid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                String w1 = task.getResult().getString("w1");
                String w2 = task.getResult().getString("w2");
                String w3 = task.getResult().getString("w3");
                String name = task.getResult().getString("name");
                final String fee = task.getResult().getString("fee");
                String kill = task.getResult().getString("kills");
                String time = task.getResult().getString("time");


                resultmatchname.setText(name);
                winner1.setText("₹"+w1);
                winner2.setText("₹"+w2);
                resultmatchtime.setText("Organised on "+time);
                perkills.setText("₹"+kill);
                entryfee.setText("₹"+fee);
                winner3.setText("₹"+w3);


            }
        });

        if (firebaseAuth.getCurrentUser() != null) {

            firebaseFirestore = FirebaseFirestore.getInstance();





            firebaseFirestore.collection("match").document(documentid).collection("players").whereEqualTo("position",1).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot documentSnapshots) {
                    if (documentSnapshots.isEmpty()) {
                        Toast.makeText(ResultDetails.this, "No Results Today...!", Toast.LENGTH_SHORT).show();

                    } else {
                        for (DocumentSnapshot documentSnapshot : documentSnapshots) {

//                            String battleListId =  documentSnapshot.getId();
                            ResultList resultList = documentSnapshot.toObject(ResultList.class);
                            result_list.add(resultList);
                            resultRecyclerAdapter.notifyDataSetChanged();


                        }
                    }
                }
            });



            firebaseFirestore.collection("match").document(documentid).collection("players").
                                                        orderBy("position", Query.Direction.ASCENDING)
                                                        .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot documentSnapshots) {
                    if (documentSnapshots.isEmpty()) {
                        Toast.makeText(ResultDetails.this, "No Results Today...!", Toast.LENGTH_SHORT).show();

                    } else {
                        for (DocumentSnapshot documentSnapshot : documentSnapshots) {

//                            String battleListId =  documentSnapshot.getId();
                            TotalResultList totalResultList = documentSnapshot.toObject(TotalResultList.class);
                            total_result_list.add(totalResultList);
                            totalResultRecyclerAdapter.notifyDataSetChanged();


                        }
                    }
                }
            });

        }

    }
}
