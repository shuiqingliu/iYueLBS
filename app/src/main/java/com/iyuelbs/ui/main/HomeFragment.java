package com.iyuelbs.ui.main;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.getbase.floatingactionbutton.AddFloatingActionButton;
import com.iyuelbs.BaseFragment;
import com.iyuelbs.R;

/**
 * Created by xifan on 15-4-15.
 */
public class HomeFragment extends BaseFragment implements View.OnClickListener {

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private boolean mInited = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment,container,false);

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.home_refresh);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.home_list);
        AddFloatingActionButton fabBtn = (AddFloatingActionButton) view.findViewById(R.id.home_fab);

        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mSwipeRefreshLayout.setOnRefreshListener(new RefreshListener());
        fabBtn.setOnClickListener(this);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!mInited) {
            mSwipeRefreshLayout.setRefreshing(true);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.home_fab) {
            onFabClick();
        }
    }

    private void onFabClick() {

    }

    private class RefreshListener implements SwipeRefreshLayout.OnRefreshListener {

        @Override
        public void onRefresh() {

        }
    }
}
