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
import java.util.List;
import java.util.Map;

public class BattleRecyclerAdapter  extends RecyclerView.Adapter<BattleRecyclerAdapter.ViewHolder>{

    private List<BattleList> battle_list;
    public Context context;
    private ProgressDialog mLoginProgress;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    String limit_data;
    int totalentrycount;



    public BattleRecyclerAdapter(List<BattleList> battle_list){

        this.battle_list = battle_list;

    }

    @NonNull
    @Override
    public BattleRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.battlelayout, parent, false);
        context = parent.getContext();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        mLoginProgress = new ProgressDialog(view.getContext());
        return new BattleRecyclerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final BattleRecyclerAdapter.ViewHolder viewHolder, int i) {

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

        limit_data = battle_list.get(i).getLimit();


        String kills_data = battle_list.get(i).getKills();
        viewHolder.setKills(kills_data);

        final String fee_val = battle_list.get(i).getFee();

        //Get Likes Count
        firebaseFirestore.collection("match/" + battleListId + "/players").addSnapshotListener( new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                if(!documentSnapshots.isEmpty()){

                    int count = documentSnapshots.size();
                    totalentrycount = documentSnapshots.size();

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

                } else {

                    viewHolder.battleJoinBtn.setText("Join");
                    viewHolder.battleJoinBtn.setEnabled(true);
                    viewHolder.battleJoinBtn.setBackgroundResource(R.color.PUBGColor);
                }

            }
        });



        firebaseFirestore.collection("match/" + battleListId + "/players").addSnapshotListener( new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                if(!documentSnapshots.isEmpty()){

                    final int count = documentSnapshots.size();

                    //      Joining Button
                    viewHolder.battleJoinBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(final View v) {

                            long vl = Long.valueOf(limit_data);

                            if (count < vl){

                                mLoginProgress.setTitle("Joining in");
                                mLoginProgress.setMessage("Please wait while wallet transaction is completed.");
                                mLoginProgress.setCanceledOnTouchOutside(false);
                                mLoginProgress.show();

                                firebaseFirestore.collection("users").document(currentUserId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                        final String username =task.getResult().getString("name");
                                        final Long walletmoney = task.getResult().getLong("wallet");

                                        long fee = Long.parseLong(fee_val);

                                        if (walletmoney >= fee){
                                            final long charges = walletmoney - fee ;

                                            firebaseFirestore.collection("match/" + battleListId + "/players").document(currentUserId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                    if(!task.getResult().exists()){

                                                        final Map<String, Object> likesMap = new HashMap<>();
                                                        likesMap.put("name",username);
                                                        likesMap.put("kills",0);
                                                        likesMap.put("amount",0);
                                                        likesMap.put("position",0);
                                                        likesMap.put("timestamp", FieldValue.serverTimestamp());

                                                        firebaseFirestore.collection("match/" + battleListId + "/players").document(currentUserId).set(likesMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {


                                                                Map<String, Object> walletmap = new HashMap<>();
                                                                walletmap.put("wallet",charges);

                                                                firebaseFirestore.collection("users").document(currentUserId).update(walletmap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {

                                                                        mLoginProgress.dismiss();

                                                                    }
                                                                });

                                                            }
                                                        });





                                                    } else {

                                                        firebaseFirestore.collection("match/" + battleListId + "/players").document(currentUserId).delete();

                                                    }
                                                }
                                            });

                                        }else{
                                            mLoginProgress.dismiss();
                                            Toast.makeText(v.getContext(),"You didn't have enough amount to join in",Toast.LENGTH_LONG).show();
                                        }



                                    }
                                });



                            }


                            else{

                                Toast.makeText(v.getContext(),"Players are filled, Join in next battle.",Toast.LENGTH_LONG).show();

                            }



                        }
                    });


                } else {

                    viewHolder.updatePlayersCount(0);

                    final int count = documentSnapshots.size();

                    //      Joining Button
                    viewHolder.battleJoinBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(final View v) {

                            long vl = Long.valueOf(limit_data);

                            if (count < vl){

                                mLoginProgress.setTitle("Joining in");
                                mLoginProgress.setMessage("Please wait while wallet transaction is completed.");
                                mLoginProgress.setCanceledOnTouchOutside(false);
                                mLoginProgress.show();

                                firebaseFirestore.collection("users").document(currentUserId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                        final String username =task.getResult().getString("name");
                                        final Long walletmoney = task.getResult().getLong("wallet");

                                        long fee = Long.parseLong(fee_val);

                                        if (walletmoney >= fee){
                                            final long charges = walletmoney - fee ;

                                            firebaseFirestore.collection("match/" + battleListId + "/players").document(currentUserId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                    if(!task.getResult().exists()){

                                                        final Map<String, Object> likesMap = new HashMap<>();
                                                        likesMap.put("name",username);
                                                        likesMap.put("kills",0);
                                                        likesMap.put("amount",0);
                                                        likesMap.put("position",0);
                                                        likesMap.put("timestamp", FieldValue.serverTimestamp());

                                                        firebaseFirestore.collection("match/" + battleListId + "/players").document(currentUserId).set(likesMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {


                                                                Map<String, Object> walletmap = new HashMap<>();
                                                                walletmap.put("wallet",charges);

                                                                firebaseFirestore.collection("users").document(currentUserId).update(walletmap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {

                                                                        mLoginProgress.dismiss();

                                                                    }
                                                                });

                                                            }
                                                        });





                                                    } else {

                                                        firebaseFirestore.collection("match/" + battleListId + "/players").document(currentUserId).delete();

                                                    }
                                                }
                                            });

                                        }else{
                                            mLoginProgress.dismiss();
                                            Toast.makeText(v.getContext(),"You didn't have enough amount to join in",Toast.LENGTH_LONG).show();
                                        }



                                    }
                                });



                            }


                            else{

                                Toast.makeText(v.getContext(),"Players are filled, Join in next battle.",Toast.LENGTH_LONG).show();

                            }



                        }
                    });

                }

            }
        });







        viewHolder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent battledetailsintent = new Intent(context,BattleDetails.class);
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
        private Button battleJoinBtn;
        private RelativeLayout battlelayoutcard;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;

            firebaseFirestore = FirebaseFirestore.getInstance();
            firebaseAuth = FirebaseAuth.getInstance();
            battleJoinBtn = mView.findViewById(R.id.battleJoinBtn);

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
            battle_limit.setText(count + "/"+limit_data);

        }

    }

}
