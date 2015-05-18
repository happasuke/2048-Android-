//セルが4つ生成された
//画像の元画像を伸縮されて何とかしたい

package com.example.make2048;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.Random;


/**
 * Created by user09 on 2015/05/09.
 */
public class GameView extends SurfaceView implements SurfaceHolder.Callback,Runnable,alias{

    private Rect back_src_rect;
    private Rect back_rect;
    private Rect cell_src_rect;
    private Rect cell_rect;

    Canvas canvas;
    Thread thread=null;
    Paint paint;

    Bitmap back_img = BitmapFactory.decodeResource(getResources(),R.drawable.whole);
    Random random;
    Map m;

    private int point;
    private int mode;
    private int table_size;

    //touch event
    private float density; //最低限の移動距離
    private float startx;
    private float starty;
    private float endx;
    private float endy;
    private double leastDistance;
    private boolean isTouchProcess;
    int direction=0;


    //picture
    private Bitmap restart_img = BitmapFactory.decodeResource(getResources(),R.drawable.restart_button);
    private Bitmap[] number_img={
            BitmapFactory.decodeResource(getResources(),R.drawable.cell0),
            BitmapFactory.decodeResource(getResources(),R.drawable.cell2),
            BitmapFactory.decodeResource(getResources(),R.drawable.cell4),
            BitmapFactory.decodeResource(getResources(),R.drawable.cell8),
            BitmapFactory.decodeResource(getResources(),R.drawable.cell16),
            BitmapFactory.decodeResource(getResources(),R.drawable.cell32),
            BitmapFactory.decodeResource(getResources(),R.drawable.cell64),
            BitmapFactory.decodeResource(getResources(),R.drawable.cell128),
            BitmapFactory.decodeResource(getResources(),R.drawable.cell256),
            BitmapFactory.decodeResource(getResources(),R.drawable.cell512),
            BitmapFactory.decodeResource(getResources(),R.drawable.cell1024),
            BitmapFactory.decodeResource(getResources(),R.drawable.cell2048),
            BitmapFactory.decodeResource(getResources(),R.drawable.cell4096),
            BitmapFactory.decodeResource(getResources(),R.drawable.cell8192),
            BitmapFactory.decodeResource(getResources(),R.drawable.cell16384),
            BitmapFactory.decodeResource(getResources(),R.drawable.cell32768),
            BitmapFactory.decodeResource(getResources(),R.drawable.cell65536),
    };

    //cell
    public int CELL_WIDTH;
    public int CELL_START_X;
    public int CELL_START_Y;

    //move cells
    int markMap[] = new int[4];
    boolean isMoved=false;
    int t;

    //surfaceCreateが再描画されたときに初期設定を再びやらせないため
    private int isFirstCreate=0;

    //restart時にrestart button がタッチされたかどうかを判断するため
    private boolean restartButtonTouched=false;

    public GameView(Context context){
        super(context);

        density = getContext().getResources().getDisplayMetrics().density;

        getHolder().addCallback(this);

        paint = new Paint();

        table_size = 4;
        leastDistance = 50;
    }


