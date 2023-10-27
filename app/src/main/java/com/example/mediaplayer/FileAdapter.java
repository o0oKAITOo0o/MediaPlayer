package com.example.mediaplayer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class FileAdapter extends RecyclerView.Adapter<FileAdapter.FileViewHolder> {
    private ArrayList<FileItemMP3> fileMp3List;

    public FileAdapter(ArrayList<FileItemMP3> fileMp3List) {
        this.fileMp3List = fileMp3List;
    }

    @NonNull
    @Override
    public FileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemfile, parent, false);
        return new FileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FileViewHolder holder, int position) {
        holder.fileNameTextView.setText(fileMp3List.get(position).getDISPLAY_NAME());
        long creationTime = new File(fileMp3List.get(position).getDATA()).lastModified(); // This is in milliseconds since epoch

        // Format the creation date and time
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = dateFormat.format(new Date(creationTime));
        holder.dateTv.setText(formattedDate);
    }

    @Override
    public int getItemCount() {
        return fileMp3List.size();
    }

    public static class FileViewHolder extends RecyclerView.ViewHolder {
        TextView fileNameTextView,dateTv;

        public FileViewHolder(View itemView) {
            super(itemView);
            fileNameTextView = itemView.findViewById(R.id.mp3name);
            dateTv = itemView.findViewById(R.id.tv_date);
        }
    }
}
