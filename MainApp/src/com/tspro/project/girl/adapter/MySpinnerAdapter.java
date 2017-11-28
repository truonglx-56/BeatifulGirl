package com.tspro.project.girl.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.tspro.project.girl.BeatifulApplication;
import com.tspro.project.girl.Constant;
import com.tspro.project.girl.MainActivity;
import com.tspro.project.girl.R;
import com.tspro.project.girl.model.Entry;
import com.tspro.project.girl.model.Group;

import org.json.JSONArray;
import org.json.JSONObject;

/***** Adapter class extends with ArrayAdapter ******/
public class MySpinnerAdapter extends ArrayAdapter<String> {

    private static final String TAG = "" ;
    private Activity activity;
    private ArrayList<Group> data;
    public Resources res;
    Group tempValues = null;
    LayoutInflater inflater;
    private final Object lock = new Object();

    /*************  CustomAdapter Constructor *****************/
    public MySpinnerAdapter(
            MainActivity activitySpinner,
            int textViewResourceId,
            ArrayList objects,
            Resources resLocal
    ) {
        super(activitySpinner, textViewResourceId, objects);

        /********** Take passed values **********/
        activity = activitySpinner;
        data = objects;
        res = resLocal;

        /***********  Layout inflator to call external xml layout () **********************/
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    // This funtion called for each row ( Called data.size() times )
    public View getCustomView(int position, View convertView, ViewGroup parent) {

        /********** Inflate spinner_rows.xml file for each row ( Defined below ) ************/
        View row = inflater.inflate(R.layout.spinner_rows, parent, false);

        /***** Get each Model object from Arraylist ********/
        tempValues = null;
        tempValues = (Group) data.get(position);

        TextView label = (TextView) row.findViewById(R.id.name);

        if (position == 0) {

            // Default selected Spinner item
            label.setText("Group");
        } else {
            // Set values for spinner each row
            label.setText(tempValues.getName());


        }

        return row;
    }

    private void setData(final String link) {
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



                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d(TAG, "Error: " + error.getMessage());
                }
            });
            // Adding request to request queue
            BeatifulApplication.getInstance().addToRequestQueue(jsonObjReq, BeatifulApplication.JSON_QUERY_GROUP);
        }
    }

}