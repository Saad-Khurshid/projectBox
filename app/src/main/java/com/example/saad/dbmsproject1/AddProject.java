package com.example.saad.dbmsproject1;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AddProject extends AppCompatActivity {

    Button btnAddProject, btnSelectProject;
    TextView textViewSelectedFile;
    EditText editTextTitle, editTextAbstract;
    private int currentProgress;

    String url, name;

    JSONParser jsonParser = new JSONParser();
    int success;
    String message;
    List<NameValuePair> params;
    ProgressDialog pDialog;
    private static final String url_add_project = "http://projectbux.000webhostapp.com/androidDBMS/addProject.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";


    FirebaseDatabase firebaseDatabase; // urls here
    FirebaseStorage firebaseStorage; // files here

    Uri pdfUri; // Uri are actually URLs that are meant for local storage

    ProgressDialog progressDialog;


    DatabaseHelper helper = new DatabaseHelper(this);
    PROJECT project = new PROJECT();






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_project);
        Bundle bundle = getIntent().getExtras();
        name = bundle.getString("name");

        btnAddProject = findViewById(R.id.addproject);
        btnAddProject.setBackgroundColor(getResources().getColor(R.color.buttonColor));
        btnSelectProject = findViewById(R.id.selectFile);
        btnSelectProject.setBackgroundColor(getResources().getColor(R.color.buttonColor));
        editTextAbstract = findViewById(R.id.p_abstract);
        textViewSelectedFile = findViewById(R.id.SelectedFile);
        editTextTitle = findViewById(R.id.p_title);



        firebaseStorage= FirebaseStorage.getInstance(); // returns an object of firebase storage
        firebaseDatabase = FirebaseDatabase.getInstance(); // object of firebase database


        btnSelectProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(AddProject.this, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                    selectPDF();
                }
                else{
                    ActivityCompat.requestPermissions(AddProject.this,new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 9 );
                }
            }
        });

        btnAddProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                if(pdfUri != null ) {
                   //if ( helper.ProjectNameNotExist(editTextTitle.getText().toString()) ) {
                       uploadFile(pdfUri);

                   //}
                    //else{
                     //  Toast.makeText(AddProject.this, "The Project with the title already exist... please change the title.", Toast.LENGTH_SHORT).show();
                   //}

                }
                else{
                    Toast.makeText(AddProject.this, "You should select the file", Toast.LENGTH_SHORT).show();
                }
            }
        });



    }





    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //automatically invoked check for permission results
        if(requestCode == 9 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            selectPDF();
        }
        else{
            Toast.makeText(AddProject.this, "Please Provide Permission",Toast.LENGTH_SHORT).show();
        }
    }

    private void selectPDF() {
        //to offer user to select file using file manager

        // we will be using an intent

        Intent intent =  new Intent();
        intent.setType("application/pdf"); // denotes that our intent is targeted for PDF files
        intent.setAction( intent.ACTION_GET_CONTENT); //denoets that the intent is made to fetch files
        startActivityForResult(intent, 86);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // automatically invoked check whether user has selected file or not

        if(requestCode == 86 && resultCode==RESULT_OK && data!=null){

            pdfUri= data.getData();
            textViewSelectedFile.setText("A file is selected: " + data.getData().getLastPathSegment());
        }
        else{
            Toast.makeText(AddProject.this,"Please Select a file",Toast.LENGTH_SHORT).show();
        }
    }



    //Upload File functions here

    private void uploadFile(Uri pdfUri) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setTitle("Uploading File...");
        progressDialog.setProgress(0);
        progressDialog.show();

        final String filename = editTextTitle.getText().toString() +".pdf";
        //final String filename1 = editTextTitle.getText().toString() ;
        StorageReference storageReference = firebaseStorage.getReference(); //returns root Path
        storageReference.child("Uploads").child(filename).putFile(pdfUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                /*url = taskSnapshot.getStorage().getDownloadUrl().toString();*///taskSnapshot.getMetadata().getReference().getDownloadUrl().toString();
                url = taskSnapshot.getDownloadUrl().toString();



                final String title =  editTextTitle.getText().toString();
                final String Abstract = editTextAbstract.getText().toString();
                //final int uid = helper.get_idForUname(name);
               /* project.set_name(title);
                project.set_abstract(Abstract);
                project.set_uid(uid);
                project.set_time("NULL");
                project.set_url(url);
                helper.newProject(project);*/
                params = new ArrayList<>();
                params.add(new BasicNameValuePair("u_name", name));
                params.add(new BasicNameValuePair("abstract", Abstract));
                params.add(new BasicNameValuePair("URL", url));
                params.add(new BasicNameValuePair("title", title));
                if(currentProgress > 99) {
                    progressDialog.dismiss();
                    new AddNewProject().execute();
                }




            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(AddProject.this, "File is not successfully uploaded", Toast.LENGTH_SHORT).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                // track the progress of our upload show the progress bar;
                currentProgress = (int) ( 100 *  taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                progressDialog.setProgress(currentProgress);
            }
        }); // to make the sub folder or sub element where we will save our file

    }

   ////////////////////////////////////////////////////////////////////////////////////////////////////////////////



    class AddNewProject extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(AddProject.this);
            pDialog.setMessage("Adding Project...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Creating product
         * */
        protected String doInBackground(String... args) {
            JSONObject json = jsonParser.makeHttpRequest(url_add_project,
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
                Toast.makeText(AddProject.this,"Success: "+ message, Toast.LENGTH_SHORT).show();
                //Intent intent = new Intent(AddProject.this, Home.class);
                //intent.putExtra("name",name);
                finish();
                //startActivity(intent);

            } else {
                // failed to create product
                Toast.makeText(AddProject.this,"Error: " + message , Toast.LENGTH_SHORT).show();
            }
        }

    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

}
