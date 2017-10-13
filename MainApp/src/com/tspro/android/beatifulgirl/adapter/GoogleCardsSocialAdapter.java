package com.tspro.android.beatifulgirl.adapter;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tspro.android.beatifulgirl.ActivityTransitionToActivity;
import com.tspro.android.beatifulgirl.Constant;
import com.tspro.android.beatifulgirl.R;
import com.tspro.android.beatifulgirl.fragment.HomePageFragment;
import com.tspro.android.beatifulgirl.model.Entry;
import com.tspro.android.beatifulgirl.util.ImageUtil;

public class GoogleCardsSocialAdapter extends BaseAdapter
        implements OnClickListener {

    private LayoutInflater mInflater;
    private List<Entry> entryList;
    private WeakReference<HomePageFragment> reference;

    public GoogleCardsSocialAdapter(WeakReference<HomePageFragment> context) {
        this.reference = context;
        mInflater = (LayoutInflater) context.get().getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        entryList = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return entryList.size();
    }

    @Override
    public Object getItem(int i) {
        return entryList.get(i);
    }

    @Override
    public long getItemId(int position) {
        return entryList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(
                    R.layout.list_item_google_cards_social, parent, false);
            holder = new ViewHolder();
            holder.image = (ImageView) convertView
                    .findViewById(R.id.list_item_google_cards_social_image);
            holder.profileImage = (ImageView) convertView
                    .findViewById(R.id.list_item_google_cards_social_profile_image);
            holder.username = (TextView) convertView
                    .findViewById(R.id.list_item_google_cards_social_name);
            holder.place = (TextView) convertView
                    .findViewById(R.id.list_item_google_cards_social_place);
            holder.text = (TextView) convertView
                    .findViewById(R.id.list_item_google_cards_social_text);
            holder.like = (TextView) convertView
                    .findViewById(R.id.list_item_google_cards_social_like);
            holder.follow = (TextView) convertView
                    .findViewById(R.id.list_item_google_cards_social_follow);
            holder.like.setOnClickListener(this);
            holder.follow.setOnClickListener(this);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final Entry item = entryList.get(position);

        ImageUtil.displayRoundImage(holder.profileImage, item.getFrom().getLinkProfileUser(), null);
        ImageUtil.displayImage(holder.image, item.getFull_picture(), null);
        holder.image.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                transition(view, item);
            }
        });

        OnClickListener onClickListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                final String url = getFacebookPageURL(reference.get().getContext(), item.getFrom().getGirlId());
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                reference.get().getContext().startActivity(intent);
            }
        };
        holder.username.setOnClickListener(onClickListener);
        holder.profileImage.setOnClickListener(onClickListener);

        holder.username.setText("@" + item.getFrom().

                getNameUser());
        holder.place.setText(item.getStringDateUpdate());
        holder.text.setText(item.getMessage());
        holder.like.setTag(position);
        holder.follow.setTag(position);

        return convertView;
    }

    private static class ViewHolder {
        public ImageView profileImage;
        public ImageView image;
        public TextView username;
        public TextView place;
        public TextView text;
        public TextView like;
        public TextView follow;
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub

        switch (v.getId()) {
            case R.id.list_item_google_cards_social_like:
                // click on like button
                break;
            case R.id.list_item_google_cards_social_follow:
                // click on follow button
                break;
        }
    }

    public void addEntry(List<Entry> entries) {
        entryList = (entries);
    }

    private void transition(final View view, final Entry entry) {

        Intent intent = new Intent(reference.get().getContext(), ActivityTransitionToActivity.class);
        intent.putExtra(Constant.ENTRY, entry);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && reference != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && reference != null) {
                view.setTransitionName(reference.get().getString(R.string.transition_image));

                Pair<View, String> pair1 = Pair.create(view, view.getTransitionName());

                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation(reference.get().getActivity(), pair1);
                reference.get().startActivity(intent, options.toBundle());
            }
        } else {
            reference.get().startActivity(intent);
        }
    }

    //method to get the right URL to use in the intent
    public String getFacebookPageURL(Context context, String id) {
        PackageManager packageManager = context.getPackageManager();
        try {
            packageManager.getPackageInfo("com.facebook.katana", 0);
            return "fb://profile/" + id; //normal web url
            // return "fb://facewebmodal/f?href=" + url;
        } catch (PackageManager.NameNotFoundException e) {
            return "https://www.facebook.com/" + id; //normal web url
        }
    }
}
