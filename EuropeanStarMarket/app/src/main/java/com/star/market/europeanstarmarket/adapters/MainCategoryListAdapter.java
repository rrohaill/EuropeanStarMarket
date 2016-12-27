package com.star.market.europeanstarmarket.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.star.market.europeanstarmarket.R;
import com.star.market.europeanstarmarket.activities.Constants;
import com.star.market.europeanstarmarket.activities.MainCatagoryActivity;
import com.star.market.europeanstarmarket.activities.SubCategoryActivity;
import com.star.market.europeanstarmarket.models.MainCategoryModel;
import com.star.market.europeanstarmarket.models.SubCategoryModel;

import java.util.ArrayList;

/**
 * Created by rohail on 11/12/2016.
 */

public class MainCategoryListAdapter extends RecyclerView.Adapter<MainCategoryListAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<MainCategoryModel> statementItems;
    private ArrayList<SubCategoryModel> listSubCategoryModels;
    private IOnItemClickCustomListner iOnItemClickCustomListner;
    private Integer[] mThumbIds;


    public MainCategoryListAdapter(Context context, ArrayList<MainCategoryModel> statementItems, Integer[] ids, IOnItemClickCustomListner iOnItemClickCustomListner) {
        this.context = context;
        this.statementItems = statementItems;
        this.iOnItemClickCustomListner = iOnItemClickCustomListner;
        this.mThumbIds = ids;
    }

    public MainCategoryListAdapter(Context context, ArrayList<SubCategoryModel> statementItems, IOnItemClickCustomListner iOnItemClickCustomListner) {
        this.context = context;
        this.listSubCategoryModels = statementItems;
        this.iOnItemClickCustomListner = iOnItemClickCustomListner;
    }

    @Override
    public int getItemCount() {
        int size = 0;
        if (context instanceof MainCatagoryActivity)
            size = statementItems.size();
        else if (context instanceof SubCategoryActivity)
            size = listSubCategoryModels.size();

        return size;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        if (context instanceof MainCatagoryActivity) {
            holder.tvType.setText(statementItems.get(position).getName());
            holder.tvCount.setText("Sub Categories: " + statementItems.get(position).getSubCatCount());
            if (mThumbIds[position] != null) {
                holder.icon.setBackgroundResource(mThumbIds[position]);
            } else {
                holder.icon.setBackgroundResource(R.drawable.stub);
            }
        } else if (context instanceof SubCategoryActivity) {
            holder.tvType.setText(listSubCategoryModels.get(position).getName());
            holder.tvCount.setText("Total Products: " + listSubCategoryModels.get(position).getProductcount());
            Picasso.with(context).load(Constants.baseUrl.concat(listSubCategoryModels.get(position).getImagelink())).
                    placeholder(R.drawable.stub).into(holder.icon);
//            android.view.ViewGroup.LayoutParams layoutParams = holder.icon.getLayoutParams();
//            layoutParams.width = layoutParams.MATCH_PARENT;
//            layoutParams.height = 380;
//            holder.icon.setLayoutParams(layoutParams);
        }

        holder.llMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iOnItemClickCustomListner.onItemClicked(view, position);
            }
        });

    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.main_category_list_item, parent, false);
        return new MyViewHolder(itemView);
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView icon;
        public TextView tvType;
        public TextView tvCount;
        public LinearLayout llMain;

        public MyViewHolder(View itemView) {
            super(itemView);
            icon = (ImageView) itemView.findViewById(R.id.iv_icon);
            tvType = (TextView) itemView.findViewById(R.id.tv_type);
            tvCount = (TextView) itemView.findViewById(R.id.tv_count);
            llMain = (LinearLayout) itemView.findViewById(R.id.ll_main);
        }
    }

}
