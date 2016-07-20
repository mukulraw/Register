package com.regis.gway.register;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

public class AddData extends AppCompatActivity {

    private String ADD_DATA = "http://mr-techs.16mb.com/add_data.php";
    private static final int CONNECTION_TIMEOUT=10000;
    private static final int READ_TIMEOUT=15000;
    EditText name , phone , address , date;
    Button add;
    String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_data);
        add = (Button)findViewById(R.id.addData);

        name = (EditText)findViewById(R.id.namer);
        phone = (EditText)findViewById(R.id.phoner);
        address = (EditText)findViewById(R.id.addresser);
        date = (EditText)findViewById(R.id.dater);




        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String n = name.getText().toString();
                String p = phone.getText().toString();
                String a = address.getText().toString();
                String d = date.getText().toString();
                String m = getIntent().getExtras().getString("id");

                new login(n , a , p , d , m).execute();



            }
        });



    }

    private class login extends AsyncTask<Void , Void , Void>
    {
        String result = "";
        HttpURLConnection conn;
        ProgressDialog pdLoading = new ProgressDialog(AddData.this);
        URL url = null;


        String n , a , p ,d , m;
        login(String n , String e , String p , String d , String m)
        {
            this.n = n;
            this.a = e;
            this.p = p;
            this.d = d;
            this.m = m;
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

                url = new URL(ADD_DATA);
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
                        .appendQueryParameter("mainId", m)
                        .appendQueryParameter("username", n)
                        .appendQueryParameter("address", a)
                        .appendQueryParameter("phone", p)
                        .appendQueryParameter("date", d);
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



            if(result.equals("success"))
            {

                DBHandler db = new DBHandler(getApplicationContext());
                userDataBean item = new userDataBean();
                item.setUserId(m);
                item.setName(n);
                item.setAddress(a);
                item.setPhone(p);
                item.setDate(d);

                db.insertUser(item);
            }









            super.onPostExecute(aVoid);
        }
    }


}
