package com.example.keisuken.vibrate_detect;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class AnswerActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer);
        Button button;
        Resources res = getResources();
        int i;
        for(i=0;i<11;i++){
            int id = res.getIdentifier("button"+(5+i), "id", getPackageName());
            button = (Button)findViewById(id);
            final String str = button.getText().toString();
            button.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    TextView e = (TextView) findViewById(R.id.textView7);
                    String string = e.getText()+str;
                    if(!str.equals("Clear"))e.setText(string);
                    else e.setText("");
                }
            });
        }
        button = (Button) findViewById(R.id.button3);
        button.setText("Answer");

        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                TextView e = (TextView) findViewById(R.id.textView7);
                String answer = getIntent().getStringExtra("answer");
                Intent intent = new Intent(getApplicationContext(), MailActivity.class);
                intent.putExtra("trial",getIntent().getIntExtra("trial",0));
                if(answer.equals(e.getText().toString())){
                    intent.putExtra("result", true);
                }else{
                    intent.putExtra("result",false);
                }
                intent.putExtra("answer",answer);
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

    public static Finishflag f_flag = new Finishflag();
    @Override
    public void onRestart(){
        super.onRestart();
        if(f_flag.getflag()){
            finish();;
        }
    }

}
