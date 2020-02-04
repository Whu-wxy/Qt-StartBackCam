
package com.dynavin.example;

import java.util.ArrayList;
import java.util.List;
import java.io.IOException;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.Button;
import android.util.Log;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.app.Instrumentation;
import android.view.KeyEvent;

public class TopWindowService extends Service
{
    public static final String TAG = "lkb";
	public static final String OPERATION = "operation";
	public static final int OPERATION_SHOW = 100;
	public static final int OPERATION_HIDE = 101;

	private static final int HANDLE_CHECK_ACTIVITY = 200;

	private boolean isAdded = false; // 是否已增加悬浮窗
	private static WindowManager wm;
	private static WindowManager.LayoutParams params;
        private static WindowManager.LayoutParams params2;
        private Button btn_ctrl; //to control button panel show/hide

        public boolean bPanelAdd = false;

	private List<String> homeList; // 桌面应用程序包名列表
	private ActivityManager mActivityManager;        

	@Override
	public IBinder onBind(Intent intent)
	{
            Log.v(TAG,new Exception().getStackTrace()[0].getMethodName());
            return null;
	}

	@Override
	public void onCreate()
	{
            Log.v(TAG,new Exception().getStackTrace()[0].getMethodName());
            super.onCreate();

            homeList = getHomes();
            createFloatView();
	}

	@Override
	public void onDestroy()
	{
            Log.v(TAG,new Exception().getStackTrace()[0].getMethodName());
            super.onDestroy();
	}

        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            Log.v(TAG,new Exception().getStackTrace()[0].getMethodName());

            //lkb delete:
//            int operation = intent.getIntExtra(OPERATION, OPERATION_SHOW);
//            switch (operation)
//            {
//            case OPERATION_SHOW:
//                    mHandler.removeMessages(HANDLE_CHECK_ACTIVITY);
//                    mHandler.sendEmptyMessage(HANDLE_CHECK_ACTIVITY);
//                    break;
//            case OPERATION_HIDE:
//                    mHandler.removeMessages(HANDLE_CHECK_ACTIVITY);
//                    break;
//            }

