package com.example.chao.myapplication;

import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class MainActivity extends AppCompatActivity {


    Random random = new Random();
    int weight = 30;
    int off = 2;

    private int MAX = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final WordCloudView wordCloudView = findViewById(R.id.wcv);


        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (MAX--  > 0){
                    String s = String.valueOf(System.currentTimeMillis());
                    s = s.substring(random.nextInt(11));
                    wordCloudView.addTextView(s, weight);
                    if(--off == 0) {
                        off = 3;
                        if(weight > 5) weight--;
//                    weight--;
//                    if(weight == 5) return;
                    }
                    sendEmptyMessageDelayed(0, 100);
                }
            }
        };
        handler.sendEmptyMessage(0);
    }
}
