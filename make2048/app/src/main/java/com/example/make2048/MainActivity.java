package com.example.make2048;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class MainActivity extends Activity implements alias{

    GameView game_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("donatu", "program ran");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        game_view = new GameView(this);
    }


    public void onClickStart(View v){
        //ボタン等が消えるアニメーション
        for(int i=0;i<5;i++){
            try{
                Thread.sleep(sleep_time);
            }catch(Exception e){
                Log.e("donatu","MainActivity Thread.sleep");
            }
        }

        setContentView(game_view);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
