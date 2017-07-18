package com.example.keisuken.vibrate_detect;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Vibrator;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Random;


public class MainActivity extends Activity  {

    Random random = new Random();
    int try_number=0;
    String Answer_PIN = "";
    boolean FLAG=true;
    boolean Start_Flag=false;
    boolean Goal_Flag=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Answer_PIN=""+getIntent().getIntExtra("answer_number", 0);
        Button button = (Button)findViewById(R.id.button2);
        button.setText("Detect");
        if(getIntent().getBooleanExtra("hand",true)){
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) button.getLayoutParams();
            lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 1);
            button.setLayoutParams(lp);
        }else{
            TextView text = (TextView)findViewById(R.id.textView);
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) text.getLayoutParams();
            lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 1);
            text.setLayoutParams(lp);
        }
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public  void onClick(View view){
                Intent intent = new Intent(getApplicationContext(), AnswerActivity.class);
                intent.putExtra("answer", Answer_PIN);
                intent.putExtra("trial", try_number);
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



    Handler long_press_handler = new Handler();
    Runnable long_press_runnable = new Runnable() {
        @Override
        public void run() {
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(50);
            FLAG=false;
        }
    };


    public boolean dispatchTouchEvent(MotionEvent event){
        if(event.getPointerCount()<2){
            int touchX = (int)event.getRawX();
            int touchY = (int)event.getRawY();

            int start_area=90;
            int black_area=40;

            View view = findViewById(R.id.view);
            int view_coordinate[] = new int[2];
            view.getLocationOnScreen(view_coordinate);
            int view_left = view_coordinate[0];
            int view_right = view_left+view.getWidth();
            int viewY = view_coordinate[1];
            int view_height = (view.getHeight()-(start_area-black_area))/11;
            int i,j;
            view.onTouchEvent(event);

            Button button=(Button)findViewById(R.id.button2);
            int button_coordinate[] = new int[2];
            button.getLocationOnScreen(button_coordinate);
            if(touchX>button_coordinate[0]&&touchX<button_coordinate[0]+button.getWidth()&&touchY>button_coordinate[1]&&Goal_Flag&&try_number>0) button.onTouchEvent(event);


            switch (event.getAction()){

                case MotionEvent.ACTION_DOWN:
                    if(touchX>view_left&&touchX<view_right&&touchY<(viewY+start_area)&&!Start_Flag){
                        Start_Flag=true;
                        Goal_Flag=false;
                        try_number++;
                    }
                    break;

                case MotionEvent.ACTION_UP:
                    long_press_handler.removeCallbacks(long_press_runnable);
                    FLAG=true;
                    Start_Flag=false;
                    break;

                case MotionEvent.ACTION_MOVE:
                    for(i=0;i<10;i++) {
                        int rect_black_Top = (viewY+start_area)+i*view_height;
                        int rect_black_Bottom = viewY+(i+1)*view_height+(start_area-black_area);
                        if(touchX>view_left&&touchX<view_right&&touchY>rect_black_Top&&touchY<rect_black_Bottom&&FLAG&&touchY<viewY+start_area+10*view_height&&Answer_PIN.equals(""+i)&&Start_Flag){
                            long_press_handler.postDelayed(long_press_runnable, 0);
                        }else if((touchX<view_left||touchX>view_right)||(touchY<rect_black_Top+view_height&&touchY>rect_black_Bottom)||touchY<(viewY + 90)||touchY>viewY+start_area+10*view_height){
                            long_press_handler.removeCallbacks(long_press_runnable);
                            FLAG=true;
                        }
                    }
                    if(touchY>viewY+start_area+10*view_height&&Start_Flag){
                        Goal_Flag=true;
                    }
                    break;
            }
            TextView text = (TextView)findViewById(R.id.textView);
            String str = "Trial:"+try_number;
            text.setText(str);

        }else{
            Start_Flag=false;
            Goal_Flag=false;
        }
        return false;
    }

    public static Finishflag f_flag = new Finishflag();
    @Override
    public void onRestart(){
        super.onRestart();
        if(f_flag.getflag()==true){
            finish();;
        }
    }

}
