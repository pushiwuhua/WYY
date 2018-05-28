package com.orieange.wcounter.view;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.orieange.wcounter.AiLog;
import com.orieange.wcounter.R;

/**
 * AiTextDialog 函数作用
 * wzz created at 2017/8/4 9:52
 */
public class AiTextDialog extends DialogFragment {
    private CharSequence text;

    /**
     * 打开等待框
     */
    public static AiTextDialog open(FragmentManager manager, CharSequence text) {
        AiTextDialog dialog = new AiTextDialog();
        dialog.setText(text);
        dialog.show(manager, "wait");
        return dialog;
    }

    public void setText(CharSequence text) {
        this.text = text;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        AiLog.i(AiLog.TAG_WZZ, "AiOptionDialog onCreateView");
        final View view = inflater.inflate(R.layout.view_stock_quickcount, container, false);//加载布局文件
        TextView tv = view.findViewById(R.id.text);
        tv.setText(text);
        return view;
    }

}