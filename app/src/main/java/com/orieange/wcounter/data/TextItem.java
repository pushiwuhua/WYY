package com.orieange.wcounter.data;

import android.content.Context;
import android.support.v4.content.ContextCompat;

/**
 * Created by Administrator on 2017/7/18.
 */

public class TextItem {
    public String text;
    public int textColor;
    private Context context;

    public TextItem(Context context, String text, int textColor) {
        this.text = text;
        this.textColor = textColor;
        this.context = context;
    }

    public TextItem(Context context, String text) {
        this(context, text, ContextCompat.getColor(context, android.R.color.black));
    }

    public TextItem() {
    }

    @Override
    public String toString() {
        return "TextItem{" +
                "text='" + text + '\'' +
                ", textColor=" + textColor +
                '}';
    }
}
