package com.example.saad.dbmsproject1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.TextView;

public class Home extends AppCompatActivity {

    public Button btnAddProject, btnListProject, btnLogout, btnMyProject;
    ImageButton btnSearch ;
    EditText textSearch;
    TextView textView ;


    String name ;

    SearchView searchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Bundle bundle = getIntent().getExtras();
        name =  bundle.getString("name");

        btnSearch = findViewById(R.id.searchButton);
        textSearch = findViewById(R.id.searchText);

        textView = findViewById(R.id.textView);


        if( name!= null)
        {
            textView.setText("Welcome " + name);
        }

        btnAddProject = findViewById(R.id.addProject);
        btnAddProject.setBackgroundColor(getResources().getColor(R.color.buttonColor));
        btnListProject = findViewById(R.id.listProject);
        btnListProject.setBackgroundColor(getResources().getColor(R.color.buttonColor));
        btnLogout = findViewById(R.id.Logout);
        btnLogout.setBackgroundColor(getResources().getColor(R.color.buttonColor));
        btnMyProject = findViewById(R.id.myProject);
        btnMyProject.setBackgroundColor(getResources().getColor(R.color.buttonColor));


    }

    public void onClickListProject(View view) {
        Intent intent = new Intent(this,ListProject.class);
        intent.putExtra("name",name);
        startActivity(intent);
    }


    public void onClickAddProject(View view) {
        Intent intent = new Intent(this,AddProject.class);
        intent.putExtra("name",name);
        //finish();
        startActivity(intent);

    }

    public void onClickLogout(View view) {
       // Intent intent = new Intent(this,MainActivity.class);
        //intent.putExtra("name",name);
        finish();
        //startActivity(intent);
    }

    public void onClickMyProjects(View view) {
        Intent intent = new Intent(this,MyProjects.class);
        intent.putExtra("name",name);

        startActivity(intent);

    }

    public void onClickSearchProject(View view) {
        String search = textSearch.getText().toString();
        Intent intent = new Intent(this,SearchProject.class);
        intent.putExtra("search",search);
        //finish();
        startActivity(intent);

    }
}
