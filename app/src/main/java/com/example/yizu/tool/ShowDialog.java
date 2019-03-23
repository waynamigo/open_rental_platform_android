package com.example.yizu.tool;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yizu.CreateMessageActivity;
import com.example.yizu.R;
import com.example.yizu.UserMessageActivity;

import java.io.File;

/**
 * Created by 10591 on 2017/7/27.
 */

public class ShowDialog {
        public static void  showCustomizeDialog(final Activity activity, String title, String message) {
        AlertDialog.Builder customizeDialog =
                new AlertDialog.Builder(activity);
        final View dialogView = LayoutInflater.from(activity)
                .inflate(R.layout.create_dialog,null);
        TextView textView = (TextView)dialogView.findViewById(R.id.title_message);
        textView.setText(title);
        Button button = (Button)dialogView.findViewById(R.id.confirm);
        button.setText(message);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
            }
        });
        customizeDialog.setView(dialogView);
        customizeDialog.show();

    }
    public static void  showZhuceDialog(final Activity activity,String rules) {
        final AlertDialog.Builder customizeDialog =
                new AlertDialog.Builder(activity);
        final View dialogView = LayoutInflater.from(activity)
                .inflate(R.layout.zhuce_dialog,null);
        TextView textView = (TextView)dialogView.findViewById(R.id.rules);
        textView.setText(rules);
        Button cancel=(Button) dialogView.findViewById(R.id.button_cancel);
        new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                arg0.dismiss();

            }
        };
        customizeDialog.setView(dialogView);
      //  customizeDialog.show();
        final Dialog dialog = customizeDialog.show();
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  dialog.dismiss();
            }
        });
    }

}

