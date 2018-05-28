package com.orieange.wcounter.model;

import android.content.Context;

import com.orieange.wcounter.data.ObservableFieldStock;
import com.orieange.wcounter.data.Stock;
import com.orieange.wcounter.data.StockPool;

/**
 * Created by Administrator on 2017/8/1.
 */

public class StockModel extends ZZBaseModel {
    public StockModel(Context context) {
        super(context);
    }

    @Override
    public int count() {
        return StockPool.getInstance(mContext).count();
    }

    @Override
    public void add(Object data) {
        StockPool.getInstance(mContext).addStock((Stock) data);
        if (rvAdapter != null) {
            rvAdapter.notifyItemInserted(count() - 1);
        }
    }

    @Override
    public void remove(int position) {
        StockPool.getInstance(mContext).removeStock(getItem(position).code.get());
        if (rvAdapter != null) {
            rvAdapter.notifyItemRemoved(position);
        }
    }

    /**
     * 将pos位置的数据向上移动1位
     *
     * @param position
     */
    public void up(int position) {
        if (position > 0) {
            StockPool.getInstance(mContext).upStock(getItem(position).code.get(), position - 1);
            if (rvAdapter != null) {
                rvAdapter.notifyItemRemoved(position);
                rvAdapter.notifyItemInserted(position - 1);
            }
        }
    }

    /**
     * 将pos位置的数据向下移动1位
     *
     * @param position
     */
    public void down(int position) {
        if (position < count() - 1) {
            StockPool.getInstance(mContext).upStock(getItem(position).code.get(), position + 1);
            if (rvAdapter != null) {
                rvAdapter.notifyItemRemoved(position);
                rvAdapter.notifyItemInserted(position + 1);
            }
        }
    }

    @Override
    public ObservableFieldStock getItem(int position) {
        return StockPool.getInstance(mContext).getObsvFieldStock(position);
    }

}
