package com.example.myfirstapplication;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //append

        Button caliculation = (Button)findViewById(R.id.calc);

        final int messageiterates=0;

        caliculation.setOnClickListener(
            new View.OnClickListener() {
                public void onClick(View v) {

                    EditText getpopulation  = (EditText)findViewById(R.id.population);
                    EditText getvalue       = (EditText)findViewById(R.id.inputValue);
                    TextView getresult      = (TextView)findViewById(R.id.result);
                    TextView message        = (TextView)findViewById(R.id.den);

                    int warikan = Integer.parseInt(getvalue.getText().toString())/Integer.parseInt(getpopulation.getText().toString());

                    getresult.setText(Integer.toString(warikan));

                    if(messageiterates==0) {
                        message.setText("電の本気をみるのです！");
                    }else if(messageiterates==1){
                        message.setText("一人当たりの金額なのです！");
                    }else{
                        message.setText("あってますか？なのです");
                    }

                    getresult.setVisibility(View.VISIBLE);
                }
            }
        );
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


    //append
}
