package com.dynavin.example;

import android.content.Context;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.util.Log;
import android.os.Bundle;
import android.view.View;

public class AndroidApi extends org.qtproject.qt5.android.bindings.QtActivity {
    private static final String TAG = "launchApp";
    private static AndroidApi m_instance;

    public AndroidApi(){ //this function called when indicate this class name in AndroidManifest.xml for lunchable activity
        Log.d(TAG, "lkb java construct");
        m_instance = this;
    }

    public static void launchApplication(String packageName, String className) {
        Log.d(TAG, "lkb java launch +");
        Intent intent = new Intent();
        intent.setClassName("com.rockchip.car.recorder","com.rockchip.car.recorder.activity.IndexActivity");
        //intent.setComponent(new ComponentName("com.rockchip.car.recorder","com.rockchip.car.recorder.activity.IndexActivity"));
        intent.setAction(Intent.ACTION_VIEW);
        Log.d(TAG, "lkb java start activity");
        m_instance.startActivity(intent);
        Log.d(TAG, "lkb java launch -");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //outState.putBoolean(KEY_SAVE_GRID_OPENED, mGrid.getVisibility() == View.VISIBLE);
    }
}

