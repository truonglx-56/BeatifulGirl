package com.tspro.project.girl.model;

public class DrawerItem {

    /* Commented tags are expected in future updates.
     */
    public static final int DRAWER_ITEM_TAG_HOMEPAGE = 10;
    public static final int DRAWER_ITEM_TAG_LIKE = 11;
    //public static final int DRAWER_ITEM_TAG_DOWNLOAD = 12;
    public static final int DRAWER_ITEM_TAG_ABOUT = 13;
    public static final int DRAWER_ITEM_DEFAULT = 1;
    private Group group;

    public void setGroup(Group group) {
        this.group = group;
    }

    public DrawerItem(int icon, String title, int tag) {
        this.icon = icon;
        this.title = title;
        this.tag = tag;
        this.size = 0;
    }

    public DrawerItem(Group group, int icon, String title, int tag, int size) {
        this.group = group;
        this.icon = icon;
        this.title = title;
        this.tag = tag;
        this.size = size;
    }

    public Group getGroup() {
        return group;
    }

    public DrawerItem(int icon, String title, int tag, int size) {
        this.icon = icon;
        this.title = title;
        this.tag = tag;
        this.size = size;
    }

    private int icon;
    private String title;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

}
