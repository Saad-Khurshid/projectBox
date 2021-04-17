package com.example.saad.dbmsproject1;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ProjectBox extends AppCompatActivity {
    TextView ptitle, pabstract;
    Button downloadProject;
    PROJECT project = new PROJECT();
    String p_id, title, abs, url;
    DatabaseHelper helper = new DatabaseHelper(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_box);

        ptitle = findViewById(R.id.TitleData);
        pabstract = findViewById(R.id.AbstractData);
        downloadProject = findViewById(R.id.Download);
        downloadProject.setBackgroundColor(getResources().getColor(R.color.buttonColor));


        Bundle bundle = getIntent().getExtras();
        p_id = bundle.getString("p_id");
        title = bundle.getString("title");
        abs = bundle.getString("abs");
        url = bundle.getString("url");

        //project = helper.getProjectbyID(p_id);

        ptitle.setText(title);
        pabstract.setText(abs);


        downloadProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                finish();
                startActivity(intent);

            }
        });

    }
}
