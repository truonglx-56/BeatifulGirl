package com.tspro.project.girl.fragment;

/**
 * Created by truonglx on 09/11/2017.
 */

import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.nhaarman.listviewanimations.appearance.simple.SwingBottomInAnimationAdapter;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.DismissableManager;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.OnDismissCallback;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.SwipeDismissAdapter;
import com.tspro.project.girl.BeatifulApplication;
import com.tspro.project.girl.BeatifulApplication.Callback;
import com.tspro.project.girl.R;
import com.tspro.project.girl.adapter.MainAdapter;
import com.tspro.project.girl.model.Entry;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FavoriteFragment extends Fragment implements Callback, OnDismissCallback {
    private static final int INITIAL_DELAY_MILLIS = 300;

    private MainAdapter mGoogleCardsAdapter;
    private List entryList;

    private String next;
    private String previous;
    private int mCurrentSize;
    private final Object lock = new Object();
    private ListView listView;
    private WeakReference<FavoriteFragment> reference;

    public static FavoriteFragment newInstance() {
        return new FavoriteFragment();
    }

    View emptyView;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listView = (ListView) view.findViewById(R.id.list_view);

        entryList = new ArrayList<>();

        reference = new WeakReference<>(this);
        emptyView = view.findViewById(R.id.lyt_not_found);
        emptyView.setVisibility(View.GONE);
        mGoogleCardsAdapter = new MainAdapter(reference);
        SwipeDismissAdapter swipeDismissAdapter = new SwipeDismissAdapter(mGoogleCardsAdapter, this);
        SwingBottomInAnimationAdapter swingBottomInAnimationAdapter = new SwingBottomInAnimationAdapter(
                swipeDismissAdapter);

        swingBottomInAnimationAdapter.setAbsListView(listView);
        assert swingBottomInAnimationAdapter.getViewAnimator() != null;
        swingBottomInAnimationAdapter.getViewAnimator().setInitialDelayMillis(
                INITIAL_DELAY_MILLIS);
        swipeDismissAdapter.setDismissableManager(new DismissableManager() {
            @Override
            public boolean isDismissable(long l, int i) {
                return false;
            }
        });
        listView.setClipToPadding(false);
        listView.setDivider(null);
        Resources r = getResources();
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                8, r.getDisplayMetrics());
        listView.setDividerHeight(px);
        listView.setFadingEdgeLength(0);
        listView.setFitsSystemWindows(true);
        px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12,
                r.getDisplayMetrics());
        listView.setPadding(px, px, px, px);
        listView.setScrollBarStyle(ListView.SCROLLBARS_OUTSIDE_OVERLAY);
        listView.setAdapter(swingBottomInAnimationAdapter);

        if (mGoogleCardsAdapter != null && mGoogleCardsAdapter.getCount() == 0)
            setData();

    }

    private void setData() {
        mHandler.sendEmptyMessage(0);
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            synchronized (lock) {
                List<Entry> list = new ArrayList<>(BeatifulApplication.getInstance().getAllFavorite().values());
                Collections.sort(list);
                mGoogleCardsAdapter.addEntry(list);
                if (list.size() == 0) emptyView.setVisibility(View.VISIBLE);
                else emptyView.setVisibility(View.GONE);
                mGoogleCardsAdapter.notifyDataSetChanged();
            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BeatifulApplication.getInstance().addCallback(this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.list_view_favorite, container, false);

        return rootView;
    }

    @Override
    public void updateData() {
        setData();

    }

    @Override
    public void onDismiss(@NonNull ViewGroup viewGroup, @NonNull int[] ints) {

    }
}
