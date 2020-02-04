
//package com.dynavin.HelloQtOnAndroid;
package com.dynavin.example;

import android.content.Context;
import android.content.Intent;
import android.app.PendingIntent;
import android.util.Log;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.MotionEvent;
import android.provider.Settings;
import android.widget.Toast;
import android.net.Uri;

public class QtFullscreenActivity extends org.qtproject.qt5.android.bindings.QtActivity 
{
    private final static String TAG = "QtFullscreen";
    private static int OVERLAY_PERMISSION_REQ_CODE = 1234;
    @Override
    public void onCreate(Bundle savedInstanceState) {
          super.onCreate(savedInstanceState);
          getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

          //start floating window
          Log.v(TAG, "lkb onCreate top service +");
          if (!Settings.canDrawOverlays(this)) {
              //Toast.makeText(QtFullscreenActivity.this, "当前无权限，请授权！", Toast.LENGTH_SHORT).show();
              Intent intentPermission = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                                                //,Uri.parse("package:" + getPackageName()));
              intentPermission.setData(Uri.parse("package:" + getPackageName())); //com.dynavin.example
              startActivityForResult(intentPermission, OVERLAY_PERMISSION_REQ_CODE);
          } else {
                startTopWindowService();
          }
          Log.v(TAG, "lkb onCreate top service -");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.v(TAG, "lkb on activity result");
        if (requestCode == OVERLAY_PERMISSION_REQ_CODE) {
            Log.v(TAG, "lkb on activity result - permission");
            if (Settings.canDrawOverlays(this)) {
                Log.v(TAG, "onActivityResult granted");
                startTopWindowService();
            }
        }
    }

    private void startTopWindowService(){
        Intent intentTopWindow = new Intent(this, TopWindowService.class);
        intentTopWindow.putExtra(TopWindowService.OPERATION, TopWindowService.OPERATION_SHOW);
        startService(intentTopWindow);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                |View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                |View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                | View.SYSTEM_UI_FLAG_FULLSCREEN      // hide status bar
                | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                | View.SYSTEM_UI_FLAG_FULLSCREEN      // hide status bar
                | View.SYSTEM_UI_FLAG_IMMERSIVE);
        return super.dispatchTouchEvent(ev);
    }

}
