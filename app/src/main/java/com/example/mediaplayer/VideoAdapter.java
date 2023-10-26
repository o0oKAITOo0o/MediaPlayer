package com.example.mediaplayer;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;

import java.util.ArrayList;
import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder> {
    private List<VideoItem> videoItems;
    private Context context;

    public VideoAdapter(Context context, List<VideoItem> videoItems) {
        this.context = context;
        this.videoItems = videoItems;
    }

    @Override
    public VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.imagefile, parent, false);
        return new VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(VideoViewHolder holder, int position) {

//        FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(videoItems.get(position).getVideoPath());
//        try {
//            grabber.start();
//
//            // Chọn khung hình cụ thể (ví dụ: lấy khung hình ở giây thứ 10)
//            int targetSecond = 10;
//            int targetFrameIndex = (int) (targetSecond * grabber.getFrameRate());
//            grabber.setFrameNumber(targetFrameIndex);
//
//            Frame frame = grabber.grab();
//
//            if (frame != null) {
//                // Chuyển đổi Frame thành Bitmap
//                Bitmap bitmap = frameToBitmap(frame);
//
//                // Sử dụng Glide để hiển thị ảnh
//                Glide.with(context)
//                        .load(bitmap)
//                        .apply(new RequestOptions().centerCrop())
//                        .into(holder.imagevideo);
//            }
//
//            grabber.stop();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        holder.tv_Name.setText(videoItems.get(position).getNameVideo());
    }

    @Override
    public int getItemCount() {
        return videoItems.size();
    }

    public static class VideoViewHolder extends RecyclerView.ViewHolder {
//        ImageView imagevideo;
        TextView tv_Name;


        public VideoViewHolder(View view) {
            super(view);
            tv_Name = view.findViewById(R.id.mp4);
//            imagevideo = view.findViewById(R.id.videoimage);
        }
    }
    public Bitmap frameToBitmap(Frame frame) {
        // Chuyển đổi Frame thành Bitmap
        // Lưu ý rằng frame.image[0] chứa dữ liệu hình ảnh
        int width = frame.imageWidth;
        int height = frame.imageHeight;
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bitmap.copyPixelsFromBuffer(frame.image[0]);
        return bitmap;
    }
}

