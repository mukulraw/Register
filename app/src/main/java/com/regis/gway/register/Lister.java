package com.regis.gway.register;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;



import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Lister extends AppCompatActivity {

    private static final int CONNECTION_TIMEOUT=10000;
    private static final int READ_TIMEOUT=15000;
    private ListAdapter adapter;
    private String id;
    private RecyclerView grid;
    private GridLayoutManager layoutManager;
    private List<userDataBean> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        FloatingActionButton add = (FloatingActionButton) findViewById(R.id.add);

        grid = (RecyclerView)findViewById(R.id.listView);

        try
        {
            id = getIntent().getExtras().getString("id");
        }catch (NullPointerException e)
        {
            e.printStackTrace();
        }


        layoutManager = new GridLayoutManager(this , 1);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getBaseContext() , AddData.class);
                Bundle b = new Bundle();
                b.putString("id" , id);
                i.putExtras(b);
                startActivityForResult(i , 123);


            }
        });



        ItemTouchHelper.Callback _ithCallback = new ItemTouchHelper.Callback() {
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                Collections.swap(list, viewHolder.getAdapterPosition(), target.getAdapterPosition());
                adapter.notifyItemMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());
                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

            }


            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                return makeFlag(ItemTouchHelper.ACTION_STATE_DRAG,
                        ItemTouchHelper.DOWN | ItemTouchHelper.UP | ItemTouchHelper.START | ItemTouchHelper.END);
            }
        };


        ItemTouchHelper ith = new ItemTouchHelper(_ithCallback);
        ith.attachToRecyclerView(grid);

        DBHandler db = new DBHandler(this);

        list = new ArrayList<>();


        list = db.getAllusers();

        for (int i = 0 ; i<list.size() ; i++)
        {
            Log.d("asdasdasd" , list.get(i).getName());
        }

        Log.d("asdasdasdcount" , String.valueOf(db.getUsersCount()));


        adapter = new ListAdapter(this , list);

        grid.setLayoutManager(layoutManager);
        grid.setAdapter(adapter);

        sync();

        //adapter.notifyDataSetChanged();



    }

    @Override
    protected void onResume() {
        super.onResume();

       //adapter.notifyDataSetChanged();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 123)
        {

            DBHandler db = new DBHandler(getApplicationContext());
            adapter.setGridData(db.getAllusers());
            adapter.notifyDataSetChanged();

        }

    }

    private void sync()
    {
        new syncer().execute();
    }


   private class syncer extends AsyncTask<Void , Void , Void>
    {
        String result = "";
        HttpURLConnection conn;

        URL url = null;

        List<userDataBean> lister = new ArrayList<>();










        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... params) {




            try {

                String GET_ALL_USERS = "http://mr-techs.16mb.com/getAllUsers.php";
                url = new URL(GET_ALL_USERS + "?id="+id);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            try {
                conn = (HttpURLConnection)url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("GET");

                conn.setDoInput(true);



                conn.connect();

                //InputStream is = new BufferedInputStream(conn.getInputStream());

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));


                String line;
                //result = bufferedReader.readLine();

                while ((line = bufferedReader.readLine()) != null) {
                    result = result + line;
                }




                Log.d("Asdasd" , result);


                JSONArray arr = new JSONArray(result);

                int length = arr.length();



                for (int i = 0 ; i<length ; i++)
                {
                    userDataBean item = new userDataBean();
                    JSONObject obj = arr.getJSONObject(i);
                    item.setUserId(obj.getString("mainId"));
                    item.setName(obj.getString("name"));
                    item.setAddress(obj.getString("address"));
                    item.setPhone(obj.getString("phone"));
                    item.setDate(obj.getString("start_date"));
                    lister.add(item);
                }


            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }


            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {



            Log.d("Asdasdasd" , result);


            DBHandler db = new DBHandler(getApplicationContext());

            for (int i = 0 ; i < lister.size() ; i++)
            {
                userDataBean item = lister.get(i);
                db.insertUser(item);
            }

            adapter.setGridData(db.getAllusers());
            adapter.notifyDataSetChanged();

            super.onPostExecute(aVoid);
        }
    }


}
