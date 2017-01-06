package com.reversecoder.library.customview.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;

import java.util.List;

/**
 * Created by alam on 7/26/16.
 */
public class AppUtils {

    private static void showAppInMarket(Context context, String desiredPackageName) {
        String url = "";

        try {
            //Check whether Google Play store is installed or not:
            context.getPackageManager().getPackageInfo("com.android.vending", 0);
            url = "market://details?id=" + desiredPackageName;
        } catch (final Exception e) {
            url = "https://play.google.com/store/apps/details?id=" + desiredPackageName;
        }

        //Open the app page in Google Play store:
        final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * This method is used for opening any activity from another application. In this case we must ensure that
     * the desired activity is set to "exported". Such as in manifest file:
     *
     * <activity
     *     android:name=".activity.DesiredActivity"
     *     android:exported="true"
     *     android:screenOrientation="portrait">
     * </activity>
     *
     *
     *
     * @param context                                The context of the activity.
     * @param desiredPackageName                     The package name of the desired or another application.
     * @param desiredActivityNameWithFullPackageName The full path of the activity. Such as : com.example.activity.DesiredActivity
     */
    public static void launchActivityFromAnotherApp(Context context, String desiredPackageName, String desiredActivityNameWithFullPackageName) {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName(desiredPackageName, desiredActivityNameWithFullPackageName));
        context.startActivity(intent);
    }

    public static boolean isAppInstalled(Context context, String desiredPackageName) {
        Intent intent = new Intent("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.LAUNCHER");

        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        List<ResolveInfo> resolveInfoList = context.getPackageManager().queryIntentActivities(intent, 0);

        for (ResolveInfo info : resolveInfoList) {
            if (info.activityInfo.packageName.equalsIgnoreCase(desiredPackageName)) {
                return true;
            }
        }
        return false;
    }

    public static void launchActivityFromAnotherAppOrDownloadApp(Context context, String desiredPackageName, String desiredActivityNameWithFullPackageName) {
        if (isAppInstalled(context, desiredPackageName)) {
            launchActivityFromAnotherApp(context, desiredPackageName, desiredActivityNameWithFullPackageName);
        } else {
            showAppInMarket(context, desiredPackageName);
        }
    }
}
