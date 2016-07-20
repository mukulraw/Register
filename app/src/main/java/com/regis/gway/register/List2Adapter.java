package com.regis.gway.register;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;
import java.util.Random;

/**
 * Created by hi on 7/18/2016.
 */

public class List2Adapter extends RecyclerView.Adapter<List2Adapter.ViewHolder> {
    List<userBean> list;
    Context context;


    public List2Adapter(Context context, List<userBean> list) {
        this.context = context;
        this.list = list;
    }

    void setGridData(List<userBean> list) {
        this.list = list;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View rootView = inflater.inflate(R.layout.user_data_model, parent, false);


        return new ViewHolder(rootView);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {


        userBean item = list.get(position);



        holder.name.setText(item.getName());



    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView name;
        //   LinearLayout expandable;
     //   LinearLayout card;
     //   ImageButton editor;

        ViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);


            name = (TextView) itemView.findViewById(R.id.username);


        }


        @Override
        public void onClick(View view) {


            Intent i = new Intent(context , ChatActivity.class);
            Bundle b = new Bundle();
            b.putString("id" , list.get(getAdapterPosition()).getId());
            i.putExtras(b);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);

        }
    }
}

