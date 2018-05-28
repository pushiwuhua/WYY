package com.orieange.wcounter.data;

import android.content.Context;
import android.databinding.ObservableField;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;

import java.text.SimpleDateFormat;

/**
 * Stock 股票模型
 * wzz created at 2017/7/21 17:31
 */
public class ObservableFieldStock {
    public ObservableField<String> market = new ObservableField<>();//0:沪市 1:深市
    public ObservableField<String> code = new ObservableField<>();
    public ObservableField<String> name = new ObservableField<>();
    public ObservableField<String> price = new ObservableField<>();//价格
    public ObservableField<String> moneyChange = new ObservableField<>();
    public ObservableField<Integer> moneyChangeColor = new ObservableField<>();
    public ObservableField<String> percentChange = new ObservableField<>();
    public ObservableField<String> exchangeCount = new ObservableField<>();//成交数, 单位:手
    public ObservableField<String> exchangeMoney = new ObservableField<>();//成交额, 单位:万
    public ObservableField<String> updateTime = new ObservableField<>();//更新时间
    public ObservableField<Boolean> optionVisiable = new ObservableField<>();//更多选项是否可见

    public static String timeFormat(String time) {
        if (TextUtils.isEmpty(time)) {
            return "";
        } else {
            SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
            return format.format(Long.valueOf(time));
        }
    }

    public static ObservableFieldStock buildWith(Context context, Stock stock) {
        ObservableFieldStock oneItem = new ObservableFieldStock();
        if (stock != null) {
            oneItem.updateWith(context, stock);
        }
        return oneItem;
    }

    public void updateWith(Context context, Stock stock) {
        this.market.set(String.valueOf(stock.market));
        this.code.set(stock.code);
        this.name.set(stock.name);
        this.price.set(String.valueOf(stock.price));
        this.moneyChange.set(String.valueOf(stock.moneyChange));
        if (stock.moneyChange > 0) {
            this.moneyChangeColor.set(ContextCompat.getColor(context, android.R.color.holo_red_dark));
        } else if (stock.moneyChange == 0) {
            this.moneyChangeColor.set(ContextCompat.getColor(context, android.R.color.darker_gray));
        } else if (stock.moneyChange < 0) {
            this.moneyChangeColor.set(ContextCompat.getColor(context, android.R.color.holo_green_dark));
        }
        this.percentChange.set(String.valueOf(stock.percentChange));
        this.exchangeCount.set(String.valueOf(stock.exchangeCount));
        this.exchangeMoney.set(String.valueOf(stock.exchangeMoney));
        this.updateTime.set("" + System.currentTimeMillis());
    }

    @Override
    public String toString() {
        return "ObservableFieldStock{" +
                "market=" + market +
                ", code=" + code +
                ", name=" + name +
                ", price=" + price +
                ", moneyChange=" + moneyChange +
                ", percentChange=" + percentChange +
                ", exchangeCount=" + exchangeCount +
                ", exchangeMoney=" + exchangeMoney +
                '}';
    }
}
