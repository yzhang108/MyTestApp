package com.example.mytestapplication.ui.activity;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.Debug;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import com.example.mytestapplication.R;
import com.example.mytestapplication.utils.FileUtil;
import com.example.mytestapplication.utils.RejectedTest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

public class MainActivity extends AppCompatActivity {

    private static final String TAG="MainActivity ";
    private Button bt_openApp;
    private Button bt_html5, mBtNotification, mBtRejected, mBtMemoryTest,mBtCopyDB,tablaoutTest;
    private Button bt_listview_image,bt_test_scrollto,btn_test_databing,btn_coordinatorylayout;
    private ActivityManager activityManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        bt_openApp = (Button) findViewById(R.id.bt_openApp);
        bt_html5 = (Button) findViewById(R.id.bt_html5);
        mBtMemoryTest = (Button) findViewById(R.id.bt_memory_test);
        mBtCopyDB=(Button)findViewById(R.id.bt_copy_db);
        tablaoutTest=(Button)findViewById(R.id.tablayout_test);
        bt_listview_image=(Button)findViewById(R.id.bt_listview_refresh_pic) ;
        bt_test_scrollto=(Button)findViewById(R.id.bt_test_scrollto);
        btn_test_databing=(Button)findViewById(R.id.bt_test_databinding) ;
        btn_coordinatorylayout=(Button)findViewById(R.id.bt_coordinatorylayout) ;

