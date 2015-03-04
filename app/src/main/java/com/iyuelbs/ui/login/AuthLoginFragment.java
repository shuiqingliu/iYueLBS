package com.iyuelbs.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.*;
import android.widget.ProgressBar;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.LogInCallback;
import com.iyuelbs.BaseFragment;
import com.iyuelbs.R;
import com.iyuelbs.app.AppConfig;
import com.iyuelbs.app.AppHelper;
import com.iyuelbs.app.Keys;
import com.iyuelbs.entity.User;
import com.iyuelbs.support.utils.AVUtils;
import com.iyuelbs.support.utils.Utils;
import com.iyuelbs.support.utils.ViewUtils;
import com.loopj.android.http.JsonHttpResponseHandler;
import org.apache.http.Header;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by Bob Peng on 2015/2/20.
 */
public class AuthLoginFragment extends BaseFragment {
    private static final String WEIBO_AUTHORIZE_URL =
            "https://api.weibo.com/oauth2/authorize?client_id=" + AppConfig.WEIBO_ID
                    + "&display=mobile&response_type=code&redirect_uri=" + AppConfig.REDIRECT_WEIBO_URL;
    private static final String WEIBO_AT_URL =
            "https://api.weibo.com/oauth2/access_token?client_id=" + AppConfig.WEIBO_ID
                    + "&client_secret=" + AppConfig.WEIBO_SECRET + "&grant_type" +
                    "=authorization_code&code=%s&redirect_uri=" + AppConfig.REDIRECT_WEIBO_URL;
    private static final String QQ_BASE_URL =
            "https://graph.qq.com/oauth2.0/authorize";
    private static final String KEY_ACCESS_TOKEN = "access_token";
    private static final String KEY_EXPIRES_IN = "expires_in";
    private static final String KEY_UID = "uid";
    private static final String KEY_OPENID = "openid";

    private WebView mWebView;
    private ProgressBar mProgressBar;
    private int mAuthType = 0;

    public static AuthLoginFragment newInstance(Bundle data) {
        AuthLoginFragment fragment = new AuthLoginFragment();
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
                    AuthLoginFragment.this.startActivity(sendIntent);
                    return true;
                } else if (url.startsWith(AppConfig.REDIRECT_WEIBO_URL + "?code=")) {
                    onWeiboAuthSuccess(url);
                    return true;
                } else if (url.contains("access_token")) {
                    onQQAuthSuccess(url);
                    return true;
                }
                Log.e(":", url);
                return super.shouldOverrideUrlLoading(view, url);
            }
        });

        mWebView.clearCache(true);
        CookieManager.getInstance().removeSessionCookie();

        String url = WEIBO_AUTHORIZE_URL;
        if (getArguments() != null) {
            mAuthType = getArguments().getInt(Keys.EXTRA_OPEN_TYPE, 0);
            if (mAuthType > 0) {
                if (mAuthType == Keys.OPEN_WEIBO_AUTH) {
                    url = WEIBO_AUTHORIZE_URL;
                } else if (mAuthType == Keys.OPEN_QQ_AUTH) {
                    url = getQQAuthorizeUrl();
                }
            }
        }
        mWebView.loadUrl(url);
        return view;
    }

    private String getQQAuthorizeUrl() {
        String redirectUrl = AppConfig.REDIRECT_URL;
        try {
            redirectUrl = URLEncoder.encode(AppConfig.REDIRECT_URL, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return QQ_BASE_URL + "?&client_id=" + AppConfig.QQ_ID +
                "&display=mobile&response_type=token&state=iyuelbs&redirect_uri=" + redirectUrl;
    }

    private void onWeiboAuthSuccess(String url) {
        showDialog();

        String code = url.replace(AppConfig.REDIRECT_WEIBO_URL + "?code=", "");
        url = String.format(WEIBO_AT_URL, code);

        AppHelper.getHttpClient().post(url, new AuthResponseHandler());
    }

    private void onQQAuthSuccess(String url) {
        JSONObject json = Utils.getUrlParams(url);
        if (json.length() > 0) {
            onAuthSuccess(json);
        }
    }

    private void onAuthSuccess(JSONObject json) {
            User.AVThirdPartyUserAuth userAuth = new User.AVThirdPartyUserAuth(json.optString
                    (KEY_ACCESS_TOKEN), json.optString(KEY_EXPIRES_IN),
                    mAuthType == Keys.OPEN_WEIBO_AUTH ? "weibo" : "qq",
                    json.optString(KEY_UID, json.optString(KEY_OPENID)));

            User.loginWithAuthData(User.class, userAuth, new LogInCallback<User>() {
                public void done(User user, AVException e) {
                    if (user != null) {
                        getActivity().setResult(-1);
                    } else {
                        AVUtils.onFailure(mContext, e);
                    }
                    getActivity().finish();
                }
            });
    }

    private class AuthResponseHandler extends JsonHttpResponseHandler {
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            dismissDialog();
            onAuthSuccess(response);
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            super.onFailure(statusCode, headers, responseString, throwable);
            dismissDialog();
            ViewUtils.showToast(mContext, throwable.getMessage());
            getActivity().finish();
        }
    }
}
