/*
 * Copyright 2014 Niek Haarman
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.tspro.project.girl;

import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.tspro.project.girl.adapter.MainAdapter;
import com.tspro.project.girl.model.Entry;
import com.nhaarman.listviewanimations.appearance.simple.SwingBottomInAnimationAdapter;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.OnDismissCallback;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.SwipeDismissAdapter;
import com.lib.pull.PullToRefreshView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class GoogleCardsSocialActivity extends AppCompatActivity implements
        OnDismissCallback {
    public static final String TAG = "fragment_latest";

    private static final int INITIAL_DELAY_MILLIS = 300;

    private MainAdapter mGoogleCardsAdapter;

    private List<Entry> entryList;

    private String next;

    private final Object lock = new Object();
    private PullToRefreshView mPullToRefreshView;
    private WeakReference<GoogleCardsSocialActivity> reference;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_view_homepage);
        entryList = new ArrayList<>();
        ListView listView = (ListView) findViewById(R.id.list_view);
        reference = new WeakReference<>(this);
        //mGoogleCardsAdapter = new MainAdapter(reference);

        SwingBottomInAnimationAdapter swingBottomInAnimationAdapter = new SwingBottomInAnimationAdapter(
                new SwipeDismissAdapter(mGoogleCardsAdapter, this));

        swingBottomInAnimationAdapter.setAbsListView(listView);
        assert swingBottomInAnimationAdapter.getViewAnimator() != null;
        swingBottomInAnimationAdapter.getViewAnimator().setInitialDelayMillis(
                INITIAL_DELAY_MILLIS);

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

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Google cards social");
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScroll(AbsListView view,
                                 int firstVisibleItem, int visibleItemCount,
                                 int totalItemCount) {
                if (firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount != 0) {
                    if (flag_loading == false) {
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
        setData(Constant.LINK);
        setPullToRefesh();
    }

    private void setPullToRefesh() {
        mPullToRefreshView = (PullToRefreshView) findViewById(R.id.pull_to_refresh);

        mPullToRefreshView.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPullToRefreshView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
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
                    setData(next);
                    return null;
                }
            };
            asyncTask.execute();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
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
        synchronized (lock) {
            for (int i = 0; i < entryList.size(); i++) {
                Log.d("TruongLX-dismiss", entryList.get(i).getId() + "");
            }
        }
    }

    private void setData(String link) {
        if (!TextUtils.isEmpty(link)) {
            Log.d("TruongLX", link);
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
                                            if (!entryList.contains(item)) {
                                                entryList.add(item);
                                                Log.d("TruongLX - IF", item.getId() + "");
                                            } else Log.d("TruongLX - trung", item.toString());
                                        }
                                    }
                                    Log.d("TruongLX", response.toString());
                                    JSONObject jsonObject = response.getJSONObject(Constant.PAGING);
                                    String urlNext = jsonObject.getString(Constant.NEXT).replace(Constant.PATH, "");


                                    next = String.format(Constant.LINK_NEXT, urlNext);
                                    BeatifulApplication.getInstance().setNext(next);

                                    flag_loading = true;
                                }
                            } catch (JSONException e) {
                                flag_loading = false;
                                e.printStackTrace();
                            }
                            mHandler.sendEmptyMessage(0);
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
                mGoogleCardsAdapter.addEntry(entryList);
                mGoogleCardsAdapter.notifyDataSetChanged();
                flag_loading = false;
            }
        }
    };
}