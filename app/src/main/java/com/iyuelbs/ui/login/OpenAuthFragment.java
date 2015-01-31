package com.iyuelbs.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.iyuelbs.BaseFragment;
import com.iyuelbs.R;
import com.iyuelbs.app.AppConfig;
import com.iyuelbs.app.AppHelper;
import com.iyuelbs.app.Keys;
import com.iyuelbs.utils.BaseErrorListener;

import org.json.JSONException;
import org.json.JSONObject;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by Bob Peng on 2015/1/31.
 */
public class OpenAuthFragment extends BaseFragment {

    private static final String WEIBO_AUTHORIZE_URL =
            "https://api.weibo.com/oauth2/authorize?client_id=" + AppConfig.WEIBO_KEY
                    + "&display=mobile&response_type=code&redirect_uri=" + AppConfig.REDIRECT_URL;
    private static final String WEIBO_AT_URL =
            "https://api.weibo.com/oauth2/access_token?client_id=" + AppConfig.WEIBO_KEY
                    + "&client_secret=" + AppConfig.WEIBO_SECRET + "&grant_type" +
                    "=authorization_code&code=%s&redirect_uri=" + AppConfig.REDIRECT_URL;

    private WebView mWebView;
    private ProgressBar mProgressBar;

    private RequestQueue mRequestQueue;

    private int mAuthType = 0;

    public static OpenAuthFragment getInstance(Bundle data) {
        OpenAuthFragment fragment = new OpenAuthFragment();
        fragment.setArguments(data);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_auth, container, false);
        mWebView = (WebView) view.findViewById(R.id.auth_webview);
        mProgressBar = (ProgressBar) view.findViewById(R.id.auth_progress);

        mProgressBar.setTag(0);
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true); // 设置js支持
        settings.setAppCachePath(AppHelper.getCacheDirPath()); // 设置缓存路径
        settings.setAppCacheEnabled(true); // 启用缓存

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
                    OpenAuthFragment.this.startActivity(sendIntent);
                    return true;
                } else if (url.startsWith(AppConfig.REDIRECT_URL + "?code=")) {
                    String code = url.replace(AppConfig.REDIRECT_URL + "?code=", "");
                    url = String.format(WEIBO_AT_URL, code);
                    JSONObject json = new JSONObject();
                    try {
                        json.put("client_id", AppConfig.WEIBO_KEY);
                        json.put("client_secret", AppConfig.WEIBO_SECRET);
                        json.put("grant_type", "authorization_code");
                        json.put("code", code);
                        json.put("redirect_uri", AppConfig.REDIRECT_URL);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    JsonObjectRequest request = new JsonObjectRequest(url, json,
                            new Response.Listener<JSONObject>() {
                                public void onResponse(JSONObject response) {
                                    handleAuthResult(response);
                                }
                            }, new BaseErrorListener(mContext));
                    getRequestQueue().add(request);
                    return true;
                }
                return super.shouldOverrideUrlLoading(view, url);
            }
        });

//        BmobUser.weiboLogin(mContext,);
        mAuthType = getArguments().getInt(Keys.EXTRA_OPEN_TYPE, 0);
        if (mAuthType > 0) {
            String url = null;
            if (mAuthType == Keys.OPEN_WEIBO_AUTH) {
                url = WEIBO_AUTHORIZE_URL;
            } else if (mAuthType == Keys.OPEN_QQ_AUTH) {
                // TODO
            }
            mWebView.loadUrl(url);
        }
        return view;
    }

    private void handleAuthResult(JSONObject response) {
        JSONObject userAuth = new JSONObject();
        try {
            userAuth.put("weibo", response);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        BmobUser.associateWithAuthDate(mContext, BmobUser.getCurrentUser(mContext), userAuth,
                new UpdateListener() {
                    @Override
                    public void onSuccess() {
                        Log.e("xifan", "onSuccess");
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        Log.e("xifan", s);
                    }
                });
        getActivity().setResult(-1);
    }

    private RequestQueue getRequestQueue() {
        if (mRequestQueue == null)
            mRequestQueue = Volley.newRequestQueue(mContext);
        return mRequestQueue;
    }
}
