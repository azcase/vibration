package com.example.keisuken.vibrate_detect;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class MailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail);
        MainActivity.f_flag.setflag(true);
        AnswerActivity.f_flag.setflag(true);
        TextView text = (TextView) findViewById(R.id.textView4);
        if(getIntent().getBooleanExtra("result",false)){
            text.setText("Success");
        }else{
            text.setText("Failed");
        }
        text = (TextView) findViewById(R.id.textView6);
        text.setText("Trial:"+getIntent().getIntExtra("trial",0));
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                TextView textview = (TextView) findViewById(R.id.textView4);
                String address="n1410092kp@gmail.com";
                String subject=textview.getText()+" to detection";
                String text = "Trial:"+getIntent().getIntExtra("trial",0);
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.setType("plain/text");

                String filename ="test.csv";
                String output = "trial, "+getIntent().getIntExtra("trial",0);
                FileOutputStream OutputStream;
                try{
                    OutputStream=openFileOutput(filename, 0);
                    OutputStream.write(output.getBytes());
                    OutputStream.close();
                }catch(Exception e){
                    e.printStackTrace();
                }
                File file=new File(filename);
                Uri uri = Uri.parse("file://data/data/package_name/files/"+file.getAbsolutePath());

                intent.putExtra(Intent.EXTRA_EMAIL, new String[] { address });
                intent.putExtra(Intent.EXTRA_SUBJECT, subject);
                intent.putExtra(Intent.EXTRA_TEXT, text);
                intent.putExtra(Intent.EXTRA_STREAM, uri);

                startActivity(Intent.createChooser(intent,null));
            }
        });

        Button button = (Button) findViewById(R.id.button4);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void onResume(){
        super.onResume();
        View decor = this.getWindow().getDecorView();
        decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }
}
