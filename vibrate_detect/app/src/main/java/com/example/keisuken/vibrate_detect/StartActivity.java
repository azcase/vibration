package com.example.keisuken.vibrate_detect;

import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.Random;

public class StartActivity extends AppCompatActivity {

    Random random = new Random();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        TextView text =(TextView) findViewById(R.id.textView3);
        text.setText("Where is the place of vibration?");

        Button button = (Button)findViewById(R.id.button);
        button.setText("Start");
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public  void onClick(View view){
                RadioGroup radioGroup = (RadioGroup) findViewById(R.id.RadioGroup);
                int checkID = radioGroup.getCheckedRadioButtonId();
                RadioButton radioButton = (RadioButton) findViewById(checkID);
                boolean hand=true;
                if(radioButton.getText().equals("right handed")){
                    hand=false;
                }
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("answer_number", random.nextInt(10));
                intent.putExtra("hand",hand);
                startActivity(intent);
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
