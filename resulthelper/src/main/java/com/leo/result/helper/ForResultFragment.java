package com.leo.result.helper;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.SparseArray;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

/**
 * @Author : xinlong.lv
 * @Date : 2019/10/22 15:06
 * @Desc : 无界面 Fragment 操作类
 */
public class ForResultFragment extends Fragment {

    private SparseArray<OnActivityResultListener> actResultListeners = new SparseArray<>();
    private SparseArray<OnPermissionResultListener> permissionListeners = new SparseArray<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * 跳转打开 Activity
     *
     * @param intent   意图
     * @param listener 结果回调 监听器
     */
    public void startActForResult(Intent intent, OnActivityResultListener listener) {
        int requestCode = actResultListeners.size() + 1;
        actResultListeners.put(requestCode, listener);
        startActivityForResult(intent, requestCode);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (actResultListeners.size() > 0) {
            OnActivityResultListener listener = actResultListeners.get(requestCode);
            if (listener != null && data != null) {
                listener.onResult(resultCode, data);
            }
            actResultListeners.remove(requestCode);
        }
    }


    /**
     * 是否所有权限都已获取授权
     *
     * @param permissions
     * @return
     */
    private boolean isAllGranted(String[] permissions) {
        ArrayList<Integer> checkResult = new ArrayList<>();
        for (String permission : permissions) {
            checkResult.add(ActivityCompat.checkSelfPermission(getActivity(), permission));
        }
        return !checkResult.contains(PackageManager.PERMISSION_DENIED);
    }


    /**
     * 检查权限
     *
     * @param permissions
     * @param listener
     */
    public void checkPermissions(String[] permissions, OnPermissionResultListener listener) {
        if (isAllGranted(permissions)) {
            listener.onResult(true);
            return;
        }
        int requestCode = permissionListeners.size() + 1;
        permissionListeners.put(requestCode, listener);
        requestPermissions(permissions, requestCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        OnPermissionResultListener listener = permissionListeners.get(requestCode);
        permissionListeners.remove(requestCode);
        boolean isGrant = true;
        for (int grantResult : grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                isGrant = false;
            }
        }
        listener.onResult(isGrant);
        permissionListeners.remove(requestCode);
    }
}
