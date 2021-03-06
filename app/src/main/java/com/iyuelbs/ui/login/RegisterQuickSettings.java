package com.iyuelbs.ui.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iyuelbs.R;
import com.iyuelbs.support.utils.NavUtils;
import com.iyuelbs.support.utils.Utils;
import com.iyuelbs.support.utils.ViewUtils;
import com.iyuelbs.ui.main.MainActivity;

/**
 * Created by Bob Peng on 2015/2/10.
 */
public class RegisterQuickSettings extends PreferenceFragment {

    private static final String KEY_SHOW_LOCATION_STATUS = "key_show_location_status";
    private static final String KEY_SHOW_STATUS_TO_ALL = "key_show_status_to_all";

    private Context mContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        setHasOptionsMenu(true);

        addPreferencesFromResource(R.xml.settings_quick);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LinearLayout linearLayout = new LinearLayout(mContext);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(-1, -1);
        linearLayout.setLayoutParams(params);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        TextView alertText = new TextView(mContext);
        alertText.setText(getString(R.string.title_quick_settings_summary));
        int padding = ViewUtils.getPixels(8);
        alertText.setPadding(padding, padding, padding, padding);

        View view = super.onCreateView(inflater, container, savedInstanceState);
        linearLayout.addView(alertText);
        linearLayout.addView(view);

        return linearLayout;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(R.string.title_quick_settings);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_register, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_next || Utils.onUpKeyClick(id)) {
            NavUtils.go(mContext, MainActivity.class, Intent.FLAG_ACTIVITY_CLEAR_TASK);
            getActivity().finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
