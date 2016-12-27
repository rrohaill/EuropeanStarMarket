package com.star.market.europeanstarmarket.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.star.market.europeanstarmarket.R;
import com.star.market.europeanstarmarket.models.HistoryModel;

import java.util.ArrayList;

/**
 * Created by rohail on 12/3/2016.
 */

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<HistoryModel> statementItems;

    public HistoryAdapter(Context context, ArrayList<HistoryModel> statementItems) {
        this.context = context;
        this.statementItems = statementItems;
    }

    @Override
    public int getItemCount() {
        return statementItems.size();
    }

    @Override
    public void onBindViewHolder(HistoryAdapter.MyViewHolder holder, final int position) {

        holder.name.setText("Item: " + statementItems.get(position).getName());
        holder.quantity.setText("Quantity: " + statementItems.get(position).getQty());
        holder.price.setText("Price: " + statementItems.get(position).getPrice());
        holder.date.setText("Date: " + statementItems.get(position).getDate());

    }


    @Override
    public HistoryAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.history_item, parent, false);
        return new HistoryAdapter.MyViewHolder(itemView);
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView date;
        public TextView name;
        public TextView quantity;
        public TextView price;

        public MyViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.tv_name);
            date = (TextView) itemView.findViewById(R.id.tv_date);
            quantity = (TextView) itemView.findViewById(R.id.tv_quantity);
            price = (TextView) itemView.findViewById(R.id.tv_price);
        }
    }

}
