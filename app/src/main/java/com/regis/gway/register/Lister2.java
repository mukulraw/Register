package com.regis.gway.register;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

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
import java.util.List;

public class Lister2 extends AppCompatActivity {

    RecyclerView grid;
    private GridLayoutManager layoutManager;
    List<userBean> list;
    List2Adapter adapter;
    private static final int CONNECTION_TIMEOUT=10000;
    private static final int READ_TIMEOUT=15000;
    Button group;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lister2);

        grid = (RecyclerView)findViewById(R.id.lister2);

        group = (Button)findViewById(R.id.group);


      //  startService(new Intent(this , refreshChat.class));


        group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getBaseContext() , GroupChatList.class);
                startActivity(i);

            }
        });


        layoutManager = new GridLayoutManager(this , 1);

        list = new ArrayList<>();

        new syncer().execute();


    }


    private class syncer extends AsyncTask<Void , Void , Void>
    {
        String result = "";
        HttpURLConnection conn;

        URL url = null;

        List<userBean> lister = new ArrayList<>();










        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... params) {




            try {

                String GET_ALL_USERS = "http://mr-techs.16mb.com/getAllUsers.php";
                url = new URL(GET_ALL_USERS);
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


                bean b = (bean)getApplicationContext();

                for (int i = 0 ; i<length ; i++)
                {
                    userBean item = new userBean();
                    JSONObject obj = arr.getJSONObject(i);
                    String iidd = obj.getString("id");

                    if (!iidd.equals(b.id))
                    {

                        item.setId(iidd);
                        item.setName(obj.getString("name"));
                        lister.add(item);
                    }

                }


            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }


            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {



            Log.d("Asdasdasd" , result);


            adapter = new List2Adapter(getApplicationContext() , lister);
            grid.setAdapter(adapter);
            grid.setLayoutManager(layoutManager);


            super.onPostExecute(aVoid);
        }
    }


}
