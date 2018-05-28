package com.orieange.wcounter;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.ViewCompat;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;

import com.orieange.wcounter.data.StockPool;
import com.orieange.wcounter.view.SmallWindowView;
import com.orieange.wcounter.view.WindowView;

public class OverlayWindowManager implements Constants {

    private static OverlayWindowManager instance;
    private WindowManager mWindowManager;
    private WindowView mWindowView;
    private LayoutParams windowParams;
    private int windowType;//窗口类型
    private TextView tvShow;//显示控件

    public static OverlayWindowManager getInstance() {
        if (instance == null) {
            instance = new OverlayWindowManager();
        }
        return instance;
    }

    public void createWindow(final Context context) {
        createWindow(context, SMALL_WINDOW_TYPE);
    }

    private void createWindow(final Context context, int type) {
        final WindowManager windowManager = getWindowManager(context);
        if (windowParams == null) {
            windowParams = getWindowParams(context);
        }
        windowType = type;
        if (mWindowView == null) {
            mWindowView = new SmallWindowView(context);
            Drawable background = ContextCompat.getDrawable(context, R.drawable.float_bg);
            ViewCompat.setBackground(mWindowView, background);//设置背景
            windowManager.addView(mWindowView, windowParams);
        }
        tvShow = mWindowView.findViewById(R.id.tvSum);

        if (windowType == SMALL_WINDOW_TYPE) {
            setOnGeatureListener(windowManager, context, mWindowView, BIG_WINDOW_TYPE);
        } else {
            setOnGeatureListener(windowManager, context, mWindowView, SMALL_WINDOW_TYPE);
        }
        updateViewData(context);//更新数据
    }

    public void initData() {
    }

    private LayoutParams getWindowParams(Context context) {
        final WindowManager windowManager = getWindowManager(context);
        Point sizePoint = new Point();
        windowManager.getDefaultDisplay().getSize(sizePoint);
        int screenWidth = sizePoint.x;
        int screenHeight = sizePoint.y;
        LayoutParams windowParams = new WindowManager.LayoutParams();
        windowParams.type = LayoutParams.TYPE_SYSTEM_ALERT;//wzz实测,官方sdk参数https://developer.android.com/reference/android/view/WindowManager.LayoutParams.html#TYPE_APPLICATION_OVERLAY
        windowParams.format = PixelFormat.RGBA_8888;
        windowParams.flags = LayoutParams.FLAG_LAYOUT_IN_SCREEN | LayoutParams.FLAG_NOT_FOCUSABLE | LayoutParams.FLAG_NOT_TOUCH_MODAL;
        windowParams.gravity = Gravity.START | Gravity.TOP;
        windowParams.width = LayoutParams.WRAP_CONTENT;
        windowParams.height = LayoutParams.WRAP_CONTENT;
        int x = PreferenceUtil.getSingleton(context).getInt(SP_X, -1);
        int y = PreferenceUtil.getSingleton(context).getInt(SP_Y, -1);
        if (x == -1 || y == -1) {
            x = screenWidth;
            y = screenHeight / 2;
        }
        windowParams.x = x;
        windowParams.y = y;
        return windowParams;
    }

    private void setOnGeatureListener(final WindowManager windowManager, final Context context, final WindowView windowView, final int type) {
        final GestureDetectorCompat gestureDetector = new GestureDetectorCompat(context, new GestureDetector.OnGestureListener() {
            int paramX, paramY;

            @Override
            public boolean onDown(MotionEvent motionEvent) {
                paramX = windowParams.x;
                paramY = windowParams.y;
                return false;
            }

            @Override
            public void onShowPress(MotionEvent motionEvent) {

            }

            @Override
            public boolean onSingleTapUp(MotionEvent motionEvent) {
                createWindow(context, type);
                return false;
            }

            @Override
            public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float dx, float dy) {
                windowParams.x = (int) (paramX + (motionEvent1.getRawX() - motionEvent.getRawX()));
                windowParams.y = (int) (paramY + (motionEvent1.getRawY() - motionEvent.getRawY()));
                // 更新悬浮窗位置
                windowManager.updateViewLayout(windowView, windowParams);
                return false;
            }

            @Override
            public void onLongPress(MotionEvent motionEvent) {
                Intent intent = new Intent(context, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }

            @Override
            public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
                return false;
            }
        });
        windowView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gestureDetector.onTouchEvent(event);
                return false;
            }
        });
    }

    private void removeWindow(Context context, WindowView windowView) {
        if (windowView != null) {
            WindowManager windowManager = getWindowManager(context);
            windowManager.removeView(windowView);
        }
    }

    public void removeAllWindow(Context context) {
        removeWindow(context, mWindowView);
        mWindowView = null;
    }

    public void updateViewData(Context context) {
        if (mWindowView != null) {
            if (windowType == BIG_WINDOW_TYPE) {
                tvShow.setText(StockPool.getInstance(context).getSpannabelSBOverlayWindowTotal());
            } else {
                tvShow.setText(StockPool.getInstance(context).getSpannabelSBOverlayWindowSimple());
            }
        }
    }

    public boolean isWindowShowing() {
        return mWindowView != null;
    }

    private WindowManager getWindowManager(Context context) {
        if (mWindowManager == null) {
            mWindowManager = (WindowManager) context
                    .getSystemService(Context.WINDOW_SERVICE);
        }
        return mWindowManager;
    }
}
