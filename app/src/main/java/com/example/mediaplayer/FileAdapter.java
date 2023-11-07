package com.example.mediaplayer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
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
    private Context context;
    private ArrayList<FileItemMP3> fileMp3List;

    public FileAdapter(Context context, ArrayList<FileItemMP3> fileMp3List) {
        this.context = context;
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
        FileItemMP3 file = fileMp3List.get(position);

        holder.fileNameTextView.setText(file.getDISPLAY_NAME());

        long creationTime = new File(file.getDATA()).lastModified();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = dateFormat.format(new Date(creationTime));
        holder.dateTv.setText(formattedDate);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, PlayMusic.class);
            intent.putExtra("position", position);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return fileMp3List.size();
    }

    public static class FileViewHolder extends RecyclerView.ViewHolder {
        TextView fileNameTextView, dateTv;

        public FileViewHolder(View itemView) {
            super(itemView);
            fileNameTextView = itemView.findViewById(R.id.mp3name);
            dateTv = itemView.findViewById(R.id.tv_date);
        }
    }
}
