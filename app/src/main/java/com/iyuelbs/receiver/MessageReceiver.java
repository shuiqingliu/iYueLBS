package com.iyuelbs.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


/**
 * Created by Bob Peng on 2015/1/20.
 */
public class MessageReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
//        if (intent.getAction().equals(PushConstants.ACTION_MESSAGE)) {
//            Log.d("bmob", "客户端收到推送内容：" + intent.getStringExtra(PushConstants.EXTRA_PUSH_MESSAGE_STRING));
//        }
    }
}
