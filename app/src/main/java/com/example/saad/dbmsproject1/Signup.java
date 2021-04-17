package com.example.saad.dbmsproject1;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.ArrayList;
import java.util.List;

public class Signup extends Activity implements AdapterView.OnItemSelectedListener {

    Spinner spinner;
    int success;
    String message;
    ContentValues values;
    List<NameValuePair> params;
    ProgressDialog pDialog;
    private static final String url_create_user = "http://projectbux.000webhostapp.com/androidDBMS/register.php";
    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    JSONParser jsonParser = new JSONParser();


    String dname;
    Button signUp;
    DatabaseHelper helper = new DatabaseHelper(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        signUp = findViewById(R.id.Signup);
        signUp.setBackgroundColor(getResources().getColor(R.color.buttonColor));
        spinner = findViewById(R.id.spinner);

        spinner.setOnItemSelectedListener(this);
        loadSpinnerData();

        
    }






    private void loadSpinnerData() {
        // database handler
        DatabaseHelper db = new DatabaseHelper(getApplicationContext());

        // Spinner Drop down elements
        List<String> departments = db.getAllDepartments();

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, departments);

        // Drop down layout style - list view with radio button
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position,
                               long id) {
        // On selecting a spinner item
        dname = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "You selected: " + dname,
                Toast.LENGTH_LONG).show();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    public void onSignUpClick(View v)
    {
        /*EditText did = (EditText)findViewById(R.id.did);*/
        EditText name = findViewById(R.id.name);
        EditText email = findViewById(R.id.email);
        EditText pass1 = findViewById(R.id.pass);
        EditText pass2 = findViewById(R.id.cpass);

        String namestr = name.getText().toString();
        int d_id = helper.getDepIDbyName(dname);
        String d_idstr = Integer.toString(d_id);
        String emailstr = email.getText().toString();
        String pass1str = pass1.getText().toString();
        String pass2str = pass2.getText().toString();

        if(!pass1str.equals(pass2str))
        {
            Toast.makeText(Signup.this,"Passwords don't match", Toast.LENGTH_SHORT).show();

        }
        else
        {
           USER u = new USER();
            u.set_did(d_id);
            u.set_un(namestr);
            u.set_email(emailstr);
            u.set_pass(pass1str);
            params = new ArrayList<>();
            params.add(new BasicNameValuePair("d_id", d_idstr));
            params.add(new BasicNameValuePair("name", namestr));
            params.add(new BasicNameValuePair("email", emailstr));
            params.add(new BasicNameValuePair("pass",pass1str));

            new CreateNewUser().execute();

        }
    }





    //////////////////////////////////////////////////////////////////////////////////////////////////




    class CreateNewUser extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Signup.this);
            pDialog.setMessage("Registering User...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Creating product
         * */
        protected String doInBackground(String... args) {
           JSONObject json = jsonParser.makeHttpRequest(url_create_user,
                    "POST", params);

            // check log cat fro response
           Log.d("Create Response", json.toString());

            // check for success tag
            try {
                success = json.getInt(TAG_SUCCESS);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                message = json.getString(TAG_MESSAGE);
            } catch (JSONException e) {
                e.printStackTrace();
            }



            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done
            pDialog.dismiss();

            if (success == 1) {
                Toast.makeText(Signup.this,"Registration Successful.", Toast.LENGTH_SHORT).show();

            } else {
                // failed to create product
                Toast.makeText(Signup.this,"Failed to create user..." + message , Toast.LENGTH_SHORT).show();
            }

            Intent intent = new Intent(Signup.this, MainActivity.class);
            finish();
            startActivity(intent);
            // closing this screen
            //finish();
        }

    }


    /////////////////////////////////////////////////////////////////////////////////////////////////



}
