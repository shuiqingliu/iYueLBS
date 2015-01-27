package com.iyuelbs.utils;

import android.content.Context;
import android.util.SparseArray;
import android.view.View;
import android.widget.Toast;

import com.iyuelbs.app.AppHelper;

/**
 * Created by Bob Peng on 2015/1/25.
 */
public class ViewUtils {

    private static float mDensity = 0f;
    private static int mScreenWidth = 0;

    @SuppressWarnings("unchecked")
    public static View get(View rootView, int resId) {
        SparseArray<View> views = (SparseArray<View>) rootView.getTag();
        if (views == null) {
            views = new SparseArray<>();
            rootView.setTag(views);
        }

        View childView = views.get(resId);
        if (childView == null) {
            childView = rootView.findViewById(resId);
            views.put(resId, childView);
        }
        return childView;
    }

    public static int getPixels(int unit) {
        if (mDensity == 0f)
            mDensity = AppHelper.getAppContext().getResources().getDisplayMetrics().density;

        return (int) (unit * mDensity + 0.5f);
    }

    public static int getScreenWidth() {
        if (mScreenWidth == 0)
            mScreenWidth = AppHelper.getAppContext().getResources().getDisplayMetrics().widthPixels;
        return mScreenWidth;
    }

    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void showToast(Context context, int stringId) {
        showToast(context, context.getString(stringId));
    }
}
