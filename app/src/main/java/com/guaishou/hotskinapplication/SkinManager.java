package com.guaishou.hotskinapplication;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import java.lang.reflect.Method;

/**
 * 作用是实现从提供资源的skin.apk中获取资源文件
 */

public class SkinManager {
    private static SkinManager skinManager = new SkinManager();
    private Context context;
    //皮肤插件APK的资源对象
    private Resources resources;
    private String skinPackageName;

    private SkinManager(){
    }
    public void setContext(Context context){
        this.context = context;
    }

    public static SkinManager getInstance() {
        return skinManager;
    }

    //根据皮肤apk的路径去获取到它的资源对象
    public void loadSkinApk(@NonNull String path){
        //获取包管理器
        PackageManager packageManager = context.getPackageManager();
        //获取皮肤APK的包信息类
        PackageInfo packageInfo = packageManager.getPackageArchiveInfo(
            path,PackageManager.GET_ACTIVITIES);
        if(null==packageInfo){
            Toast.makeText(context,"没有找到皮肤包！\n请把皮肤apk包(skin.apk)放到sdcard根目录下",Toast.LENGTH_SHORT).show();
            return;
        }
        //获取到皮肤apk的包名
        skinPackageName = packageInfo.packageName;
        try {
            AssetManager assetManager = AssetManager.class.newInstance();
            Method addAssetPath = assetManager.getClass()
                    .getMethod("addAssetPath",String.class);
            addAssetPath.invoke(assetManager,path);
            resources = new Resources(assetManager,context.getResources().getDisplayMetrics()
            ,context.getResources().getConfiguration());
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
    //根据传进来的id 如果有类型和名字一样的就返回
    public int getColor(int id){
        if (resources == null){
            return id;
        }

        //获取属性值的名字 colorPrimary
        String entryName = context.getResources().getResourceEntryName(id);
        //获取到属性值的类型 color
        String typeName = context.getResources().getResourceTypeName(id);
        //通过名字和类型匹配的资源对象中的ID
        //获取的是资源id
        int iden = resources.getIdentifier(entryName,typeName,skinPackageName);
        if (iden==0){
            return id;
        }
        return resources.getColor(iden);
    }
    public Drawable getDrawable(int id){
        if(resources == null){
            return ContextCompat.getDrawable(context,id);
        }
        //获取到资源id的类型
        String resourceTypeName = context.getResources().getResourceTypeName(id);
        //获取到就是资源id的名字
        String resourceEntryName = context.getResources().getResourceEntryName(id);

        //就是colorAccent这个资源外外置apk中的id
        int identifier = resources.getIdentifier(resourceEntryName, resourceTypeName, skinPackageName);
        if(identifier == 0){
            return ContextCompat.getDrawable(context,id);
        }
        return resources.getDrawable(identifier);
    }
}
