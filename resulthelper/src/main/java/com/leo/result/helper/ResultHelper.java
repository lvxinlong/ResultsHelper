package com.leo.result.helper;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

/**
 * @Author : xinlong.lv
 * @Date : 2019/10/23 09:14
 * @Desc : ResultHelper 工具操作类
 */
public class ResultHelper {

    public static Builder with(FragmentActivity activity) {
        return new Builder(activity);
    }

    public static class Builder {
        private FragmentActivity activity;

        Builder(FragmentActivity activity) {
            this.activity = activity;
        }

        public void requestPermissions(String[] permissions, OnPermissionResultListener listener) {
            getFragment(activity).checkPermissions(permissions, listener);
        }

        public void startForResult(Class clz, OnActivityResultListener listener) {
            getFragment(activity).startActForResult(new Intent(activity, clz), listener);
        }

        public void startForResult(Intent intent, OnActivityResultListener listener) {
            getFragment(activity).startActForResult(intent, listener);
        }

        public void startForResult(Class clz, Intent data, OnActivityResultListener listener) {
            Intent intent = new Intent(activity, clz);
            intent.putExtras(data);
            getFragment(activity).startActForResult(intent, listener);
        }

        public void startForResult(Intent intent, Intent data, OnActivityResultListener listener) {
            intent.putExtras(data);
            getFragment(activity).startActForResult(intent, listener);
        }

        public void startForResult(Class clz, Bundle bundle, OnActivityResultListener listener) {
            Intent intent = new Intent(activity, clz);
            intent.putExtras(bundle);
            getFragment(activity).startActForResult(intent, listener);
        }

        public void startForResult(Intent intent, Bundle bundle, OnActivityResultListener listener) {
            intent.putExtras(bundle);
            getFragment(activity).startActForResult(intent, listener);
        }

        private ForResultFragment getFragment(FragmentActivity activity) {
            String tag = "ForResultFragment";
            FragmentManager fragmentManager = activity.getSupportFragmentManager();
            Fragment fragment = fragmentManager.findFragmentByTag(tag);
            if (fragment == null) {
                fragment = new ForResultFragment();
                fragmentManager.beginTransaction().add(fragment, tag).commitAllowingStateLoss();
                fragmentManager.executePendingTransactions();
            }
            return (ForResultFragment) fragment;
        }

    }

}
