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
        //input = Shuffle();

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
                    c.drawText(""+ (input[i][j]-1), j*width/10+10, start_area+i*height+60,p);
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
                    c.drawRect(new Rect(j * width / 10, start_area + i * height, (j + 1) * width / 10, ((i + 1) * height + (start_area - black_area))), p);
                    setWB(j + 1);
                    //p.setTextSize(32);
                    if(i<divide-1)c.drawText(""+ (input[i][j]-1), j*width/10+30, start_area+i*height+60,p);
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
        int r[][]=new int[15][15];
        int key = get_key(this.context);
        r[0]=get_Input(this.context);
        ArrayList<Integer> input = new ArrayList<>();
        ArrayList[] row = new ArrayList[10];
        int h,i,j;
        boolean flag;
        for(i=1;i<=10;i++){
            input.add(i);
            row[i-1]=new ArrayList();
            for(j=1;j<=10;j++)row[i-1].add(j);
        }
        for(i=0;i<10;i++)row[i].remove(row[i].indexOf(r[0][i]));
        for (h=1;h<8;h++){
            flag=true;
            Collections.shuffle(input);
            for (i=0;i<h&&flag;i++){
                if(row[i].indexOf(input.get(i))==-1){
                    flag=false;
                    h--;
                }
            }
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
                row[i].remove(row[i].indexOf(input.get(i)));
            }
        }
        //8列目を選択
        /*//入れられる数を複製
        ArrayList[] remain_row = new ArrayList[10];
        for(h=0;h<10;h++){
            remain_row[h]=new ArrayList();
            for(i=0;i<row[h].size();i++)remain_row[h].add(row[h].get(i));
        }
        //最初に、入れられる数字が二個重複している列から選択
        int input_a,input_b;
        for(h=0;h<10;h++){
            if(remain_row[h].size()<2)continue;
            for(i=h+1;i<10;i++){
                if(remain_row[i].size()<2)continue;
                input_a=0;
                input_b=0;
                for(j=0;j<remain_row[i].size();j++){
                    if(remain_row[h].indexOf(remain_row[i].get(j))!=-1){
                        if(input_a==0)input_a=(int)remain_row[h].get(remain_row[h].indexOf(remain_row[i].get(j)));
                        else if(input_b==0){
                            input_b=(int)remain_row[h].get(remain_row[h].indexOf(remain_row[i].get(j)));
                            break;
                        }
                    }
                }
                if(input_a!=0&&input_b!=0){
                    r[7][h]=input_a;
                    r[7][i]=input_b;
                    remain_row[h].clear();
                    remain_row[i].clear();
                    for(j=0;j<10;j++){
                        if(remain_row[j].indexOf(r[7][h])!=-1)remain_row[j].remove(remain_row[j].indexOf(r[7][h]));
                        if(remain_row[j].indexOf(r[7][i])!=-1)remain_row[j].remove(remain_row[j].indexOf(r[7][i]));
                    }
                    break;
                }
            }
            for(i=0;i<10;i++){
                if(remain_row[i].size()==1){
                    r[7][i]=(int) remain_row[i].get(0);
                    remain_row[i].clear();
                    for(j=0;j<10;j++){
                        if(remain_row[j].indexOf(r[7][i])!=-1)remain_row[j].remove(remain_row[j].indexOf(r[7][i]));
                        if(remain_row[j].size()==1)i=j;
                    }
                }
            }
        }


        //最後に、選択されなかった数字を選択する


        //for(h=0;h<10;h++) row[h].remove(row[h].indexOf(r[7][h]));

        */
        // 9列目の選択
        i=0;
        r[8][i]= (int) row[i].get(0);
        row[i].clear();
        for(j=0;j<10;j++){
            for(h=1;h<10;h++){
                if(h!=i&&row[h].indexOf(r[8][i])!=-1){
                    row[h].remove(row[h].indexOf(r[8][i]));
                    r[8][h]=(int) row[h].get(0);
                    row[h].clear();
                    i=h;
                    break;
                }
            }
            if(h==10){
                i=0;
                while(i<10&&row[i].size()<2)i++;
                if (i==10)break;
                r[8][i]= (int) row[i].get(0);
                row[i].clear();
            }
        }

        //10列目を選択
        for(h=0,j=0;h<10;h++,j=0){
            for(i=0;i<9;i++){
                j+=r[i][h];
            }
            r[9][h]=(55-j);
        }
        //振動する配列をkeyの位置に入れる
        r[10] = r[key];
        r[key]=r[0];
        r[0]=r[10];

        return r;

    }

    private int[] get_Input(Context context){
        SharedPreferences preferences = context.getSharedPreferences("Ten_Ten",Context.MODE_PRIVATE | context.MODE_MULTI_PROCESS);
        int i;
        int input[]=new int[10];
        for(i=0;i<10;i++){
            input[i]=preferences.getInt("input"+i,i);
        }
        return input;
    }

    /*int r[]=new int[10];
    private int[] setArray(ArrayList[] row){
        ArrayList[] a = new ArrayList[10];
        int r[] = new int[10];
        int h,i,j;
        for(h=0;h<10;h++){
            a[h]=new ArrayList();
            for(i=0;i<row[h].size();i++)a[h].add((int)row[h].get(i));
        }
        MakeArray(a, 0, r);

        return this.r;
    }

    private boolean MakeArray(ArrayList[] A, int num, int[] r){
        int h,i,j;
        boolean flag=true;
        boolean match_flag=true;
        for(h=0;num<10&&h<A[num].size()&&flag;h++){
            r[num] = (int)A[num].get(h);
            for(i=0;i<num&&flag;i++){
                if(r[num]==r[i]) match_flag=false;
            }
            if(num==0||match_flag)flag=MakeArray(A,num+1,r);

        }
        if(num==9&&flag){
            this.r=r;
            return false;
        }
        if(!flag)return false;
        return true;
    }*/

    private int get_key(Context context){
        SharedPreferences preferences = context.getSharedPreferences("Ten_Ten",Context.MODE_PRIVATE | context.MODE_MULTI_PROCESS);
        int key = preferences.getInt("key", 0);

        return key;
    }
}

