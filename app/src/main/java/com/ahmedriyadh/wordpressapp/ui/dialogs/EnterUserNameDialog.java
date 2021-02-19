package com.ahmedriyadh.wordpressapp.ui.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.ahmedriyadh.wordpressapp.databinding.DialogEnterNewNameBinding;



public class EnterUserNameDialog extends AppCompatDialogFragment {
    private Context context;
    private AlertDialog alertDialog;
    private DialogEnterNewNameBinding binding;
    private EnterUserNameListener listener;


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        context = getContext();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        /*View view = inflater.inflate(R.layout.dialog_enter_new_name, null);*/
        binding = DialogEnterNewNameBinding.inflate(inflater);


        builder.setTitle("الرجاء ادخال اسم مستخدم")
                .setPositiveButton("موافق", null)
                .setNegativeButton("الغاء", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setView(binding.getRoot());
        alertDialog = builder.create();


        return alertDialog;
    }

    @Override
    public void onStart() {
        super.onStart();

        Button button = alertDialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = binding.textInputUsername.getEditText().getText().toString().trim();

                if (!validateUsername(username)) {
                    return;
                }

                listener.onGetUserName(username);
                dismiss();
            }
        });
    }

    private boolean validateUsername(String username){
        if (username.isEmpty()){
            binding.textInputUsername.setError("الرجاء ادخال اسم مستخدم");
            binding.textInputUsername.requestFocus();
            return false;
        } else if (username.length() >= 60) {
            binding.textInputUsername.setError("لايمكن ان يكون اسم المستخدم اكثر من 60 حرف");
            return false;
        } else {
            binding.textInputUsername.setError(null);
            return true;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (EnterUserNameListener) getTargetFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " EnterUserNameListener is not implemented ");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public interface EnterUserNameListener{
        void onGetUserName(String username);
    }
}