    private void init(){
        point = 0;

        isTouchProcess = false;
        random = new Random();
        mode = START_GAME;
        CELL_WIDTH = getWidth()/5;
        CELL_START_X = getWidth()/10;
        CELL_START_Y = getHeight()/10;

        m = new Map(table_size);

        //CELLの画像サイズ変更用
        Matrix matrix = new Matrix();
        float widthScale = (float)number_img[0].getWidth()/(float)getWidth();
        Log.d("donatu","widthScale:"+widthScale);
        matrix.postScale(widthScale,widthScale);

        for(int i=0;i<17;i++) {
            number_img[i] = Bitmap.createBitmap(number_img[i], 0, 0, number_img[i].getWidth(), number_img[i].getWidth(),matrix,true);
        }

        back_src_rect = new Rect(0,0,back_img.getWidth(),back_img.getHeight());
        back_rect = new Rect(0,0,getWidth(),getHeight());
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder){
        Log.d("donatu","surfaceCreated");

        Log.d("donatu","isFirstCreated" + isFirstCreate);
        if(isFirstCreate==0) {
            init();
//            setIreg();

            isFirstCreate = 1;
        }

        Log.d("donatu","width:" + getWidth() + " height;" + getHeight());

        thread = new Thread(this);
        thread.start();

        mode = RUN_GAME;
    }


    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height){

    }


    @Override
    public void surfaceDestroyed(SurfaceHolder holder){
        Log.d("donatu","surface destroyed");
        thread = null;
    }


    @Override
    public void run(){
        while(thread != null){
/*            Update();
*/            Draw();

            try{
                Thread.sleep(sleep_time);
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event){

/*        Log.d("donatu","mode:"+mode);
        Log.d("donatu","x:"+event.getX()+" y:"+event.getY());
*/
        if(mode==RUN_GAME) {

            //タッチされた瞬間
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                startx = (int) event.getX();
                starty = (int) event.getY();
                isTouchProcess = false;

                //移動している間
            } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                endx = (int) event.getX();
                endy = (int) event.getY();

                float segmentx = endx - startx;
                float segmenty = endy - starty;

                if (leastDistance < Math.sqrt(Math.pow(segmentx, 2) + Math.pow(segmenty, 2))) {

                    if (isTouchProcess == false) {

                        isTouchProcess = true;

                        //vertical
                        if (Math.abs(segmenty) > Math.abs(segmentx)) {
                            //下
                            if (segmenty > 0) {
                                Log.d("donatu", "flick under");
                                direction = DOWN;

                                for (int i = 0; i < 4; i++) {
                                    for (int k = 0; k < 4; k++) markMap[k] = 0;
                                    for (int j = 2; j >= 0; j--) {
                                        if (m.getCell(i, j) > 0) {
                                            t = j - 1;
                                            while (t != 2) {
                                                ++t;
                                                if (m.getCell(i, t) == m.getCell(i, t + 1) && markMap[t + 1] == 0) {
                                                    markMap[t + 1] = 1;
                                                    m.setDoubleCell(i, t + 1);
                                                    m.setZeroCell(i, t);
                                                    isMoved = true;
                                                    break;
                                                } else if (m.getCell(i, t + 1) == 0) {
                                                    m.setCell(m.getCell(i, t), i, t + 1);
                                                    m.setZeroCell(i, t);
                                                    isMoved = true;
                                                }

                                            }
                                        }
                                    }
                                }

                            } else {
                                //上
                                Log.d("donatu", "flick upper");
                                direction = UP;

                                for (int i = 0; i < 4; i++) {
                                    for (int k = 0; k < 4; k++) markMap[k] = 0;
                                    for (int j = 1; j < 4; j++) {
                                        if (m.getCell(i, j) > 0) {
                                            t = j + 1;
                                            while (t != 1) {
                                                --t;
                                                if (m.getCell(i, t) == m.getCell(i, t - 1) && markMap[t - 1] == 0) {
                                                    markMap[t - 1] = 1;
                                                    m.setDoubleCell(i, t - 1);
                                                    m.setZeroCell(i, t);
                                                    isMoved = true;
                                                    break;
                                                } else if (m.getCell(i, t - 1) == 0) {
                                                    m.setCell(m.getCell(i, t), i, t - 1);
                                                    m.setZeroCell(i, t);
                                                    isMoved = true;
                                                }

                                            }
                                        }
                                    }
                                }

                            }

                            //horizontal
                        } else {
                            //右
                            if (segmentx > 0) {
                                Log.d("donatu", "flick right");
                                direction = RIGHT;

                                for (int i = 0; i < 4; i++) {
                                    for (int k = 0; k < 4; k++) markMap[k] = 0;
                                    for (int j = 2; j >= 0; j--) {
                                        if (m.getCell(j, i) > 0) {
                                            t = j - 1;
                                            while (t != 2) {
                                                ++t;
                                                if (m.getCell(t, i) == m.getCell(t + 1, i) && markMap[t + 1] == 0) {
                                                    markMap[t + 1] = 1;
                                                    m.setDoubleCell(t + 1, i);
                                                    m.setZeroCell(t, i);
                                                    isMoved = true;
                                                    break;
                                                } else if (m.getCell(t + 1, i) == 0) {
                                                    m.setCell(m.getCell(t, i), t + 1, i);
                                                    m.setZeroCell(t, i);
                                                    isMoved = true;
                                                }

                                            }
                                        }
                                    }
                                }

                                //左
                            } else {
                                Log.d("donatu", "flick left");
                                direction = LEFT;

                                for (int i = 0; i < 4; i++) {
                                    for (int k = 0; k < 4; k++) markMap[k] = 0;
                                    for (int j = 1; j < 4; j++) {
                                        if (m.getCell(j, i) > 0) {
                                            t = j + 1;
                                            while (t != 1) {
                                                --t;
                                                if (m.getCell(t, i) == m.getCell(t - 1, i) && markMap[t - 1] == 0) {
                                                    markMap[t - 1] = 1;
                                                    m.setDoubleCell(t - 1, i);
                                                    m.setZeroCell(t, i);
                                                    isMoved = true;
                                                    break;
                                                } else if (m.getCell(t - 1, i) == 0) {
                                                    m.setCell(m.getCell(t, i), t - 1, i);
                                                    m.setZeroCell(t, i);
                                                    isMoved = true;
                                                }

                                            }
                                        }
                                    }
                                }
                            }
                        }

                        if (isMoved == true) {
                            m.genCell();
                            point += m.detectScore();

                            isMoved = false;
                        }

                        if(m.checkGameOver()==true){
                            mode = END_GAME;

                            Log.d("donatu","x:"+event.getX() + " y:"+event.getY());

                            try{
                                Thread.sleep(1500);
                            }catch(Exception e){

                            }
                        }

                    }
                }

            }

        }else if(mode==END_GAME){
            Log.d("donatu","motion type:"+event.getAction());
            if(event.getAction()==MotionEvent.ACTION_DOWN) {
                float x = event.getX();
                float y = event.getY();

                if (getWidth() / 2 - restart_img.getWidth() / 2 < x && getWidth() / 2 + restart_img.getWidth() / 2 > x) {
                    if (getHeight() / 2 < y && getHeight() / 2 + restart_img.getHeight() > y) {
                        restartButtonTouched = true;
                        mode = RESET_GAME;
                        Log.d("donatu", "reset game");
                    }
                }
            }

        }

        return true;
    }



    public void Draw(){
        canvas = getHolder().lockCanvas();

        canvas.drawBitmap(back_img,back_src_rect,back_rect,paint);

        if(mode==RUN_GAME) {
            drawCells();
        }else if(mode==END_GAME){
            drawEndPrompt();
        }else if(mode==RESET_GAME){
            m.resetMap();
            point = 0;
            mode = RUN_GAME;
        }

        getHolder().unlockCanvasAndPost(canvas);
    }

    public void Update(){

    }

    public void drawCells(){
        for(int y=0;y<4;y++){
            for(int x=0;x<4;x++){
                canvas.drawBitmap(number_img[interchange(m.getCell(x,y))],CELL_START_X+x*CELL_WIDTH,CELL_START_Y+y*CELL_WIDTH,null);
            }
        }
    }

    public void drawEndPrompt(){
        //これだと端末によってばらつきが出るので画像を使おう
        paint.setARGB(255,242,105,100);
        paint.setTextSize(60);
        canvas.drawText("Game Over",getWidth()/2-150,getHeight()/3,paint);
        canvas.drawText(Integer.toString(point)+"pt",getWidth()/2-150,getHeight()/3+70,paint);
        canvas.drawBitmap(restart_img,getWidth()/2-restart_img.getWidth()/2,getHeight()/2,paint);
    }

    public int interchange(int n){
        if (n==0)return 0;
        if (n==2)return 1;
        if (n==4)return 2;
        if (n==8)return 3;
        if (n==16)return 4;
        if (n==32)return 5;
        if (n==64)return 6;
        if (n==128)return 7;
        if (n==256)return 8;
        if (n==512)return 9;
        if (n==1024)return 10;
        if (n==2048)return 11;
        if (n==4096)return 12;
        if (n==8192)return 13;
        if (n==16384)return 14;
        if (n==32768)return 15;
        if (n==65536)return 16;
        return 0;
    }


    public void setIreg(){
        m.setCell(2,0,0);
        m.setCell(32,0,1);
        m.setCell(2,0,2);
//        m.setCell(32,0,3);
        m.setCell(32,1,0);
        m.setCell(2,1,1);
        m.setCell(32,1,2);
//        m.setCell(32,1,3);
        m.setCell(16,2,0);
        m.setCell(64,2,1);
//        m.setCell(128,2,2);
//        m.setCell(1024,2,3);
//        m.setCell(16,3,0);
//        m.setCell(64,3,1);
//        m.setCell(128,3,2);
    }
}
