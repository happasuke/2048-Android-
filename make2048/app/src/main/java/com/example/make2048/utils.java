package com.example.make2048;

/**
 * Created by 葉介 on 2015/05/14.
 */
public class utils {
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
}


