package com.iyuelbs.ui.main;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.iyuelbs.R;
import com.iyuelbs.entity.Tag;

import java.util.List;

/**
 * Created by xifan on 15-4-15.
 */
public class HomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int TYPE_LIST = 1;
    public static final int TYPE_HEADER = 2;
    public static final int TYPE_FOOT = 3;

    private List<Tag> mList;

    public HomeAdapter() {
        this.mList = mList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        Context context = parent.getContext();
        switch (viewType) {
            case TYPE_LIST:
                view = View.inflate(context, R.layout.list_home_item, null);
                break;
            case TYPE_HEADER:
                view = View.inflate(context, R.layout.view_home_header, null);
                break;
            case TYPE_FOOT:
                break;
        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    private class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
