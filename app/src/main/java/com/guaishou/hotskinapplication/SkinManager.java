package com.guaishou.hotskinapplication;

import android.app.Application;
import android.content.Context;
import android.provider.ContactsContract;

public class SkinManager {
    private static SkinManager skinManager = new SkinManager();
    private Context context;

    private SkinManager(){
    }
    private void init(Context context){
        this.context = context;
    }
    public static SkinManager getInstance() {
        return skinManager;
    }
    public void loadSkinApk(String path){

    }
    //根据传进来的id 如果有类型和名字一样的就返回
    public int getColor(int id){
        if (resources == null){
            return id;
        }
        String entryName = context.getResources().getResourceEntryName(id);
        String typeName = context.getResources().getResourceTypeName(id);
        int iden = resouces.getId(entryName,typeName,skinPackageName);
        if (iden==0){
            return id;
        }
        return resources.getColor(iden);
    }
}
