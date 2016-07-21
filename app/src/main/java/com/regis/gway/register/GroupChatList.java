package com.regis.gway.register;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class GroupChatList extends AppCompatActivity {

    TextView create;
    ListView grouplist;
    private String CREATE_GROUP = "http://mr-techs.16mb.com/create_group.php";
    String id;
    private static final int CONNECTION_TIMEOUT=10000;
    private static final int READ_TIMEOUT=15000;
    FloatingActionButton refre;
    GroupListAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat_list);

        create = (TextView)findViewById(R.id.create_group);
        grouplist = (ListView)findViewById(R.id.groups_list);

        bean b = (bean)getApplicationContext();

        id = b.id;

        refre = (FloatingActionButton)findViewById(R.id.refresh);

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog dialog = new Dialog(GroupChatList.this);
                dialog.setContentView(R.layout.create_group_dialog);
                dialog.setCancelable(true);
                dialog.show();

                final EditText grp_name = (EditText)dialog.findViewById(R.id.grp_name);
                Button create_grp = (Button)dialog.findViewById(R.id.create_button);


                create_grp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String name = grp_name.getText().toString();

                        if(name.length()>0)
                        new login(name).execute();
                        dialog.dismiss();

                    }
                });


            }
        });

        refre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refresh();
            }
        });


        grouplist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                GroupListBean item = (GroupListBean) adapterView.getItemAtPosition(i);

                String id = item.getId();

                Intent intent = new Intent(getApplicationContext() , GroupChatRoom.class);
                Bundle b = new Bundle();
                b.putString("id" , id);
                intent.putExtras(b);
                startActivity(intent);

            }
        });


        refresh();




    }

    void refresh()
    {
        new syncer().execute();
    }


    private class syncer extends AsyncTask<Void , Void , Void>
    {
        String result = "";
        HttpURLConnection conn;

        URL url = null;

        List<GroupListBean> lister = new ArrayList<>();










        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... params) {




            try {

                String GET_ALL_USERS = "http://mr-techs.16mb.com/getAllGroups.php";
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
                    GroupListBean item = new GroupListBean();
                    JSONObject obj = arr.getJSONObject(i);
                    String iidd = obj.getString("id");



                        item.setId(iidd);
                        item.setName(obj.getString("group_name"));
                    item.setAdmin(obj.getString("admin_id"));
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


            adapter = new GroupListAdapter(getApplicationContext() , lister);
            grouplist.setAdapter(adapter);


            super.onPostExecute(aVoid);
        }
    }


    private class login extends AsyncTask<Void , Void , Void>
    {
        String result = "";
        HttpURLConnection conn;
        ProgressDialog pdLoading = new ProgressDialog(GroupChatList.this);
        URL url = null;


        String n;
        login(String n)
        {
            this.n = n;

        }






        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdLoading.setMessage("\tLoading...");
            pdLoading.setCancelable(false);
            pdLoading.show();
        }

        @Override
        protected Void doInBackground(Void... params) {


            //String postURL = "?username=" + n + "&email="+ a +"&password="+ p;

            try {

                url = new URL(CREATE_GROUP);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            try {
                conn = (HttpURLConnection)url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("POST");


                conn.setDoInput(true);
                conn.setDoOutput(true);

                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("admin",id )
                        .appendQueryParameter("name", n);
                String query = builder.build().getEncodedQuery();

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();

                conn.connect();

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));


                String line;

                while ((line = bufferedReader.readLine()) != null) {
                    result = result + line;
                }


            } catch (IOException e) {
                e.printStackTrace();
            }






            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {



            pdLoading.dismiss();

            Toast.makeText(getApplicationContext() , result , Toast.LENGTH_SHORT).show();

            Log.d("asdasdasd" , result);

            super.onPostExecute(aVoid);
        }
    }





}
