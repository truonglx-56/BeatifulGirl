package com.tspro.project.girl.model;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by truonglx on 18/11/2017.
 */
   /*
    {
      "name": "AND1505E",
      "privacy": "SECRET",
      "version": 1,
      "id": "636348793133972"
    },
     */
public class Group implements Comparable {
    private String name;
    private String privacy;
    private int version;
    private long id;
    private String token;
    private String mask;
    private long createdDate;

    public Group(String name, String privacy, int version, long id) {
        this.name = name;
        this.privacy = privacy;
        this.version = version;
        this.id = id;
        this.createdDate = System.currentTimeMillis();
    }

    public Group(String name, String privacy, int version, long id, String token, String mask) {
        this.name = name;
        this.privacy = privacy;
        this.version = version;
        this.id = id;
        this.token = token;
        this.mask = mask;
    }

    public static Group getGroup(JSONObject jsonObject) {
        try {
            return new Group(jsonObject.getString("name"), jsonObject.getString("privacy"), jsonObject.getInt("version"), Long.parseLong(jsonObject.getString("id")));
        } catch (JSONException e) {
            return null;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrivacy() {
        return privacy;
    }

    public void setPrivacy(String privacy) {
        this.privacy = privacy;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMask() {
        return mask;
    }

    public void setMask(String mask) {
        this.mask = mask;
    }


    @Override
    public String toString() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("mask", this.mask == null ? "" : mask);
            jsonObject.put("name", this.name == null ? "" : name);
            jsonObject.put("privacy", this.privacy == null ? "" : this.privacy);
            jsonObject.put("version", this.version);
            jsonObject.put("token", this.privacy == null ? "" : privacy);
            jsonObject.put("id", this.id);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Group group = (Group) o;
        if (id != group.id) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (privacy != null ? privacy.hashCode() : 0);
        result = 31 * result + version;
        result = 31 * result + (int) (id ^ (id >>> 32));
        result = 31 * result + (token != null ? token.hashCode() : 0);
        result = 31 * result + (mask != null ? mask.hashCode() : 0);
        result = 31 * result + (int) (createdDate ^ (createdDate >>> 32));
        return result;
    }

    @Override
    public int compareTo(@NonNull Object o) {
        if (o instanceof Group) {
            return (this.createdDate > ((Group) o).createdDate) ? 1 : -1;
        }
        return 1;
    }
}

