package com.iyuelbs.ui.login;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.SaveCallback;
import com.iyuelbs.BaseFragment;
import com.iyuelbs.R;
import com.iyuelbs.app.AppHelper;
import com.iyuelbs.app.Keys;
import com.iyuelbs.entity.User;
import com.iyuelbs.support.utils.AVUtils;
import com.iyuelbs.support.utils.Utils;
import com.iyuelbs.support.utils.ViewUtils;
import com.iyuelbs.support.widget.RoundedImageView;
import com.iyuelbs.ui.settings.SettingsActivity;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.soundcloud.android.crop.Crop;

import java.io.File;
import java.io.IOException;

/**
 * Created by Bob Peng on 2015/2/6.
 */
public class RegisterUserDetail extends BaseFragment implements View.OnClickListener {

    private RoundedImageView mAvatarView;
    private EditText mNickNameText;
    private TextView mSexText;
    private Spinner mSexSpinner;
    private MaterialEditText mSignatureText;

    private String mAvatarUri;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.reg_detail, container, false);
        mAvatarView = (RoundedImageView) view.findViewById(R.id.reg_detail_avatar_preview);
        mNickNameText = (EditText) view.findViewById(R.id.reg_detail_nickname);
        mSignatureText = (MaterialEditText) view.findViewById(R.id.reg_detail_signature);
        mSexText = (TextView)       view.findViewById(R.id.reg_detail_sex_text);
        mSexSpinner = (Spinner) view.findViewById(R.id.reg_detail_sex_spinner);
        RelativeLayout sexSelector = (RelativeLayout) view.findViewById(R.id.reg_detail_sex_layout);
        FrameLayout avatarLayout = (FrameLayout) view.findViewById(R.id.reg_detail_avatar_layout);

        mSignatureText.setLines(3);
        mSignatureText.setHorizontallyScrolling(false);

        sexSelector.setOnClickListener(this);
        avatarLayout.setOnClickListener(this);

        mSignatureText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    onFinishUpdateUserDetails();
                    return true;
                }
                return false;
            }
        });

        mSexSpinner.setAdapter(new ArrayAdapter<>(mContext, R.layout.spinner_dropdown_item,
                getResources().getStringArray(R.array.dropdown_sex)));
        mSexSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String sex = mSexSpinner.getAdapter().getItem(position).toString();
                mSexText.setText(sex);
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(R.string.title_user_detail);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_next) {
            if (checkField()) {
                showDialog();
                if (mAvatarUri != null) {
                    try {
                        AVFile avatarFile = AVFile.withAbsoluteLocalPath("avatar.jpg", mAvatarUri);
                        avatarFile.saveInBackground(new AvatarUploadCallback(avatarFile));
                    } catch (IOException e) {
                        e.printStackTrace();
                        ViewUtils.showToast(mContext, R.string.msg_invalid_uri);
                    }
                } else {
                    onFinishUpdateUserDetails();
                }
            }
        }
        return super.onOptionsItemSelected(item);
    }


    private boolean checkField() {
        boolean valid = true;
        if (TextUtils.isEmpty(mNickNameText.getText())) {
            ViewUtils.showToast(mContext, R.string.msg_nickname_required);
            valid = false;
        }
        return valid;
    }

    private void onFinishUpdateUserDetails() {
        User user = AppHelper.getUpdatedUser();
        user.setNickName(mNickNameText.getText().toString());
        user.setIsMale(mSexSpinner.getSelectedItemPosition() == 0);
        user.setSignature(mSignatureText.getText().toString());
        if (mAvatarUri == null) {
            user.setAvatar(null);
        }
        user.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                dismissDialog();
                if (e == null) {
                    AppHelper.getUpdatedUser(); // force to refresh user cache
                    Intent intent = new Intent(mContext, SettingsActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra(Keys.EXTRA_OPEN_TYPE, Keys.OPEN_QUICK_SETTINGS);
                    startActivity(intent);
                    getActivity().finish();
                } else {
                    AVUtils.onFailure(mContext, e);
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == -1) {
            if (requestCode == Crop.REQUEST_PICK) {
                File tmpFile = Utils.generateTempFile(Keys.FILE_TMP_AVATAR, ".jpg");
                mAvatarUri = tmpFile.getPath();
                new Crop(data.getData()).asSquare().withMaxSize(300, 300).output(Uri.fromFile(tmpFile))
                        .start(getActivity());
            } else if (requestCode == Crop.REQUEST_CROP) {
                mAvatarView.setImageURI(Crop.getOutput(data));
            }
        } else {
            if (requestCode == Crop.REQUEST_PICK || requestCode == Crop.REQUEST_CROP) {
                mAvatarUri = null;
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.reg_detail_avatar_layout) {
            Crop.pickImage(getActivity());
        } else if (v.getId() == R.id.reg_detail_sex_layout) {
            mSexSpinner.performClick();
        }
    }

    private class AvatarUploadCallback extends SaveCallback {
        private AVFile avFile;

        private AvatarUploadCallback(AVFile avFile) {
            this.avFile = avFile;
        }

        @Override
        public void done(AVException e) {
            dismissDialog();
            if (e == null) {
                AppHelper.getUpdatedUser().setAvatar(avFile);
                onFinishUpdateUserDetails();
            } else {
                AVUtils.onFailure(mContext, e);
            }
        }
    }

}
