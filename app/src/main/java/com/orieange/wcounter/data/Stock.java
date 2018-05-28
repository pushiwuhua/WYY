package com.orieange.wcounter.data;

import android.text.TextUtils;

/**
 * Stock 股票模型
 * wzz created at 2017/7/21 17:31
 */
public class Stock {
    public int market;//0:沪市 1:深市
    public String code;
    public String name;
    public double price;//价格
    public double moneyChange;
    public double percentChange;
    public double exchangeCount;//成交数, 单位:手
    public double exchangeMoney;//成交额, 单位:万

    public boolean isValid() {
        return !TextUtils.isEmpty(name) && !TextUtils.isEmpty(code) && code.length() == 6;
    }

    @Override
    public String toString() {
        return "Stock{" +
                "market=" + market +
                ", code=" + code +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", moneyChange=" + moneyChange +
                ", percentChange=" + percentChange +
                ", exchangeCount=" + exchangeCount +
                ", exchangeMoney=" + exchangeMoney +
                '}';
    }
}