            return super.onStartCommand(intent, flags, startId);
        }

        private Handler mHandler = new Handler()
        {
            @Override
            public void handleMessage(Message msg)
            {
                Log.v(TAG,new Exception().getStackTrace()[0].getMethodName());
                switch (msg.what)
                {
                case HANDLE_CHECK_ACTIVITY:
                Log.v(TAG,new Exception().getStackTrace()[0].getMethodName() + "check activity");
                        if (isHome())
                        {
                                if (!isAdded)
                                {
                                        wm.addView(btn_ctrl, params);
                                        isAdded = true;
                                }
                        } else
                        {
                                if (isAdded)
                                {
                                        wm.removeView(btn_ctrl);
                                        isAdded = false;
                                }
                        }
                        mHandler.sendEmptyMessageDelayed(HANDLE_CHECK_ACTIVITY, 1000);
                        break;
                }
            }
        };

	/**
	 * 创建悬浮窗
	 */
	private void createFloatView()
        {
            final LinearLayout panelLayout = new LinearLayout(this);
            panelLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.v(TAG,"panel click: id="+view.getId());
//                        switch(view.getId()){
//                        case
//                        }
                    }
            });
            Log.v(TAG,new Exception().getStackTrace()[0].getMethodName());

		wm = (WindowManager) getApplicationContext().getSystemService(
				Context.WINDOW_SERVICE);
		params = new WindowManager.LayoutParams();

		// 设置window type
                params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
                //params.type = WindowManager.LayoutParams.TYPE_PHONE;
		/*
		 * 如果设置为params.type = WindowManager.LayoutParams.TYPE_PHONE; 那么优先级会降低一些,
		 * 即拉下通知栏不可见
		 */
                params.format = PixelFormat.RGBA_8888; // 设置图片格式，效果为背景透明

		// 设置Window flag
                params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
		/*
		 * 下面的flags属性的效果形同“锁定”。 悬浮窗不可触摸，不接受任何事件,同时不影响后面的事件响应。
		 * wmParams.flags=LayoutParams.FLAG_NOT_TOUCH_MODAL |
		 * LayoutParams.FLAG_NOT_FOCUSABLE | LayoutParams.FLAG_NOT_TOUCHABLE;
		 */

		// 设置悬浮窗的长得宽
                params.width = 1200;
                params.height = 100;
                params.x = -550;
                params.y = 400;

                btn_ctrl = new Button(getApplicationContext());
                btn_ctrl.setText("Ctrl");
                btn_ctrl.setWidth(80);
                btn_ctrl.setHeight(80);
                btn_ctrl.setBackgroundResource(R.drawable.btn_back); //!!
		// 设置悬浮窗的Touch监听
                btn_ctrl.setOnTouchListener(new OnTouchListener()
		{
			int lastX, lastY;
			int paramX, paramY;
                        boolean bDownflag = false;

			public boolean onTouch(View v, MotionEvent event)
			{
				switch (event.getAction())
				{
				case MotionEvent.ACTION_DOWN:
                                    lastX = (int) event.getRawX();
                                    lastY = (int) event.getRawY();
                                    paramX = params2.x;
                                    paramY = params2.y;
                                    bDownflag = true;
                                    break;
				case MotionEvent.ACTION_MOVE:
                                    int dx = (int) event.getRawX() - lastX;
                                    int dy = (int) event.getRawY() - lastY;
                                    params2.x = paramX + dx;
                                    params2.y = paramY + dy;
                                    //update btn_ctrl pos
                                    wm.updateViewLayout(btn_ctrl, params2);
                                    bDownflag = false;
                                    break;
                                 case MotionEvent.ACTION_UP:
                                    if (bDownflag){
                                        bDownflag = false;
                                        Log.v(TAG,"ctrl click");
                                        if (bPanelAdd){
                                            wm.removeView(panelLayout);
                                            bPanelAdd = false;
                                        }
                                        else{
                                            wm.addView(panelLayout, params);
                                            bPanelAdd = true;
                                        }
                                    }
                                    break;
                                 }
				return true;
			}
                });

                View.OnClickListener handler = new View.OnClickListener() {
                    static final String ACTION_REVERSE_EVENT = "android.intent.action.REVERSE_EVENT";
                    static final String EXTRA_REVERSE_STATE = "android.intent.extra.REVERSE_STATE";
                        public void onClick(View view) {
                            Log.v(TAG,"panel button click: id="+view.getId());
                            switch (view.getId()) {
                               case 0: //test send key 1
                               new Thread(){
                                public void run() {
                                 try{
                                  Instrumentation inst = new Instrumentation();
                                  inst.sendKeyDownUpSync(KeyEvent.KEYCODE_BACK);
                                 }
                                 catch (Exception e) {
                                              Log.e("Exception when onBack", e.toString());
                                          }
                                }
                               }.start();
                               break;
                               case 1: //test send key 2
                               try{
                                  Runtime runtime=Runtime.getRuntime();
                                  Log.v(TAG,"input keyevent");
                                  runtime.exec("input keyevent " + KeyEvent.KEYCODE_BACK);
                                 }catch(IOException e){
                                   Log.e("Exception when doBack", e.toString());
                                 }
                               break;
                               case 2: //test open camera
                               Log.v(TAG,"send intent to open cam");
                               Intent intent1 = new Intent(ACTION_REVERSE_EVENT);
                               intent1.addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING);
                               intent1.putExtra(EXTRA_REVERSE_STATE, 1);
                               //getApplicationContext().sendBroadcastAsUser(intent, UserHandle.ALL);
                               sendBroadcast(intent1);
                               break;
                               case 3: //test close camera
                               Log.v(TAG,"send intent to close cam");
                               Intent intent2 = new Intent(ACTION_REVERSE_EVENT);
                               intent2.addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING);
                               intent2.putExtra(EXTRA_REVERSE_STATE, 0);
                               //getApplicationContext().sendBroadcastAsUser(intent, UserHandle.ALL);
                               sendBroadcast(intent2);
                               break;
                            }
                        }
                };

                int idCount = 0;
                String btnName[]={"USB","SD","CAM+","CAM-","IPOD","DISC","RADIO","FM"};
                Button[] btn = new Button[8];
                for(int i=0; i<8; i++){ //add buttons to panel layout
                    btn[i] = new Button(this);
                    btn[i].setId(i);
                    btn[i].setWidth(80);
                    btn[i].setHeight(80);
                    btn[i].setText(btnName[i]);
                    btn[i].setOnClickListener(handler);
                    panelLayout.addView(btn[i]);
                }
                wm.addView(panelLayout, params); //add panel layout to WindowManager
                bPanelAdd = true;

                params2 = new WindowManager.LayoutParams();
                params2.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
                params2.width = 80;
                params2.height = 80;
                params2.x = -550;
                params2.y = 200;
                params2.format = PixelFormat.RGBA_8888;
                params2.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
                wm.addView(btn_ctrl, params2); //add ctrl button to WindowManager
		isAdded = true;
	}

	/**
	 * 获得属于桌面的应用的应用包名称
	 * 
	 * @return 返回包含所有包名的字符串列表
	 */
	private List<String> getHomes()
	{
		List<String> names = new ArrayList<String>();
		PackageManager packageManager = this.getPackageManager();
		// 属性
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		List<ResolveInfo> resolveInfo = packageManager.queryIntentActivities(
				intent, PackageManager.MATCH_DEFAULT_ONLY);
		for (ResolveInfo ri : resolveInfo)
		{
			names.add(ri.activityInfo.packageName);
		}
		return names;
	}

	/**
	 * 判断当前界面是否是桌面
	 */
	public boolean isHome()
	{
		if (mActivityManager == null)
		{
			mActivityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		}
		List<RunningTaskInfo> rti = mActivityManager.getRunningTasks(1);
		return true;
		// return homeList.contains(rti.get(0).topActivity.getPackageName());
	}

}
