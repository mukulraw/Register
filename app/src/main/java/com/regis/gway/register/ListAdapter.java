package com.regis.gway.register;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;
import java.util.Random;
import java.util.zip.Inflater;


class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {
    List<userDataBean> list;
    Context context;


    


    public ListAdapter(Context context , List<userDataBean> list)
    {
        this.context = context;
        this.list = list;
    }

    void setGridData(List<userDataBean> list)
    {
        this.list = list;
    }




    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View rootView = inflater.inflate(R.layout.user_data_model , parent , false);



        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        userDataBean item = list.get(position);
        holder.name.setText(item.getName());
        holder.address.setText(item.getAddress());
        holder.phone.setText(item.getPhone());
        holder.date.setText(item.getDate());
        Random rnd = new Random();
        int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        holder.card.setBackgroundColor(color);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }




    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        TextView name , phone , address , date;
        LinearLayout expandable;
        LinearLayout card;
        ImageButton editor;

        ViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);

            name = (TextView)itemView.findViewById(R.id.username);
            phone = (TextView)itemView.findViewById(R.id.userphone);
            address = (TextView)itemView.findViewById(R.id.useraddress);
            date = (TextView)itemView.findViewById(R.id.userdate);
            expandable = (LinearLayout)itemView.findViewById(R.id.expandable);
            editor = (ImageButton)itemView.findViewById(R.id.deleter);
            card = (LinearLayout)itemView.findViewById(R.id.card);

        }

        @Override
        public void onClick(View view) {


            Log.d("Asdasd" , "clicked");

            if (expandable.getVisibility() == View.GONE)
            {
                expandable.setVisibility(View.VISIBLE);

                editor.setVisibility(View.VISIBLE);


            }
            else {


                expandable.setVisibility(View.GONE);

                editor.setVisibility(View.GONE);
            }

        }
    }
}
