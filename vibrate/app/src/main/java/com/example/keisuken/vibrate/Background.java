package com.example.keisuken.vibrate;

import android.annotation.SuppressLint;
import android.content.Context;
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


/**
 * Created by keisuke n on 2017/05/26.
 */
public class Background extends SurfaceView implements SurfaceHolder.Callback {
    private Paint p=new Paint();;
    int TouchX;
    int TouchY;
    boolean FLAG=true;

    private void init(){
        SurfaceHolder holder = this.getHolder();
        holder.addCallback(this);
    }

    public Background(Context context){
        super(context);
        init();
    }

    public Background(Context context, AttributeSet attributes){
        super(context, attributes);
        init();
    }

    public Background(Context context, AttributeSet attributes, int defstyle){
        super(context, attributes, defstyle);
        init();
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
        TouchX=(int)event.getX();
        TouchY=(int)event.getY();
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

    @SuppressLint("DrawAllocation")
    protected void onDraw(SurfaceHolder holder){
        int divide = 11;
        int start_area=90;
        int black_area=40;
        Canvas c = holder.lockCanvas();
        c.drawColor(Color.BLACK);
        int height=(c.getHeight()-(start_area-black_area))/divide;
        int width=c.getWidth();
        int i,j;
        p.setColor(Color.WHITE);
        p.setTextSize(64);
        c.drawText("START", width / 2 - 80, start_area/2+20, p);
        Rect rect;
        for(i=0;i<divide;i++){
            if(TouchX>0&&TouchX<width&&TouchY>start_area+i*height&&TouchY<(i+1)*height+(start_area-black_area)&&FLAG&&i<divide-1) {
                p.setColor(Color.BLUE);
                rect = new Rect(0, start_area+i*height, width, ((i+1)*height+(start_area-black_area)));
                c.drawRect(rect, p);
                for(j=0;j<10;j++) {
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

}

