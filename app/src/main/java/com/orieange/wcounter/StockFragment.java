package com.orieange.wcounter;

import android.annotation.TargetApi;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.orieange.wcounter.data.ObservableFieldStock;
import com.orieange.wcounter.data.StockPool;
import com.orieange.wcounter.databinding.FragmentOverlayconfBinding;
import com.orieange.wcounter.databinding.ItemSheetStockBinding;
import com.orieange.wcounter.model.StockModel;
import com.orieange.wcounter.recyclerview.ZZCommonAdapter;
import com.orieange.wcounter.recyclerview.base.ZZViewHolder;
import com.orieange.wcounter.service.FloatWindowService;
import com.orieange.wcounter.view.AiTextDialog;
import com.orieange.wcounter.viewModel.StockVM;

import static com.orieange.wcounter.Constants.OVERLAY_PERMISSION_REQ_CODE;

public class StockFragment extends Fragment {
    private ZZCommonAdapter mAdapter;

    public StockFragment() {
    }

    public static StockFragment newInstance() {
        StockFragment fragment = new StockFragment();
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        StockPool.getInstance(getContext()).registWatcher(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        StockPool.getInstance(getContext()).unregistWatcher(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FragmentOverlayconfBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_overlayconf, container, false);
        StockVM vmStock = new StockVM(getContext(), dataBinding);//生成本视图的viewmodel
        dataBinding.setVariable(BR.stockvm, vmStock);

        SpannableStringBuilder spannableString = new SpannableStringBuilder("添加");//##用于替换图片
//        spannableString.setSpan(imageSpan, 0, 2, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        dataBinding.buttonAdd.setText(spannableString);

        dataBinding.switchOpen.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                AiLog.i(AiLog.TAG_WZZ, "StockFragment onCheckedChanged:" + b);
                if (b) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(getActivity().getApplicationContext())) {
                        AiLog.i(AiLog.TAG_WZZ, "StockFragment onCheckedChanged 没有权限");
                        Toast.makeText(getContext(), getResources().getString(R.string.permission_alert), Toast.LENGTH_LONG).show();
                        Intent permissionIntent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                                Uri.parse("package:" + getActivity().getPackageName()));
                        startActivityForResult(permissionIntent, OVERLAY_PERMISSION_REQ_CODE);
                    } else {
                        AiLog.i(AiLog.TAG_WZZ, "StockFragment onCheckedChanged 已有权限");
                        Intent intent = new Intent(getContext(), FloatWindowService.class);
                        getContext().startService(intent);
                    }
                } else {
                    Intent intent = new Intent(getContext(),
                            FloatWindowService.class);
                    getContext().stopService(intent);
                }
            }
        });

        //设置线性布局 Creates a vertical LinearLayoutManager
        dataBinding.rvSheet.setLayoutManager(new LinearLayoutManager(getContext()));
        //设置recyclerView每个item间的分割线
        DividerItemDecoration itemDecoration = new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL);
        itemDecoration.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.divider));
        dataBinding.rvSheet.addItemDecoration(itemDecoration);

        final StockModel model = new StockModel(getContext());
        vmStock.setRvModel(model);//viewmodel设置列表的数据model
        mAdapter = new ZZCommonAdapter<ObservableFieldStock>(getContext(), R.layout.item_sheet_stock, model) {
            @Override
            protected void convert(final ZZViewHolder vh, final ObservableFieldStock stockItem, int position) {
                vh.getBind().setVariable(BR.stock, stockItem);
                final ItemSheetStockBinding binding = (ItemSheetStockBinding) vh.getBind();
                binding.viewMore.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        binding.layoutMore.setVisibility(binding.layoutMore.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                    }
                });
                binding.optionDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        model.remove(vh.getAdapterPosition());
                    }
                });
                binding.optionUp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        model.up(vh.getAdapterPosition());
                    }
                });
                binding.optionDown.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        model.down(vh.getAdapterPosition());
                    }
                });
                binding.optionCount.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SpannableStringBuilder builder = StockPool.quickCountStockPrice(stockItem);
                        AiTextDialog.open(getFragmentManager(), builder);
                    }
                });
            }
        };
        dataBinding.rvSheet.setAdapter(mAdapter);

        return dataBinding.getRoot();
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == OVERLAY_PERMISSION_REQ_CODE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(getContext().getApplicationContext())) {
                Toast.makeText(getContext(), getResources().getString(R.string.permission_result), Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(getContext(), FloatWindowService.class);
                getActivity().startService(intent);
            }
        }
    }
}
