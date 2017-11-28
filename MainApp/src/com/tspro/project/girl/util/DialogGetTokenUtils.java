package com.tspro.project.girl.util;

import android.app.*;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.tspro.project.girl.BeatifulApplication;
import com.tspro.project.girl.Constant;
import com.tspro.project.girl.MainActivity;
import com.tspro.project.girl.R;
import com.tspro.project.girl.adapter.MySpinnerAdapter;
import com.tspro.project.girl.model.Group;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class DialogGetTokenUtils {

    private static final String TAG = DialogGetTokenUtils.class.getSimpleName();
    private MainActivity mainActivity;
    private Dialog mDialog;
    private final Object lock = new Object();


    private TextView mDialogFreeOKButton;
    private TextView mDialogFreeCancelButton;
    private TextView mDialogFreeGetGroupButton;
    private EditText mDialogFreeEditUsername;
    private EditText mDialogFreeEditToken;

    private Spinner mSpinnerGroup;
    private Dialog progressDialog;
    private ArrayList<Group> group;
    private MySpinnerAdapter spinnerAdapter;
    private Group mCurrentGroup;
    private Callback callback;

    public DialogGetTokenUtils(MainActivity mDialogSocialActivity) {
        this.mainActivity = mDialogSocialActivity;
    }

    public void showDialog() {
        if (mDialog == null) {
            mDialog = new Dialog(mainActivity, R.style.CustomDialogTheme);
        }
        callback = mainActivity;
        mDialog.setContentView(R.layout.dialog_social);
        group = new ArrayList<>();
        mDialog.show();

        mDialogFreeOKButton = (TextView) mDialog.findViewById(R.id.dialog_social_ok);
        mDialogFreeCancelButton = (TextView) mDialog.findViewById(R.id.dialog_social_cancel);
        mDialogFreeGetGroupButton = (TextView) mDialog.findViewById(R.id.dialog_social_get_group);
        mSpinnerGroup = (Spinner) mDialog.findViewById(R.id.spinner_group);
        mSpinnerGroup.setVisibility(View.GONE);
        mDialogFreeEditUsername = (EditText) mDialog.findViewById(R.id.dialog_social_edit_mask);
        mDialogFreeEditToken = (EditText) mDialog.findViewById(R.id.dialog_social_edit_password);
        try {
            mDialogFreeEditToken.setText(BeatifulApplication.getInstance().getCurrentToken());
        } catch (Exception e) {
        }
        spinnerAdapter = new MySpinnerAdapter(this.mainActivity, R.layout.spinner_rows, group, this.mainActivity.getResources());
        mSpinnerGroup.setAdapter(spinnerAdapter);
        mSpinnerGroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                mCurrentGroup = (Group) parent.getItemAtPosition(pos);
                mCurrentGroup.setToken(mDialogFreeEditToken.getText().toString());
                mCurrentGroup.setMask(mDialogFreeEditUsername.getText().toString());
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        Typeface sRobotoThin = Typeface.createFromAsset(mainActivity.getAssets(),
                "fonts/Roboto-Light.ttf");
        mDialogFreeEditUsername.setTypeface(sRobotoThin);
        mDialogFreeEditToken.setTypeface(sRobotoThin);

        initDialogFreeButtons();
    }


    private void initDialogFreeButtons() {

        mDialogFreeOKButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                try {
                    BeatifulApplication.getInstance().addInJSONArray(mCurrentGroup);
                    if (callback != null) callback.callback(mCurrentGroup);
                } catch (Exception e) {
                    Toast.makeText(mainActivity.getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                } finally {
                   // Toast.makeText(mainActivity.getApplicationContext(), BeatifulApplication.getInstance().getDataFromSharedPreferences().toString(), Toast.LENGTH_SHORT).show();
                    dismissDialog();

                }
            }
        });

        mDialogFreeCancelButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                dismissDialog();
            }
        });

        mDialogFreeGetGroupButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = ProgressDialog.show(mainActivity, mainActivity.getApplicationContext().getString(R.string.please_wait));
                String text = mDialogFreeEditToken.getText() == null ? "" : mDialogFreeEditToken.getText().toString();
                String link = String.format(Constant.QUERY_GROUP, text);
                setData(link);
            }
        });
    }


    public void dismissDialog() {
        mDialog.dismiss();
    }

    private void setData(final String link) {
        group.clear();
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
                                    Group item;
                                    int size = jsonArray.length();

                                    for (int i = 0; i < size; i++) {
                                        objJson = jsonArray.getJSONObject(i);
                                        item = Group.getGroup(objJson);
                                        if (item != null) {
                                            group.add(item);
                                        }
                                    }
                                    mHandler.sendEmptyMessage(0);
                                }
                            } catch (Exception e) {
                                Toast.makeText(mainActivity.getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d(TAG, "Error: " + error.getMessage());
                    Toast.makeText(mainActivity.getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                    mSpinnerGroup.setVisibility(View.GONE);
                    progressDialog.dismiss();
                }
            });
            // Adding request to request queue
            BeatifulApplication.getInstance().addToRequestQueue(jsonObjReq, BeatifulApplication.JSON_ARRAY_QUEUE_LIST);
        }
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            try {
                BeatifulApplication.getInstance().putCurrentToken(mDialogFreeEditToken.getText().toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
            progressDialog.dismiss();
            group.add(0, new Group("group", "scret", 1, -113246l));

            spinnerAdapter = new MySpinnerAdapter(mainActivity, R.layout.spinner_rows, group, mainActivity.getResources());
            mSpinnerGroup.setAdapter(spinnerAdapter);

            mSpinnerGroup.setVisibility(View.VISIBLE);


        }
    };

    public interface Callback {
        void callback(Group group);
    }

}
