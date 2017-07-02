package com.example.keisuken.vibrate_detect;

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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;


/**
 * Created by keisuke n on 2017/05/26.
 */
public class Background extends SurfaceView implements SurfaceHolder.Callback {
    private Paint p=new Paint();;
    int TouchX;
    int TouchY;
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

        for(i=0;i<divide-1;i++){
            if(TouchX>0&&TouchX<width&&TouchY>start_area+i*height&&TouchY<(i+1)*height+(start_area-black_area)&&FLAG&&i<divide-1) {
                p.setColor(Color.BLUE);
                rect = new Rect(0, start_area+i*height, width, ((i+1)*height+(start_area-black_area)));
                c.drawRect(rect, p);
                p.setColor(Color.WHITE);
            }else{
                p.setColor(Color.WHITE);
                rect = new Rect(0, start_area+i*height, width, ((i+1)*height+(start_area-black_area)));
                c.drawRect(rect, p);
                p.setColor(Color.BLACK);
            }
            p.setTextSize(64);
            c.drawText("" + i, width / 2, start_area + i * height + (height / 2), p);
        }
        p.setColor(Color.WHITE);
        rect = new Rect(0, start_area+i*height, width, c.getHeight());
        c.drawRect(rect, p);
        p.setColor(Color.BLACK);
        p.setTextSize(64);
        c.drawText("Goal", width / 2-50, start_area + i * height + (height / 2), p);
        holder.unlockCanvasAndPost(c);
    }


}

