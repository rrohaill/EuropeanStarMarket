package com.star.market.europeanstarmarket.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.star.market.europeanstarmarket.R;
import com.star.market.europeanstarmarket.activities.CheckoutActivity;
import com.star.market.europeanstarmarket.activities.Constants;
import com.star.market.europeanstarmarket.models.SubProductModel;

import java.util.ArrayList;

/**
 * Created by rohail on 11/14/2016.
 */

public class SubProductListAdapter extends RecyclerView.Adapter<SubProductListAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<SubProductModel> productModelArrayList;
    private IOnItemClickCustomListner iOnItemClickCustomListner;

    public SubProductListAdapter(Context context, ArrayList<SubProductModel> listSubCategoryModels, IOnItemClickCustomListner iOnItemClickCustomListner) {
        this.context = context;
        this.productModelArrayList = listSubCategoryModels;
        this.iOnItemClickCustomListner = iOnItemClickCustomListner;
    }

    @Override
    public int getItemCount() {
        return productModelArrayList.size();
    }

    @Override
    public void onBindViewHolder(SubProductListAdapter.MyViewHolder holder, final int position) {

        holder.tvType.setText(productModelArrayList.get(position).getName());
        holder.tvCount.setText("AED " + productModelArrayList.get(position).getPrice());
        Picasso.with(context).load(Constants.baseUrl.concat(productModelArrayList.get(position).getImagelink())).
                placeholder(R.drawable.stub).into(holder.icon);
//        android.view.ViewGroup.LayoutParams layoutParams = holder.icon.getLayoutParams();
//        layoutParams.width = 320;
//        layoutParams.height = layoutParams.MATCH_PARENT;
//        holder.icon.setLayoutParams(layoutParams);
        if (context instanceof CheckoutActivity) {
            holder.btnCart.setText("Remove");
        }
        holder.btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iOnItemClickCustomListner.onItemClicked(view, position);
            }
        });

    }


    @Override
    public SubProductListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sub_product_list_item, parent, false);
        return new SubProductListAdapter.MyViewHolder(itemView);
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView icon;
        public TextView tvType;
        public TextView tvCount;
        public LinearLayout llMain;
        public Button btnCart;

        public MyViewHolder(View itemView) {
            super(itemView);
            icon = (ImageView) itemView.findViewById(R.id.iv_icon);
            tvType = (TextView) itemView.findViewById(R.id.tv_type);
            tvCount = (TextView) itemView.findViewById(R.id.tv_count);
            llMain = (LinearLayout) itemView.findViewById(R.id.ll_main);
            btnCart = (Button) itemView.findViewById(R.id.btn_cart);
        }
    }
}
