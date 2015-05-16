package com.example.dentataki;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Created by 葉介 on 2015/05/09.
 */
public class den {

    public static final Bitmap[] gang_images = new Bitmap[3];
    public static final Bitmap[] hit_gang_images = new Bitmap[3];
    public static final Bitmap[] friend_images = new Bitmap[3];
    public static final Bitmap[] hit_friend_images = new Bitmap[3];

    public static final int STATUS_HIDDEN = 0;
    public static final int STATUS_HARF = 1;
    public static final int STATUS_FULL = 2;

    public static final int UP = 0;
    public static final int DOWN = 1;

    public static final int DEN_HEIGHT = 71;
    public static final int DEN_WIDTH = 60;

    public static final double DEN_DIRECTION_UP_RATE = 0.01;
    public static final double DEN_DIRECTION_DOWN_RATE = 0.05;

    protected float density;
    protected boolean isDen;
    protected int status;
    protected boolean isHit;
    protected int direction;
    protected float posX;
    protected float posY;


    public den(float posX,float posY,float density){
        this.posX = posX;
        this.posY = posY;
        this.density = density;
        this.status = STATUS_HIDDEN;
    }

    public boolean checkHit(float hitX, float hitY){
        if(this.posX > hitX ||
           hitX > this.posX + DEN_WIDTH + density||
           this.posY > hitY||
           hitY > this.posY + DEN_HEIGHT
        ){
            return false;
        }

        if(this.status == STATUS_HIDDEN || this.isHit){
            return false;
        }
        return true;
    }

    public void setHit(){
        this.isHit = true;
    }

    public boolean checkDen(){
        return isDen;
    }

    public void nextFrame(){

        //隠れていれば
        if(this.status == STATUS_HIDDEN){
            if(Math.random() <= DEN_DIRECTION_UP_RATE){
                this.direction = UP;
                this.isHit = false;
                this.status = STATUS_HARF;

                //電ちゃんが本物かどうかを設定
                if(Math.random() < 0.5){
                    this.isDen = true;
                }else{
                    this.isDen = false;
                }
            }

        //半分出ていれば
        }else if(this.status == STATUS_HARF){
            if(this.isHit || this.direction == DOWN){
                this.status = STATUS_HIDDEN;
            }else{
                this.status = STATUS_FULL;
            }

        //顔がすべて出ていれば
        }else if(this.status == STATUS_FULL){
            if(this.isHit || Math.random() <= DEN_DIRECTION_DOWN_RATE){
                this.direction = DOWN;
                this.status = STATUS_HARF;
            }
        }
    }


    public void draw(Canvas c){
        if(this.isHit){
            if(this.isDen){
                c.drawBitmap(hit_gang_images[this.status],this.posX,this.posY,null);
            }else{
                c.drawBitmap(hit_friend_images[this.status],this.posX,this.posY,null);
            }
        }else{
            if(this.isDen){
                c.drawBitmap(gang_images[this.status],this.posX,this.posY,null);
            }else{
                c.drawBitmap(friend_images[this.status],this.posX,this.posY,null);
            }
        }
    }

}
