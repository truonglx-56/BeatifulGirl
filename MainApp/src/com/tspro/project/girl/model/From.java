package com.tspro.project.girl.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.tspro.project.girl.Constant;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by truonglx on 02/10/2017.
 */

public class From implements Parcelable {
    private String nameUser;
    private String girlId;

    protected From(Parcel in) {
        nameUser = in.readString();
        girlId = in.readString();
        linkProfileUser = in.readString();
    }

    public static final Creator<From> CREATOR = new Creator<From>() {
        @Override
        public From createFromParcel(Parcel in) {
            return new From(in);
        }

        @Override
        public From[] newArray(int size) {
            return new From[size];
        }
    };

    public String getLinkProfileUser() {
        return linkProfileUser;
    }

    private String linkProfileUser;


    public From(String nameUser, String userId) {
        this.nameUser = nameUser;
        this.girlId = userId;
        this.linkProfileUser = String.format(Constant.PROFILE_LINK, userId);
    }

    public static From getFrom(JSONObject jsonObject) {
        try {
            return new From(jsonObject.getString(Constant.NAME), jsonObject.getString(Constant.ID));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new From("", "");
    }

    public String getNameUser() {
        return nameUser;
    }

    public void setNameUser(String nameUser) {
        this.nameUser = nameUser;
    }

    public String getGirlId() {
        return girlId;
    }

    public void setGirlId(String girlId) {
        this.girlId = girlId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(nameUser);
        parcel.writeString(girlId);
        parcel.writeString(linkProfileUser);
    }
}
