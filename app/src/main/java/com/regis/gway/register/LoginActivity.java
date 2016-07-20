package com.regis.gway.register;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetui.CompactTweetView;
import com.twitter.sdk.android.tweetui.TweetUtils;
import com.twitter.sdk.android.tweetui.TweetView;

import io.fabric.sdk.android.Fabric;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "2J6mp6rneRr3mHoX8uRlurNCE";
    private static final String TWITTER_SECRET = "XqAbH7CX5FGFoWbLBKCbs56uLHEbBvPUep3i6NnaRFTm2XByYB";


    private EditText email , password;
    private static final int CONNECTION_TIMEOUT=10000;
    private static final int READ_TIMEOUT=15000;
    private SharedPreferences pref;
    private SharedPreferences.Editor edit;

    SharedPreferences pref2;
    SharedPreferences.Editor edit2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        setContentView(R.layout.activity_login);






        email = (EditText)findViewById(R.id.email);
        password = (EditText)findViewById(R.id.password);
        Button login = (Button) findViewById(R.id.login);
        TextView create = (TextView) findViewById(R.id.jumptocreate);

        pref = getSharedPreferences("login_data" , Context.MODE_PRIVATE);
        edit = pref.edit();


        pref2 = getSharedPreferences("myId" , Context.MODE_PRIVATE);
        edit2 = pref2.edit();


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String e = email.getText().toString();
                String p = password.getText().toString();


                new login(e , p).execute();


            }
        });



        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getBaseContext() , Register.class);
                startActivity(i);
            }
        });


    }


    private class login extends AsyncTask<Void , Void , Void>
    {
        String result = "";
        HttpURLConnection conn;
        ProgressDialog pdLoading = new ProgressDialog(LoginActivity.this);
        URL url = null;



        String e , p;
        login(String e , String p)
        {
            this.e = e;
            this.p = p;
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


            String postURL = "?email="+ e +"&password="+ p;

            try {
                String LOGIN = "http://mr-techs.16mb.com/login.php";
                url = new URL(LOGIN + postURL);
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


                //result = String.valueOf(is);

                Log.d("Asdasd" , result);

            } catch (IOException e) {
                e.printStackTrace();
            }






            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {


            String status = null, id = null;

            pdLoading.dismiss();

            try {
                JSONObject obj = new JSONObject(result);
                status = obj.getString("status");
                id = obj.getString("userId");

                Log.d("asdasdasd" , status);
                Log.d("asdasdasd" , id);

            } catch (JSONException e1) {
                e1.printStackTrace();
            }

            if (Integer.parseInt(status) == 1)
            {


                    edit.putBoolean("status" , true);



                Intent i = new Intent(getApplicationContext() , Lister2.class);

               bean b = (bean) getApplicationContext();

                edit2.putString("id" , id);
                edit2.commit();

                b.id = id;



                startActivity(i);
                finish();

            }
            else
            {
                Toast.makeText(getApplicationContext() , "failure" , Toast.LENGTH_SHORT).show();
            }






            email.setText("");
            password.setText("");







            super.onPostExecute(aVoid);
        }
    }





}
