package com.reversecoder.library.customview.model;

import android.graphics.drawable.Drawable;
import android.util.Log;

public class PInfo {

    private String appname = "";
    private String pname = "";
    private String versionName = "";
    private int versionCode = 0;
    private Drawable icon;

    public PInfo() {

    }

    public PInfo(String appname, String pname, String versionName,
                 int versionCode, Drawable icon) {

        this.appname = appname;
        this.pname = pname;
        this.versionName = versionName;
        this.versionCode = versionCode;
        this.icon = icon;
    }

    public String getAppname() {
        return appname;
    }

    public void setAppname(String appname) {
        this.appname = appname;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public void prettyPrint() {
        Log.d("AppInfo: ", appname + "\t" + pname + "\t" + versionName + "\t"
                + versionCode);
    }

}
