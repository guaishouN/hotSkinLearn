package com.guaishou.hotskinapplication;

import android.os.Environment;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void changeSkin(View view){
        //先加载皮肤APK
        SkinManager.getInstance().loadSkinApk(
                Environment.getExternalStorageDirectory()
                +"/skin.apk");
        apply();
    }

}
