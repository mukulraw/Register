package com.regis.gway.register;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;



public class refreshChat extends Service {
    private static final int CONNECTION_TIMEOUT=10000;
    private static final int READ_TIMEOUT=15000;
    private String result = "";
    private int count = -1;
    String id;
    Timer timer;

    SharedPreferences pref;
    SharedPreferences.Editor edit;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {


        doSomethingRepeatedly();

        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        bean b = (bean)getApplicationContext();



        Log.d("asdasdasdasd" , "service Started");
        doSomethingRepeatedly();


    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.d("asdasdasd" , "service destroyed");
        timer.cancel();

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
        HttpURLConnection conn;
        URL url = null;


        String status;
        int length = 0;
        boolean flag = false;

        @Override
        protected Void doInBackground(Void... voids) {

            pref = getSharedPreferences("myId" , Context.MODE_PRIVATE);
            try {
                String URL = "http://mr-techs.16mb.com/getMessage.php?id=";
                String asd = pref.getString("id" , "not found");
                url = new URL(URL+asd);
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

                for (int i = 0 ; i<length ; i++)
                {
                    JSONObject object = arr.getJSONObject(i);
                    String sts = object.getString("status");
                    if (sts.equals("sent"))
                    {
                        flag = true;
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

            if (length > count)
            {

                if (flag)
                {
                    Toast.makeText(getApplicationContext() , "Message Received" , Toast.LENGTH_SHORT).show();
                }


                count = length;
            }
            result = "";
            flag = false;

        }
    }


}
