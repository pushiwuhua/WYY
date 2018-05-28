package com.orieange.wcounter.viewModel;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.orieange.wcounter.AiLog;
import com.orieange.wcounter.data.CountUtil;
import com.orieange.wcounter.data.ObservableOneItem;
import com.orieange.wcounter.databinding.FragmentMouthBinding;
import com.orieange.wcounter.model.ZZBaseModel;

/**
 * Created by Administrator on 2017/8/2.
 */

public class Mouth {
    private Context mContext;
    private FragmentMouthBinding mVDB;
    private ZZBaseModel<ObservableOneItem> rvModel;//列表的数据model

    public Mouth(Context context, FragmentMouthBinding vdb) {
        mContext = context;
        mVDB = vdb;
    }

    /**
     * 计算
     *
     * @param view
     */
    public void onItemCount(View view) {
        AiLog.i(AiLog.TAG_WZZ, "Mouth count");
        if (TextUtils.isEmpty(mVDB.editTextMoney.getText()) || TextUtils.isEmpty(mVDB.editTextRate.getText())) {
            Toast.makeText(mContext, "请填入计算数据", Toast.LENGTH_SHORT).show();
            return;
        }

        double money = Double.valueOf(mVDB.editTextMoney.getText().toString());
        String[] rateStrArr = mVDB.editTextRate.getText().toString().split(",");

        boolean isOneByOne = mVDB.switchOneByOne.isChecked();//是否打开定存开关

        rvModel.clear();
        for (int i = 1; i <= 30; i++) {
            for (String rate : rateStrArr) {
                if (isOneByOne) {
                    rvModel.add(CountUtil.countWithOneByOne(mContext, money, i, Double.valueOf(rate)));
                } else {
                    rvModel.add(CountUtil.countWith(mContext, money, i, Double.valueOf(rate)));
                }
            }
        }
    }

    /**
     * 设置列表数据model
     *
     * @param model
     */
    public void setRvModel(ZZBaseModel<ObservableOneItem> model) {
        rvModel = model;
    }
}
