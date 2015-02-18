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
import com.iyuelbs.ui.main.MainActivity;
import com.iyuelbs.utils.ViewUtils;

/**
 * Created by Bob Peng on 2015/2/10.
 */
public class RegQuickSettings extends PreferenceFragment {

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
        alertText.setText(getString(R.string.title_quick_settings));
        int padding = ViewUtils.getPixels(8);
        alertText.setPadding(padding, padding, padding, padding);

        View view = super.onCreateView(inflater, container, savedInstanceState);
        linearLayout.addView(alertText);
        linearLayout.addView(view);

        return linearLayout;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_register, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_next) {
            Intent intent = new Intent(mContext, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            getActivity().finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
