package com.regis.gway.register;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by hi on 7/21/2016.
 */

public class GroupAdapter extends BaseAdapter {
    private List<GroupChatBean> list;
    private Context context;
    private static LayoutInflater inflater=null;
    private int res;

    GroupAdapter(Context context, int resource, List<GroupChatBean> list) {
        this.context = context;
        this.list = list;
        this.res = resource;

        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    void setGridData(List<GroupChatBean> list)
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
            vi = inflater.inflate(R.layout.group_model, null);

            /****** View Holder Object to contain tabitem.xml file elements ******/

            holder = new ViewHolder();
            holder.text = (TextView) vi.findViewById(R.id.messagebox1);
            holder.container = (LinearLayout)vi.findViewById(R.id.container1);
            holder.name = (TextView)vi.findViewById(R.id.name1);
            holder.colored = (LinearLayout)vi.findViewById(R.id.colored);

            /************  Set holder with LayoutInflater ************/
            vi.setTag( holder );
        }
        else
            holder=(ViewHolder)vi.getTag();


        GroupChatBean item = list.get(i);
        bean b = (bean)context.getApplicationContext();

        String id = b.id;

        holder.text.setText(item.getMessage());
        holder.name.setText(item.getName());

        if (item.getSid().equals(id))
        {
            holder.container.setGravity(Gravity.START);
           // holder.text.setBackgroundResource(R.drawable.bubblea);
            holder.colored.setBackgroundResource(R.drawable.bubblea);
        }
        else
        {
            holder.container.setGravity(Gravity.END);
            //holder.text.setBackgroundResource(R.drawable.bubbleb);
            holder.colored.setBackgroundResource(R.drawable.bubbleb);
        }






        return vi;
    }

    private static class ViewHolder{

        public TextView text , name;
        public LinearLayout container , colored;


    }




}
