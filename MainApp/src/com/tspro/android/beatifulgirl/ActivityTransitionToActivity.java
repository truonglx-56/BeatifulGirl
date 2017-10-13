package com.tspro.android.beatifulgirl;

/**
 * Created by TruongLX on 03/10/2017.
 */

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.tspro.android.beatifulgirl.model.Entry;
import com.tspro.android.beatifulgirl.util.ImageUtil;

public class ActivityTransitionToActivity extends AppCompatActivity {
    private ImageView mImage;
   private Entry mEntry;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transition_to);
        mEntry = getIntent().getParcelableExtra(Constant.ENTRY);
        mImage =(ImageView) findViewById(R.id.iv_photo);
        ImageUtil.displayImage(mImage, mEntry.getFull_picture(), null);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mImage.setTransitionName(getString(R.string.transition_image));
            // webView.setTransitionName(getString(R.string.activity_text_trans));





        }
    }
}