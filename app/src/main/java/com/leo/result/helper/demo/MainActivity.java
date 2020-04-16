package com.leo.result.helper.demo;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.leo.result.helper.OnActivityResultListener;
import com.leo.result.helper.ResultHelper;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentActivity;


/**
 * @Author : xinlong.lv
 * @Date : 2019/12/10 10:52
 * @Desc : 功能演示页面
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.context = this;

        Button btnRequestPermission = findViewById(R.id.btn_click_permission);
        Button btnStartResult = findViewById(R.id.btn_click_test_start_result);
        Button btnTaskePhoto = findViewById(R.id.btn_click_test_take_photo);

        btnRequestPermission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //申请相机权限
//                ResultHelper.with(MainActivity.this).requestPermissions(new String[]{Manifest.permission.CAMERA}, new OnPermissionResultListener() {
//                    @Override
//                    public void onResult(boolean isGrant) {
//                        if (isGrant) {
//                            //所申请的权限已经获取成功，可进行下一步操作
//                            Toast.makeText(context, "相机权限已经获取", Toast.LENGTH_LONG).show();
//                        } else {
//                            Toast.makeText(context, "权限获取失败，请去应用权限管理界面手动开启", Toast.LENGTH_LONG).show();
//                        }
//                    }
//                });

                //Java 8 lambda code
                ResultHelper.with(MainActivity.this).requestPermissions(new String[]{Manifest.permission.CAMERA}, isGrant -> {
                    if (isGrant) {
                        //所申请的权限已经获取成功，可进行下一步操作
                        Toast.makeText(context, "相机权限已经获取", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(context, "权限获取失败，请去应用权限管理界面手动开启", Toast.LENGTH_LONG).show();
                    }
                });

            }
        });

        btnStartResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //跳转页面并回传数据
//                ResultHelper.with(MainActivity.this).startForResult(TestActivity.class, new OnActivityResultListener() {
//                    @Override
//                    public void onResult(int resultCode, Intent data) {
//                        //处理测试页面回传的数据
//                        if (resultCode == Activity.RESULT_OK && data != null) {
//                            String resultStr = data.getStringExtra("testData");
//                            Toast.makeText(context, "返回的数据为：" + resultStr, Toast.LENGTH_LONG).show();
//                        }
//                    }
//                });

                //Java 8 lambda code
                ResultHelper.with(MainActivity.this).startForResult(TestActivity.class, (resultCode, data) -> {
                    //处理测试页面回传的数据
                    if (resultCode == Activity.RESULT_OK && data != null) {
                        String resultStr = data.getStringExtra("testData");
                        Toast.makeText(context, "返回的数据为：" + resultStr, Toast.LENGTH_LONG).show();
                    }
                });

                //数据用 Intent 包装传递
                Intent intent = new Intent();
                intent.putExtra("type", "1");
                ResultHelper.with(MainActivity.this).startForResult(TestActivity.class, intent, (resultCode, data) -> {
                    String resultStr = data.getStringExtra("testData");
                    Toast.makeText(context, "返回的数据为：" + resultStr, Toast.LENGTH_LONG).show();
                });

                //数据用 Bundle 包装传递
                Bundle bundle = new Bundle();
                bundle.putString("type", "2");
                ResultHelper.with(MainActivity.this).startForResult(TestActivity.class, bundle, (resultCode, data) -> {
                    String resultStr = data.getStringExtra("testData");
                    Toast.makeText(context, "返回的数据为：" + resultStr, Toast.LENGTH_LONG).show();
                });
            }
        });


        String ROOT_FILE_PATH = getExternalFilesDir("").getAbsolutePath();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
        String fileName = sdf.format(new Date()) + ".jpg";

        btnTaskePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto(MainActivity.this, new File(ROOT_FILE_PATH, fileName), (resultCode, data) -> {
                    if (resultCode == RESULT_OK) {
                        String filePath = new File(ROOT_FILE_PATH, fileName).getAbsolutePath();
                        Toast.makeText(MainActivity.this, "Path: "+filePath, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

    public static void takePhoto(Activity activity, File photoFile, OnActivityResultListener listener) {
        Log.i(TAG, "takePhoto: photoFile======" + photoFile.getAbsolutePath());
        if (!photoFile.getParentFile().exists()) {
            photoFile.getParentFile().mkdirs();
        }
        if (photoFile.exists()) {
            photoFile.delete();
        }
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        // 把文件地址转换成Uri格式
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //此处的 authority配置， 注意要和 AndroidManifest.xml 中的保持一致
            Uri uri = FileProvider.getUriForFile(activity, BuildConfig.APPLICATION_ID + ".fileProvider", photoFile);
            //添加这一句表示对目标应用临时授权该Uri所代表的文件
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        } else {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
        }

        ResultHelper.with((FragmentActivity) activity).startForResult(intent, listener);

    }

}
