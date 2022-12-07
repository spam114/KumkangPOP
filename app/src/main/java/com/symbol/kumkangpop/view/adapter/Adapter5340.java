package com.symbol.kumkangpop.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.symbol.kumkangpop.R;
import com.symbol.kumkangpop.databinding.Row5340Binding;
import com.symbol.kumkangpop.model.object.Customer;
import com.symbol.kumkangpop.view.dialog.Dialog5340;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class Adapter5340 extends RecyclerView.Adapter<Adapter5340.ViewHolder> implements Filterable {

    /*
     LinearLayout layoutTop;
     String contractNo;
     String fromDate;*/
    Context context;
    private ArrayList<Customer> items = new ArrayList<>();
    boolean confirmFlag;
    //ActivityResultLauncher<Intent> resultLauncher;

    ArrayList<Customer> unFilteredlist;//for filter
    ArrayList<Customer> filteredList;//for filter

    Dialog5340 dialog5340;

    public Adapter5340(ArrayList<Customer> items, Context context, Dialog5340 dialog5340) {
        this.context = context;
        this.items = items;
        this.unFilteredlist = items;
        this.filteredList = items;
        this.dialog5340=dialog5340;
    }

    public void updateAdapter(ArrayList<Customer> newCountries) {
        items.clear();
        items.addAll(newCountries);
        notifyDataSetChanged();

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        Row5340Binding binding = DataBindingUtil.inflate(inflater, R.layout.row5340, viewGroup, false);
        return new Adapter5340.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        //Customer item = items.get(position);
        Customer item = filteredList.get(position);//for filter
        viewHolder.setItem(item, position); //왜오류
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
                    ArrayList<Customer> filteringList = new ArrayList<>();
                    for (Customer Customer : unFilteredlist) {
                        if (Customer.CustomerName.toLowerCase().contains(charString.toLowerCase())) {
                            filteringList.add(Customer);
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
                filteredList = (ArrayList<Customer>) results.values;
                notifyDataSetChanged();
            }
        };
    }


    //보통은 ViewHolder를 Static 으로 쓴다.
    //범용성을 위해서, 나는 제거함
    class ViewHolder extends RecyclerView.ViewHolder {
        Row5340Binding binding;
        //View row;

        public ViewHolder(Row5340Binding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void setItem(Customer item, int position) {
            DecimalFormat numFormatter = new DecimalFormat("###,###");
            //binding.tvPartName.setText(item.PartName);
            //textViewExYearMonth.setText(((Dong) data.get(position)).ExProgressDate);
            //binding.tvPartSpec.setText(item.PartSpec);
            //binding.tvReceivedQty.setText(numFormatter.format(item.ReceivedQty));
            binding.tvCustomerCode.setText(item.CustomerCode);
            binding.tvCustomerName.setText(item.CustomerName);

            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (item.CustomerCode.equals(""))
                        return;

                    dialog5340.customerCode = item.CustomerCode;
                    dialog5340.dismiss();

                    //CommonMethod.FNBarcodeConvertPrint(item.CustomerNo, barcodeConvertPrintViewModel);
                    /*if(confirmFlag)
                        GoActivity1100(item.WorderLot);*/
                    //Toast.makeText(context, "Test", Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

    public void addItem(Customer item) {
        items.add(item);
        notifyDataSetChanged();
    }

    public void setItems(ArrayList<Customer> items) {
        this.items = items;
    }

    public Customer getItem(int position) {
        return items.get(position);
    }

    public ArrayList<Customer> getItemList() {
        return items;
    }

    public void setItem(int position, Customer item) {
        items.set(position, item);
    }

    public void setConfirmFlag(boolean confirmFlag) {
        this.confirmFlag = confirmFlag;
    }
}


