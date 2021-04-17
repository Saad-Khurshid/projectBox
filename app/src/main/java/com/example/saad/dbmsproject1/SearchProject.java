package com.example.saad.dbmsproject1;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SearchProject extends ListActivity {

    private ProgressDialog pDialog;

    // Creating JSON Parser object
    JSONParser jParser = new JSONParser();
    List<NameValuePair> params;
    ArrayList<HashMap<String, String>> projectsList;
    int success=0;
    // url to get all products list

    private static final String url_search_projects = "http://projectbux.000webhostapp.com/androidDBMS/searchProject.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PROJECTS = "projects";
    private static final String TAG_PID = "p_id";
    private static final String TAG_TITLE = "name";
    private static final String TAG_ABSTRACT = "abstract";
    private static final String TAG_URL = "URL";
    JSONArray projects = null;
    String search;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        search = bundle.getString("search");
        Log.d("AAAAAAAAAAAAAAAAAAAAAAA",search);

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());
        // Hashmap for ListView
        projectsList = new ArrayList<>();
        //listProjects = getListView();
        new SearchProjects().execute();
        // Get listview
        ListView lv = getListView();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                String pid = ((TextView) view.findViewById(R.id.pid)).getText()
                        .toString();
                String title = ((TextView) view.findViewById(R.id.name)).getText()
                        .toString();
                String abs = ((TextView) view.findViewById(R.id.abs)).getText()
                        .toString();
                String url = ((TextView) view.findViewById(R.id.url)).getText()
                        .toString();
                Intent intent = new Intent(SearchProject.this, ProjectBox.class);
                intent.putExtra("p_id", pid);
                intent.putExtra("title", title);
                intent.putExtra("abs", abs);
                intent.putExtra("url", url);
                finish();
                startActivity(intent);
            }
        });

    }


    class SearchProjects extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(SearchProject.this);
            pDialog.setMessage("Loading Projects. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * getting All products from url
         */
        protected String doInBackground(String... args) {
            // Building Parameters
            List<NameValuePair> params = new ArrayList<>();

            // getting JSON string from URL
            params = new ArrayList<>();
            params.add(new BasicNameValuePair("search", search));
            params.add(new BasicNameValuePair("search1", search));

            JSONObject json = jParser.makeHttpRequest(url_search_projects, "GET", params);

            // Check your log cat for JSON reponse
            Log.d("All Projects: ", json.toString());

            try {
                // Checking for SUCCESS TAG
                success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // products found
                    // Getting Array of patients
                    projects = json.getJSONArray(TAG_PROJECTS);

                    // looping through All Patients
                    for (int i = 0; i < projects.length(); i++) {
                        JSONObject c = projects.getJSONObject(i);

                        // Storing each json item in variable
                        String id = c.getString(TAG_PID);
                        String name = c.getString(TAG_TITLE);
                        String Abstract = c.getString(TAG_ABSTRACT);
                        String url = c.getString(TAG_URL);
                        // creating new HashMap
                        HashMap<String, String> map = new HashMap<>();

                        // adding each child node to HashMap key => value
                        map.put(TAG_PID, id);
                        map.put(TAG_TITLE, name);
                        map.put(TAG_ABSTRACT, Abstract);
                        map.put(TAG_URL, url);

                        // adding HashList to ArrayList
                        projectsList.add(map);
                    }
                } else {
                    //Toast.makeText(SearchProject.this,"No results found for your search...", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products
            pDialog.dismiss();
            // updating UI from Background Thread
            if(success == 1) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        /**
                         * Updating parsed JSON data into ListView
                         * */
                        ListAdapter adapter = new SimpleAdapter(
                                SearchProject.this, projectsList,
                                R.layout.list_item, new String[]{TAG_PID,
                                TAG_TITLE, TAG_ABSTRACT, TAG_URL},
                                new int[]{R.id.pid, R.id.name, R.id.abs, R.id.url});
                        // updating listview
                        setListAdapter(adapter);
                    }
                });
            }
            else{
                Toast.makeText(SearchProject.this,"No results found for your search...", Toast.LENGTH_SHORT).show();
            }
        }
    }





}
