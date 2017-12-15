package com.gpiopi.www.colorlight;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private int i=0;
    @SuppressLint("HandlerLeak")
    private Handler uiHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    String inmsg=(String)msg.obj;
                    ((TextView)MainActivity.this.findViewById(R.id.button)).setText(inmsg);
                    break;
                case 7:
                    String color=(String)msg.obj;
                    setTitle(color);
                    break;
                case 9:
                    String returnMSG=(String)msg.obj;
                    setTitle("蓝牙彩灯（"+returnMSG+"）");
                    break;
            }
        }
    };


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Button btn = (Button) findViewById(R.id.button);
        final Ble ble=new Ble(this,uiHandler ,"00:15:83:00:92:C7","0000ffe0-0000-1000-8000-00805f9b34fb","0000ffe1-0000-1000-8000-00805f9b34fb","0000ffe1-0000-1000-8000-00805f9b34fb");
        ble.conn();
        setTitle("智能蓝牙彩灯");
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                i+=1;
                if(i==1){
                    ble.write("gpiopi@255.255.255\n".getBytes());
                    btn.setBackgroundColor(Color.rgb(255,255,255));
                }
                if(i==2){
                    ble.write("gpiopi@255.0.0\n".getBytes());
                    btn.setBackgroundColor(Color.rgb(255,0,0));
                }
                if(i==3){
                    ble.write("gpiopi@255.255.0\n".getBytes());
                    btn.setBackgroundColor(Color.rgb(255,255,0));
                }
                if(i==4){
                    ble.write("gpiopi@0.0.255\n".getBytes());
                    btn.setBackgroundColor(Color.rgb(0,0,255));
                }
                if(i==5){
                    ble.write("gpiopi@255.0.255\n".getBytes());
                    btn.setBackgroundColor(Color.rgb(255,0,255));
                }
                if(i==6){
                    ble.write("gpiopi@192.192.192\n".getBytes());
                    btn.setBackgroundColor(Color.rgb(192,192,192));
                }
                if(i==7){
                    ble.write("gpiopi@0.0.0\n".getBytes());
                    btn.setBackgroundColor(Color.rgb(0,0,0));
                    i=0;
                }

            }
        });
    }
}
