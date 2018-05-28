package com.orieange.wcounter.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.orieange.wcounter.Constants;
import com.orieange.wcounter.OverlayWindowManager;
import com.orieange.wcounter.data.StockPool;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class FloatWindowService extends Service implements Constants {
    private Disposable mDisposableLooper;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (mDisposableLooper == null) {
            updateOverlayWindow();
            mDisposableLooper = Observable.interval(0, 5000, TimeUnit.MILLISECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<Long>() {
                        @Override
                        public void accept(@NonNull Long aLong) throws Exception {
                            updateOverlayWindow();
                        }
                    });
        }
        StockPool.getInstance(getApplicationContext()).registWatcher(this);//注册循环
        int result = super.onStartCommand(intent, flags, startId);
        return result;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        StockPool.getInstance(getApplicationContext()).unregistWatcher(this);//取消注册循环
        // Service被终止的同时也停止定时器继续运行
        mDisposableLooper.dispose();
        mDisposableLooper = null;
        OverlayWindowManager.getInstance().removeAllWindow(getApplicationContext());
    }

    private void updateOverlayWindow() {
        // 当前没有悬浮窗显示，则创建悬浮窗。
        if (!OverlayWindowManager.getInstance().isWindowShowing()) {
            OverlayWindowManager.getInstance().initData();
            OverlayWindowManager.getInstance().createWindow(getApplicationContext());
        }
        // 当前有悬浮窗显示，则更新内存数据。
        else {
            OverlayWindowManager.getInstance().updateViewData(getApplicationContext());
        }
    }
}
