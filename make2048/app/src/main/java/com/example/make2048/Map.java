package com.example.make2048;

import java.util.Random;

/**
 * Created by 葉介 on 2015/05/14.
 */
public class Map {
    private int x;
    private int y;
    private int map_size;
    private int match;//２がどれだけでやすいかみたいな

    private static int map[][] = new int[5][5];

    Random random;

    public Map(int size){
        map_size = size;
        match = 10;
        random = new Random();

        genCell();
        genCell();
    }

    //アラワザ！
    public int getCell(int x,int y){
        return map[y][x];
    }

    public void setCell(int v,int x,int y){
        map[y][x] = v;
    }

    public void setDoubleCell(int x,int y){
        map[y][x] *=2;
    }

    public void setZeroCell(int x,int y){
        map[y][x] = 0;
    }



    public void genCell(){
        int tx,ty;
        while(true){
            tx = random.nextInt(map_size);
            ty = random.nextInt(map_size);

            if(map[ty][tx]==0){
                map[ty][tx] = genNum();
                break;
            }
        }
    }


    //game over true
    //continue  false
    public boolean checkGameOver(){
        int isSpace=0,isntSameNum=0;

        for (int i=0;i<4;i++) {
            if (map[0][i]==0 || map[1][i]==0 || map[2][i]==0 || map[3][i]==0) {
                isSpace = 1;
            }

            if(map[i][0]==map[i][1] || map[i][1]==map[i][2] || map[i][2]==map[i][3]){
                isntSameNum = 1;
            }

            if(map[0][i]==map[1][i] || map[1][i]==map[2][i] || map[2][i]==map[3][i]){
                isntSameNum = 1;
            }
        }

        if(isntSameNum + isSpace ==0){
            return true;
        }

        return false;
    }


    public int getx(){
        return this.x;
    }


    public int gety(){
        return this.y;
    }


    public int genNum(){
        if(random.nextInt(match)==1){
            return 4;
        }else{
            return 2;
        }
    }

}
