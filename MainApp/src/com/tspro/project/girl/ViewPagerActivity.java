/*******************************************************************************
 * Copyright 2011, 2012 Chris Banes.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.tspro.project.girl;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

import com.github.chrisbanes.photoview.PhotoView;
import com.tspro.project.girl.model.Entry;
import com.tspro.project.girl.util.ImageUtil;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerActivity extends AppCompatActivity {
    private List<Entry> mEntries;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager);
        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        ArrayList<Entry> mEntrys = getIntent().getParcelableArrayListExtra(Constant.ENTRY);
        int pos = getIntent().getIntExtra("TruongLX", 0);
        mEntries = (mEntrys == null) ? new ArrayList<Entry>() : mEntrys;
        viewPager.setAdapter(new SamplePagerAdapter(mEntries, this, pos));
        viewPager.setCurrentItem(pos);
    }

    static class SamplePagerAdapter extends PagerAdapter {

        private List<Entry> sDrawables;
        private Context context;
        private int pos;
        private boolean check;

        public SamplePagerAdapter(List<Entry> drEntries, Context context, int pos) {
            this.sDrawables = drEntries;
            this.context = context;
            this.pos = pos;
            this.check = false;
        }

        @Override
        public int getCount() {
            return sDrawables.size();
        }

        @Override
        public View instantiateItem(ViewGroup container, int position) {
            PhotoView photoView = new PhotoView(container.getContext());
            //photoView.setImageResource(sDrawables.get(position).getFull_picture());
            ImageUtil.displayImage(photoView, sDrawables.get(position).getFull_picture(), null);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && !check && pos == position) {
                photoView.setTransitionName(context.getString(R.string.transition_image));
                check = true;
            }
            // Now just add PhotoView to ViewPager and return it
            container.addView(photoView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

            return photoView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

    }
}
