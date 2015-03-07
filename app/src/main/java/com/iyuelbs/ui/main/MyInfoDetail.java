package com.iyuelbs.ui.main;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.iyuelbs.BaseActivity;
import com.iyuelbs.R;
import com.iyuelbs.app.AppHelper;
import com.iyuelbs.entity.User;
import com.iyuelbs.support.widget.RoundedImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

/**
 * Created by qingliu on 3/4/15.
 */
public class MyInfoDetail extends BaseActivity {

    private DisplayImageOptions mImageOptions;
    private ImageLoader mImageLoader;
    private RoundedImageView mAvatarImage;
    private TextView mUserNameText;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private String[] mDataset = new String[100];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_ditail);
        setUserHead();

        for (int i = 0;i<100;i++) {
            mDataset[i] = "fuck" + i + "次";
        }
        mRecyclerView = (RecyclerView)findViewById(R.id.my_recycler_view);
        //mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new MyAdapter(mDataset);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void setUserHead(){
        mAvatarImage = (RoundedImageView)findViewById(R.id.drawer_user_avatar);
        mUserNameText = (TextView)findViewById(R.id.drawer_user_name);
        userInfo();
    }

    private void userInfo(){
        User user = AppHelper.getCurrentUser();
        getImageLoader().displayImage(user.getAvatarUrl(), mAvatarImage, mImageOptions);
        mAvatarImage.setVisibility(View.VISIBLE);
        mUserNameText.setText(user.getNickName());
        Log.e("name", user.getNickName());
    }

    private ImageLoader getImageLoader() {
        if (mImageLoader == null) {
            mImageLoader = AppHelper.getImageLoader();
            mImageOptions = AppHelper.getDefaultOptsBuilder().displayer(new SimpleBitmapDisplayer()).build();
        }
        return mImageLoader;
    }

    @Override
    protected void setupWindowStyle() {
        super.setupWindowStyle();
    }

    @Override
    protected void setupActionBar(int themeColor) {
        super.setupActionBar(themeColor);
    }

    @Override
    protected void initFragments(Bundle savedInstanceState) {

    }

    @Override
    protected void initView() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_common, menu);
        return true;
    }

    public class MyAdapter extends  RecyclerView.Adapter<MyAdapter.ViewHolder>{
        private String[] mDataset;

        public MyAdapter(String[] mDataset){
            this.mDataset = mDataset;
        }

        public  class ViewHolder extends RecyclerView.ViewHolder{

            public TextView mTextView;
            public ViewHolder(View v) {
                super(v);
                mTextView = (TextView)v.findViewById(R.id.info_text);
            }
        }

        @Override
        public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.card_view,parent,false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(MyAdapter.ViewHolder viewHolder, int position) {
            viewHolder.mTextView.setText(mDataset[position]);
        }

        @Override
        public int getItemCount() {
            return mDataset.length;
        }
    }
}
