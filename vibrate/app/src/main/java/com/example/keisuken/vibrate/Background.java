package com.example.keisuken.vibrate;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;


/**
 * Created by keisuke n on 2017/05/26.
 */
public class Background extends SurfaceView implements SurfaceHolder.Callback {
    private Paint p=new Paint();;
    int TouchX;
    int TouchY;
    int key;
    boolean FLAG=true;
    boolean change_flag=true;
    Context context;

    private void init(Context context){
        this.context=context;
        SurfaceHolder holder = this.getHolder();
        holder.addCallback(this);
    }

    public Background(Context context){
        super(context);
        init(context);
    }

    public Background(Context context, AttributeSet attributes){
        super(context, attributes);
        init(context);
    }

    public Background(Context context, AttributeSet attributes, int defstyle){
        super(context, attributes, defstyle);
        init(context);
    }
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        onDraw(holder);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    public boolean onTouchEvent(MotionEvent event){
        TouchX=(int)event.getX()-48;
        TouchY=(int)event.getY()-105;
        FLAG=true;
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                onDraw(this.getHolder());
                break;

            case MotionEvent.ACTION_UP:
                FLAG=false;
                onDraw(this.getHolder());
                break;

            case MotionEvent.ACTION_MOVE:
                onDraw(this.getHolder());
                break;
        }
        return true;
    }


    int input[][] = new int[15][15];
    @SuppressLint("DrawAllocation")
    protected void onDraw(SurfaceHolder holder){
        int divide = 11;
        int start_area=90;
        int black_area=40;
        Canvas c = holder.lockCanvas();
        c.drawColor(Color.BLACK);
        int height=(c.getHeight()-(start_area-black_area))/divide;
        int width=c.getWidth();
        int i,j,k;
        p.setColor(Color.WHITE);
        p.setTextSize(64);
        c.drawText("START", width / 2 - 80, start_area/2+20, p);
        Rect rect;

        if(this.change_flag&&TouchX>0&&TouchX<width&&TouchY>0&&TouchY<start_area){
            input = Shuffle();
            this.change_flag=false;
        }else if(TouchX>0&&TouchX<width&&TouchY>start_area+(divide-1)*height&&TouchY<divide*height+(start_area-black_area)){
            this.change_flag=true;
        }
        for(i=0;i<divide;i++){
            if(TouchX>0&&TouchX<width&&TouchY>start_area+i*height&&TouchY<(i+1)*height+(start_area-black_area)&&FLAG&&i<divide-1) {
                p.setColor(Color.BLUE);
                rect = new Rect(0, start_area+i*height, width, ((i+1)*height+(start_area-black_area)));
                c.drawRect(rect, p);
                for(j=0;j<10;j++) {
                    p.setColor(Color.BLUE);
                    c.drawText(""+ input[i][j], j*width/10+10, start_area+i*height+60,p);
                    if (i != 0) {
                        setWB(j+1);
                        rect = new Rect(j*width/10, start_area+i*height-40, (j+1)*width/10, start_area+i*height);
                        c.drawRect(rect,p);
                    }
                }
                p.setColor(Color.WHITE);
            }else{
                for(j=0;j<10;j++) {
                    if (i != 0) {
                        setWB(j+1);
                        c.drawRect(new Rect(j*width/10, start_area+i*height-40, (j+1)*width/10, start_area+i*height), p);
                    }
                    setWB(j);
                    c.drawRect(new Rect(j*width/10, start_area+i*height, (j+1)*width/10,  ((i+1)*height+(start_area-black_area))), p);
                    setWB(j+1);
                    if(i<divide-1)c.drawText(""+ input[i][j], j*width/10+30, start_area+i*height+60,p);
                }
                p.setColor(Color.BLACK);
            }
            p.setTextSize(64);
            c.drawText("" + i, width / 2, start_area + i * height + (height / 2), p);
        }
        holder.unlockCanvasAndPost(c);
    }


    private void setWB(int number){
        if (number % 2 == 0) {
            p.setColor(Color.WHITE);
        } else {
            p.setColor(Color.BLACK);
        }
    }

    private int[][] Shuffle(){
        get_Input(this.context);
        ArrayList<Integer> input = new ArrayList<>();
        int h,i,j;
        boolean flag;
        int r[][]=new int[15][15];
        r[0]=this.input[0];
        for(i=0;i<10;i++)input.add(i);
        for (h=1;h<9;h++){
            flag=true;
            Collections.shuffle(input);
            for (i=0;i<h&&flag;i++){
                for (j=0;j<10&&flag;j++){
                    if(r[i][j]==input.get(j)){
                        flag=false;
                        h--;
                    }
                }
            }
            for(i=0;i<10&&flag;i++){
                r[h][i]=input.get(i);
            }
        }
        //一番下の数列を選択
        for(h=0;h<10;h++){
            j=0;
            for(i=0;i<9;i++){
               j+=r[i][h];
            }
            r[9][h]=(45-j);
        }
        r[0] = r[this.key];
        r[this.key]=this.input[0];

        return r;

    }

    private void get_Input(Context context){
        SharedPreferences preferences = context.getSharedPreferences("Ten_Ten",Context.MODE_PRIVATE | context.MODE_MULTI_PROCESS);
        int i;
        this.key = preferences.getInt("key", 0);
        for(i=0;i<10;i++){
            this.input[0][i]=preferences.getInt("input"+i,i);
        }
    }
}

