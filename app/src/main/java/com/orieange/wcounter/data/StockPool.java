package com.orieange.wcounter.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v4.content.SharedPreferencesCompat;
import android.support.v4.util.ArrayMap;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;

import com.orieange.wcounter.AiLog;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * StockPool 股票数据池
 * wzz created at 2017/7/25 10:05
 */
public class StockPool {
    private static volatile StockPool ourInstance;
    //    private List<Stock> stockList = new ArrayList<>();
    private LinkedHashMap<String, ObservableFieldStock> mapObservableFieldStock = new LinkedHashMap<>();//带顺序的map
    private Map<Object, Object> loopWatcher = new ArrayMap<>();
    private List<String> listStockCodes = new ArrayList<>();
    private Context mContext;
    private Disposable loopDisposable;//刷新控制器

    public void registWatcher(Object watcher) {
        loopWatcher.put(watcher, watcher);

        refreshOneLoop();//整体刷新一次
        if (loopDisposable == null) {
            Observable.interval(5000, TimeUnit.MILLISECONDS)
                    .subscribe(new Observer<Long>() {
                        @Override
                        public void onSubscribe(@NonNull Disposable d) {
                            loopDisposable = d;
                        }

                        @Override
                        public void onNext(@NonNull Long aLong) {
                            refreshOneLoop();//整体刷新一次
                        }

                        @Override
                        public void onError(@NonNull Throwable e) {

                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        }
    }

    public void unregistWatcher(Object watcher) {
        loopWatcher.remove(watcher);
        if (loopWatcher.size() == 0) {
            loopDisposable.dispose();
            loopDisposable = null;
        }
    }

    public static StockPool getInstance(Context context) {
        if (ourInstance == null) {
            synchronized (StockPool.class) {
                ourInstance = new StockPool(context);
            }
        }
        return ourInstance;
    }

    private StockPool(Context context) {
        mContext = context;
        String codes = context.getApplicationContext().getSharedPreferences("stock", Context.MODE_PRIVATE).getString("favorite", "");
        String[] arr = codes.split(",");
        AiLog.i(AiLog.TAG_WZZ, "StockPool StockPool codes:" + codes);
        for (int i = 0; i < arr.length; i++) {
            if (!TextUtils.isEmpty(arr[i])) {
                listStockCodes.add(arr[i]);
            }
        }
    }

    public SpannableStringBuilder getSpannabelSBOverlayWindowTotal() {
        SpannableStringBuilder spannableString = new SpannableStringBuilder();

        if (listStockCodes != null) {
            for (int i = 0; i < listStockCodes.size(); i++) {
                String stockCode = listStockCodes.get(i);
                ObservableFieldStock obsv = mapObservableFieldStock.get(stockCode);
                if (obsv != null && !TextUtils.isEmpty(obsv.price.get())) {
                    int start = spannableString.length();
                    spannableString.append(obsv.name.get() + " " + obsv.price.get() + " " + obsv.percentChange.get());
                    if (i < listStockCodes.size() - 1) {
                        spannableString.append("\n");
                    }
                    ForegroundColorSpan colorSpan;
                    if (Double.valueOf(obsv.moneyChange.get()) > 0) {
                        colorSpan = new ForegroundColorSpan(Color.parseColor("#FF0000"));
                    } else if (Double.valueOf(obsv.moneyChange.get()) == 0) {
                        colorSpan = new ForegroundColorSpan(Color.parseColor("#FFFFFF"));
                    } else {
                        colorSpan = new ForegroundColorSpan(Color.parseColor("#00FF00"));
                    }
                    spannableString.setSpan(colorSpan, start, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
        }
        return spannableString;
    }

    public static SpannableStringBuilder quickCountStockPrice(ObservableFieldStock stock) {
        if(stock != null){
            AiLog.i(AiLog.TAG_WZZ, "StockPool quickCountStockPrice:" + stock.price.get());
            AiLog.i(AiLog.TAG_WZZ, "StockPool quickCountStockPrice:" + stock.moneyChange.get());
            double price = Double.valueOf(stock.price.get()) - Double.valueOf(stock.moneyChange.get());
            return quickCountStockPrice(price);
        }
        else {
            return new SpannableStringBuilder();
        }
    }
    public static SpannableStringBuilder quickCountStockPrice(double price) {
        AiLog.i(AiLog.TAG_WZZ, "StockPool quickCountStockPrice:" + price);
        SpannableStringBuilder spannableString = new SpannableStringBuilder();
        spannableString.append(String.format("涨跌幅位%-6s", " ") + "    " + "价格");//1个汉字占2位宽度,补齐6位对齐20位
        spannableString.append("\n");
        for (double i = 10; i >= -10; i--) {
            double newPrice = price * (1 + i * 0.01);
            double newPriceFix = new BigDecimal(newPrice).setScale(2, BigDecimal.ROUND_HALF_EVEN).doubleValue();
            SpannableStringBuilder sb = new SpannableStringBuilder();
            sb.append(String.format("%-20.2f",i * 0.01) + "   ");//补齐为20位的字符串并且左对齐
            String pf = String.valueOf(newPriceFix);
            sb.append(pf);
            ForegroundColorSpan colorSpan = null;
            if (i > 0) {
                colorSpan = new ForegroundColorSpan(Color.parseColor("#FF0000"));
            } else if (i == 0) {
                colorSpan = new ForegroundColorSpan(Color.parseColor("#000000"));
            } else if (i < 0) {
                colorSpan = new ForegroundColorSpan(Color.parseColor("#00FF00"));
            }
            sb.setSpan(colorSpan, sb.length() - pf.length(), sb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            sb.append("\n");
            spannableString.append(sb);
        }
        return spannableString;
    }

    public SpannableStringBuilder getSpannabelSBOverlayWindowSimple() {
        SpannableStringBuilder spannableString = new SpannableStringBuilder();
        if (listStockCodes != null) {
            for (int i = 0; i < listStockCodes.size(); i++) {
                String stockCode = listStockCodes.get(i);
                ObservableFieldStock obsv = mapObservableFieldStock.get(stockCode);
                if (obsv != null && !TextUtils.isEmpty(obsv.price.get())) {
                    int start = spannableString.length();
                    spannableString.append(obsv.price.get());
                    if (i < listStockCodes.size() - 1) {
                        spannableString.append("\n");
                    }
                    ForegroundColorSpan colorSpan;
                    if (Double.valueOf(obsv.moneyChange.get()) > 0) {
                        colorSpan = new ForegroundColorSpan(Color.parseColor("#FF0000"));
                    } else if (Double.valueOf(obsv.moneyChange.get()) == 0) {
                        colorSpan = new ForegroundColorSpan(Color.parseColor("#FFFFFF"));
                    } else {
                        colorSpan = new ForegroundColorSpan(Color.parseColor("#00FF00"));
                    }
                    spannableString.setSpan(colorSpan, start, spannableString.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
                }
            }
        }
        return spannableString;
    }

    public int count() {
        return listStockCodes != null ? listStockCodes.size() : 0;
    }

    public ObservableFieldStock getObsvFieldStock(int pos) {
        String stockCode = listStockCodes.get(pos);
        ObservableFieldStock obsv = mapObservableFieldStock.get(stockCode);
        if (obsv == null) {
            obsv = new ObservableFieldStock();
            mapObservableFieldStock.put(stockCode, obsv);
        }
        return obsv;
    }

    public void removeStock(String code) {
        listStockCodes.remove(code);
        mapObservableFieldStock.remove(code);
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < listStockCodes.size(); i++) {
            builder.append(listStockCodes.get(i) + ",");
        }
        SharedPreferences.Editor editor = mContext.getApplicationContext().getSharedPreferences("stock", Context.MODE_PRIVATE).edit();
        editor.putString("favorite", builder.toString());
        SharedPreferencesCompat.EditorCompat.getInstance().apply(editor);
    }

    public void upStock(String code, int pos) {
        listStockCodes.remove(code);
        listStockCodes.add(pos, code);
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < listStockCodes.size(); i++) {
            builder.append(listStockCodes.get(i) + ",");
        }
        SharedPreferences.Editor editor = mContext.getApplicationContext().getSharedPreferences("stock", Context.MODE_PRIVATE).edit();
        editor.putString("favorite", builder.toString());
        SharedPreferencesCompat.EditorCompat.getInstance().apply(editor);
    }

    public void addStock(Stock stock) {
        listStockCodes.add(stock.code);
        mapObservableFieldStock.put(stock.code, ObservableFieldStock.buildWith(mContext, stock));
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < listStockCodes.size(); i++) {
            builder.append(listStockCodes.get(i) + ",");
        }
        SharedPreferences.Editor editor = mContext.getApplicationContext().getSharedPreferences("stock", Context.MODE_PRIVATE).edit();
        editor.putString("favorite", builder.toString());
        SharedPreferencesCompat.EditorCompat.getInstance().apply(editor);
    }

    public void updateObsvFieldStock(final String stockCode) {
        UTRxHttp.get().getStock(stockCode)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Stock>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull Stock stock) {
                        AiLog.i(AiLog.TAG_WZZ, "StockPool onNext 更新查询:" + stock);
                        ObservableFieldStock obsv = mapObservableFieldStock.get(stockCode);
                        if (obsv != null) {
                            obsv.updateWith(mContext, stock);
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * 刷新一次整体数值
     */
    private void refreshOneLoop() {
        if (listStockCodes != null) {
            for (int i = 0; i < listStockCodes.size(); i++) {
                String code = listStockCodes.get(i);
                updateObsvFieldStock(code);
            }
        }
    }

}
