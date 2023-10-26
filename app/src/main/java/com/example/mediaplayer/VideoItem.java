package com.example.mediaplayer;

import android.graphics.Bitmap;

public class VideoItem {
    private String videoPath;
    private String nameVideo;

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }

    public String getNameVideo() {
        return nameVideo;
    }

    public void setNameVideo(String nameVideo) {
        this.nameVideo = nameVideo;
    }

    public VideoItem(String videoPath, String nameVideo) {
        this.videoPath = videoPath;
        this.nameVideo = nameVideo;
    }

    public String getVideoPath() {
        return videoPath;
    }

}

