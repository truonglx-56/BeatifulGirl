package com.tspro.project.girl.util;

/**
 * Created by truonglx on 18/11/2017.
 */

import android.app.Dialog;
import android.widget.TextView;


import com.tspro.project.girl.MainActivity;
import com.tspro.project.girl.R;


public class ProgressDialog {

    public static Dialog show(MainActivity ctx, String text) {
        final Dialog dialog = new Dialog(ctx, R.style.full_screen_dialog);
        dialog.setContentView(R.layout.progress_dialog);
        ((TextView) dialog.findViewById(R.id.label_loading)).setText(text);
        dialog.setCancelable(false);
        dialog.show();
        return dialog;
    }
}
