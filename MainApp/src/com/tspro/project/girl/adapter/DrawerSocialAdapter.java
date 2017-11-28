package com.tspro.project.girl.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tspro.project.girl.R;
import com.tspro.project.girl.model.DrawerItem;

public class DrawerSocialAdapter extends BaseAdapter {

    private List<DrawerItem> mDrawerItems;
    private LayoutInflater mInflater;

    public DrawerSocialAdapter(Context context, List<DrawerItem> drawerItems) {
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mDrawerItems = drawerItems;
    }

    @Override
    public int getCount() {
        return mDrawerItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mDrawerItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mDrawerItems.get(position).getTag();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(
                    R.layout.list_view_item_navigation_drawer_social, parent,
                    false);
            holder = new ViewHolder();
            holder.icon = (TextView) convertView
                    .findViewById(R.id.icon_social_navigation_item);
            holder.title = (TextView) convertView.findViewById(R.id.title);
            holder.count = (TextView) convertView.findViewById(R.id.number_of_notification);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final DrawerItem item = mDrawerItems.get(position);

        holder.icon.setText(item.getIcon());
        holder.title.setText(item.getTitle());
        holder.count.setText(String.valueOf(item.getSize()));

        return convertView;
    }

    private static class ViewHolder {
        public TextView icon;
        public/* Roboto */ TextView title;
        public TextView count;
    }
}
