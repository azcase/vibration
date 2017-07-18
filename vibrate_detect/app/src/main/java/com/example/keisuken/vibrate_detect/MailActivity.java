package com.example.keisuken.vibrate_detect;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Environment;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AndroidException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.jar.Manifest;

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
        text.setText("Trial:"+getIntent().getIntExtra("trial",0)+"  answer:"+getIntent().getStringExtra("answer"));

        String filename ="test.csv";
        File f = new File(Environment.getExternalStorageDirectory().getPath()+"/download/"+filename);
        f.getParentFile().mkdir();
        String output_string = "trial, answer\n";
        String output_number = ""+getIntent().getIntExtra("trial",0)+", "+getIntent().getStringExtra("answer");

        Log.d("debug",Environment.getExternalStorageDirectory().getPath());
        FileOutputStream OutputStream;
        try{
            OutputStream=new FileOutputStream(f,false);
            OutputStream.write(output_string.getBytes());
            OutputStream.write(output_number.getBytes());
            OutputStream.flush();
            OutputStream.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                TextView textview = (TextView) findViewById(R.id.textView4);
                String address="n1410092kp@gmail.com";
                String subject=textview.getText()+" to detection";
                String text = "Trial:"+getIntent().getIntExtra("trial",0)+"  answer:"+getIntent().getStringExtra("answer");
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);

                File file=new File(Environment.getExternalStorageDirectory().getPath()+"/download/test.csv");

                intent.putExtra(Intent.EXTRA_EMAIL, new String[] { address });
                intent.putExtra(Intent.EXTRA_SUBJECT, subject);
                intent.putExtra(Intent.EXTRA_TEXT, text);
                intent.setType("plain/text");
                intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));

                startActivity(intent);
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
