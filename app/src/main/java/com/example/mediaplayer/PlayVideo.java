package com.example.mediaplayer;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class PlayVideo extends AppCompatActivity {
    ImageView bt_next_play, bt_prev_play, bt_play;
    private boolean isPlaying = false;
    SeekBar pg_time_load;
    VideoView videoView;
    TextView txt_time_max, txt_time_position;
    int currentPosition;
    ArrayList<VideoItem> videoItems = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_video);
        linkViews();

        currentPosition = getIntent().getIntExtra("position1", 0);
        videoItems = MainActivity.videoItems;

        if (videoItems != null && !videoItems.isEmpty()) {
            videoView.setVideoPath(videoItems.get(currentPosition).getVideoPath());
            playVideo();
        } else {
            // Handle the case where videoItems is empty or null
        }
        event();

    }

    private void event() {

        bt_play.setOnClickListener(v -> {
            if(isPlaying){
//                bt_play.setRe
                pauseVideo();
            }else{
                playVideo();
            }
        });

        bt_next_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playNextVideo();
            }
        });

        bt_prev_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playPreviousVideo();
            }
        });
    }


    private void linkViews() {
        bt_play = findViewById(R.id.bt_play);
        bt_next_play = findViewById(R.id.bt_next_play);
        bt_prev_play = findViewById(R.id.bt_prive_play);
        pg_time_load = findViewById(R.id.pg_time_load);
        txt_time_max = findViewById(R.id.txt_time_max);
        txt_time_position = findViewById(R.id.txt_time_position);
        videoView = findViewById(R.id.videoView);

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                // Video has ended, handle what to do next (e.g., play the next video)
                playNextVideo();
            }
        });
    }

    private Handler handler = new Handler();
    private Runnable updateSeekBar = new Runnable() {
        @Override
        public void run() {
            int currentPosition = videoView.getCurrentPosition();
            int totalDuration = videoView.getDuration();

            // Update seek bar
            pg_time_load.setMax(totalDuration);
            pg_time_load.setProgress(currentPosition);

            // Update time positions
            txt_time_position.setText(formatTime(currentPosition));
            txt_time_max.setText(formatTime(totalDuration));

            handler.postDelayed(this, 1000);
        }
    };

    private void startUpdatingSeekBar() {
        handler.removeCallbacks(updateSeekBar);
        handler.postDelayed(updateSeekBar, 1000);
    }

    private void stopUpdatingSeekBar() {
        handler.removeCallbacks(updateSeekBar);
    }

    private void playVideo() {
        if (!isPlaying) {
            bt_play.setImageResource(R.drawable.ic_play);
            videoView.start();
            isPlaying = true;
            startUpdatingSeekBar();
        }
    }

    private void pauseVideo() {
        if (isPlaying) {
            bt_play.setImageResource(R.drawable.ic_pause);
            videoView.pause();
            isPlaying = false;
            stopUpdatingSeekBar();
        }
    }

    private void playNextVideo() {
        if (currentPosition < videoItems.size() - 1) {
            currentPosition++;
            videoView.setVideoPath(videoItems.get(currentPosition).getVideoPath());
            playVideo();
        }
    }

    private void playPreviousVideo() {
        if (currentPosition > 0) {
            currentPosition--;
            videoView.setVideoPath(videoItems.get(currentPosition).getVideoPath());
            playVideo();
        }
    }

    private String formatTime(int timeInMilliseconds) {
        int seconds = (timeInMilliseconds / 1000) % 60;
        int minutes = (timeInMilliseconds / (1000 * 60)) % 60;
        int hours = (timeInMilliseconds / (1000 * 60 * 60)) % 24;

        if (hours > 0) {
            return String.format("%02d:%02d:%02d", hours, minutes, seconds);
        } else {
            return String.format("%02d:%02d", minutes, seconds);
        }
    }
}
