package com.guaishou.hotskinapplication;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends BaseActivity {
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private final static int REQUEST_PERMISSION_CODE = 123;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkPermission();
    }

    /**
     * path要设置为存放skin包的路径
     * @param view 点击的view
     */
    public void changeSkin(View view){
        //真机
        //String path = Environment.getExternalStorageDirectory()+"/skin.apk";
        //虚拟机文件
        String path = "/sdcard/Download/skin.00";
        File file=new File(path);
        if (!file.exists()){
            Toast.makeText(this,"没有找到皮肤包 "+path+"！",Toast.LENGTH_SHORT).show();
            return;
        }
        //先加载皮肤APK
        SkinManager.getInstance().loadSkinApk(path);
        apply();
    }

    private boolean checkPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //用户已经拒绝过一次，再次弹出权限申请对话框需要给用户一个解释
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission
                    .WRITE_EXTERNAL_STORAGE)) {
                Toast.makeText(this, "请开通相关权限，否则无法正常使用本应用！", Toast.LENGTH_SHORT).show();
            }
            ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, REQUEST_PERMISSION_CODE);
            return false;
        }
        return true;
    }

}
