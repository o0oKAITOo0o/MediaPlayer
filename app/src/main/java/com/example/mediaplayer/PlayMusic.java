package com.example.mediaplayer;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

public class PlayMusic extends AppCompatActivity {
    private int currentPosition;
    private MediaPlayer mediaPlayer;
    private TextView tvName;
    Animation animation;
    private ImageView btnPlay, btnPrev, btnNext,btnBack,imgDemo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_music);
        currentPosition = getIntent().getIntExtra("position", 0);


        link();

        event();
        initializeMediaPlayer();
    }

    private void initializeMediaPlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.release(); // Release the existing MediaPlayer
        }
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnCompletionListener(mp -> {
            // Handle song completion here, e.g., play the next song
        });

        FileItemMP3 file = MainActivity.fileMp3.get(currentPosition);
        try {
            mediaPlayer.setDataSource(file.getDATA());
            mediaPlayer.prepare();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        mediaPlayer.start();
        btnPlay.setImageResource(R.drawable.ic_play);
        tvName.setText(file.getDISPLAY_NAME());
        startRotateAnimation();

    }

    private void event() {
        btnPlay.setOnClickListener(v -> {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
                btnPlay.setImageResource(R.drawable.ic_pause);
            } else {
                mediaPlayer.start();
                btnPlay.setImageResource(R.drawable.ic_play);
            }
        });
        btnPrev.setOnClickListener(v -> {
            if (currentPosition > 0) {
                // Play the previous song
                currentPosition--;
                initializeMediaPlayer();
            } else {
                // Handle when there are no previous songs (e.g., loop to the end)
                // You can customize this behavior based on your requirements
            }
        });
        btnNext.setOnClickListener(v -> {
            if (currentPosition < MainActivity.fileMp3.size() - 1) {
                // Play the next song
                currentPosition++;
                initializeMediaPlayer();
            } else {
                // Handle when there are no more songs (e.g., loop to the beginning)
                // You can customize this behavior based on your requirements
            }
        });
        btnBack.setOnClickListener(v -> {
            onBackPressed();
        });
    }

    private void link() {
        btnPlay = findViewById(R.id.btn_Play);
        btnNext = findViewById(R.id.btn_Next);
        btnPrev = findViewById(R.id.btn_Prev);
        btnBack = findViewById(R.id.btn_Back);
        tvName = findViewById(R.id.tv_Name);
        imgDemo = findViewById(R.id.imgDemo);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release(); // Release the MediaPlayer when the activity is destroyed
        }
    }
    private void startRotateAnimation() {
        RotateAnimation rotateAnimation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(5000); // Duration of the animation in milliseconds (5 seconds in this example)
        rotateAnimation.setRepeatCount(Animation.INFINITE); // Infinite rotation
        imgDemo.startAnimation(rotateAnimation);
    }
}
