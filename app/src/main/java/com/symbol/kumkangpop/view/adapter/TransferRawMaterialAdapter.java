package com.symbol.kumkangpop.view.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.symbol.kumkangpop.R;
import com.symbol.kumkangpop.databinding.RowTransferRawMaterialBinding;
import com.symbol.kumkangpop.model.SearchCondition;
import com.symbol.kumkangpop.model.object.StockOut3Detail;
import com.symbol.kumkangpop.model.object.Users;
import com.symbol.kumkangpop.viewmodel.CommonViewModel;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class TransferRawMaterialAdapter extends RecyclerView.Adapter<TransferRawMaterialAdapter.ViewHolder> implements Filterable {

    /*
     LinearLayout layoutTop;
     String contractNo;
     String fromDate;*/
    Context context;
    private ArrayList<StockOut3Detail> items = new ArrayList<>();
    boolean confirmFlag;
    ActivityResultLauncher<Intent> resultLauncher;

    ArrayList<StockOut3Detail> unFilteredlist;//for filter
    ArrayList<StockOut3Detail> filteredList;//for filter
    CommonViewModel commonViewModel;
    String inputData;

    public TransferRawMaterialAdapter(ArrayList<StockOut3Detail> items, Context context, ActivityResultLauncher<Intent> resultLauncher, CommonViewModel commonViewModel) {
        this.context = context;
        this.items = items;
        this.resultLauncher = resultLauncher;
        this.unFilteredlist = items;
        this.filteredList = items;
        this.commonViewModel = commonViewModel;
    }


   /* public ProgressFloorReturnViewAdapter(Context context, LinearLayout layoutTop, String contractNo, String fromDate) {
        super();
        this.context = context;
        this.layoutTop = layoutTop;
        this.contractNo = contractNo;
        this.fromDate = fromDate;
    }*/

    public void updateAdapter(ArrayList<StockOut3Detail> newCountries, String inputData) {
        items.clear();
        items.addAll(newCountries);
        this.inputData = inputData;
        notifyDataSetChanged();

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        RowTransferRawMaterialBinding binding = DataBindingUtil.inflate(inflater, R.layout.row_transfer_raw_material, viewGroup, false);
        return new TransferRawMaterialAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        //StockOut3Detail item = items.get(position);
        StockOut3Detail item = filteredList.get(position);//for filter
        viewHolder.setItem(item, position);
    }

    @Override
    public int getItemCount() {
        //return items.size();
        return filteredList.size();//for filter

    }

    //for filter
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString();
                if (charString.isEmpty()) {
                    filteredList = unFilteredlist;
                } else {
                    ArrayList<StockOut3Detail> filteringList = new ArrayList<>();
                    for (StockOut3Detail stockOut3Detail : unFilteredlist) {
                        if (stockOut3Detail.ItemTag.toLowerCase().contains(charString.toLowerCase())) {
                            filteringList.add(stockOut3Detail);
                        }
                    }
                    filteredList = filteringList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredList = (ArrayList<StockOut3Detail>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    //보통은 ViewHolder를 Static 으로 쓴다.
    //범용성을 위해서, 나는 제거함
    class ViewHolder extends RecyclerView.ViewHolder {
        RowTransferRawMaterialBinding binding;
        //View row;

        public ViewHolder(RowTransferRawMaterialBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void setItem(StockOut3Detail item, int position) {
            DecimalFormat numFormatter = new DecimalFormat("###,###");

            binding.tvItemTag.setText(item.ItemTag);
            binding.tvLocation.setText(item.StockOutLocationName);
            binding.tvPartName.setText(item.PartName);
            binding.tvPartSpec.setText(item.PartSpec);
            binding.tvQty.setText(numFormatter.format(item.ItemCnt));
            if(item.ItemTag.equals(inputData))
                binding.linearLayout.setBackgroundColor(Color.YELLOW);
            else{
                binding.linearLayout.setBackgroundResource(R.drawable.borderline_bottom3);
                //binding.getRoot().setBackgroundResource(R.drawable.borderline_bottom);
                //binding.getRoot().setBackgroundColor(Color.parseColor("#FFFFFF"));
            }

            binding.getRoot().setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    String title="창고 이동 취소";
                    String message = "TAG NO: " + item.ItemTag + "\n"
                            + "품명: "+item.PartName + "\n"
                            + "규격: "+item.PartSpec + "\n"
                            + "수량: "+ numFormatter.format(item.ItemCnt) + "\n"
                            +"창고 이동한 데이터를 취소하시겠습니까?";
                    String okButton = "예";
                    String cancelButton = "아니요";


                    if(Users.Language != 0){
                        title = "Cancellation of warehouse movement";
                        message = "TAG NO: " + item.ItemTag + "\n"
                                + "PartName: "+item.PartName + "\n"
                                + "Spec: "+item.PartSpec + "\n"
                                + "Qty: "+ numFormatter.format(item.ItemCnt) + "\n"
                                +"Are you sure you want to cancel the data moved to the warehouse?";
                        okButton = "YES";
                        cancelButton = "NO";
                    }

                    v.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
                    new MaterialAlertDialogBuilder(context)
                            .setTitle(title)
                            .setMessage(message)
                            .setCancelable(true)
                            .setPositiveButton
                                    (okButton, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            DeleteTransferRawMaterial(item.ItemTag);
                                        }
                                    }).setNegativeButton(cancelButton, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).show();
                    return false;
                }
            });

        }
    }

    private void DeleteTransferRawMaterial(String itemTag){
        SearchCondition sc = new SearchCondition();
        sc.ItemTag = itemTag;
        sc.StockOutType = 11;//11:창고이동, 12:투입불출
        commonViewModel.Get3("StockOutDeleteAll", sc);
    }

   /* private void GoActivity1100(String result){
        Intent intent = new Intent(context, Activity1100.class);
        intent.putExtra("result", result);
        resultLauncher.launch(intent);
    }*/

    public void addItem(StockOut3Detail item) {
        items.add(item);
    }

    public void setItems(ArrayList<StockOut3Detail> items) {
        this.items = items;
    }

    public StockOut3Detail getItem(int position) {
        return items.get(position);
    }

    public ArrayList<StockOut3Detail> getItemList() {
        return items;
    }

    public void setItem(int position, StockOut3Detail item) {
        items.set(position, item);
    }

    public void setConfirmFlag(boolean confirmFlag) {
        this.confirmFlag = confirmFlag;
    }
}

