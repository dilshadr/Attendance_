package com.example.attendance_app;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private  static  int SPLASH_SCREEN = 5000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        Animation topanimation;
        ImageView image;
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        //Animation
        topanimation = AnimationUtils.loadAnimation(this,R.anim.top_animation);

        //Hooks
          image=findViewById(R.id.imageView);

          image.setAnimation(topanimation);


          new Handler().postDelayed(new Runnable() {
              @Override
              public void run() {
                  Intent intent = new Intent(MainActivity.this, Login.class );
                  startActivity(intent);
                  finish();
              }
          },SPLASH_SCREEN);


    }


}
