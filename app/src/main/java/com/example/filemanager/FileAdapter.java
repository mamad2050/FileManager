package com.example.filemanager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class FileAdapter extends RecyclerView.Adapter<FileAdapter.FileViewHolder> {

    private List<File> files;
    private List<File> filteredFiles;
    private FileItemEventListener eventListener;
    private ViewType viewType = ViewType.ROW;

    public FileAdapter(List<File> files) {

        this.files = new ArrayList<>(files);
        this.filteredFiles = this.files;
    }

    @NonNull
    @Override
    public FileViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        return new FileViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(viewType == ViewType.ROW.getValue() ?  R.layout.item_file : R.layout.item_file_grid
                        , parent, false));


    }

    @NonNull
    @Override
    public void onBindViewHolder(@NonNull FileAdapter.FileViewHolder holder, int position) {
        holder.bindFile(filteredFiles.get(position));
    }

    @Override
    public int getItemCount() {
        return filteredFiles.size();
    }

    public class FileViewHolder extends RecyclerView.ViewHolder {

        private TextView fileNameTv;
        private ImageView fileIconIv;
        private ImageView moreIv;

        public FileViewHolder(@NonNull View itemView) {
            super(itemView);

            fileNameTv = itemView.findViewById(R.id.tv_file_name);
            fileIconIv = itemView.findViewById(R.id.iv_file);
            moreIv = itemView.findViewById(R.id.iv_file_more);

        }

        public void bindFile(File file) {

            fileNameTv.setText(file.getName());

            if (file.isDirectory()) {
                fileIconIv.setImageResource(R.drawable.ic_folder_black_32dp);

            } else {
                fileIconIv.setImageResource(R.drawable.ic_file_black_32dp);
            }

            itemView.setOnClickListener(e -> eventListener.onFileItemClick(file));

            moreIv.setOnClickListener(view -> {

                PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
                popupMenu.getMenuInflater().inflate(R.menu.menu_file_item, popupMenu.getMenu());
                popupMenu.show();

                popupMenu.setOnMenuItemClickListener(item -> {

                    switch (item.getItemId()) {
                        case R.id.menuItem_delete:
                            eventListener.onDeleteFileItemClick(file);
                            break;


                        case R.id.menuItem_copy:
                            eventListener.onCopyFileItemClick(file);
                            break;


                        case R.id.menuItem_move:
                            eventListener.onMoveFileItemClick(file);
                            break;
                    }
                    return true;

                });

            });


        }
    }

    public interface FileItemEventListener {

        void onFileItemClick(File file);

        void onDeleteFileItemClick(File file);

        void onCopyFileItemClick(File file);

        void onMoveFileItemClick(File file);

    }

    public void setEventListener(FileItemEventListener eventListener) {
        this.eventListener = eventListener;
    }

    public void addFile(File file) {

        files.add(0, file);
        notifyItemInserted(0);

    }

    public void deleteFile(File file) {

        int index = files.indexOf(file);
        if (index > -1) {

            files.remove(index);
            notifyItemRemoved(index);

        }

    }

    public void search(String query) {

        if (query.length() > 0) {

            List<File> result = new ArrayList<>();
            for (File file : this.files) {

                if (file.getName().toLowerCase().contains(query.toLowerCase())) {

                    result.add(file);

                }

            }

            this.filteredFiles = result;
            notifyDataSetChanged();

        } else {

            this.filteredFiles = this.files;
            notifyDataSetChanged();
        }

    }

    @Override
    public int getItemViewType(int position) {
        return viewType.getValue();
    }


    public void setViewType(ViewType viewType) {
        this.viewType = viewType;
        notifyDataSetChanged();
    }
}
