package com.example.mediaplayer;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    TextView btnMusic,btnVideo;
    private static final int REQUEST_PERMISSION = 1;
    private String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};

    private     RecyclerView recyclerView,recyclerView2;
    private ArrayList<FileItemMP3> fileMp3 = new ArrayList<>();
    private ArrayList<VideoItem> videoItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView2 = findViewById(R.id.recyclerView2);
        recyclerView2.setLayoutManager(new LinearLayoutManager(this));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.status_bar_color));
        }
        if (checkPermissions()) {
            fileMp3 = loadFiles(); // Cập nhật danh sách file MP3
            FileAdapter fileAdapter = new FileAdapter(fileMp3);
            recyclerView.setAdapter(fileAdapter);
            videoItems = loadFileVideo();
            VideoAdapter videoAdapter = new VideoAdapter(this,videoItems);
            recyclerView2.setAdapter(videoAdapter);
        } else {
            requestPermissions();
        }


        link();
        event();


    }


    private void event() {
        btnMusic.setOnClickListener(view -> {
            recyclerView2.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE); // Hiển thị danh sách âm nhạc
            btnVideo.setTextColor(getResources().getColor(R.color.black));
            btnVideo.setBackgroundResource(R.drawable.bg_tab);
            btnMusic.setTextColor(getResources().getColor(R.color.white));
            btnMusic.setBackgroundResource(R.drawable.bg_yes);
        });

        btnVideo.setOnClickListener(view -> {
            recyclerView.setVisibility(View.GONE);
            recyclerView2.setVisibility(View.VISIBLE); // Hiển thị danh sách video
            btnMusic.setTextColor(getResources().getColor(R.color.black));
            btnMusic.setBackgroundResource(R.drawable.bg_tab);
            btnVideo.setTextColor(getResources().getColor(R.color.white));
            btnVideo.setBackgroundResource(R.drawable.bg_yes);
        });
    }


    private void link() {
        btnMusic = findViewById(R.id.btn_music);
        btnVideo = findViewById(R.id.btn_video);

    }

    //Cấp quyền

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

    //loadfileMP3
    private ArrayList<FileItemMP3> loadFiles() {
        ArrayList<FileItemMP3> fileMp3 = new ArrayList<>();

        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Audio.AudioColumns.DATA};

        Cursor cursor = null;

        try {
            cursor = getContentResolver().query(uri, projection, null, null, null);

            if (cursor != null) {
                while (cursor.moveToNext()) {
                    String filePath = cursor.getString(0);
                    String fileName = getFileNameFromPath(filePath);
                    fileMp3.add(new FileItemMP3(fileName, filePath));
//                    Log.d("FileLoading", "Loaded file: " + filePath);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Xử lý lỗi ở đây nếu cần
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return fileMp3;
    }
    private ArrayList<VideoItem> loadFileVideo() {
        ArrayList<VideoItem> videoFileList = new ArrayList<>();

        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Audio.AudioColumns.DATA};

        Cursor cursor = null;

        try {
            cursor = getContentResolver().query(uri, projection, null, null, null);

            if (cursor != null) {
                while (cursor.moveToNext()) {
                    String filePath = cursor.getString(0);
                    String fileName = getFileNameFromPath(filePath);
                    videoFileList.add(new VideoItem(filePath,fileName));
                    Log.d("FileLoading", "Loaded file: " + filePath);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Xử lý lỗi ở đây nếu cần
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return videoFileList;
    }

    private String getFileNameFromPath(String filePath) {
        File file = new File(filePath);
        return file.getName();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                fileMp3 = loadFiles(); // Cập nhật danh sách file MP3
                FileAdapter fileAdapter = new FileAdapter(fileMp3);
                recyclerView.setAdapter(fileAdapter);

                videoItems = loadFileVideo();
                VideoAdapter videoAdapter = new VideoAdapter(this,videoItems);
                recyclerView2.setAdapter(videoAdapter);
            }
        }
    }

    private Bitmap getVideoThumbnail(String videoPath) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(videoPath);
        return retriever.getFrameAtTime();
    }




}