package com.tspro.project.girl.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tspro.project.girl.MainActivity;
import com.tspro.project.girl.R;

/**
 * Created by truonglx on 22/11/2017.
 */

public class AboutFragment extends Fragment {
    public static final String TAG = "fragment_about";
    private MainActivity mActivity;

    private TextView tvHomepage;

    public AboutFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        Activity a;

        if (context instanceof Activity) {
            a = (Activity) context;
            mActivity = (MainActivity) a;
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.about, container, false);

        setHasOptionsMenu(true);

        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public static Fragment newInstance() {
        return new AboutFragment();
    }
}
