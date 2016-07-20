package com.regis.gway.register;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by hi on 7/20/2016.
 */

class ChatAdapter extends BaseAdapter {

    private List<messageBean> list;
    private Context context;
    private static LayoutInflater inflater=null;
    private int res;

    ChatAdapter(Context context, int resource, List<messageBean> list) {
        this.context = context;
        this.list = list;
        this.res = resource;

        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    void setGridData(List<messageBean> list)
    {
        this.list = list;
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View vi = view;
        ViewHolder holder;
        if(view==null){

            /****** Inflate tabitem.xml file for each row ( Defined below ) *******/
            vi = inflater.inflate(R.layout.chat_list_model, null);

            /****** View Holder Object to contain tabitem.xml file elements ******/

            holder = new ViewHolder();
            holder.text = (TextView) vi.findViewById(R.id.messagebox);
            holder.container = (LinearLayout)vi.findViewById(R.id.container);

            /************  Set holder with LayoutInflater ************/
            vi.setTag( holder );
        }
        else
            holder=(ViewHolder)vi.getTag();


        messageBean item = list.get(i);
        bean b = (bean)context.getApplicationContext();

        String id = b.id;

        holder.text.setText(item.getMessage());

        if (item.getSid().equals(id))
        {
            holder.container.setGravity(Gravity.START);
            holder.text.setBackgroundResource(R.drawable.bubblea);
        }
        else
        {
            holder.container.setGravity(Gravity.END);
            holder.text.setBackgroundResource(R.drawable.bubbleb);
        }






return vi;
    }

    private static class ViewHolder{

        public TextView text;
        public LinearLayout container;


    }




}
