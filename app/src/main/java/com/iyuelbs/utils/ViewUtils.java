package com.iyuelbs.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.SparseArray;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.iyuelbs.R;
import com.iyuelbs.app.AppHelper;
import com.iyuelbs.app.Keys;

/**
 * Created by Bob Peng on 2015/1/25.
 */
public class ViewUtils {

    private static float mDensity = 0f;
    private static int mScreenWidth = 0;
    private static int mScreenHeight = 0;

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

    public static int getScreenHeight() {
        if (mScreenHeight == 0)
            mScreenHeight = AppHelper.getAppContext().getResources().getDisplayMetrics().heightPixels;
        return mScreenHeight;
    }

    /**
     * @return color
     */
    public static int getThemeColor(Resources res, int style) {
        switch (style) {
            case Keys.STYLE_COLOR_PRIMARY:
                return res.getColor(R.color.teal);
            case Keys.STYLE_COLOR_PRIMARY_DARK:
                return res.getColor(R.color.teal_dark);
            case Keys.STYLE_COLOR_ACCENT:
                return res.getColor(R.color.pink);
            default:
                return 0;
        }
    }

    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void showToast(Context context, int stringId) {
        showToast(context, context.getString(stringId));
    }

    public static MaterialDialog createLoadingDialog(Context context, String message) {
        View view = View.inflate(context, R.layout.dialog_loading_view, null);
        if (message != null) {
            TextView msgText = (TextView) view.findViewById(R.id.dialog_loading_msg);
            msgText.setText(message);
        }

        return new MaterialDialog.Builder(context)
                .customView(view, false)
                .cancelable(false)
                .show();
    }

    public static void setDrawableTop(TextView textView, int id) {
        Drawable drawable = getCompoundDrawable(textView.getContext(), id);
        textView.setCompoundDrawables(null, drawable, null, null);
    }

    public static void setDrawableTop(TextView textView, int id, int xDp, int yDp) {
        Drawable drawable = getCompoundDrawable(textView.getContext(), id, xDp, yDp);
        textView.setCompoundDrawables(null, drawable, null, null);
    }

    public static Drawable getCompoundDrawable(Context context, int id) {
        Drawable drawable = context.getResources().getDrawable(id);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        return drawable;
    }

    public static Drawable getCompoundDrawable(Context context, int id, int xDp, int yDp) {
        Drawable drawable = context.getResources().getDrawable(id);
        drawable.setBounds(0, 0, getPixels(xDp), getPixels(yDp));
        return drawable;
    }
}
