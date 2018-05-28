package com.orieange.wcounter.recyclerview.base;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

public class ZZViewHolder extends RecyclerView.ViewHolder {
    ViewDataBinding mViewDataBinding;
    private Context mContext;

    public ZZViewHolder(Context context, ViewDataBinding viewDataBinding) {
        super(viewDataBinding.getRoot());
        mContext = context;
        mViewDataBinding = viewDataBinding;
    }

    public static ZZViewHolder createViewHolder(Context context,
                                                ViewGroup parent, int layoutId) {

        ViewDataBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), layoutId, parent, false);
        ZZViewHolder holder = new ZZViewHolder(context, binding);
        return holder;
    }

    public ViewDataBinding getBind() {
        return mViewDataBinding;
    }

}
