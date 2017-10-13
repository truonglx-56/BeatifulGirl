package com.tspro.android.beatifulgirl.model;

public class DrawerItem {

    /* Commented tags are expected in future updates.
     */
    public static final int DRAWER_ITEM_TAG_HOMEPAGE = 10;
    public static final int DRAWER_ITEM_TAG_LIKE = 11;
    public static final int DRAWER_ITEM_TAG_DOWNLOAD = 12;
    public static final int DRAWER_ITEM_TAG_ABOUT = 13;

    public DrawerItem(int icon, int title, int tag) {
        this.icon = icon;
        this.title = title;
        this.tag = tag;
        this.size = 0;
    }

    public DrawerItem(int icon, int title, int tag, int size) {
        this.icon = icon;
        this.title = title;
        this.tag = tag;
        this.size = size;
    }

    private int icon;
    private int title;
    private int tag;
    private int size;

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public int getTitle() {
        return title;
    }

    public void setTitle(int title) {
        this.title = title;
    }

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

}
