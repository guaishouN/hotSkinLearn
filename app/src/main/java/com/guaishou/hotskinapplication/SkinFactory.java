package com.guaishou.hotskinapplication;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

public class SkinFactory implements LayoutInflater.Factory2 {
    //装载需要焕肤的控件
    List<SkinView> viewList = new ArrayList<>();
    public final static String[] prefixList = {"android.widget","android.view", "android.webkit"};
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
        //区分这个控件是否是自定义的控件
        if(name.contains(".")){
            view = onCreateView(name,context,attrs);
        }else{
            for(String string: prefixList){
               view = onCreateView(string+name,context,attrs);
               if (view!=null){
                   break;
               }
            }
        }

        //收集所需要的换肤控件
        if(view!=null){
            //如果控件已经被实例化 就去判断这个控件是否满足我们的换肤要求，
            //然后收集起来
            parseView(view,name,attrs);
        }
        return view;
    }

    private void parseView(View view, String name, AttributeSet attrs) {
        List<SkinItem> itemList = new ArrayList<>();
        for (int i=0;i<attrs.getAttributeCount();i++){
            String attrName = attrs.getAttributeName(i);
            String attrValue = attrs.getAttributeValue(i);
            if (attrName.contains("background")
                || attrName.contains("textColor")
                || attrName.contains("src")
                || attrName.contains("color")){
                //获取资源id
                int resId=Integer.parseInt(attrValue.substring(1));
                //获取到属性的值的类型
                String typeName = view.getResources().getResourceTypeName(resId);
                //获取属性值的类型
                String entryName = view.getResources().getResourceEntryName(resId);
                SkinItem skinItem = new SkinItem(attrName,resId,entryName,typeName);
                itemList.add(skinItem);
            }
            //如果长度大于零，说明有我们要找的属性
            if (itemList.size()>0){
                SkinView skinView = new SkinView(view,itemList);
                viewList.add(skinView);
                skinView.apply();
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
        return view;
    }

    class SkinItem{
        String name;//属性名字background
        int resId;//属性id
        String entryName;//属性值的名字
        String typeName;//属性类型

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

    class SkinView{
        View view;
        List<SkinItem> list;

        public SkinView(View view, List<SkinItem> list) {
            this.view = view;
            this.list = list;
        }

        public View getView() {
            return view;
        }

        public void setView(View view) {
            this.view = view;
        }

        public List<SkinItem> getList() {
            return list;
        }

        public void setList(List<SkinItem> list) {
            this.list = list;
        }

        //给单个控件换肤
        public void apply(){
            for (SkinItem skinItem:list){
                if (skinItem.getName().equals("background")){
                    if(skinItem.getTypeName().equals("color")){
                        view.setBackgroundColor(
                                SkinManager.getInstance()
                        .getColor(skinItem.getResId()));
                    }else if(skinItem.getTypeName().equals("drawable")
                        || skinItem.getTypeName().equals("mipmap")){
                        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.JELLY_BEAN){
                            view.setBackground(SkinManager.getInstance().getDrawable(skinItem.getResId()));
                        }else{
                            view.setBackgroundDrawable(SkinManager.getInstance().getDrawable(skinItem.getResId()));
                        }
                    }
                }else if(skinItem.getName().equals("textcolor")){
                    if (view instanceof TextView){
                        ((TextView)view).setTextColor(SkinManager.getInstance().getColor(skinItem.getResId()));
                    }else if(view instanceof Button){
                        ((Button)view).setTextColor(SkinManager.getInstance().getColor(skinItem.getResId()));
                    }
                }
            }
        }
    }
    public void apply(){
        for (SkinView skinView:viewList) {
            skinView.apply();
        }
    }
}
