package com.tspro.project.girl.adapter;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pair;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tspro.project.girl.BeatifulApplication;
import com.tspro.project.girl.Constant;
import com.tspro.project.girl.Downloader;
import com.tspro.project.girl.R;
import com.tspro.project.girl.ViewPagerActivity;
import com.tspro.project.girl.model.Entry;
import com.tspro.project.girl.util.ImageUtil;

import static android.support.design.widget.Snackbar.*;

public class MainAdapter extends BaseAdapter
        implements OnClickListener {

    private LayoutInflater mInflater;
    private List<Entry> entryList;
    private WeakReference<? extends Fragment> reference;

    public MainAdapter(WeakReference<? extends Fragment> context) {
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
    public View getView(final int position, View convertView, ViewGroup parent) {
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
            holder.download = (TextView) convertView
                    .findViewById(R.id.list_item_google_cards_social_download);
            holder.like.setOnClickListener(this);
            holder.download.setOnClickListener(this);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final Entry item = entryList.get(position);

        ImageUtil.displayImage(holder.image, item.getFull_picture(), null);
        ImageUtil.displayRoundImage(holder.profileImage, item.getFrom().getLinkProfileUser(), null);
        holder.image.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                transition(view, new ArrayList<>(entryList), position);
            }
        });

        OnClickListener onClickListenerDownload = new OnClickListener() {
            @Override
            public void onClick(View v) {
                // BeatifulApplication.getInstance().getGirlDatabaseManager().save(item, true);
                Downloader.getInstance().downloadData(item);
                makeSnackbar(v, reference.get().getText(R.string.downloading));
            }
        };

        OnClickListener onClickListenerFavorite = new OnClickListener() {
            @Override
            public void onClick(View v) {
                long ressult = -1;
                if (BeatifulApplication.getInstance().isFavorite(item.getIdRaw())) {
                    ressult = BeatifulApplication.getInstance().getGirlDatabaseManager().delete(item, false);
                } else {
                    ressult = BeatifulApplication.getInstance().getGirlDatabaseManager().save(item, false);
                }
                if (ressult > 0) makeSnackbar(v, reference.get().getText(R.string.favorite));


            }
        };

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
        holder.download.setOnClickListener(onClickListenerDownload);
        holder.like.setOnClickListener(onClickListenerFavorite);
        if (BeatifulApplication.getInstance().isFavorite(item.getIdRaw()))
            holder.like.setTextColor(ContextCompat.getColor(reference.get().getContext(), R.color.material_teal_A700));
        else
            holder.like.setTextColor(ContextCompat.getColor(reference.get().getContext(), R.color.white));

        holder.username.setText("@" + item.getFrom().

                getNameUser());
        holder.place.setText(item.getStringDateUpdate());
        holder.text.setText(item.getMessage());
        holder.like.setTag(position);
        holder.download.setTag(position);

        return convertView;
    }

    private static class ViewHolder {
        public ImageView profileImage;
        public ImageView image;
        public TextView username;
        public TextView place;
        public TextView text;
        public TextView like;
        public TextView download;
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub

        switch (v.getId()) {
            case R.id.list_item_google_cards_social_like:
                // click on like button
                break;
            case R.id.list_item_google_cards_social_download:
                // click on download button
                break;
        }
    }

    public void addEntry(List<Entry> entries) {
        entryList = (entries);
    }

    private void transition(final View view, final ArrayList<Entry> entry, int pos) {

        Intent intent = new Intent(reference.get().getContext(), ViewPagerActivity.class);
        intent.putParcelableArrayListExtra(Constant.ENTRY, entry);
        intent.putExtra("TruongLX", pos);

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

    private void makeSnackbar(final View v, CharSequence sequence) {
        Snackbar snackbar = make(v, sequence, LENGTH_SHORT);
        snackbar.show();
        View view = snackbar.getView();
        TextView txtv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
        txtv.setGravity(Gravity.CENTER_HORIZONTAL);
    }

}
