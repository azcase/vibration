package com.example.keisuken.vibrate;

import android.os.Vibrator;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.os.Handler;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity implements View.OnTouchListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        View view = findViewById(R.id.view);
        view.setOnTouchListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    boolean FLAG=true;

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
        View view = findViewById(R.id.view);
        view.onTouchEvent(event);
        int view_coordinate[] = new int[2];
        view.getLocationOnScreen(view_coordinate);
        int view_left = view_coordinate[0];
        int view_right = view.getRight();
        int viewY = view_coordinate[1];
        int view_height = (view.getHeight()-40)/10;
        int i;
        Background bg = new Background(this);


        switch (event.getAction()){

            case MotionEvent.ACTION_DOWN:
                for(i=0;i<10;i++) {
                    int rect_black_Top = (viewY+40)+i*view_height;
                    int rect_black_Bottom = viewY+(i+1)*view_height;
                    if(touchX>view_left&&touchX<view_right&&touchY>rect_black_Top&&touchY<rect_black_Bottom){
                        long_press_handler.postDelayed(long_press_runnable, 0);
                    }
                }
                break;

            case MotionEvent.ACTION_UP:
                FLAG=true;
                long_press_handler.removeCallbacks(long_press_runnable);
                break;

            case MotionEvent.ACTION_MOVE:
                for(i=0;i<10;i++) {
                    int rect_black_Top = (viewY+40)+i*view_height;
                    int rect_black_Bottom = viewY+(i+1)*view_height;
                    if(touchX>view_left&&touchX<view_right&&touchY>rect_black_Top&&touchY<rect_black_Bottom&&FLAG){
                        long_press_handler.postDelayed(long_press_runnable, 0);
                    }else if((touchX<view_left||touchX>view_right)||(touchY<rect_black_Top+view_height&&touchY>rect_black_Bottom)||touchY<(viewY+40)||touchY>viewY+10*view_height){
                        long_press_handler.removeCallbacks(long_press_runnable);
                        FLAG=true;
                    }
                }
                break;
        }
        TextView text = (TextView)findViewById(R.id.textView);
        text.setText("view("+view_left+","+viewY+"),touch("+touchX+","+touchY+")");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
