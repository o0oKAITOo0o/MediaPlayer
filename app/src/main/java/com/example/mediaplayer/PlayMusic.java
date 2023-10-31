package com.example.mediaplayer;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

public class PlayMusic extends AppCompatActivity {
    private int currentPosition;
    private MediaPlayer mediaPlayer;
    private TextView tvName;
    private SeekBar seekBar;
    private ImageView btnPlay, btnPrev, btnNext, btnBack, imgDemo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_music);
        currentPosition = getIntent().getIntExtra("position", 0);

        linkViews();
        initializeMediaPlayer();
        setupEventListeners();
    }

    private void linkViews() {
        btnPlay = findViewById(R.id.btn_Play);
        btnNext = findViewById(R.id.btn_Next);
        btnPrev = findViewById(R.id.btn_Prev);
        btnBack = findViewById(R.id.btn_Back);
        tvName = findViewById(R.id.tv_Name);
        imgDemo = findViewById(R.id.imgDemo);
        seekBar = findViewById(R.id.seekBar);
    }

    private void initializeMediaPlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.release(); // Release the existing MediaPlayer
        }
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnCompletionListener(mp -> handleSongCompletion());

        FileItemMP3 file = MainActivity.fileMp3.get(currentPosition);
        try {
            mediaPlayer.setDataSource(file.getDATA());
            mediaPlayer.prepare();
        } catch (IOException e) {
            handleMediaPlayerError(e);
        }
        mediaPlayer.start();
        seekBar.setMax(mediaPlayer.getDuration());
        btnPlay.setImageResource(R.drawable.ic_play);
        tvName.setText(file.getDISPLAY_NAME());
        startRotateAnimation();
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // User started touching the SeekBar, handle as needed
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // User stopped touching the SeekBar, handle as needed
            }
        });
        updateSeekBarProgress();
    }
    private void updateSeekBarProgress() {
        new Thread(() -> {
            while (mediaPlayer != null) {
                if (!mediaPlayer.isPlaying()) {
                    continue; // Wait while not playing
                }

                runOnUiThread(() -> {
                    int currentPosition = mediaPlayer.getCurrentPosition();
                    seekBar.setProgress(currentPosition);
                });

                try {
                    Thread.sleep(1000); // Update SeekBar progress every second
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    private void handleSongCompletion() {
        // Handle song completion here, e.g., play the next song
    }

    private void handleMediaPlayerError(IOException e) {
        // Handle MediaPlayer error, e.g., show an error message
        throw new RuntimeException(e);
    }

    private void setupEventListeners() {
        btnPlay.setOnClickListener(v -> togglePlayback());
        btnPrev.setOnClickListener(v -> playPreviousSong());
        btnNext.setOnClickListener(v -> playNextSong());
        btnBack.setOnClickListener(v -> onBackPressed());
    }

    private void togglePlayback() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            btnPlay.setImageResource(R.drawable.ic_pause);
        } else {
            mediaPlayer.start();
            btnPlay.setImageResource(R.drawable.ic_play);
        }
    }

    private void playPreviousSong() {
        if (currentPosition > 0) {
            currentPosition--;
            initializeMediaPlayer();
        } else {
            // Handle when there are no previous songs
        }
    }

    private void playNextSong() {
        if (currentPosition < MainActivity.fileMp3.size() - 1) {
            currentPosition++;
            initializeMediaPlayer();
        } else {
            // Handle when there are no more songs
        }
    }

    @Override
    public void onBackPressed() {
        // Pause the MediaPlayer and stop the SeekBar update when back button is pressed
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        // Release MediaPlayer and SeekBar resources when the activity is destroyed
        releaseMediaPlayerResource();
        super.onDestroy();
    }

    private void releaseMediaPlayerResource() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
    }

    private void startRotateAnimation() {
        RotateAnimation rotateAnimation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(5000); // Duration of the animation in milliseconds
        rotateAnimation.setRepeatCount(Animation.INFINITE);
        imgDemo.startAnimation(rotateAnimation);
    }


}
