package com.orieange.wcounter.data;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;

import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2017/7/18.
 */

public class CountUtil {
    /**
     * 本金复利的计算
     *
     * @param context
     * @param money   本金
     * @param num     期数
     * @param rate    利率
     * @return
     */
    public static ObservableOneItem countWith(Context context, double money, int num, double rate) {
        double total = money;
        for (int i = 0; i < num; i++) {
            total = total * (1 + rate);
        }
        double interest = Double.valueOf(new DecimalFormat("#.00").format(total - money));
        total = Double.valueOf(new DecimalFormat("#.00").format(total));

        ObservableOneItem item = ObservableOneItem.buildWith(context, String.valueOf(num), String.valueOf(interest), String.valueOf(total));
        item.getItem2().textColor = ContextCompat.getColor(context, android.R.color.holo_red_light);
        return item;
    }

    /**
     * 定存复利的计算
     *
     * @param context
     * @param money
     * @param num
     * @param rate
     * @return
     */
    public static ObservableOneItem countWithOneByOne(Context context, double money, int num, double rate) {
        double total = 0;
        for (int i = 0; i < num; i++) {
            total = total * (1 + rate) + money * (1 + rate);
        }
        double interest = Double.valueOf(new DecimalFormat("#.00").format(total - money * num));
        total = Double.valueOf(new DecimalFormat("#.00").format(total));

        ObservableOneItem item = ObservableOneItem.buildWith(context, String.valueOf(num), String.valueOf(interest), String.valueOf(total));
        item.getItem2().textColor = ContextCompat.getColor(context, android.R.color.holo_red_light);
        return item;
    }

    /**
     * 解析股票价格
     *
     * @param source 原始文本
     * @param code   股票代码
     * @return 股票模型
     */
    public static Stock decodeStock(String source, String code) {
        Matcher matcher = Pattern.compile(code + "=\"\\S+\"", Pattern.CASE_INSENSITIVE).matcher(source);
        while (matcher.find()) {
            String content = matcher.group(0);
            String subContent = content.substring(content.indexOf("\"") + 1, content.lastIndexOf("\""));
            if (!TextUtils.isEmpty(subContent)) {
                Stock stock = new Stock();
                if (source.contains("s_sh")) {
                    stock.market = 0;
                } else {
                    stock.market = 1;
                }
                String[] arr = subContent.split(",");
                stock.code = code;
                stock.name = arr[0];
                stock.price = Double.valueOf(arr[1]);
                stock.moneyChange = Double.valueOf(arr[2]);
                stock.percentChange = Double.valueOf(arr[3]);
                stock.exchangeCount = Double.valueOf(arr[4]);
                stock.exchangeMoney = Double.valueOf(arr[5]);
                return stock;
            }
        }
        return null;
    }
}
