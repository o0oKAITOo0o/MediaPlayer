package com.example.mediaplayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_PERMISSION = 1;
    private final String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};

    private RecyclerView recyclerView, recyclerView2;
    public static ArrayList<FileItemMP3> fileMp3 = new ArrayList<>();
    private ArrayList<VideoItem> videoItems = new ArrayList();
    private boolean isMusicSelected = true;
    TextView btnVideo,btnMusic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        link();
        linkViews();
        setupUI();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView2.setLayoutManager(new LinearLayoutManager(this));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.status_bar_color));
        }


        if (checkPermissions()) {
            loadMediaFiles();
        } else {
            requestPermissions();
        }

        btnMusic.setOnClickListener(view -> selectMedia(true));
        btnVideo.setOnClickListener(view -> selectMedia(false));

        EditText searchQueryEditText = findViewById(R.id.ed_Search);
        searchQueryEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                searchFiles(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });


        // Start a thread to update the SeekBar's progress as the media plays
    }

    private void setupUI() {
        btnMusic.setOnClickListener(view -> selectMedia(true));
        btnVideo.setOnClickListener(view -> selectMedia(false));

    }

    private void linkViews() {
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView2 = findViewById(R.id.recyclerView2);
        btnMusic = findViewById(R.id.btn_music);
        btnVideo = findViewById(R.id.btn_video);
    }

    private void link() {
        btnMusic = findViewById(R.id.btn_music);
        btnVideo = findViewById(R.id.btn_video);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView2 = findViewById(R.id.recyclerView2);

    }

    private void selectMedia(boolean isMusic) {
        isMusicSelected = isMusic;
        loadUITabBar(isMusicSelected);
        recyclerView.setVisibility(isMusic ? View.VISIBLE : View.GONE);
        recyclerView2.setVisibility(isMusic ? View.GONE : View.VISIBLE);
        loadMediaFiles();
    }
    private void loadUITabBar(boolean isMusic){

        if(isMusic){
            btnVideo.setTextColor(getResources().getColor(R.color.black));
            btnVideo.setBackgroundResource(R.drawable.bg_tab);
            btnMusic.setTextColor(getResources().getColor(R.color.white));
            btnMusic.setBackgroundResource(R.drawable.bg_yes);
        }else{
            btnMusic.setTextColor(getResources().getColor(R.color.black));
            btnMusic.setBackgroundResource(R.drawable.bg_tab);
            btnVideo.setTextColor(getResources().getColor(R.color.white));
            btnVideo.setBackgroundResource(R.drawable.bg_yes);
        }
    }

    private boolean checkPermissions() {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, permissions, REQUEST_PERMISSION);
    }

    private void loadMediaFiles() {
        if (isMusicSelected) {
            fileMp3 = loadAudioFiles();
            FileAdapter fileAdapter = new FileAdapter(this, fileMp3);
            recyclerView.setAdapter(fileAdapter);
        } else {
            videoItems = loadVideoFiles();
            VideoAdapter videoAdapter = new VideoAdapter(this, videoItems);
            recyclerView2.setAdapter(videoAdapter);
        }
    }

    private ArrayList<FileItemMP3> loadAudioFiles() {
        ArrayList<FileItemMP3> audioFiles = new ArrayList<>();

        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Audio.AudioColumns.DATA};

        Cursor cursor = null;

        try {
            cursor = getContentResolver().query(uri, projection, null, null, null);

            if (cursor != null) {
                while (cursor.moveToNext()) {
                    String filePath = cursor.getString(0);
                    String fileName = getFileNameFromPath(filePath);
                    if (isMP3File(fileName)) {
                        audioFiles.add(new FileItemMP3(fileName, filePath));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return audioFiles;
    }

    private boolean isMP3File(String fileName) {
        // Check if the file has a ".mp3" extension
        return fileName.toLowerCase().contains("mp3");
    }

    private String getFileNameFromPath(String filePath) {
        // Implement your logic to extract the file name from the path
        // This may involve splitting the path or using other methods
        // For example, you can use File class:
        File file = new File(filePath);
        return file.getName();
    }

    private ArrayList<VideoItem> loadVideoFiles() {
        ArrayList<VideoItem> videoFiles = new ArrayList<>();

        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Video.VideoColumns.DATA};

        Cursor cursor = null;

        try {
            cursor = getContentResolver().query(uri, projection, null, null, null);

            if (cursor != null) {
                while (cursor.moveToNext()) {
                    String filePath = cursor.getString(0);
                    String fileName = getFileNameFromPath(filePath);
                    videoFiles.add(new VideoItem(filePath, fileName));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return videoFiles;
    }


    private void searchFiles(String searchQuery) {
        searchQuery = searchQuery.toLowerCase();

        if (isMusicSelected) {
            ArrayList<FileItemMP3> searchResults = new ArrayList<>();
            for (FileItemMP3 file : fileMp3) {
                String fileName = file.getDISPLAY_NAME().toLowerCase();
                if (fileName.contains(searchQuery)) {
                    searchResults.add(file);
                }
            }
            FileAdapter searchResultAdapter = new FileAdapter(this,searchResults);
            recyclerView.setAdapter(searchResultAdapter);
        } else {
            ArrayList<VideoItem> searchResults = new ArrayList<>();
            for (VideoItem file : videoItems) {
                String fileName = file.getNameVideo().toLowerCase();
                if (fileName.contains(searchQuery)) {
                    searchResults.add(file);
                }
            }
            VideoAdapter searchResultAdapter = new VideoAdapter(this, searchResults);
            recyclerView2.setAdapter(searchResultAdapter);
        }
    }

}
