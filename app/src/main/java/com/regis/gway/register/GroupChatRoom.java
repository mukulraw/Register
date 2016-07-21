package com.regis.gway.register;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
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
import java.util.Timer;
import java.util.TimerTask;

public class GroupChatRoom extends AppCompatActivity {

    String groupId;
    private String SEND_GROUP_MESSAGES = "http://mr-techs.16mb.com/sendGroupMessage.php";
    private String GET_GROUP_MESSAGES = "http://mr-techs.16mb.com/getAllGroupMessages.php?group=";
    private String ADD_USER_TO_CHATROOM = "http://mr-techs.16mb.com/addUserToCharRoom.php";
    ListView chatlist;
    EditText chatBox;
    private int count = -1;
    private static final int CONNECTION_TIMEOUT=10000;
    private static final int READ_TIMEOUT=15000;
    Button send;
    List<GroupChatBean> list;
    GroupAdapter adapter;

    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat_room);

        groupId = getIntent().getExtras().getString("id");


        new adder(groupId).execute();



        chatlist = (ListView)findViewById(R.id.group_chat_list);
        chatBox = (EditText)findViewById(R.id.group_message);
        send = (Button)findViewById(R.id.send_group_message);




        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String mess = chatBox.getText().toString();

                bean b = (bean)getApplicationContext();

                new sender(b.id , groupId , mess).execute();

            }
        });


        list = new ArrayList<>();


        adapter = new GroupAdapter(this , R.layout.chat_list_model, list);
        chatlist.setAdapter(adapter);


    }


    @Override
    protected void onStart() {
        super.onStart();
        doSomethingRepeatedly();
    }

    private void doSomethingRepeatedly() {
        timer = new Timer();
        timer.scheduleAtFixedRate( new TimerTask() {
            public void run() {

                try{




                    new refresh().execute();




                }
                catch (Exception e) {
                    // TODO: handle exception
                }

            }
        }, 0, 1000);
    }



    private class refresh extends AsyncTask<Void , Void , Void>
    {
        final bean b = (bean)getApplicationContext();
        HttpURLConnection conn;
        URL url = null;
        String mess;
        String result = "";

        String status;
        int length = 0;
        boolean flag = false;

        @Override
        protected Void doInBackground(Void... voids) {

          //  pref = getSharedPreferences("myId" , Context.MODE_PRIVATE);
            try {


                url = new URL(GET_GROUP_MESSAGES+groupId);
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

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));


                String line;

                while ((line = bufferedReader.readLine()) != null) {
                    result = result + line;
                }
                Log.d("Asdasd" , result);


                JSONArray arr = new JSONArray(result);

                length = arr.length();

                list = new ArrayList<>();

                for (int i = 0 ; i<length ; i++)
                {
                    JSONObject object = arr.getJSONObject(i);
                    String sts = object.getString("status");


                    if(length > count)
                    {
                        GroupChatBean item = new GroupChatBean();
                        mess = object.getString("message");

                        item.setMessage(mess);
                        item.setName(object.getString("sender_name"));
                        item.setSid(object.getString("S_id"));

                        list.add(item);
                    }





                }

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }






            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            // if (length > count)
            // {

            if (length>count)
            {

                adapter.setGridData(list);
                adapter.notifyDataSetChanged();

                count = length;
            }



            //count = length;
            // }
            result = "";
            mess = "";
            flag = false;









        }
    }




    private class sender extends AsyncTask<Void , Void , Void>
    {
        String result = "";

        bean b = (bean)getApplicationContext();

        HttpURLConnection conn;
        URL url = null;
        String sender , receiver , messag;

        sender(String sender , String groupId , String message)
        {
            this.sender = sender;
            this.receiver = groupId;
            this.messag = message;
        }

        @Override
        protected Void doInBackground(Void... voids) {

            try {

                url = new URL(SEND_GROUP_MESSAGES);
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
                        .appendQueryParameter("sid",sender)
                        .appendQueryParameter("gid", receiver)
                        .appendQueryParameter("name",b.name)
                        .appendQueryParameter("message",messag);
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

Log.d("Asdasdresult" , result);

            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            Toast.makeText(getApplicationContext() , result , Toast.LENGTH_SHORT).show();
            result = "";


            chatBox.setText("");


        }
    }


    @Override
    protected void onStop() {
        super.onStop();
        timer.cancel();
    }

    private class adder extends AsyncTask<Void , Void , Void>
    {
        String result = "";

        bean b = (bean)getApplicationContext();

        HttpURLConnection conn;
        URL url = null;
        String sender;

        adder(String sender)
        {
            this.sender = sender;
        }

        @Override
        protected Void doInBackground(Void... voids) {

            try {


                url = new URL(ADD_USER_TO_CHATROOM);
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
                        .appendQueryParameter("id",b.id)
                        .appendQueryParameter("gid", sender);
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
            super.onPostExecute(aVoid);

            if (result.length()>0)
            Toast.makeText(getApplicationContext() , result , Toast.LENGTH_SHORT).show();
            result = "";





        }
    }






}
