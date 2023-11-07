package com.example.mediaplayer;

import static org.bytedeco.opencv.global.opencv_core.finish;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.ArrayList;

public class PlayMusic extends AppCompatActivity {
    private int currentPosition;
    private ArrayList<FileItemMP3> mp3list = new ArrayList<>();
    private MediaPlayer mediaPlayer;
    private TextView tvName;
    private SeekBar seekBar;
    FileItemMP3 file;
    private Thread seekBarUpdateThread;
    private ImageView btnPlay, btnPrev, btnNext, btnBack, imgDemo;

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_music);
        linkViews();
        currentPosition = getIntent().getIntExtra("position", 0);
        mp3list = MainActivity.fileMp3; // Assign the value to mp3list
        if (mp3list != null && !mp3list.isEmpty()) {
            initializeMediaPlayer();
            setupEventListeners();

        } else {
            // Handle the case where mp3list is empty or null
        }
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
        releaseMediaPlayerResource();

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnCompletionListener(mp -> handleSongCompletion());

        try {
            file = mp3list.get(currentPosition);
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
        updateSeekBarProgress();
    }

    private void updateSeekBarProgress() {
        seekBarUpdateThread = new Thread(() -> {
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
        });
        seekBarUpdateThread.start();
    }

    private void handleSongCompletion() {
        // Handle song completion here, e.g., play the next song
        if (currentPosition < mp3list.size() - 1) {
            currentPosition++;
            initializeMediaPlayer();
        } else {
            // Handle when there are no more songs
        }
    }

    private void handleMediaPlayerError(IOException e) {
        // Handle MediaPlayer error, e.g., show an error message
        throw new RuntimeException(e);
    }

    private void setupEventListeners() {
        btnPlay.setOnClickListener(v -> togglePlayback());
        btnPrev.setOnClickListener(v -> playPreviousSong());
        btnNext.setOnClickListener(v -> playNextSong());
        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            this.startActivity(intent);
            finish();
        });
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
        if (currentPosition < mp3list.size() - 1) {
            currentPosition++;
            initializeMediaPlayer();
        } else {
            // Handle when there are no more songs
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseMediaPlayerResource();
    }

    private void releaseMediaPlayerResource() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        if (seekBarUpdateThread != null && seekBarUpdateThread.isAlive()) {
            seekBarUpdateThread.interrupt();
        }
    }

    private void startRotateAnimation() {
        RotateAnimation rotateAnimation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(5000); // Duration of the animation in milliseconds
        rotateAnimation.setRepeatCount(Animation.INFINITE);
        imgDemo.startAnimation(rotateAnimation);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        this.startActivity(intent);
        finish();
        super.onBackPressed();
    }
}
