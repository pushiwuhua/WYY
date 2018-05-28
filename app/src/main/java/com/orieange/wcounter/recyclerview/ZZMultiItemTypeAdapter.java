package com.orieange.wcounter.recyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.orieange.wcounter.model.ZZBaseModel;
import com.orieange.wcounter.recyclerview.base.ZZItemViewDelegate;
import com.orieange.wcounter.recyclerview.base.ZZItemViewDelegateManager;
import com.orieange.wcounter.recyclerview.base.ZZViewHolder;

import java.util.List;

/**
 * Created by zhy on 16/4/9.
 */
public class ZZMultiItemTypeAdapter<T> extends RecyclerView.Adapter<ZZViewHolder> {
    protected Context mContext;
    protected ZZBaseModel<T> baseModel;

    protected ZZItemViewDelegateManager mItemViewDelegateManager;
    protected OnItemClickListener mOnItemClickListener;


    public ZZMultiItemTypeAdapter(Context context, ZZBaseModel<T> baseModel) {
        mContext = context;
        mItemViewDelegateManager = new ZZItemViewDelegateManager();
        this.baseModel = baseModel;
        this.baseModel.registAdapter(this);
    }

    @Override
    public int getItemViewType(int position) {
        if (!useItemViewDelegateManager()) return super.getItemViewType(position);
        return mItemViewDelegateManager.getItemViewType(baseModel.getItem(position), position);
    }


    @Override
    public ZZViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ZZItemViewDelegate itemViewDelegate = mItemViewDelegateManager.getItemViewDelegate(viewType);
        int layoutId = itemViewDelegate.getItemViewLayoutId();
        ZZViewHolder holder = ZZViewHolder.createViewHolder(mContext, parent, layoutId);
        setListener(parent, holder, viewType);
        return holder;
    }


    public void convert(ZZViewHolder holder, T t) {
        mItemViewDelegateManager.convert(holder, t, holder.getAdapterPosition());
    }

    protected boolean isEnabled(int viewType) {
        return true;
    }


    protected void setListener(final ViewGroup parent, final ZZViewHolder viewHolder, int viewType) {
        if (!isEnabled(viewType)) return;
        viewHolder.getBind().getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    int position = viewHolder.getAdapterPosition();
                    mOnItemClickListener.onItemClick(v, viewHolder, position);
                }
            }
        });

        viewHolder.getBind().getRoot().setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mOnItemClickListener != null) {
                    int position = viewHolder.getAdapterPosition();
                    return mOnItemClickListener.onItemLongClick(v, viewHolder, position);
                }
                return false;
            }
        });
    }

    @Override
    public void onBindViewHolder(ZZViewHolder holder, int position) {
        convert(holder, baseModel.getItem(position));
    }

    @Override
    public int getItemCount() {
        return baseModel.count();
    }

    public List<T> getDatas() {
        return baseModel.getDatas();
    }

    public ZZMultiItemTypeAdapter addItemViewDelegate(ZZItemViewDelegate<T> itemViewDelegate) {
        mItemViewDelegateManager.addDelegate(itemViewDelegate);
        return this;
    }

    public ZZMultiItemTypeAdapter addItemViewDelegate(int viewType, ZZItemViewDelegate<T> itemViewDelegate) {
        mItemViewDelegateManager.addDelegate(viewType, itemViewDelegate);
        return this;
    }

    protected boolean useItemViewDelegateManager() {
        return mItemViewDelegateManager.getItemViewDelegateCount() > 0;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, RecyclerView.ViewHolder holder, int position);

        boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }
}
