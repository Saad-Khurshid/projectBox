package com.example.saad.dbmsproject1;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    public DatabaseHelper helper;
    Button btnLogin, btnRegister;

    List<NameValuePair> params;
    int success;
    String message;

    private static final String url_login = "http://projectbux.000webhostapp.com/androidDBMS/signin.php";
    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    JSONParser jsonParser = new JSONParser();
    String name;
    String pass;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnLogin = findViewById(R.id.Login);
        btnLogin.setBackgroundColor(getResources().getColor(R.color.buttonColor));
        btnRegister = findViewById(R.id.Register);
        btnRegister.setBackgroundColor(getResources().getColor(R.color.buttonColor));

        helper = new DatabaseHelper(this);
        helper.PopulateDepartmentTable();

    }

    public void onButtonClick(View v)
    {
        if(v.getId() == R.id.Login)
        {
            EditText a = findViewById(R.id.editText);
            name = a.getText().toString();
            EditText b = findViewById(R.id.editText2);
            pass = b.getText().toString();

            params = new ArrayList<>();
            params.add(new BasicNameValuePair("name", name));
            params.add(new BasicNameValuePair("pass", pass));
            new Login().execute();
        }
        if(v.getId() == R.id.Register)
        {
            Intent intent = new Intent(MainActivity.this,Signup.class);
            finish();
            startActivity(intent);
        }
    }



//////////////////////////////////////////////////////////////////////////////////////////////////////////////

    class Login extends AsyncTask<String, String, String> {

        ProgressDialog pdLoading = new ProgressDialog(MainActivity.this);
        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //this method will be running on UI thread
            pdLoading.setMessage("Processing Signin Request...");
            pdLoading.setCancelable(false);
            pdLoading.show();
        }

        /**
         * Creating product
         * */
        protected String doInBackground(String... args) {
            JSONObject json = jsonParser.makeHttpRequest(url_login,
                    "POST", params);

            // check log cat fro response
//            Log.d("Create Response", json.toString());

            // check for success tag
            try {
                success = json.getInt(TAG_SUCCESS);
            } catch (JSONException e) {
                e.printStackTrace();
            }



            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String result) {
            //this method will be running on UI thread

            pdLoading.dismiss();

            if (success == 1) {
                Toast.makeText(MainActivity.this,"Signin Successful.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, Home.class);
                //finish();
                intent.putExtra("name",name);
                startActivity(intent);

            } else {
                // failed to create product
                Toast.makeText(MainActivity.this,"Signin Failed..." , Toast.LENGTH_SHORT).show();
            }
        }

    }

/////////////////////////////////////////////////////////////////////////////////////////////////////////////


}



