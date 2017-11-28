package com.tspro.project.girl.adapter;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tspro.project.girl.BeatifulApplication;
import com.tspro.project.girl.MainActivity;
import com.tspro.project.girl.R;
import com.tspro.project.girl.WizardTokenActivity;
import com.tspro.project.girl.model.DrawerItem;
import com.tspro.project.girl.util.DialogGetTokenUtils;

public class DrawerAdapterMore extends BaseAdapter {

    public void setList(ArrayList<DrawerItem> list) {
        this.mDrawerItems = list;
    }

    public interface Callback {
        void excute(final int pos, final DrawerItem drawerItem);

        void delete();
    }

    public List<DrawerItem> getDrawerItems() {
        return mDrawerItems;
    }

    private Callback callback;
    private List<DrawerItem> mDrawerItems;
    private LayoutInflater mInflater;
    private WeakReference<MainActivity> reference;

    public DrawerAdapterMore(MainActivity context, List<DrawerItem> items) {
        this.reference = new WeakReference<>(context);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        callback = context;
        mDrawerItems = items;

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
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        final DrawerItem item = mDrawerItems.get(position);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_view_item_navigation_drawer_2, parent, false);

            holder = new ViewHolder();
            holder.icon = (TextView) convertView.findViewById(R.id.icon); // holder.icon object is null if mIsFirstType is set to false
            holder.title = (TextView) convertView.findViewById(R.id.title);
            holder.iconDelete = (TextView) convertView.findViewById(R.id.icon_delete);


            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.icon.setText(item.getIcon());
        holder.title.setText(item.getTitle());
        holder.iconDelete.setVisibility((item.getTag() == DrawerItem.DRAWER_ITEM_DEFAULT) ? View.INVISIBLE : View.VISIBLE);
        holder.icon.setVisibility((item.getTag() == DrawerItem.DRAWER_ITEM_DEFAULT) ? View.VISIBLE : View.GONE);

        holder.iconDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    BeatifulApplication.getInstance().removeInJSONArray(item.getGroup());
                    callback.delete();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //Toast.makeText(reference.get().getApplicationContext(), "iconDelete", Toast.LENGTH_LONG).show();
            }
        });

        holder.icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (item.getTag() == DrawerItem.DRAWER_ITEM_DEFAULT) {
                    Intent intent = new Intent(reference.get(), WizardTokenActivity.class);
                    reference.get().startActivity(intent);
                } else {
                    //Todo: Thay doi nhom:

                   // Toast.makeText(reference.get().getApplicationContext(), "icon", Toast.LENGTH_LONG).show();
                }
            }
        });

        holder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (item.getTag() == DrawerItem.DRAWER_ITEM_DEFAULT) {
                    DialogGetTokenUtils dialogGetTokenUtils = new DialogGetTokenUtils(reference.get());
                    dialogGetTokenUtils.showDialog();
                } else {
                    callback.excute(position, item);
                }
                // Toast.makeText(reference.get().getApplicationContext(), "title", Toast.LENGTH_LONG).show();

            }
        });
        return convertView;
    }

    public void addDrawerItems(DrawerItem mDrawerItem) {
        this.mDrawerItems.add(mDrawerItem);
    }

    private static class ViewHolder {
        public TextView icon;
        public /*Roboto*/ TextView title;
        public TextView iconDelete;
    }
}
