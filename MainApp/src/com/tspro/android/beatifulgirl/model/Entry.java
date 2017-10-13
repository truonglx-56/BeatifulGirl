package com.tspro.android.beatifulgirl.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.tspro.android.beatifulgirl.Constant;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by truonglx on 02/10/2017.
 */

public class Entry implements Parcelable, Comparable<Entry> {

    private static DateFormat df = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
    private String full_picture;
    private String message;
    private long updated_time;
    private From from;
    private long id;
    private String idRaw;
    private String pathDownload;

    protected Entry(Parcel in) {
        full_picture = in.readString();
        message = in.readString();
        updated_time = in.readLong();
        id = in.readLong();
        idRaw = in.readString();
    }

    public static final Creator<Entry> CREATOR = new Creator<Entry>() {
        @Override
        public Entry createFromParcel(Parcel in) {
            return new Entry(in);
        }

        @Override
        public Entry[] newArray(int size) {
            return new Entry[size];
        }
    };

    public static Entry getEntry(JSONObject jsonObject) {
        try {
            From from = From.getFrom(jsonObject.getJSONObject(Constant.FROM));
            if ("100002516348305".equals(from.getGirlId())) return null;

            String full_picture = jsonObject.getString(Constant.FULL_LINK);
            String message = jsonObject.getString(Constant.MESSAGE);

            String update_timeS = jsonObject.getString(Constant.UPDATED_TIME);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            Date d = sdf.parse(update_timeS);

            String idRaw = jsonObject.getString(Constant.ID);
            long id = Long.parseLong(idRaw.split("_")[1]);

            return new Entry(full_picture, message, d.getTime(), from, id, idRaw);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Entry() {
    }

    public String getPathDownload() {
        return pathDownload;
    }

    public void setPathDownload(String pathDownload) {
        this.pathDownload = pathDownload;
    }

    public String getIdRaw() {
        return idRaw;
    }

    public void setIdRaw(String idRaw) {
        this.idRaw = idRaw;
    }

    public Entry(String full_picture, String message, long updated_time, From from, long id, String idRaw) {
        this.full_picture = full_picture;
        this.message = message;
        this.updated_time = updated_time;
        this.from = from;
        this.id = id;
        this.idRaw = idRaw;
    }

    public String getStringDateUpdate() {

        return df.format(new Date(updated_time));
    }

    public String getFull_picture() {
        return full_picture;
    }

    public void setFull_picture(String full_picture) {
        this.full_picture = full_picture;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getUpdated_time() {
        return updated_time;
    }

    public void setUpdated_time(long updated_time) {
        this.updated_time = updated_time;
    }

    public From getFrom() {
        return from;
    }

    public void setFrom(From from) {
        this.from = from;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        return Long.valueOf(id).hashCode();

    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Entry) {

            return (((Entry) obj).getId() == (this.getId())) && (((Entry) obj).getUpdated_time() == this.getUpdated_time());

        }
        return false;
    }

    @Override
    public String toString() {
        return "Entry{" +
                "full_picture='" + full_picture + '\'' +
                ", message='" + message + '\'' +
                ", updated_time='" + updated_time + '\'' +
                ", from=" + from +
                ", id='" + id + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(full_picture);
        parcel.writeString(message);
        parcel.writeLong(updated_time);
        parcel.writeLong(id);
        parcel.writeString(idRaw);
    }

    @Override
    public int compareTo(@NonNull Entry entry) {
        return (int) (entry.getUpdated_time() - this.getUpdated_time());
    }


}
