package com.iyuelbs.ui.user;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.bmob.BmobProFile;
import com.bmob.btp.callback.UploadListener;
import com.iyuelbs.BaseFragment;
import com.iyuelbs.R;

public class AvatarFragment extends BaseFragment implements View.OnClickListener {

    private ImageView mPreviewImage;
    private Button mUploadBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_avatar, container, false);
        mUploadBtn = (Button) view.findViewById(R.id.avatar_upload_btn);
        mPreviewImage = (ImageView) view.findViewById(R.id.avatar_preview);

        mUploadBtn.setOnClickListener(this);
        mPreviewImage.setOnClickListener(this);
        return view;
    }


    @Override
    public void onClick(View v) {
        if (v == mPreviewImage) {

        } else if (v == mUploadBtn) {
            BmobProFile.getInstance(mContext).upload("", new UploadListener() {
                @Override
                public void onSuccess(String s, String s2) {

                }

                @Override
                public void onProgress(int i) {

                }

                @Override
                public void onError(int i, String s) {

                }
            });
        }
    }
}
