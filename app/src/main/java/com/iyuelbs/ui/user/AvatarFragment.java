package com.iyuelbs.ui.user;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.iyuelbs.BaseFragment;
import com.iyuelbs.R;
import com.iyuelbs.app.AppHelper;
import com.iyuelbs.utils.Utils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.soundcloud.android.crop.Crop;

import java.io.File;
import java.util.Random;

public class AvatarFragment extends BaseFragment implements View.OnClickListener {

    private ImageView mPreviewImage;
    private Button mUploadBtn;
    private File mTmpFile;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_avatar, container, false);
        mUploadBtn = (Button) view.findViewById(R.id.avatar_upload_btn);
        mPreviewImage = (ImageView) view.findViewById(R.id.avatar_preview);

        mUploadBtn.setOnClickListener(this);
        mPreviewImage.setOnClickListener(this);

        if (AppHelper.getCurrentUser().getAvatar() != null) {
            AppHelper.getImageLoader().displayImage(AppHelper.getCurrentUser().getAvatar().getUrl(),
                    mPreviewImage, DisplayImageOptions.createSimple());
        }
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == -1) {
            if (requestCode == Crop.REQUEST_PICK) {
                new Crop(data.getData())
                        .asSquare()
                        .withMaxSize(200, 200)
                        .output(Uri.fromFile(getTmpAvatarFile()))
                        .start(getActivity());
            } else if (requestCode == Crop.REQUEST_CROP) {
                mPreviewImage.setImageURI(Crop.getOutput(data));
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v == mPreviewImage) {
            Crop.pickImage(getActivity(), Crop.REQUEST_PICK);
        } else if (v == mUploadBtn) {
//            BmobProFile.getInstance(mContext).upload(getTmpAvatarFile().getPath(), new UploadListener() {
//                @Override
//                public void onSuccess(String filename, String url) {
//                    User user = AppHelper.getCurrentUser();
//                    if (user == null) {
//                        // TODO login
//                    } else {
//                        String signedUrl = AppHelper.signAvatar(mContext, filename, url);
//                        Log.e("xifan", "signed Url " + signedUrl);
//                        user.setAvatarUrl(signedUrl);
//                        user.update(mContext, new UpdateListener() {
//                            @Override
//                            public void onSuccess() {
//                                Log.e("xifan", "onSuccess");
//                            }
//
//                            @Override
//                            public void onFailure(int i, String s) {
//                                Log.e("xifan", s);
//                            }
//                        });
//                        getTmpAvatarFile().delete();
//                    }
//                }
//
//                @Override
//                public void onProgress(int i) {
//                    Log.e("xifan", "onProgress, " + i);
//                }
//
//                @Override
//                public void onError(int i, String s) {
//                    Log.e("xifan", "onError, " + i + " ," + s);
//                }
//            });
        }
    }

    private File getTmpAvatarFile() {
        if (mTmpFile == null) {
            mTmpFile = new File(AppHelper.getCacheDirPath(), generateTmpFile());
            if (mTmpFile.exists()) {
                mTmpFile.delete();
                mTmpFile = new File(AppHelper.getCacheDirPath(), generateTmpFile());
            }
        }
        return mTmpFile;
    }

    private String generateTmpFile() {
        return "tmp_avatar" + new Random(Utils.getTimestamp()).nextInt(1000) + ".jpg";
    }
}
