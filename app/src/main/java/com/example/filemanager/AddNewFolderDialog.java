package com.example.filemanager;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class AddNewFolderDialog extends DialogFragment {


    private AddNewFolderCallBack callBack;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        callBack = (AddNewFolderCallBack) context;

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_new_folder, null, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        TextInputEditText editText = view.findViewById(R.id.et_addNewFolder);
        TextInputLayout textInputLayout = view.findViewById(R.id.etl_addNewFolder);

        view.findViewById(R.id.btn_addNewFolder).setOnClickListener(e -> {

            if (editText.length() > 0) {

                callBack.onCreateFolderButtonClick(editText.getText().toString());
                dismiss();

            } else {

                textInputLayout.setError("folder name can't be empty");
            }

        });


        return builder.setView(view).create();
    }


    public interface AddNewFolderCallBack {

        void onCreateFolderButtonClick(String folderName);

    }
}
