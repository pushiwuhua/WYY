package com.orieange.wcounter.recyclerview;

import android.content.Context;
import android.view.LayoutInflater;

import com.orieange.wcounter.model.ZZBaseModel;
import com.orieange.wcounter.recyclerview.base.ZZItemViewDelegate;
import com.orieange.wcounter.recyclerview.base.ZZViewHolder;

import java.util.List;

/**
 * ZZCommonAdapter 通用适配器
 * wzz created at 2017/7/31 20:16
 */
public abstract class ZZCommonAdapter<T> extends ZZMultiItemTypeAdapter<T> {
    protected int mLayoutId;
    protected LayoutInflater mInflater;

    public ZZCommonAdapter(final Context context, final int layoutId, ZZBaseModel<T> baseModel) {
        super(context, baseModel);
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mLayoutId = layoutId;

        addItemViewDelegate(new ZZItemViewDelegate<T>() {
            @Override
            public int getItemViewLayoutId() {
                return layoutId;
            }

            @Override
            public boolean isForViewType(T item, int position) {
                return true;
            }

            @Override
            public void convert(ZZViewHolder holder, T t, int position) {
                ZZCommonAdapter.this.convert(holder, t, position);
            }
        });
    }

    protected abstract void convert(ZZViewHolder holder, T t, int position);


}
