package com.league.pubgleague;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class ResultRecyclerAdapter  extends RecyclerView.Adapter<ResultRecyclerAdapter.ViewHolder> {

    private List<ResultList> result_list;
    public Context context;
    private ProgressDialog mLoginProgress;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;


    public ResultRecyclerAdapter(List<ResultList> result_list){

        this.result_list = result_list;

    }

    @NonNull
    @Override
    public ResultRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.singlebattlereportlayout, parent, false);
        context = parent.getContext();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        mLoginProgress = new ProgressDialog(view.getContext());
        return new ResultRecyclerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ResultRecyclerAdapter.ViewHolder viewHolder, int i) {

        String result_name = result_list.get(i).getName();
        viewHolder.setName(result_name);

        Long result_amount = result_list.get(i).getAmount();
        viewHolder.setAmount(result_amount);

        Long result_pos = result_list.get(i).getPosition();
        viewHolder.setPosition(result_pos);

        Long result_kills = result_list.get(i).getKills();
        viewHolder.setKills(result_kills);


    }

    @Override
    public int getItemCount() {
        return result_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private View mView;
        private TextView resultplayerhashvalue,resultplayername,resultplayerkills,resultplayeramount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setName(String result_name) {
            resultplayername = mView.findViewById(R.id.resultplayername);
            resultplayername.setText(result_name);

        }

        public void setAmount(Long result_amount) {
            resultplayeramount = mView.findViewById(R.id.resultplayeramount);
            resultplayeramount.setText(String.valueOf(result_amount));

        }

        public void setPosition(Long result_pos) {
            resultplayerhashvalue = mView.findViewById(R.id.resultplayerhashvalue);
            resultplayerhashvalue.setText(String.valueOf(result_pos));

        }

        public void setKills(Long result_kills) {
            resultplayerkills = mView.findViewById(R.id.resultplayerkills);
            resultplayerkills.setText(String.valueOf(result_kills));

        }
    }
}
