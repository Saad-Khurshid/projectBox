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

public class MyProjectBox extends AppCompatActivity {


    EditText ptitle, pabstract;
    Button updateProject, deleteProject;
    PROJECT project ;
    String p_id, title, abs, url;
    List<NameValuePair> params;
    int success;
    String message;

    private static final String url_update = "http://projectbux.000webhostapp.com/androidDBMS/updateProject.php";
    private static final String url_delete = "http://projectbux.000webhostapp.com/androidDBMS/deleteProject.php";
    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    JSONParser jsonParser = new JSONParser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_project_box);
        Bundle bundle = getIntent().getExtras();
        p_id = bundle.getString("p_id");
        title = bundle.getString("title");
        abs = bundle.getString("abs");
        url = bundle.getString("url");


        ptitle = findViewById(R.id.TitleData);
        pabstract = findViewById(R.id.AbstractData);
        updateProject = findViewById(R.id.Update);
        deleteProject = findViewById(R.id.Delete);
        updateProject.setBackgroundColor(getResources().getColor(R.color.buttonColor));
        deleteProject.setBackgroundColor(getResources().getColor(R.color.buttonColor));


        //project = helper.getProjectbyID(p_id);

        ptitle.setText(title);
        pabstract.setText(abs);

    }

    public void onClickUpdateProject(View view) {
        String Abstract = pabstract.getText().toString();
        String title = ptitle.getText().toString();
        params = new ArrayList<>();
        params.add(new BasicNameValuePair("abstract", Abstract));
        params.add(new BasicNameValuePair("title", title));
        params.add(new BasicNameValuePair("p_id", p_id));
        new Update().execute();

    }

    public void onClickDeleteProject(View view) {

        params = new ArrayList<>();
        params.add(new BasicNameValuePair("p_id", p_id));
        new Delete().execute();

    }


    //////////////////////////////////////////////////////////////////////////////////////////////////////////////

    class Update extends AsyncTask<String, String, String> {

        ProgressDialog pdLoading = new ProgressDialog(MyProjectBox.this);
        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //this method will be running on UI thread
            pdLoading.setMessage("Processing Update Request...");
            pdLoading.setCancelable(false);
            pdLoading.show();
        }

        /**
         * Creating product
         * */
        protected String doInBackground(String... args) {
            JSONObject json = jsonParser.makeHttpRequest(url_update,
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
        protected void onPostExecute(String result) {
            //this method will be running on UI thread

            pdLoading.dismiss();

            if (success == 1) {
                Toast.makeText(MyProjectBox.this,"Success: " + message, Toast.LENGTH_SHORT).show();
                //Intent intent = new Intent(MainActivity.this, Home.class);
                finish();
                //intent.putExtra("name",name);
                //startActivity(intent);

            } else {
                // failed to create product
                Toast.makeText(MyProjectBox.this,"Error: " + message , Toast.LENGTH_SHORT).show();
            }
        }

    }

/////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////

    class Delete extends AsyncTask<String, String, String> {

        ProgressDialog pdLoading = new ProgressDialog(MyProjectBox.this);
        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //this method will be running on UI thread
            pdLoading.setMessage("Processing Delete Request...");
            pdLoading.setCancelable(false);
            pdLoading.show();
        }

        /**
         * Creating product
         * */
        protected String doInBackground(String... args) {
            JSONObject json = jsonParser.makeHttpRequest(url_delete,
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
        protected void onPostExecute(String result) {
            //this method will be running on UI thread

            pdLoading.dismiss();

            if (success == 1) {
                Toast.makeText(MyProjectBox.this,"Success: " + message, Toast.LENGTH_SHORT).show();
                //Intent intent = new Intent(MainActivity.this, Home.class);
                finish();
                //intent.putExtra("name",name);
                //startActivity(intent);

            } else {
                // failed to create product
                Toast.makeText(MyProjectBox.this,"Error: " + message , Toast.LENGTH_SHORT).show();
            }
        }

    }

/////////////////////////////////////////////////////////////////////////////////////////////////////////////



}
