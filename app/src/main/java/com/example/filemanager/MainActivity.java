package com.example.filemanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.button.MaterialButtonToggleGroup;

import java.io.File;

public class MainActivity extends AppCompatActivity implements AddNewFolderDialog.AddNewFolderCallBack {


//    public static int span = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (StorageHelper.isExternalStorageReadable()) {

            File externalFilesDir = getExternalFilesDir(null);
            listFiles(externalFilesDir.getPath(), false);

        }

        findViewById(R.id.iv_add).setOnClickListener(e -> {

            AddNewFolderDialog dialog = new AddNewFolderDialog();
            dialog.show(getSupportFragmentManager(), null);

        });

        EditText editText = findViewById(R.id.et_search);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
                if (fragment instanceof FileListFragment) {

                    ((FileListFragment) fragment).search(s.toString());

                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        MaterialButtonToggleGroup toggleGroup = findViewById(R.id.toggle_group);

        toggleGroup.addOnButtonCheckedListener((group, checkedId, isChecked) -> {

            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);

            if (checkedId == R.id.btn_main_list) {

                if (fragment instanceof FileListFragment) {

                    ((FileListFragment) fragment).setViewType(ViewType.ROW);
//                    span=1;
                }

            } else {

                ((FileListFragment) fragment).setViewType(ViewType.GRID);
//span=2;
            }
        });

    }


    public void listFiles(String path, boolean addToBackStack) {

        FileListFragment fileListFragment = new FileListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("path", path);
        fileListFragment.setArguments(bundle);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fileListFragment);

        if (addToBackStack) {
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }

    public void listFiles(String path) {

        this.listFiles(path, true);

    }

    @Override
    public void onCreateFolderButtonClick(String folderName) {

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);

        if (fragment instanceof FileListFragment) {

            ((FileListFragment) fragment).createNewFolder(folderName);

        }
    }
}