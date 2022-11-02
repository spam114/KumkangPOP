package com.symbol.kumkangpop.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.symbol.kumkangpop.R;
import com.symbol.kumkangpop.databinding.Row0300Binding;
import com.symbol.kumkangpop.model.object.ScanListViewItem;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class Adapter0300 extends RecyclerView.Adapter<Adapter0300.ViewHolder> {

    /*
     LinearLayout layoutTop;
     String contractNo;
     String fromDate;*/
    Context context;
    private ArrayList<ScanListViewItem> items = new ArrayList<>();
    boolean confirmFlag;
    ActivityResultLauncher<Intent> resultLauncher;

    public Adapter0300(ArrayList<ScanListViewItem> items, Context context, ActivityResultLauncher<Intent> resultLauncher) {
        this.context = context;
        this.items = items;
        this.resultLauncher = resultLauncher;
    }


   /* public ProgressFloorReturnViewAdapter(Context context, LinearLayout layoutTop, String contractNo, String fromDate) {
        super();
        this.context = context;
        this.layoutTop = layoutTop;
        this.contractNo = contractNo;
        this.fromDate = fromDate;
    }*/

    public void updateAdapter(ArrayList<ScanListViewItem> newCountries) {
        items.clear();
        items.addAll(newCountries);
        notifyDataSetChanged();

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        Row0300Binding binding = DataBindingUtil.inflate(inflater, R.layout.row0300, viewGroup, false);
        return new Adapter0300.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        ScanListViewItem item = items.get(position);
        viewHolder.setItem(item, position); //왜오류
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    //보통은 ViewHolder를 Static 으로 쓴다.
    //범용성을 위해서, 나는 제거함
    class ViewHolder extends RecyclerView.ViewHolder {
        Row0300Binding binding;
        //View row;

        public ViewHolder(Row0300Binding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void setItem(ScanListViewItem item, int position) {
            DecimalFormat numFormatter = new DecimalFormat("###,###");
            //binding.tvPartName.setText(item.PartName);
            //textViewExYearMonth.setText(((Dong) data.get(position)).ExProgressDate);
            //binding.tvPartSpec.setText(item.PartSpec);
            //binding.tvReceivedQty.setText(numFormatter.format(item.ReceivedQty));
            binding.tvPartName.setText(item.PartName);
            binding.tvPartSpec.setText(item.PartSpec);
            binding.tvMspec.setText(item.MSpec);
            binding.tvHo.setText(item.Ho);
            binding.tvDrawingNo.setText(item.DrawingNo);
            binding.tvTagNo.setText(item.ItemTag);

            if(item.PackingNo!=null){
                binding.tvPartName.setText(item.PackingNo);
            }
            if(item.Dong!=null){
                binding.tvPartSpec.setText(item.Dong);
            }

            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*if(confirmFlag)
                        GoActivity1100(item.WorderLot);*/
                    //Toast.makeText(context, "Test", Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

    public void addItem(ScanListViewItem item) {
        items.add(item);
    }

    public void setItems(ArrayList<ScanListViewItem> items) {
        this.items = items;
    }

    public ScanListViewItem getItem(int position) {
        return items.get(position);
    }

    public void setItem(int position, ScanListViewItem item) {
        items.set(position, item);
    }

    public void setConfirmFlag(boolean confirmFlag) {
        this.confirmFlag = confirmFlag;
    }
}


