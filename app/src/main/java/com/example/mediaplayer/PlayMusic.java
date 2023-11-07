package com.example.mediaplayer;

import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class PlayMusic extends AppCompatActivity {
    private MediaPlayer mediaPlayer;
    private ArrayList<FileItemMP3> fileMp3List;
    private int currentPosition;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_music);
        mediaPlayer = new MediaPlayer();
        fileMp3List = (ArrayList<FileItemMP3>) getIntent().getSerializableExtra("list");
        currentPosition = getIntent().getIntExtra("file", 0);

    }
}
