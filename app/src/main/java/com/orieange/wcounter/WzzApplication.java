package com.orieange.wcounter;

import android.app.Application;
import android.util.Log;

import com.squareup.leakcanary.LeakCanary;

/**
 * Created by Administrator on 2017/7/19.
 */

public class WzzApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("wzz", "WzzApplication");
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        LeakCanary.install(this);
    }
}
