package com.orieange.wcounter.data;

import android.content.Context;
import android.databinding.BaseObservable;

/**
 * Created by Administrator on 2017/7/18.
 */

public class ObservableOneItem extends BaseObservable {
    private TextItem item1;
    private TextItem item2;
    private TextItem item3;

    public TextItem getItem1() {
        return item1;
    }

    public void setItem1(TextItem item1) {
        this.item1 = item1;
//        notifyChange();
    }

    public TextItem getItem2() {
        return item2;
    }

    public void setItem2(TextItem item2) {
        this.item2 = item2;
    }

    public TextItem getItem3() {
        return item3;
    }

    public void setItem3(TextItem item3) {
        this.item3 = item3;
    }

    public static ObservableOneItem buildWith(Context context, String t1, String t2, String t3) {
        ObservableOneItem oneItem = new ObservableOneItem();
        oneItem.item1 = new TextItem(context, t1);
        oneItem.item2 = new TextItem(context, t2);
        oneItem.item3 = new TextItem(context, t3);
        return oneItem;
    }

    @Override
    public String toString() {
        return "OneItem{" +
                "item1=" + item1 +
                ", item2=" + item2 +
                ", item3=" + item3 +
                '}';
    }
}
