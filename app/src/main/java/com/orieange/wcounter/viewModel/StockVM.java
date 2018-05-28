package com.orieange.wcounter.viewModel;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.orieange.wcounter.AiLog;
import com.orieange.wcounter.data.CountUtil;
import com.orieange.wcounter.data.ObservableFieldStock;
import com.orieange.wcounter.data.ObservableOneItem;
import com.orieange.wcounter.data.Stock;
import com.orieange.wcounter.data.UTRxHttp;
import com.orieange.wcounter.databinding.FragmentMouthBinding;
import com.orieange.wcounter.databinding.FragmentOverlayconfBinding;
import com.orieange.wcounter.model.StockModel;
import com.orieange.wcounter.model.ZZBaseModel;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * Created by Administrator on 2017/8/2.
 */
public class StockVM {
    private Context mContext;
    private FragmentOverlayconfBinding mVDB;
    private StockModel rvModel;//列表的数据model

    public StockVM(Context context, FragmentOverlayconfBinding vdb) {
        mContext = context;
        mVDB = vdb;
    }

    /**
     * 添加
     *
     * @param view
     */
    public void onItemAdd(View view) {
        AiLog.i(AiLog.TAG_WZZ, "StockVM onItemAdd");
        if (TextUtils.isEmpty(mVDB.editTextCode.getText().toString())) {
            Snackbar.make(view, "请输入有效的沪深股市股票代码", Snackbar.LENGTH_SHORT).show();
        } else {
            addStockCode(mVDB.editTextCode.getText().toString());
        }
    }

    /**
     * get请求
     *
     * @param code 股票代码
     */
    public void addStockCode(String code) {
        AiLog.i(AiLog.TAG_WZZ, "StockFragment addStockCode");
        UTRxHttp.get().getStock(code)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Stock>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull Stock stock) {
                        AiLog.i(AiLog.TAG_WZZ, "StockFragment onNext 查询结果:" + stock);
                        rvModel.add(stock);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        AiLog.i(AiLog.TAG_WZZ, "StockFragment onError:" + e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * 设置列表数据model
     *
     * @param model
     */
    public void setRvModel(StockModel model) {
        rvModel = model;
    }
}
