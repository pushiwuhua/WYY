package com.orieange.wcounter.model;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/8/1.
 */

public class ZZBaseModel<T> {
    protected Context mContext;
    protected List<T> mDatas;
    protected RecyclerView.Adapter rvAdapter;//注入的适配器对象

    public ZZBaseModel<T> registAdapter(RecyclerView.Adapter adapter) {
        rvAdapter = adapter;
        return this;
    }

    public ZZBaseModel(Context context) {
        mDatas = new ArrayList<>();
        mContext = context;
    }

    public T getItem(int position) {
        if (mDatas != null && position < mDatas.size()) {
            return mDatas.get(position);
        }
        return null;
    }

    public List<T> getDatas() {
        return mDatas;
    }

    public int count() {
        return mDatas != null ? mDatas.size() : 0;
    }

    public void add(T item) {
        mDatas.add(item);

        if (rvAdapter != null) {
            rvAdapter.notifyItemInserted(mDatas.size() - 1);
        }
    }

    public void remove(int position) {
        mDatas.remove(position);
        if (rvAdapter != null) {
            rvAdapter.notifyItemInserted(mDatas.size() - 1);
        }
    }

    public void clear() {
        if (mDatas != null) {
            mDatas.clear();
        }
        if (rvAdapter != null) {
            rvAdapter.notifyDataSetChanged();
        }
    }
}