        toolbar.setTitle("标题");
        toolbar.setSubtitle("subtitle");
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        bt_openApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openApp();
            }
        });
        bt_html5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, HtmlTestActivity.class));
            }
        });

        mBtNotification = (Button) findViewById(R.id.bt_notification);
        mBtNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNotification();
            }
        });
        mBtRejected = (Button) findViewById(R.id.bt_rejected);
        mBtRejected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testRejected();
            }
        });
        mBtMemoryTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                memoryTest();
            }
        });
        mBtCopyDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileUtil.copyDBToSDcrad();
            }
        });
        tablaoutTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, TabLayoutTestFragment.class));
            }
        });
        bt_listview_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ListViewTest.class));
            }
        });
        bt_test_scrollto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, TestScrollToActivity.class));
            }
        });
        btn_test_databing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, DataBindingTestActivity.class));
            }
        });
        btn_coordinatorylayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CoordinatoryLayoutActivity.class));
            }
        });
    }

    /**
     * 检查系统应用程序，并打开
     */
    private void openApp() {
        //应用过滤条件
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        PackageManager mPackageManager = this.getPackageManager();
        List<ResolveInfo> mAllApps = mPackageManager.queryIntentActivities(mainIntent, 0);
        //按报名排序
        Collections.sort(mAllApps, new ResolveInfo.DisplayNameComparator(mPackageManager));

        for (ResolveInfo res : mAllApps) {
            //该应用的包名和主Activity
            String pkg = res.activityInfo.packageName;
            String cls = res.activityInfo.name;
            Log.e("==", "pkg=" + pkg);
            // 打开QQ
            if (pkg.contains("wx")) {
                ComponentName componet = new ComponentName(pkg, cls);
                Intent intent = new Intent();
                intent.setComponent(componet);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                this.startActivity(intent);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void createNotification() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0);
        Notification notification = new Notification();
        notification.defaults = Notification.DEFAULT_SOUND;
        notificationManager.notify(1, notification);
    }

    private void testRejected() {
//        RejectedTest.getInstance().testGetDeclaredMethods(MainActivity.class);
//        RejectedTest.getInstance().testGetMethods(MainActivity.class);
        RejectedTest.getInstance().testGetFileds(MainActivity.class);
    }

    private void memoryTest() {
        long rt_max_memory = Runtime.getRuntime().maxMemory();
        long rt_free_memory = Runtime.getRuntime().freeMemory();
        long rt_total_memory = Runtime.getRuntime().totalMemory();
        Log.e("zy", "rt_max_memory=" + formateFileSize(rt_max_memory) + ",rt_free_memory=" + formateFileSize(rt_free_memory) + ",rt_total_memory=" + formateFileSize(rt_total_memory));

        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        activityManager.getMemoryInfo(mi);
        long availableMegs = mi.availMem / 1048576L;

//Percentage can be calculated for API 16+
        long percentAvail = mi.availMem / mi.totalMem;
        Log.e("zy", "availableMegs=" + availableMegs + ",percentAvail=" + percentAvail);
        Log.e("zy", "mi.availMem=" + formateFileSize(mi.availMem) + ",mi.totalMem=" + formateFileSize(mi.totalMem));

        Debug.MemoryInfo memoryInfo=new Debug.MemoryInfo();

        int avaiableMem=((ActivityManager) this.getSystemService(
                Context.ACTIVITY_SERVICE)).getMemoryClass();
        Log.e("zy","avaiableMem="+avaiableMem);


//        Debug.MemoryInfo[] currMis=activityManager.getProcessMemoryInfo();

        getRunningAppProcessInfo();
    }



    //获得系统可用内存信息
    private String getSystemAvaialbeMemorySize(){
        //获得MemoryInfo对象
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo() ;
        //获得系统可用内存，保存在MemoryInfo对象上
        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        activityManager.getMemoryInfo(memoryInfo) ;
        long memSize = memoryInfo.availMem ;

        //字符类型转换
        String availMemStr = formateFileSize(memSize);

        return availMemStr ;
    }

    //调用系统函数，字符串转换 long -String KB/MB
    private String formateFileSize(long size){
        return Formatter.formatFileSize(MainActivity.this, size);
    }


    private List<ProcessInfo> processInfoList = null;

    // 获得系统进程信息
    private void getRunningAppProcessInfo() {
        // ProcessInfo Model类   用来保存所有进程信息
        processInfoList = new ArrayList<ProcessInfo>();

        // 通过调用ActivityManager的getRunningAppProcesses()方法获得系统里所有正在运行的进程
        List<ActivityManager.RunningAppProcessInfo> appProcessList = activityManager
                .getRunningAppProcesses();

        for (ActivityManager.RunningAppProcessInfo appProcessInfo : appProcessList) {
            // 进程ID号
            int pid = appProcessInfo.pid;
            // 用户ID 类似于Linux的权限不同，ID也就不同 比如 root等
            int uid = appProcessInfo.uid;
            // 进程名，默认是包名或者由属性android：process=""指定
            String processName = appProcessInfo.processName;
            // 获得该进程占用的内存
            int[] myMempid = new int[] { pid };
            // 此MemoryInfo位于android.os.Debug.MemoryInfo包中，用来统计进程的内存信息
            Debug.MemoryInfo[] memoryInfo = activityManager
                    .getProcessMemoryInfo(myMempid);
            // 获取进程占内存用信息 kb单位
            int memSize = memoryInfo[0].dalvikPrivateDirty;

            Log.e(TAG, "processName: " + processName + "  pid: " + pid
                    + " uid:" + uid + " memorySize is -->" + memSize + "kb");
            // 构造一个ProcessInfo对象
            ProcessInfo processInfo = new ProcessInfo();
            processInfo.setPid(pid);
            processInfo.setUid(uid);
            processInfo.setMemSize(memSize);
            processInfo.setProcessName(processName);
            processInfoList.add(processInfo);

            // 获得每个进程里运行的应用程序(包),即每个应用程序的包名
            String[] packageList = appProcessInfo.pkgList;
            Log.e(TAG, "process id is " + pid + "has " + packageList.length);
            for (String pkg : packageList) {
                Log.e(TAG, "packageName " + pkg + " in process id is -->"+ pid);
            }
        }
    }


    public class ProcessInfo {
        private int pid;
        private int uid;
        private int memSize;
        private String processName;

        public int getPid() {
            return pid;
        }

        public void setPid(int pid) {
            this.pid = pid;
        }

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }

        public int getMemSize() {
            return memSize;
        }

        public void setMemSize(int memSize) {
            this.memSize = memSize;
        }

        public String getProcessName() {
            return processName;
        }

        public void setProcessName(String processName) {
            this.processName = processName;
        }
    }
}


