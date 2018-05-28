package com.orieange.wcounter.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.orieange.wcounter.Constants;
import com.orieange.wcounter.PreferenceUtil;
import com.orieange.wcounter.R;

public class SmallWindowView extends WindowView implements Constants {

    public SmallWindowView(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.float_window_small, this);
        View view = findViewById(R.id.linLayoutSmall);
        int statusBarHeight = PreferenceUtil.getSingleton(context).getInt(SP_STATUSBAR_HEIGHT, 0);
        if (statusBarHeight != 0) {
            LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
            layoutParams.height = statusBarHeight;
            view.setLayoutParams(layoutParams);
        }
        viewWidth = view.getLayoutParams().width;
        viewHeight = view.getLayoutParams().height;
    }
}
