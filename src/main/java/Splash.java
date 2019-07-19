package com.league.pubgleague;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;


public class Splash extends Activity {
    Handler handler;
    MediaPlayer splashmusic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(Splash.this,Login.class);
                startActivity(intent);
                finish();
            }
        },3000);

        splashmusic = MediaPlayer.create(Splash.this,R.raw.clipreloadingfast);
        splashmusic.start();
    }
}
