package com.example.dentataki;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by 葉介 on 2015/05/09.
 */
//class GameView extends SurfaceView implements SurfaceHolder.Callback,Runnable{
class GameView extends SurfaceView implements SurfaceHolder.Callback,Runnable{

    static final long gametime = 30;
    static final int mode_setting = 0;
    static final int mode_game = 1;
    static final int mode_game_end = 2;

    static final int end_time_pos_x=  20;
    static final int end_time_pos_y = 45;
    static final int point_pos_x = 145;
    static final int point_pos_y = 45;

    static final int first_den_y = 170;
    static final int retry_button_y = 110;

    Thread thread = null;

    Canvas canvas;

    Paint paint;

    Bitmap imageBack = BitmapFactory.decodeResource(getResources(),R.drawable.back);

    private Rect backSrcRect;
    private Rect backRect;

    private Bitmap retry_img = BitmapFactory.decodeResource(getResources(),R.drawable.retry);

    private Rect retrySrcRect;
    private RectF retryRect;

    private Bitmap[] number_img = {
      BitmapFactory.decodeResource(getResources(),R.drawable.number_white_0),
      BitmapFactory.decodeResource(getResources(),R.drawable.number_white_1),
      BitmapFactory.decodeResource(getResources(),R.drawable.number_white_2),
      BitmapFactory.decodeResource(getResources(),R.drawable.number_white_3),
      BitmapFactory.decodeResource(getResources(),R.drawable.number_white_4),
      BitmapFactory.decodeResource(getResources(),R.drawable.number_white_5),
      BitmapFactory.decodeResource(getResources(),R.drawable.number_white_6),
      BitmapFactory.decodeResource(getResources(),R.drawable.number_white_7),
      BitmapFactory.decodeResource(getResources(),R.drawable.number_white_8),
      BitmapFactory.decodeResource(getResources(),R.drawable.number_white_9)
    };

    private int mode;
    private int point;
    private long restTime;
    private long endTimeMills;
    private float density;
    private ArrayList<den> dens = new ArrayList<den>();

    public GameView(Context context){
        super(context);
        density = getContext().getResources().getDisplayMetrics().density;
        getHolder().addCallback(this);

        setResources();

        paint = new Paint();
    }


    private void init(){
        point = 0;
        restTime = gametime;
        mode = mode_setting;
        dens.clear();

        float spaceX = getWidth() / 3;
        float marginX = (spaceX/2 - den.gang_images[0].getWidth()/2);
        float firstdenY = first_den_y * density;
        float spaceY=  den.gang_images[0].getHeight();

        for(int i=0;i<3;i++){
            for(int j=0;j<4;j++){
                this.dens.add(new den(i*spaceX + marginX,firstdenY + j*spaceY,density));
            }
        }
    }


    @Override
    public void surfaceChanged(SurfaceHolder holder, int format,int width,int height){

    }


    @Override
    public void surfaceCreated(SurfaceHolder holder){
        init();
        backSrcRect = new Rect(0,0,imageBack.getWidth(),imageBack.getHeight());
        backRect = new Rect(0,0,getWidth(),getHeight());
        retrySrcRect = new Rect(0,0,retry_img.getWidth(),retry_img.getHeight());
        float destLeft = getWidth() / 2 - retry_img.getWidth() / 2;
        float destTop = retry_button_y * density;

        retryRect = new RectF(destLeft,destTop,destLeft + retry_img.getWidth(),destTop + retry_img.getHeight());

        thread  = new Thread(this);
        thread.start();
    }


    @Override
    public void surfaceDestroyed(SurfaceHolder holder){
        thread = null;
    }


