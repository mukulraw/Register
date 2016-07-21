package com.regis.gway.register;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.twitter.sdk.android.core.internal.TwitterApiConstants;

import java.util.List;

/**
 * Created by hi on 7/21/2016.
 */

class GroupListAdapter extends BaseAdapter {

    List<GroupListBean> list;
    private Context context;
    private static LayoutInflater inflater=null;


    GroupListAdapter(Context context , List<GroupListBean> list )
    {
        this.context = context;
        this.list = list;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public GroupListBean getItem(int i) {
        return list.get(i);
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
            vi = inflater.inflate(R.layout.group_list_model, null);

            /****** View Holder Object to contain tabitem.xml file elements ******/

            holder = new ViewHolder();
            holder.text = (TextView) vi.findViewById(R.id.grp_text);

            /************  Set holder with LayoutInflater ************/
            vi.setTag( holder );
        }
        else
            holder=(ViewHolder)vi.getTag();


        GroupListBean item = list.get(i);
        holder.text.setText(item.getName());


        return vi;

    }


    private static class ViewHolder{

        public TextView text;


    }

}
