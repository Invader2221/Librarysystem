package com.example.librarysystem.Utils;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;

public class Util {
    public static final String PREFS_NAME = "MyPrefsFile";

    public static void showDialog(final Context context, final String title, final String msg) {
        Handler h = new Handler(Looper.getMainLooper());
        h.post(() -> {
            AlertDialog alertDialog = new AlertDialog.Builder(context).create();
            alertDialog.setTitle(title);
            alertDialog.setMessage(msg);
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    (dialog, which) -> {
                        dialog.dismiss();
                    });
            alertDialog.show();
        });
    }

}
