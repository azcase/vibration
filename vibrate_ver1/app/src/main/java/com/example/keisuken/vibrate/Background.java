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
        Canvas c = holder.lockCanvas();
        c.drawColor(Color.BLACK);
        int height=(c.getHeight()-40)/10;
        int width=c.getWidth();
        int i;
        Rect rect;
        for(i=0;i<10;i++){
            if(TouchX>0&&TouchX<width&&TouchY>40+i*height&&TouchY<(i+1)*height&&FLAG) {
                p.setColor(Color.BLUE);
            }else{
                p.setColor(Color.WHITE);
            }
            rect = new Rect(0, 40+i*height, width, ((i+1)*height));
            c.drawRect(rect, p);
            if(TouchX>0&&TouchX<width&&TouchY>40+i*height&&TouchY<(i+1)*height&&FLAG) {
                p.setColor(Color.WHITE);
            }else{
                p.setColor(Color.BLACK);
            }
            p.setTextSize(64);
            c.drawText(""+i, width/2, 40+i*height+(height/2),p);
        }
        holder.unlockCanvasAndPost(c);
    }

}

