package com.iyuelbs.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.LogInCallback;
import com.iyuelbs.BaseFragment;
import com.iyuelbs.R;
import com.iyuelbs.app.AppConfig;
import com.iyuelbs.app.AppHelper;
import com.iyuelbs.app.Keys;
import com.iyuelbs.entity.User;
import com.iyuelbs.utils.AVUtils;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Bob Peng on 2015/2/20.
 */
public class WeiboAuthFragment extends BaseFragment {
    private static final String WEIBO_AUTHORIZE_URL =
            "https://api.weibo.com/oauth2/authorize?client_id=" + AppConfig.WEIBO_KEY
                    + "&display=mobile&response_type=code&redirect_uri=" + AppConfig.REDIRECT_URL;
    private static final String WEIBO_AT_URL =
            "https://api.weibo.com/oauth2/access_token?client_id=" + AppConfig.WEIBO_KEY
                    + "&client_secret=" + AppConfig.WEIBO_SECRET + "&grant_type" +
                    "=authorization_code&code=%s&redirect_uri=" + AppConfig.REDIRECT_URL;
    private static final String KEY_ACCESS_TOKEN = "access_token";
    private static final String KEY_EXPIRES_IN = "expires_in";
    private static final String KEY_UID = "uid";

    private WebView mWebView;
    private ProgressBar mProgressBar;
    private int mAuthType = 0;

    public static WeiboAuthFragment getInstance(Bundle data) {
        WeiboAuthFragment fragment = new WeiboAuthFragment();
        fragment.setArguments(data);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.weibo_auth, container, false);
        mWebView = (WebView) view.findViewById(R.id.auth_webview);
        mProgressBar = (ProgressBar) view.findViewById(R.id.auth_progress);
        mProgressBar.setTag(0);
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setAppCachePath(AppHelper.getCacheDirPath());
        settings.setAppCacheEnabled(true);
        mWebView.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                    if (mWebView.canGoBack()) {
                        mWebView.goBack();
                        return true;
                    }
                }
                return false;
            }
        });
        mWebView.setWebChromeClient(new WebChromeClient() {
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                getActivity().setTitle(title);
            }

            public void onProgressChanged(WebView view, int progress) {
                mProgressBar.setProgress(progress);
                if (progress == 0 || progress == 100) {
                    if (mProgressBar.getTag() == 1) {
                        mProgressBar.setVisibility(View.GONE);
                        mProgressBar.setTag(0);
                    }
                } else {
                    if (mProgressBar.getTag() == 0) {
                        mProgressBar.setVisibility(View.VISIBLE);
                        mProgressBar.setTag(1);
                    }
                }
            }
        });
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith("sms:")) {  //针对WebView里的短信注册流程，需要在此单独处理sms协议
                    Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                    sendIntent.putExtra("address", url.replace("sms:", ""));
                    sendIntent.setType("vnd.android-dir/mms-sms");
                    WeiboAuthFragment.this.startActivity(sendIntent);
                    return true;
                } else if (url.startsWith(AppConfig.REDIRECT_URL + "?code=")) {
                    // 处理成功回调
                    String code = url.replace(AppConfig.REDIRECT_URL + "?code=", "");
                    url = String.format(WEIBO_AT_URL, code);
                    RequestParams params = new RequestParams();
                    params.put("client_id", AppConfig.WEIBO_KEY);
                    params.put("client_secret", AppConfig.WEIBO_SECRET);
                    params.put("grant_type", "authorization_code");
                    params.put("code", code);
                    params.put("redirect_uri", AppConfig.REDIRECT_URL);
                    AppHelper.getHttpClient().post(url, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            handleAuthResult(response);
                        }
                    });
                    return true;
                }
                return super.shouldOverrideUrlLoading(view, url);
            }
        });

        String url = WEIBO_AUTHORIZE_URL;
        if (getArguments() != null) {
            mAuthType = getArguments().getInt(Keys.EXTRA_OPEN_TYPE, 0);
            if (mAuthType > 0) {
                if (mAuthType == Keys.OPEN_WEIBO_AUTH) {
                    url = WEIBO_AUTHORIZE_URL;
                } else if (mAuthType == Keys.OPEN_QQ_AUTH) {
                    // TODO
                }
            }
        }
        mWebView.loadUrl(url);
        return view;
    }

    private void handleAuthResult(JSONObject response) {
        if (response == null) {
            return;
        }

        try {
            User.AVThirdPartyUserAuth userAuth = new User.AVThirdPartyUserAuth(response.getString
                    (KEY_ACCESS_TOKEN), response.getString(KEY_EXPIRES_IN),
                    User.AVThirdPartyUserAuth.SNS_SINA_WEIBO, response.getString(KEY_UID));

            User.loginWithAuthData(User.class, userAuth, new LogInCallback<User>() {
                public void done(User user, AVException e) {
                    if (user != null) {
                        getActivity().setResult(-1);
                        getActivity().finish();
                    } else {
                        AVUtils.onFailure(mContext, e);
                    }
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
