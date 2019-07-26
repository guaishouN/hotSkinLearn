package com.guaishou.hotskinapplication;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

public class SkinFactory implements LayoutInflater.Factory2 {
    //装载需要焕肤的控件
    List<SkinItem> itemList = new ArrayList<>();
    /**
     * Version of {@link #onCreateView(String, Context, AttributeSet)}
     * that also supplies the parent that the view created view will be
     * placed in.
     *
     * @param parent  The parent that the created view will be placed
     *                in; <em>note that this may be null</em>.
     * @param name    Tag name to be inflated.
     * @param context The context the view is being created in.
     * @param attrs   Inflation attributes as specified in XML file.
     * @return View Newly created view. Return null for the default
     * behavior.
     */
    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        //监听xml的生成过程 自己去创建这些控件
        View view = null;
        String[] strings = {        "android.widget","android.view", "android.webkit"};

        if(name.contains(".")){
            view = onCreateView(name,context,attrs);
        }else{
            for(String string:strings){
               view = onCreateView(string+name,context,attrs);
               if (view!=null){
                   break;
               }
            }
        }

        //收集所需要的换肤控件
        if(view!=null){
            parseView(view,name,attrs);
        }
        return null;
    }

    private void parseView(View view, String name, AttributeSet attrs) {
        List<SkinItem> itemList = new ArrayList<>();
        for (int i=0;i<attrs.getAttributeCount();i++){
            String attrName = attrs.getAttributeName(i);
            String attrValue = attrs.getAttributeValue(i);
            if (attrName.contains("background") ){
                int resId=Integer.parseInt(attrValue.substring(0));
                String typeName = view.getResources().getResourceTypeName(resId);
                String entryName = view.getResources().getResourceEntryName(resId);
                SkinItem skinItem = new SkinItem(attrName,resId,entryName,typeName);
                itemList.add(skinItem);
            }

            if (itemList.size()>0){
                SkinView
            }
        }
    }

    /**
     * Hook you can supply that is called when inflating from a LayoutInflater.
     * You can use this to customize the tag names available in your XML
     * layout files.
     *
     * <p>
     * Note that it is good practice to prefix these custom names with your
     * package (i.e., com.coolcompany.apps) to avoid conflicts with system
     * names.
     *
     * @param name    Tag name to be inflated.
     * @param context The context the view is being created in.
     * @param attrs   Inflation attributes as specified in XML file.
     * @return View Newly created view. Return null for the default
     * behavior.
     */
    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        View view =null;
        try {
            //这个name的class对象
            Class aClass = context.getClassLoader().loadClass(name);
            Constructor<? extends View> constructor = aClass.getConstructor(
                    new Class[]{Context.class,AttributeSet.class}
            );
            view = constructor.newInstance(context,attrs);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    class SkinItem{
        String name;//background
        int resId;//
        String entryName;
        String typeName;

        public SkinItem(String attrName, int resId, String entryName, String typeName) {
            name = attrName;
            this.resId = resId;
            this.entryName = entryName;
            this.typeName = typeName;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getResId() {
            return resId;
        }

        public void setResId(int resId) {
            this.resId = resId;
        }

        public String getEntryName() {
            return entryName;
        }

        public void setEntryName(String entryName) {
            this.entryName = entryName;
        }

        public String getTypeName() {
            return typeName;
        }

        public void setTypeName(String typeName) {
            this.typeName = typeName;
        }
    }

    public void apply(){
        for (SkinItem item:itemList){

        }
    }

}
