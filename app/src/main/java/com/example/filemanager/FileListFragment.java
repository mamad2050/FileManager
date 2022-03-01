package com.example.filemanager;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class FileListFragment extends Fragment implements FileAdapter.FileItemEventListener {

    private String path;
    private FileAdapter fileAdapter;
    private RecyclerView recyclerView;
    private GridLayoutManager gridLayoutManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        path = getArguments().getString("path");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_files, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView pathTv = view.findViewById(R.id.tv_files_path);
        ImageView backIv = view.findViewById(R.id.iv_files_back);

        gridLayoutManager = new GridLayoutManager(getContext(), 1, RecyclerView.VERTICAL, false);

        recyclerView = view.findViewById(R.id.rv_files);
        recyclerView.setLayoutManager(gridLayoutManager);


        File currentFolder = new File(path);

        if (StorageHelper.isExternalStorageWritable()) {

            File[] files = currentFolder.listFiles();
            fileAdapter = new FileAdapter(Arrays.asList(files));
            recyclerView.setAdapter(fileAdapter);

        }
        fileAdapter.setEventListener(this);

        pathTv.setText(currentFolder.getName().equalsIgnoreCase("files") ? "External Storage" : currentFolder.getName());
        backIv.setOnClickListener(e -> getActivity().onBackPressed());


    }

    @Override
    public void onFileItemClick(File file) {

        if (file.isDirectory()) {
            ((MainActivity) getActivity()).listFiles(file.getPath());
        }
    }

    @Override
    public void onDeleteFileItemClick(File file) {

        if (StorageHelper.isExternalStorageWritable()) {
            if (file.delete()) {

                fileAdapter.deleteFile(file);

            }
        }

    }

    @Override
    public void onCopyFileItemClick(File file) {

        if (StorageHelper.isExternalStorageWritable()) {

            try {
                copy(file, getDestinationPath(file.getName()));
                Toast.makeText(getContext(), "Done", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    @Override
    public void onMoveFileItemClick(File file) {

        if (StorageHelper.isExternalStorageWritable()) {

            try {
                copy(file, getDestinationPath(file.getName()));
                onDeleteFileItemClick(file);
                Toast.makeText(getContext(), "Done", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    public void createNewFolder(String folderName) {

        if (StorageHelper.isExternalStorageWritable()) {

            File newFolder = new File(path + File.separator + folderName);

            if (!newFolder.exists()) {
                if (newFolder.mkdir()) {

                    fileAdapter.addFile(newFolder);
                    recyclerView.scrollToPosition(0);


                }
            }
        }

    }


    private void copy(File source, File destination) throws IOException {

        FileInputStream fileInputStream = new FileInputStream(source);
        FileOutputStream fileOutputStream = new FileOutputStream(destination);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = fileInputStream.read(buffer)) > 0) {

            fileOutputStream.write(buffer, 0, length);
        }

        fileInputStream.close();
        fileOutputStream.close();

    }

    private File getDestinationPath(String fileName) {

        return new File(getContext().getExternalFilesDir(null).getPath() + File.separator + "Destination" + File.separator + fileName);
    }


    public void search(String query) {

        if (fileAdapter != null) {

            fileAdapter.search(query);

        }

    }

    public void setViewType(ViewType viewType) {

        if (fileAdapter != null) {

            fileAdapter.setViewType(viewType);
            if (viewType == ViewType.GRID) {

                gridLayoutManager.setSpanCount(2);

            } else {

                gridLayoutManager.setSpanCount(1);

            }

        }
    }
}