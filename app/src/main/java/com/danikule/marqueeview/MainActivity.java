package com.danikule.marqueeview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private MarqueeView mvMarquee;
    private Button btSwitch;
    private Button btChange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mvMarquee = (MarqueeView) findViewById(R.id.mvMarquee);
        btSwitch = (Button) findViewById(R.id.btSwitch);
        btChange = (Button) findViewById(R.id.btChange);
        btSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mvMarquee.isPlaying()) {
                    mvMarquee.stop();
                } else {
                    mvMarquee.start();
                }
            }
        });
        btChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mvMarquee.setText("这是替换的文字这是替换的文字");
            }
        });
    }
}
