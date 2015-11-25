package com.iyuelbs.ui.chat.ui.profile;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SaveCallback;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.avoscloud.leanchatlib.controller.ChatManager;
import com.avoscloud.leanchatlib.db.DBHelper;
import com.iyuelbs.R;
import com.iyuelbs.app.AppHelper;
import com.iyuelbs.ui.chat.service.UserService;
import com.iyuelbs.ui.chat.ui.base_activity.BaseFragment;
import com.iyuelbs.ui.chat.util.Logger;
import com.iyuelbs.ui.chat.util.PathUtils;
import com.iyuelbs.ui.chat.util.PhotoUtils;
import com.iyuelbs.ui.chat.util.SimpleNetTask;
import com.iyuelbs.ui.chat.util.Utils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by lzw on 14-9-17.
 */
public class ProfileFragment extends BaseFragment implements View.OnClickListener {
  private static final int IMAGE_PICK_REQUEST = 1;
  private static final int CROP_REQUEST = 2;
  TextView usernameView, genderView;
  ImageView avatarView;
  View usernameLayout, avatarLayout, logoutLayout,
      genderLayout, notifyLayout, updateLayout;
  ChatManager chatManager;
  SaveCallback saveCallback = new SaveCallback() {
    @Override
    public void done(AVException e) {
      refresh();
    }
  };

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    return inflater.inflate(R.layout.profile_fragment, container, false);
  }

  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    headerLayout.showTitle(R.string.profile_title);
    chatManager = ChatManager.getInstance();
    findView();
    refresh();
  }

  private void refresh() {
    AVUser curUser = AVUser.getCurrentUser();
    usernameView.setText(curUser.getUsername());
    genderView.setText(AppHelper.getCurrentUser().isMale()? "男" : "女");
    UserService.displayAvatar(curUser, avatarView);
  }

  private void findView() {
    View fragmentView = getView();
    usernameView = (TextView) fragmentView.findViewById(R.id.username);
    avatarView = (ImageView) fragmentView.findViewById(R.id.avatar);
    usernameLayout = fragmentView.findViewById(R.id.usernameLayout);
    avatarLayout = fragmentView.findViewById(R.id.avatarLayout);
    logoutLayout = fragmentView.findViewById(R.id.logoutLayout);
    genderLayout = fragmentView.findViewById(R.id.sexLayout);
    notifyLayout = fragmentView.findViewById(R.id.notifyLayout);
    genderView = (TextView) fragmentView.findViewById(R.id.sex);

    genderView.setText(AppHelper.getCurrentUser().isMale()? "男" : "女");
    avatarLayout.setOnClickListener(this);
    logoutLayout.setOnClickListener(this);
    notifyLayout.setOnClickListener(this);
  }

  @Override
  public void onClick(View v) {
    int id = v.getId();
    if (id == R.id.avatarLayout) {
      Intent intent = new Intent(Intent.ACTION_PICK, null);
      intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
      startActivityForResult(intent, IMAGE_PICK_REQUEST);
    } else if (id == R.id.logoutLayout) {
      DBHelper.getCurrentUserInstance().closeHelper();
      chatManager.closeWithCallback(new AVIMClientCallback() {
        @Override
        public void done(AVIMClient avimClient, AVIMException e) {

        }
      });
      AVUser.logOut();
      getActivity().setResult(Activity.RESULT_OK);
      getActivity().finish();
    } else if (id == R.id.notifyLayout) {
      Utils.goActivity(ctx, ProfileNotifySettingActivity.class);
    }
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    Logger.d("on Activity result " + requestCode + " " + resultCode);
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == Activity.RESULT_OK) {
      if (requestCode == IMAGE_PICK_REQUEST) {
        Uri uri = data.getData();
        startImageCrop(uri, 200, 200, CROP_REQUEST);
      } else if (requestCode == CROP_REQUEST) {
        final String path = saveCropAvatar(data);
        new SimpleNetTask(ctx) {
          @Override
          protected void doInBack() throws Exception {
            UserService.saveAvatar(path);
          }
          @Override
          protected void onSucceed() {
            refresh();
          }
        }.execute();

      }
    }
  }

  public Uri startImageCrop(Uri uri, int outputX, int outputY,
                            int requestCode) {
    Intent intent = null;
    intent = new Intent("com.android.camera.action.CROP");
    intent.setDataAndType(uri, "image/*");
    intent.putExtra("crop", "true");
    intent.putExtra("aspectX", 1);
    intent.putExtra("aspectY", 1);
    intent.putExtra("outputX", outputX);
    intent.putExtra("outputY", outputY);
    intent.putExtra("scale", true);
    String outputPath = PathUtils.getAvatarTmpPath();
    Uri outputUri = Uri.fromFile(new File(outputPath));
    intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);
    intent.putExtra("return-data", true);
    intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
    intent.putExtra("noFaceDetection", false); // face detection
    startActivityForResult(intent, requestCode);
    return outputUri;
  }

  private String saveCropAvatar(Intent data) {
    Bundle extras = data.getExtras();
    String path = null;
    if (extras != null) {
      Bitmap bitmap = extras.getParcelable("data");
      if (bitmap != null) {
        bitmap = PhotoUtils.toRoundCorner(bitmap, 10);
        String filename = new SimpleDateFormat("yyMMddHHmmss")
            .format(new Date());
        path = PathUtils.getAvatarDir() + filename;
        Logger.d("save bitmap to " + path);
        PhotoUtils.saveBitmap(PathUtils.getAvatarDir(), filename,
            bitmap, true);
        if (bitmap != null && bitmap.isRecycled() == false) {
          //bitmap.recycle();
        }
      }
    }
    return path;
  }
}
