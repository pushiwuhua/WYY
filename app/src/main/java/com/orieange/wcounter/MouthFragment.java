package com.orieange.wcounter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.orieange.wcounter.data.ObservableOneItem;
import com.orieange.wcounter.databinding.FragmentMouthBinding;
import com.orieange.wcounter.model.ZZBaseModel;
import com.orieange.wcounter.recyclerview.ZZCommonAdapter;
import com.orieange.wcounter.recyclerview.base.ZZViewHolder;
import com.orieange.wcounter.viewModel.Mouth;


public class MouthFragment extends Fragment {
    private ZZCommonAdapter mAdapter;

    public MouthFragment() {
    }

    public static MouthFragment newInstance() {
        MouthFragment fragment = new MouthFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mouth, container, false);
        FragmentMouthBinding dataBinding = DataBindingUtil.bind(view);
        Mouth vmMouth = new Mouth(getContext(), dataBinding);//生成本视图的viewmodel
        dataBinding.setVariable(BR.mouth, vmMouth);

        SpannableStringBuilder spannableString = new SpannableStringBuilder("##计算");//##用于替换图片
        Drawable drawablePos = ContextCompat.getDrawable(getContext(), R.drawable.count);
        drawablePos.setBounds(0, 0, dip2px(getContext(), 16), dip2px(getContext(), 16));
        DrawableCompat.setTint(drawablePos, Color.WHITE);
        ImageSpan imageSpan = new ImageSpan(drawablePos);
        spannableString.setSpan(imageSpan, 0, 2, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        dataBinding.buttonCount.setText(spannableString);
        dataBinding.buttonCount.setAllCaps(false);//让spannable生效

        //设置线性布局 Creates a vertical LinearLayoutManager
        dataBinding.rvSheet.setLayoutManager(new LinearLayoutManager(getContext()));
        //设置recyclerView每个item间的分割线
        DividerItemDecoration itemDecoration = new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL);
        itemDecoration.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.divider));
        dataBinding.rvSheet.addItemDecoration(itemDecoration);

        final ZZBaseModel<ObservableOneItem> model = new ZZBaseModel<>(getContext());
        vmMouth.setRvModel(model);//viewmodel设置列表的数据model
        mAdapter = new ZZCommonAdapter<ObservableOneItem>(getContext(), R.layout.vdb_item_sheet, model) {
            @Override
            protected void convert(ZZViewHolder vh, final ObservableOneItem oneItem, int position) {
                vh.getBind().setVariable(BR.oneItem, oneItem);
            }
        };

        //设置适配器
        dataBinding.rvSheet.setAdapter(mAdapter);
        return view;

    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     *
     * @param context
     * @param dpValue 要转换的dp值
     */
    public static int dip2px(Context context, float dpValue) {
        if (context != null) {
            final float scale = context.getResources().getDisplayMetrics().density;
            return (int) (dpValue * scale + 0.5f);
        } else {
            return 0;
        }
    }
}
