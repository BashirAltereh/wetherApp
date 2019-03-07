package com.example.bashir.wetherapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Slide;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;

public class IntroductionActivity extends AppCompatActivity implements View.OnClickListener {
    private Button buLogFace, buLogGoogle;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduction);

        setupWindowAnimations();

        buLogFace = findViewById(R.id.mLogin);
        buLogFace.setOnClickListener(this);
        buLogGoogle = findViewById(R.id.mLoginGmail);
        buLogGoogle.setOnClickListener(this);

        Drawable img = this.getResources().getDrawable(R.drawable.google);
        img.setBounds(0, 0, 60, 60);
        buLogGoogle.setCompoundDrawables(img, null, null, null);
        Drawable img2 = this.getResources().getDrawable(R.drawable.ic_facebook);
        img2.setBounds(0, 0, 60, 60);

        buLogFace.setCompoundDrawables(img2, null, null, null);

    }

    @Override
    protected void onStart() {
        super.onStart();
        overridePendingTransition(R.anim.move, R.anim.move_right);

    }
    @SuppressLint("NewApi")
    public void setupWindowAnimations() {
        // Re-enter transition is executed when returning to this activity
        Slide slideTransition = new Slide();
        slideTransition.setSlideEdge(Gravity.BOTTOM);
        slideTransition.setDuration(getResources().getInteger(R.integer.anim_duration_long));
        getWindow().setReenterTransition(slideTransition);
        getWindow().setExitTransition(slideTransition);
    }
    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, MainActivity.class);
      //  ActivityOptions activityOptions = ActivityOptions.makeCustomAnimation(this,R.anim.move,R.anim.move);
        startActivity(intent);
        finish();
    }
}