    @Override
    public void run(){
        while(thread != null){
            update();
            doDraw();

            try{
                Thread.sleep(50);
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event){
        Log.d("happa","touched");
        int x = (int)event.getX();
        int y = (int)event.getY();

        if(mode==mode_setting){

        }else if(mode==mode_game){
            if(event.getAction() == MotionEvent.ACTION_DOWN) {

                //電ちゃんのタッチ判定
                for (int i = 0; i < this.dens.size(); i++) {
                    if (!this.dens.get(i).checkHit(x, y)) {
                        continue;
                    }

                    this.dens.get(i).setHit();

                    if (this.dens.get(i).checkDen()) {
                        point++;
                    } else if (point > 0) {
                        point--;
                    }

                    break;
                }
            }
        }else if(this.mode == mode_game_end){
            if(retryRect.contains(x,y)){
                init();
            }
        }
        return true;
    }


    private void update(){
        if(mode==mode_setting){
            mode = mode_game;
            endTimeMills = System.currentTimeMillis() + 1000 * gametime;

        }else if(mode==mode_game){
            restTime = (int)Math.ceil((endTimeMills - System.currentTimeMillis()) / 1000);
            Log.d("happa", "restTime:" + restTime + " etm:" + endTimeMills + " ctl:" + System.currentTimeMillis());

            //電ちゃんの動きを決定！！！
            for(int i=0;i<this.dens.size();i++){
                this.dens.get(i).nextFrame();
            }

            if(restTime <= 0){
                this.mode = mode_game_end;
            }

        }else if(this.mode == mode_game_end){
            // no matter
        }
    }


    private void doDraw(){
        canvas = getHolder().lockCanvas();

        canvas.drawBitmap(imageBack,backSrcRect,backRect,paint);

        drawPoint();
        drawEndTime();

        if(mode==mode_game) {
            for (int i = 0; i < dens.size(); i++) {
                dens.get(i).draw(canvas);
            }

        }else if(mode==mode_game_end){
            canvas.drawBitmap(retry_img,retrySrcRect,retryRect,paint);
        }

        getHolder().unlockCanvasAndPost(canvas);
    }


    //獲得ポイントの表示
    private void drawPoint(){
        String str_point = String.valueOf(point);
        float pos = point_pos_x * density;

        for(int i=0;i<str_point.length();i++){
            int n = Integer.parseInt(str_point.substring(i,i+1));
            canvas.drawBitmap(number_img[n],pos,point_pos_y * density,null);
            pos += number_img[n].getWidth();
        }
    }


    //残り時間の描画
    private void drawEndTime(){
        if(0<=restTime && restTime <= gametime){
            String str = String.valueOf(restTime);
            float pos = end_time_pos_x * density;

            for(int i=0;i<str.length();i++){
                int n = Integer.parseInt(str.substring(i,i+1));
                canvas.drawBitmap(number_img[n],pos,end_time_pos_y*density,null);
                pos += number_img[n].getWidth();
            }
        }else{
            canvas.drawBitmap(number_img[0],end_time_pos_x * density,end_time_pos_y*density,null);
        }

    }


    private void setResources(){
        den.gang_images[0] = BitmapFactory.decodeResource(getResources(),R.drawable.hole);
        den.gang_images[1] = BitmapFactory.decodeResource(getResources(),R.drawable.gang_mogura1);
        den.gang_images[2] = BitmapFactory.decodeResource(getResources(),R.drawable.gang_mogura2);

        den.hit_gang_images[0] = BitmapFactory.decodeResource(getResources(),R.drawable.hole);
        den.hit_gang_images[1] = BitmapFactory.decodeResource(getResources(),R.drawable.hit_gang_mogura1);
        den.hit_gang_images[2] = BitmapFactory.decodeResource(getResources(),R.drawable.hit_gang_mogura2);

        den.friend_images[0] = BitmapFactory.decodeResource(getResources(),R.drawable.hole);
        den.friend_images[1] = BitmapFactory.decodeResource(getResources(),R.drawable.friend_mogura1);
        den.friend_images[2] = BitmapFactory.decodeResource(getResources(),R.drawable.friend_mogura2);

        den.hit_friend_images[0] = BitmapFactory.decodeResource(getResources(),R.drawable.hole);
        den.hit_friend_images[1] = BitmapFactory.decodeResource(getResources(),R.drawable.hit_friend_mogura1);
        den.hit_friend_images[2] = BitmapFactory.decodeResource(getResources(),R.drawable.hit_friend_mogura2);
    }
}
