package com.league.pubgleague;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class BattleResultRecyclerAdapter extends RecyclerView.Adapter<BattleResultRecyclerAdapter.ViewHolder>{

    private List<BattleList> battle_list;
    public Context context;
    private ProgressDialog mLoginProgress;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;


    public BattleResultRecyclerAdapter(List<BattleList> battle_list){

        this.battle_list = battle_list;

    }

    @NonNull
    @Override
    public BattleResultRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.battlelayout, parent, false);
        context = parent.getContext();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        mLoginProgress = new ProgressDialog(view.getContext());
        return new BattleResultRecyclerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        viewHolder.setIsRecyclable(false);

        final String battleListId = battle_list.get(i).BattleListId;
        final String currentUserId = firebaseAuth.getCurrentUser().getUid();

        String name_data = battle_list.get(i).getName();
        viewHolder.setName(name_data);

        String time_data = battle_list.get(i).getTime();
        viewHolder.setTime(time_data);

        String type_data = battle_list.get(i).getType();
        viewHolder.setType(type_data);

        String map_data = battle_list.get(i).getMap();
        viewHolder.setMap(map_data);

        final String fee_data = battle_list.get(i).getFee();
        viewHolder.setFee(fee_data);

        String w1_data = battle_list.get(i).getW1();
        viewHolder.setW1(w1_data);

//        String limit_data = battle_list.get(i).getLimit();
//        viewHolder.setLimit(limit_data);

        String kills_data = battle_list.get(i).getKills();
        viewHolder.setKills(kills_data);


        //Get Likes Count
        firebaseFirestore.collection("match/" + battleListId + "/players").addSnapshotListener( new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                if(!documentSnapshots.isEmpty()){

                    int count = documentSnapshots.size();

                    viewHolder.updatePlayersCount(count);

                } else {

                    viewHolder.updatePlayersCount(0);

                }

            }
        });




        //Get status
        firebaseFirestore.collection("match/" + battleListId + "/players").document(currentUserId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {


                if(documentSnapshot.exists()){

                    viewHolder.battleJoinBtn.setText("Joined");
                    viewHolder.battleJoinBtn.setEnabled(false);
                    viewHolder.battleJoinBtn.setBackgroundResource(R.color.disable);
                    viewHolder.battleJoinBtn.setTextColor(Color.parseColor("#FFFFFF"));
                    viewHolder.battle_limit.setVisibility(View.INVISIBLE);
                    viewHolder.resultmatchdetails.setVisibility(View.VISIBLE);

                } else {

                    viewHolder.battleJoinBtn.setText("Join");
                    viewHolder.battleJoinBtn.setEnabled(true);
                    viewHolder.battleJoinBtn.setBackgroundResource(R.color.PUBGColor);
                    viewHolder.battle_limit.setVisibility(View.INVISIBLE);
                    viewHolder.resultmatchdetails.setVisibility(View.VISIBLE);
                }

            }
        });



//      Joining Button




        viewHolder.resultmatchdetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent battledetailsintent = new Intent(context,ResultDetails.class);
                battledetailsintent.putExtra("doc_id", battleListId);
                context.startActivity(battledetailsintent);

            }
        });


    }



    @Override
    public int getItemCount() {
        return battle_list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private View mView;
        private TextView battle_name,battle_time,battle_type,battle_map,battle_fee,battle_w,battle_kills,battle_limit;
        private Button battleJoinBtn,resultmatchdetails;
        private RelativeLayout battlelayoutcard;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;

            firebaseFirestore = FirebaseFirestore.getInstance();
            firebaseAuth = FirebaseAuth.getInstance();
            battleJoinBtn = mView.findViewById(R.id.battleJoinBtn);
            resultmatchdetails = mView.findViewById(R.id.resultmatchdetails);



            battlelayoutcard = (RelativeLayout) mView.findViewById(R.id.battlelayoutcard);
        }


        public void setName(String name_data) {
            battle_name = mView.findViewById(R.id.battlename);
            battle_name.setText(name_data);
        }

        public void setTime(String time_data) {
            battle_time = mView.findViewById(R.id.battletime);
            battle_time.setText(time_data);
        }

        public void setType(String type_data) {
            battle_type = mView.findViewById(R.id.battle_type);
            battle_type.setText(type_data);
        }

        public void setMap(String map_data) {
            battle_map = mView.findViewById(R.id.battle_map);
            battle_map.setText(map_data);
        }

        public void setFee(String fee_data) {
            battle_fee = mView.findViewById(R.id.battle_fee);
            battle_fee.setText("₹"+fee_data);
        }

        public void setW1(String w1_data) {
            battle_w = mView.findViewById(R.id.battle_w);
            battle_w.setText("₹"+w1_data);
        }

//        public void setLimit(String limit_data) {
//            battle_limit = mView.findViewById(R.id.battle_limit);
//            battle_limit.setText("0/"+limit_data);
//        }

        public void setKills(String kills_data) {
            battle_kills = mView.findViewById(R.id.battle_kills);
            battle_kills.setText("₹"+kills_data);
        }


        public void updatePlayersCount(int count){

            battle_limit = mView.findViewById(R.id.battle_limit);
            battle_limit.setText(count + "/100");

        }

    }

}
