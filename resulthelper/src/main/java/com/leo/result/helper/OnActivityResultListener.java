package com.leo.result.helper;

import android.content.Intent;

/**
 * @Author : xinlong.lv
 * @Date : 2019/10/22 13:44
 * @Desc : Activity 跳转结果回调监听器 Interface definition for a callback to be invoked when a activity is set result.
 */
public interface OnActivityResultListener {

    void onResult(int resultCode, Intent data);
}
