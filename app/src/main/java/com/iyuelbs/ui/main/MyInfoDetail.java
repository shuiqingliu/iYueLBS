package com.iyuelbs.ui.main;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.poliveira.parallaxrecycleradapter.ParallaxRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

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
        Log.e("start", "开始加载用户头像");
        /*setUserHead();*/

        for (int i = 0;i<100;i++) {
            mDataset[i] = "fuck" + i + "次";
        }
        mRecyclerView = (RecyclerView)findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(manager);
        //尝试滑动
        final List<String> content = new ArrayList<>();
        for (int i = 0; i < 30; i++)
            content.add(getListString(i));

        ParallaxRecyclerAdapter<String> stringAdapter = new ParallaxRecyclerAdapter<>(content);
        stringAdapter.implementRecyclerAdapterMethods(new ParallaxRecyclerAdapter.RecyclerAdapterMethods() {
            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
                /*((TextView) viewHolder.itemView).setText(content.get(i));*/
            }

            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
                return new SimpleViewHolder(getLayoutInflater().inflate(R.layout.card_view, viewGroup, false));
            }

            @Override
            public int getItemCount() {
                return content.size();
            }
        });
        Log.e("header", "这里没错");
        stringAdapter.setParallaxHeader(getLayoutInflater().inflate(R.layout.my_header, mRecyclerView, false), mRecyclerView);
        Log.e("head", "有没有挂?");
        stringAdapter.setOnParallaxScroll(new ParallaxRecyclerAdapter.OnParallaxScroll() {
            @Override
            public void onParallaxScroll(float percentage, float offset, View parallax) {
                //TODO: implement toolbar alpha. See README for details
            }
        });


        /*mAdapter = new MyAdapter(mDataset);*/
        mRecyclerView.setAdapter(stringAdapter);
        /*mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                ViewHelper.setTranslationY(findViewById(R.id.my_recycler_view),dy);
            }
        });*/
    }
    public String getListString(int postion) {
        return postion + "- android";
    }

    static  class SimpleViewHolder extends RecyclerView.ViewHolder{
        public SimpleViewHolder(View itemView) {
            super(itemView);
        }
    }

    private void setUserHead(){
        mAvatarImage = (RoundedImageView)findViewById(R.id.drawer_user_avatar);
        mUserNameText = (TextView)findViewById(R.id.drawer_user_name);
        userInfo();
    }

    private void userInfo(){
        User user = AppHelper.getCurrentUser();
        Log.e("image", "开始getImageLoader");
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
        Log.e("aaa","加载完毕");
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

  /*  public class MyAdapter extends  RecyclerView.Adapter<MyAdapter.ViewHolder>{
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
    }*/
}
