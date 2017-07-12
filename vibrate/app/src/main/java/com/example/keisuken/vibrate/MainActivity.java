package com.example.keisuken.vibrate;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Vibrator;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Random;


public class MainActivity extends Activity {

    Random random = new Random();
    String Answer_PIN = "1234";
    String Enter_PIN="";
    boolean FLAG=true;
    boolean Touch_Flag=true;
    boolean Entry_Flag=true;
    boolean Start_Flag=false;
    boolean flag=true;
    int answer=100;
    int enter=-1;
    int input[]= new int[10];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        if(Enter_PIN.length()<4){
            int touchX = (int)event.getRawX();
            int touchY = (int)event.getRawY();

            int start_area=90;
            int black_area=40;

            View view = findViewById(R.id.view);
            int view_coordinate[] = new int[2];
            view.getLocationOnScreen(view_coordinate);
            int view_left = view_coordinate[0];
            int view_right = view.getRight();
            int viewY = view_coordinate[1];
            int view_height = (view.getHeight()-(start_area-black_area))/11;
            int i,j;

            if(touchY<(viewY+start_area)&&Touch_Flag){
                answer= random.nextInt(10);
                input = Shuffle();
                Touch_Flag=false;
                Start_Flag=true;
                if(enter>=0)Enter_PIN+=enter;
            }

            if(Enter_PIN.length()==Answer_PIN.length()){
                Intent intent = new Intent(getApplicationContext(), AnswerActivity.class);
                intent.putExtra("answer", Enter_PIN.equals(Answer_PIN));
                startActivity(intent);
            }

            if(Enter_PIN.length()<4)view.onTouchEvent(event);

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
                    break;

                case MotionEvent.ACTION_MOVE:
                    for(i=0;i<11;i++) {
                        int rect_black_Top = (viewY+start_area)+i*view_height;
                        int rect_black_Bottom = viewY+(i+1)*view_height+(start_area-black_area);
                        if(touchX>view_left&&touchX<view_right&&touchY>rect_black_Top&&touchY<rect_black_Bottom&&FLAG&&answer==i){
                            long_press_handler.postDelayed(long_press_runnable, 0);
                        }else if((touchX<view_left||touchX>view_right)||(touchY<rect_black_Top+view_height&&touchY>rect_black_Bottom)||touchY<(viewY+90)||touchY>viewY+10*view_height){
                            long_press_handler.removeCallbacks(long_press_runnable);
                            FLAG=true;
                        }
                        if(touchX>view_left&&touchX<view_right&&touchY>viewY+10*view_height+(start_area-black_area)&&!Entry_Flag){
                            Touch_Flag=true;
                            Entry_Flag=true;
                        }else if(touchX>view_left&&touchX<view_right&&touchY<viewY+10*view_height+(start_area-black_area)&&Entry_Flag&&Start_Flag){
                            Entry_Flag=false;
                            for(j=0;j<10;j++){
                                if(touchX>view_left+j*(view_right-view_left)/10&&touchX<view_left+(j+1)*(view_right-view_left)/10){
                                    enter=input[j];
                                }
                            }
                        }
                    }
                    break;
            }
            TextView text = (TextView)findViewById(R.id.textView);
            String str = "view("+view_left+","+viewY+"),touch("+touchX+","+touchY+") answer="+Enter_PIN;
            text.setText(str);
        }
        return true;
    }

    private int[] Shuffle(){
        SharedPreferences preferences = getSharedPreferences("Ten_Ten", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        ArrayList<Integer> input = new ArrayList<>();
        int i;
        int r[] = new int[10];
        for(i=0;i<10;i++)input.add(i);
        Collections.shuffle(input);
        for(i=0;i<10;i++){
            r[i]=input.get(i);
            editor.putInt("input"+i, input.get(i)+1);
            editor.commit();
        }
        editor.putInt("key",this.answer);
        editor.commit();
        return r;

    }

}
