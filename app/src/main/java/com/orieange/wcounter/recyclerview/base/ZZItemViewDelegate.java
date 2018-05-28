package com.orieange.wcounter.recyclerview.base;


/**
 * Created by zhy on 16/6/22.
 */
public interface ZZItemViewDelegate<T>
{

    int getItemViewLayoutId();

    boolean isForViewType(T item, int position);

    void convert(ZZViewHolder holder, T t, int position);

}
