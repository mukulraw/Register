package com.regis.gway.register;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
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

public class ChatActivity extends AppCompatActivity {

    private EditText message;
    private String id;
    private static final int CONNECTION_TIMEOUT=10000;
    private static final int READ_TIMEOUT=15000;
    private String result = "";
    private Timer timer;
    private ListView grid;
    private List<messageBean> list;
    private int count = -1;
    private SharedPreferences pref;


    private ChatAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        grid = (ListView)findViewById(R.id.chatList);

        grid.setDividerHeight(0);

        id = getIntent().getExtras().getString("id");


        message = (EditText)findViewById(R.id.message);
        Button sendMessage = (Button) findViewById(R.id.sendMessage);

        final bean b = (bean)getApplicationContext();

        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String mess = message.getText().toString();

                if (mess.length()>0)
                {
                    new sender(b.id , id , mess).execute();
                }


            }
        });

        list = new ArrayList<>();


        adapter = new ChatAdapter(this , R.layout.chat_list_model, list);
        grid.setAdapter(adapter);



        //  adapter = new ArrayAdapter<String>(this , R.layout.chat_list_model , list);
       // grid.setAdapter(adapter);






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


        String status;
        int length = 0;
        boolean flag = false;

        @Override
        protected Void doInBackground(Void... voids) {

            pref = getSharedPreferences("myId" , Context.MODE_PRIVATE);
            try {
                String URL = "http://mr-techs.16mb.com/getMessageList.php";

                url = new URL(URL);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }


            try {
                String asd = pref.getString("id" , "not found");
                conn = (HttpURLConnection)url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("POST");


                conn.setDoInput(true);
                conn.setDoOutput(true);

                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("id",b.id)
                        .appendQueryParameter("hid", id);
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
                        messageBean item = new messageBean();
                        mess = object.getString("message");

                        item.setMessage(mess);
                        item.setRid(object.getString("R_id"));
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







    @Override
    protected void onStart() {
        super.onStart();


        stopService(new Intent(this , refreshChat.class));

        doSomethingRepeatedly();

    }

    @Override
    protected void onStop() {
        super.onStop();
        startService(new Intent(this , refreshChat.class));
        timer.cancel();


    }

    private class sender extends AsyncTask<Void , Void , Void>
    {

        HttpURLConnection conn;
        URL url = null;
        String sender , receiver , messag;

        sender(String sender , String receiver , String message)
        {
            this.sender = sender;
            this.receiver = receiver;
            this.messag = message;
        }

        @Override
        protected Void doInBackground(Void... voids) {

            try {

                String SEND = "http://mr-techs.16mb.com/send_message.php";
                url = new URL(SEND);
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
                        .appendQueryParameter("S_id",sender)
                        .appendQueryParameter("R_id", receiver)
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


            message.setText("");


        }
    }

}
