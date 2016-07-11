package com.regis.gway.register;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class Register extends AppCompatActivity {

    private EditText name , email , pass;


    private static final int CONNECTION_TIMEOUT=10000;
    private static final int READ_TIMEOUT=15000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        name = (EditText)findViewById(R.id.u_name);
        email = (EditText)findViewById(R.id.u_email);
        pass = (EditText)findViewById(R.id.u_password);

        Button register = (Button) findViewById(R.id.register);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String nam = name.getText().toString();
                String emai = email.getText().toString();
                String pas = pass.getText().toString();


                new login(nam , emai , pas).execute();



            }
        });




    }


    private class login extends AsyncTask<Void , Void , Void>
    {
        String result;
        HttpURLConnection conn;
        ProgressDialog pdLoading = new ProgressDialog(Register.this);
        URL url = null;


        String n , e , p;
        login(String n , String e , String p)
        {
            this.n = n;
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


            String postURL = "?username=" + n + "&email="+ e +"&password="+ p;

            try {
                String SIGN_UP = "http://mr-techs.16mb.com/register.php";
                url = new URL(SIGN_UP + postURL);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            try {
                conn = (HttpURLConnection)url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("GET");


                //conn.setDoInput(true);
                //conn.setDoOutput(true);

                /*Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("username", n)
                        .appendQueryParameter("email", e)
                        .appendQueryParameter("password", p);
                String query = builder.build().getEncodedQuery();

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();*/

                conn.connect();

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));



                result = bufferedReader.readLine();

            } catch (IOException e) {
                e.printStackTrace();
            }






            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {



            pdLoading.dismiss();

                Toast.makeText(getApplicationContext() , result , Toast.LENGTH_SHORT).show();




                name.setText("");
                email.setText("");
                pass.setText("");







            super.onPostExecute(aVoid);
        }
    }


}
