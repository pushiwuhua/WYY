package com.orieange.wcounter.data;

import android.content.Context;

import java.util.Arrays;

/**
 * Created by Administrator on 2017/7/18.
 */

public class SheetItem {
    public TextItem[] items;

    public static SheetItem buildWith(Context context, String... datas) {
        SheetItem oneItem = new SheetItem();
        if (datas != null && datas.length > 0) {
            oneItem.items = new TextItem[datas.length];
            for (int i = 0; i < datas.length; i++) {
                oneItem.items[i] = new TextItem(context, datas[i]);
            }
        }
        return oneItem;
    }

    @Override
    public String toString() {
        return "SheetItem{" +
                "items=" + Arrays.toString(items) +
                '}';
    }
}
