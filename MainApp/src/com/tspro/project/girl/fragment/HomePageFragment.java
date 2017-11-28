package com.tspro.project.girl.fragment;

/**
 * Created by TruongLX on 03/10/2017.
 */


import android.content.res.Resources;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;


import com.lib.pull.PullToRefreshView;
import com.nhaarman.listviewanimations.appearance.simple.SwingBottomInAnimationAdapter;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.DismissableManager;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.OnDismissCallback;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.SwipeDismissAdapter;
import com.tspro.project.girl.BeatifulApplication;
import com.tspro.project.girl.Constant;
import com.tspro.project.girl.R;
import com.tspro.project.girl.adapter.MainAdapter;
import com.tspro.project.girl.model.Entry;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HomePageFragment extends Fragment implements OnDismissCallback, BeatifulApplication.Callback {
    public static final String TAG = "fragment_latest";

    private static final int INITIAL_DELAY_MILLIS = 300;

    private MainAdapter mGoogleCardsAdapter;


    private List entryList;

    private String next;
    private String previous;
    private int mCurrentSize;
    private final Object lock = new Object();
    private PullToRefreshView mPullToRefreshView;
    private ListView listView;
    private WeakReference<HomePageFragment> reference;


    public static HomePageFragment newInstance() {
        return new HomePageFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BeatifulApplication.getInstance().addCallback(this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.list_view_homepage, container, false);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listView = (ListView) view.findViewById(R.id.list_view);

        entryList = new ArrayList<>();

        reference = new WeakReference<>(this);
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

       /* getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Google cards social");*/
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScroll(AbsListView view,
                                 int firstVisibleItem, int visibleItemCount,
                                 int totalItemCount) {
                if (firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount != 0) {
                    if (!flag_loading) {
                        flag_loading = true;
                        loadMore();
                    }
                }
            }


            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                //blank, not using this
            }
        });

        if (mGoogleCardsAdapter != null) {
            next = BeatifulApplication.getInstance().getNext();

            if (BeatifulApplication.getInstance().getMapEntry().size() <= 10 || next == null) {
                setData(Constant.LINK, false);
            } else {
                flag_loading = true;
                mHandler.sendEmptyMessage(0);
            }
        }
        setPullToRefesh(view);

        if (savedInstanceState != null) {
            int index = savedInstanceState.getInt("index");
            listView.smoothScrollToPosition(index);
        }
    }

    private void setPullToRefesh(final View view) {
        mPullToRefreshView = (PullToRefreshView) view.findViewById(R.id.pull_to_refresh);

        mPullToRefreshView.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPullToRefreshView.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        if (!flag_loading) {
                            setData(Constant.LINK, true);
                        }
                        mPullToRefreshView.setRefreshing(false);
                    }
                }, 0);
            }
        });
    }

    private boolean flag_loading;

    private void loadMore() {
        synchronized (lock) {
            AsyncTask asyncTask = new AsyncTask() {
                @Override
                protected Object doInBackground(Object[] params) {
                    setData(next, false);
                    return null;
                }
            };
            asyncTask.execute();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            getActivity().finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDismiss(@NonNull final ViewGroup listView,
                          @NonNull final int[] reverseSortedPositions) {
        /*for (int position : reverseSortedPositions) {
            mGoogleCardsAdapter.remove(mGoogleCardsAdapter.getItem(position));
		}*/
        // setData();
        //Todo

    }

    private void setData(final String link, final boolean isPullToRefresh) {
        if (!TextUtils.isEmpty(link)) {
            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                    link, null,
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d(TAG, response.toString());
                            try {
                                synchronized (lock) {
                                    JSONArray jsonArray = response.getJSONArray(Constant.DATA);
                                    JSONObject objJson;
                                    Entry item;
                                    int size = jsonArray.length();

                                    for (int i = 0; i < size; i++) {
                                        objJson = jsonArray.getJSONObject(i);
                                        item = Entry.getEntry(objJson);
                                        if (item != null) {
                                            BeatifulApplication.getInstance().addEntry(item);
                                        }
                                    }

                                    JSONObject jsonObjectNext = response.getJSONObject(Constant.PAGING);
                                    String urlNext = jsonObjectNext.getString(Constant.NEXT).replace(Constant.PATH, "");

                                    next = String.format(Constant.LINK_NEXT, urlNext);

                                    flag_loading = true;
                                    mHandler.sendEmptyMessage(0);

                                }
                            } catch (Exception e) {
                                flag_loading = false;
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    flag_loading = false;
                    VolleyLog.d(TAG, "Error: " + error.getMessage());
                }
            });
            // Adding request to request queue
            BeatifulApplication.getInstance().addToRequestQueue(jsonObjReq, BeatifulApplication.JSON_ARRAY_QUEUE_LIST);
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            synchronized (lock) {
                List<Entry> list = new ArrayList<>(BeatifulApplication.getInstance().getMapEntry().values());
                Collections.sort(list);
                mGoogleCardsAdapter.addEntry(list);

                mGoogleCardsAdapter.notifyDataSetChanged();
                flag_loading = false;
            }
        }
    };

    @Override
    public void updateData() {
        handler.sendEmptyMessage(0);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mGoogleCardsAdapter.notifyDataSetChanged();
        }
    };

    @Override
    public void onSaveInstanceState(Bundle outState) {
        int i = listView.getLastVisiblePosition() - listView.getFirstVisiblePosition();
        outState.putInt("index", i);
        super.onSaveInstanceState(outState);
    }


}