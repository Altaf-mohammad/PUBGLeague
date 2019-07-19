package com.league.pubgleague;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ResultFragment extends Fragment {

    private RecyclerView battle_list_view;
    private List<BattleList> battle_list;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private BattleResultRecyclerAdapter battleResultRecyclerAdapter;


    public ResultFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_result, container, false);


        battle_list = new ArrayList<>();
        battle_list_view = v.findViewById(R.id.result_list_view);

        firebaseAuth = FirebaseAuth.getInstance();

        battleResultRecyclerAdapter = new BattleResultRecyclerAdapter(battle_list);
        battle_list_view.setLayoutManager(new LinearLayoutManager(getContext()));
        battle_list_view.setAdapter(battleResultRecyclerAdapter);
        battle_list_view.setHasFixedSize(true);

        if (firebaseAuth.getCurrentUser() != null) {

            firebaseFirestore = FirebaseFirestore.getInstance();


            firebaseFirestore.collection("match").whereEqualTo("status","complete").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot documentSnapshots) {
                    if (documentSnapshots.isEmpty()) {
                        Toast.makeText(getContext(), "No Results yet...!", Toast.LENGTH_SHORT).show();

                    } else {
                        for (DocumentSnapshot documentSnapshot : documentSnapshots) {

                            String battleListId =  documentSnapshot.getId();
                            BattleList battleList = documentSnapshot.toObject(BattleList.class).withId(battleListId);
                            battle_list.add(battleList);
                            battleResultRecyclerAdapter.notifyDataSetChanged();


                        }
                    }
                }
            });

        }
        return v;
    }

}
