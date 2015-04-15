package com.iyuelbs.ui.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.iyuelbs.R;
import com.iyuelbs.app.AppConfig;
import com.iyuelbs.app.AppHelper;
import com.iyuelbs.app.Keys;
import com.iyuelbs.entity.User;
import com.iyuelbs.support.utils.ViewUtils;
import com.iyuelbs.support.widget.RoundedImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bob Peng on 2015/1/27.
 */
public class DrawerFragment extends ListFragment {

    private Context mContext;
    private ImageLoader mImageLoader;
    private DisplayImageOptions mImageOptions;

    private List<MenuItem> mDrawerList;
    private MenuItem mDividerItem;
    private DrawerAdapter mAdapter;

    private RoundedImageView mAvatarImage;
    private TextView mUserNameText;
    private TextView mStatusText;

    private int mCurrentIndex = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        setHasOptionsMenu(true);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initDrawerItem();
        setupListView();
    }

    @Override
    public void onResume() {
        super.onResume();
        initUserInfo();
        getListView().setItemChecked(mCurrentIndex, true);
    }

    private void initDrawerItem() {
        mDrawerList = new ArrayList<>();
        mDividerItem = new MenuItem(0, null);
        mDrawerList.add(new MenuItem(R.drawable.ic_explore_grey600_24dp,
                getString(R.string.title_drawer_index)));
        mDrawerList.add(new MenuItem(R.drawable.ic_place_grey600_24dp,
                getString(R.string.title_drawer_places)));
        mDrawerList.add(new MenuItem(R.drawable.ic_local_restaurant_grey600_24dp,
                getString(R.string.title_drawer_restaurant)));
        mDrawerList.add(new MenuItem(R.drawable.ic_arrow_back_grey600_24dp,
                getString(R.string.title_drawer_me)));
        mDrawerList.add(mDividerItem);
        mDrawerList.add(new MenuItem(R.drawable.ic_settings_grey600_24dp,
                getString(R.string.action_settings)));
    }

    private void setupListView() {
        ListView listView = getListView();
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listView.setDivider(new ColorDrawable(getResources().getColor(R.color.transparent)));
        listView.setDividerHeight(0);
        listView.setBackgroundColor(getResources().getColor(R.color.white));
        listView.setCacheColorHint(Color.TRANSPARENT);

        attachListHeader();
        setListAdapter(mAdapter = new DrawerAdapter());
    }

    private void attachListHeader() {
        View view = View.inflate(mContext, R.layout.view_drawer_header, null);

        mAvatarImage = (RoundedImageView) view.findViewById(R.id.drawer_user_avatar);
        if (AppConfig.TRANSLUCENT_BAR_ENABLED) {
            ((LinearLayout.LayoutParams) mAvatarImage.getLayoutParams()).topMargin += ViewUtils.getPixels(24);
        }
        mUserNameText = (TextView) view.findViewById(R.id.drawer_user_name);
        mStatusText = (TextView) view.findViewById(R.id.drawer_user_status);
        getListView().addHeaderView(view, null, false);

        initUserInfo();
    }

    private void initUserInfo() {
        if (AppHelper.checkLoginCache()) {
            User user = AppHelper.getCurrentUser();
            getImageLoader().displayImage(user.getAvatarUrl(), mAvatarImage, mImageOptions);
            mAvatarImage.setVisibility(View.VISIBLE);
            mUserNameText.setText(user.getNickName());
            mStatusText.setText(user.getLocStatus() == null ? getString(R.string.title_no_loc_status) : user.getLocStatus());
        } else {
            mAvatarImage.setVisibility(View.INVISIBLE);
            mUserNameText.setText(R.string.title_not_login);
            mStatusText.setText(R.string.title_no_loc_status);
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        if (getListView().getHeaderViewsCount() > 0)
            position--; // fix wrong pos.

        super.onListItemClick(l, v, position, id);

        if (position < mDrawerList.size() - 1) { // don't toggle settings
            l.setItemChecked(position, true);
            mCurrentIndex = position;

            switch (position) {
                case 0: // index
                    break;
                case 1: // place
                    break;
                case 2: // restaurant
                    break;
                case 3:
                    Intent intent = new Intent(mContext, MyInfoDetail.class);
                    startActivity(intent);
                    break;
            }
        } else {
            // TODO: for now is settings
        }
    }

    private DrawerAdapter getAdapter() {
        if (mAdapter == null)
            mAdapter = new DrawerAdapter();
        return mAdapter;
    }

    private ImageLoader getImageLoader() {
        if (mImageLoader == null) {
            mImageLoader = AppHelper.getImageLoader();
            mImageOptions = AppHelper.getDefaultOptsBuilder().displayer(new SimpleBitmapDisplayer()).build();
        }
        return mImageLoader;
    }

    private boolean isDivider(int position) {
        return mDrawerList.get(position).equals(mDividerItem);
    }

    private class DrawerAdapter extends BaseAdapter {

        private Resources mRes;
        private LayoutInflater mInflater;
        private SparseIntArray mCountArray;

        private DrawerAdapter() {
            mRes = mContext.getResources();
            mInflater = ((Activity) mContext).getLayoutInflater();
        }

        @Override
        public int getCount() {
            return mDrawerList.size();
        }

        @Override
        public Object getItem(int position) {
            return mDrawerList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return mDrawerList.get(position).resId;
        }

        public void setItemCounter(int position, int count) {
            if (mCountArray == null)
                mCountArray = new SparseIntArray(1);
            mCountArray.put(position, count);
            notifyDataSetChanged();
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            if (view == null) {
                view = mInflater.inflate(R.layout.list_drawer_item, null);
            }

            if (getItemId(position) == 0) {
                ViewUtils.get(view, R.id.drawer_divider).setVisibility(View.VISIBLE);
                ViewUtils.get(view, R.id.drawer_content).setVisibility(View.GONE);
                view.setClickable(true);
            } else {
                ViewUtils.get(view, R.id.drawer_divider).setVisibility(View.GONE);
                ViewUtils.get(view, R.id.drawer_content).setVisibility(View.VISIBLE);
                view.setClickable(false);

                ImageView iconView = (ImageView) ViewUtils.get(view, R.id.drawer_icon);
                TextView titleText = (TextView) ViewUtils.get(view, R.id.drawer_title);
                TextView countText = (TextView) ViewUtils.get(view, R.id.drawer_count);

                iconView.setImageDrawable(mRes.getDrawable((int) getItemId(position)));
                titleText.setText(mDrawerList.get(position).title);

                if (mCountArray != null && mCountArray.indexOfKey(position) > -1) {
                    countText.setVisibility(View.VISIBLE);
                    countText.setText(mCountArray.get(position) + "");
                } else {
                    countText.setVisibility(View.GONE);
                }

                if (getListView().getCheckedItemPosition() == position) {
                    view.setBackgroundResource(R.color.divider_dark);
                    int color = ViewUtils.getThemeColor(mRes, Keys.STYLE_COLOR_PRIMARY);
                    titleText.setTextColor(color);
                    iconView.setColorFilter(color);
                } else {
                    view.setBackgroundResource(0);
                    titleText.setTextColor(mRes.getColor(R.color.black));
                    iconView.clearColorFilter();
                }
            }

            return view;
        }
    }

    private class MenuItem {
        public int resId;
        public String title;

        private MenuItem(int resId, String title) {
            this.resId = resId;
            this.title = title;
        }

        /**
         * Override for Object check by resId
         */
        @Override
        public boolean equals(Object o) {
            return o instanceof MenuItem && ((MenuItem) o).resId == resId;
        }
    }
}
