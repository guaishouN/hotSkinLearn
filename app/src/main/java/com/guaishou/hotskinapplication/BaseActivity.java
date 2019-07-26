package com.guaishou.hotskinapplication;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v7.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {
    private SkinFactory skinFactory;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        skinFactory = new SkinFactory();
        //监听xml的生成过程
        LayoutInflaterCompat.setFactory2(getLayoutInflater(),skinFactory);
    }

    public void apply(){
        skinFactory.apply();
    }
}
