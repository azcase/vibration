package com.example.keisuken.vibrate;

import android.os.Vibrator;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Random;


public class MainActivity extends ActionBarActivity implements View.OnTouchListener{

    Random random = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        View view = findViewById(R.id.view);
        view.setOnTouchListener(this);
    }

    @Override
    protected void onResume(){
        super.onResume();
        View decor = this.getWindow().getDecorView();
        decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }


    boolean FLAG=true;
    boolean Touch_Flag=true;
    int answer=100;

    Handler long_press_handler = new Handler();
    Runnable long_press_runnable = new Runnable() {
        @Override
        public void run() {
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(50);
            FLAG=false;
        }
    };


    public boolean onTouch(View v,MotionEvent event){
        int touchX = (int)event.getRawX();
        int touchY = (int)event.getRawY();

        int start_area=90;
        int black_area=40;

        View view = findViewById(R.id.view);
        view.onTouchEvent(event);
        int view_coordinate[] = new int[2];
        view.getLocationOnScreen(view_coordinate);
        int view_left = view_coordinate[0];
        int view_right = view.getRight();
        int viewY = view_coordinate[1];
        int view_height = (view.getHeight()-(start_area-black_area))/11;
        int i;
        int con;
        if(touchY<(viewY+start_area)&&Touch_Flag==true){
            answer= con;
            Touch_Flag=false;
        }

        switch (event.getAction()){

            case MotionEvent.ACTION_DOWN:
                for(i=0;i<11;i++) {
                    int rect_black_Top = (viewY+start_area)+i*view_height;
                    int rect_black_Bottom = viewY+(i+1)*view_height+(start_area-black_area);
                    if(touchX>view_left&&touchX<view_right&&touchY>rect_black_Top&&touchY<rect_black_Bottom&&answer==i){
                        long_press_handler.postDelayed(long_press_runnable, 0);
                    }
                }
                break;

            case MotionEvent.ACTION_UP:
                long_press_handler.removeCallbacks(long_press_runnable);
                FLAG=true;
                if(touchX>view_left&&touchX<view_right&&touchY>viewY+10*view_height+(start_area-black_area)){
                    Touch_Flag=true;
                }
                break;

            case MotionEvent.ACTION_MOVE:
                for(i=0;i<11;i++) {
                    int rect_black_Top = (viewY+start_area)+i*view_height;
                    int rect_black_Bottom = viewY+(i+1)*view_height+(start_area-black_area);
                    if(touchX>view_left&&touchX<view_right&&touchY>rect_black_Top&&touchY<rect_black_Bottom&&FLAG&&answer==i){
                        long_press_handler.postDelayed(long_press_runnable, 0);
                    }else if((touchX<view_left||touchX>view_right)||(touchY<rect_black_Top+view_height&&touchY>rect_black_Bottom)||touchY<(viewY+40)||touchY>viewY+10*view_height){
                        long_press_handler.removeCallbacks(long_press_runnable);
                        FLAG=true;
                    }
                }
                break;
        }
        TextView text = (TextView)findViewById(R.id.textView);
        text.setText("view("+view_left+","+viewY+"),touch("+touchX+","+touchY+") answer="+ answer);
        return true;
    }

}
