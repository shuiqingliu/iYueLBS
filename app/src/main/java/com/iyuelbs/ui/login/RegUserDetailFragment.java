package com.iyuelbs.ui.login;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.iyuelbs.BaseFragment;
import com.iyuelbs.R;
import com.iyuelbs.app.AppHelper;
import com.iyuelbs.app.Keys;
import com.iyuelbs.event.DialogEvent;
import com.iyuelbs.external.RoundedImageView;
import com.iyuelbs.utils.Utils;
import com.iyuelbs.utils.ViewUtils;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.soundcloud.android.crop.Crop;

import java.io.File;

/**
 * Created by Bob Peng on 2015/2/6.
 */
public class RegUserDetailFragment extends BaseFragment implements View.OnClickListener {

    private RoundedImageView mAvatarView;
    private EditText mNickNameText;
    private TextView mSexText;
    private Spinner mSexSpinner;
    private MaterialEditText mIntroduceText;

    private String mAvatarUri;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(R.string.title_user_detail);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.reg_detail, container, false);
        mAvatarView = (RoundedImageView) view.findViewById(R.id.reg_detail_avatar_preview);
        mNickNameText = (EditText) view.findViewById(R.id.reg_detail_nickname);
        mIntroduceText = (MaterialEditText) view.findViewById(R.id.reg_detail_introduce);
        mSexText = (TextView) view.findViewById(R.id.reg_detail_sex_text);
        mSexSpinner = (Spinner) view.findViewById(R.id.reg_detail_sex_spinner);
        RelativeLayout sexSelector = (RelativeLayout) view.findViewById(R.id.reg_detail_sex_layout);
        FrameLayout avatarLayout = (FrameLayout) view.findViewById(R.id.reg_detail_avatar_layout);

        sexSelector.setOnClickListener(this);
        avatarLayout.setOnClickListener(this);

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
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_next) {
            if (checkField()) {
                AppHelper.postEvent(new DialogEvent());
                if (mAvatarUri != null) {
//                    BmobProFile.getInstance(mContext).upload(mAvatarUri, new AvatarUploadListener());
                } else {
                    onFinishUpdateUserDetails();
                }
            }
        }
        return super.onOptionsItemSelected(item);
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

    private boolean checkField() {
        boolean valid = true;
        if (TextUtils.isEmpty(mNickNameText.getText())) {
            ViewUtils.showToast(mContext, R.string.msg_nickname_required);
            valid = false;
        }
        return valid;
    }

    private void onFinishUpdateUserDetails() {
        // TODO 修复用户信息不更新的问题
//        User user = new User();
//        user.setAvatarUrl(mAvatarUri);
//        user.setNickName(mNickNameText.getText().toString());
//        user.setIsMale(mSexSpinner.getSelectedItemPosition() == 0);
//        user.setIntroduce(mIntroduceText.getText().toString());
//
//        user.update(mContext, AppHelper.getUpdatedUser().getObjectId(), new UpdateListener() {
//            @Override
//            public void onSuccess() {
//                if (mDialog != null) {
//                    mDialog.dismiss();
//                }
//
//                AppHelper.getUpdatedUser(); // force to refresh user cache
//
//                Intent intent = new Intent(mContext, SettingsActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                intent.putExtra(Keys.EXTRA_OPEN_TYPE, Keys.OPEN_QUICK_SETTINGS);
//                startActivity(intent);
//                getActivity().finish();
//            }
//
//            @Override
//            public void onFailure(int i, String s) {
//                if (mDialog != null) {
//                    mDialog.dismiss();
//                }
//
//                AVUtils.onFailure(mContext, e);
//            }
//        });
    }

//    private class AvatarUploadListener implements UploadListener {
//
//        @Override
//        public void onSuccess(String filename, String url) {
//            mAvatarUri = AppHelper.signAvatar(mContext, filename, url);
//            onFinishUpdateUserDetails();
//        }
//
//        @Override
//        public void onProgress(int i) {
//        }
//
//        @Override
//        public void onError(int i, String s) {
//            if (mLoadingDialog != null) {
//                mLoadingDialog.dismiss();
//            }
//
//            AVUtils.onFailure(mContext, , i);
//        }
//    }
}