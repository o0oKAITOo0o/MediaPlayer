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
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_PERMISSION = 1;
    private final String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};

    private RecyclerView recyclerView, recyclerView2;
    private ArrayList<FileItemMP3> fileMp3 = new ArrayList<>();
    private ArrayList<VideoItem> videoItems = new ArrayList();
    private boolean isMusicSelected = true;
    TextView btnVideo,btnMusic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        link();

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
    }

    private void link() {
        btnMusic = findViewById(R.id.btn_music);
        btnVideo = findViewById(R.id.btn_video);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView2 = findViewById(R.id.recyclerView2);

    }

    private void selectMedia(boolean isMusic) {
        isMusicSelected = isMusic;
        loadUITabbar(isMusicSelected);
        recyclerView.setVisibility(isMusic ? View.VISIBLE : View.GONE);
        recyclerView2.setVisibility(isMusic ? View.GONE : View.VISIBLE);
        loadMediaFiles();
    }
    private void loadUITabbar(boolean isMusic){

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
            FileAdapter fileAdapter = new FileAdapter(fileMp3);
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
                    audioFiles.add(new FileItemMP3(fileName, filePath));
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

    private String getFileNameFromPath(String filePath) {
        File file = new File(filePath);
        return file.getName();
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
            FileAdapter searchResultAdapter = new FileAdapter(searchResults);
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
