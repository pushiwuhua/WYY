package com.orieange.wcounter.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.orieange.wcounter.BR;
import com.orieange.wcounter.R;
import com.orieange.wcounter.data.Stock;
import com.orieange.wcounter.data.StockPool;

/**
 * Created by Sasha Grey on 5/24/2016.
 */

public class StockAdapter extends RecyclerView.Adapter<StockAdapter.ViewHolderItem> {
    private Context mContext;

    public StockAdapter(Context context) {
        mContext = context;
    }

    public void addData(Stock data) {
        StockPool.getInstance(mContext).addStock(data);
    }


    @Override
    public ViewHolderItem onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewDataBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_sheet_stock, parent, false);
        return new ViewHolderItem(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolderItem holder, int position) {
        holder.mViewDataBinding.setVariable(BR.stock, StockPool.getInstance(mContext).getObsvFieldStock(position));
    }

    @Override
    public int getItemCount() {
        return StockPool.getInstance(mContext).count();
    }

    class ViewHolderItem extends RecyclerView.ViewHolder {

        ViewDataBinding mViewDataBinding;

        public ViewHolderItem(ViewDataBinding viewDataBinding) {
            super(viewDataBinding.getRoot());

            mViewDataBinding = viewDataBinding;
//            mViewDataBinding.executePendingBindings();
        }

        public ViewDataBinding getViewDataBinding() {
            return mViewDataBinding;
        }
    }
}
