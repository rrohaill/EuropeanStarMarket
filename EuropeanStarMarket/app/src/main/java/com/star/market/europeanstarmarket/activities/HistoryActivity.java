package com.star.market.europeanstarmarket.activities;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.star.market.europeanstarmarket.R;
import com.star.market.europeanstarmarket.adapters.HistoryAdapter;
import com.star.market.europeanstarmarket.adapters.NavigationDrawerManager;
import com.star.market.europeanstarmarket.models.HistoryModel;

import java.util.ArrayList;

public class HistoryActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private ArrayList<HistoryModel> historyModelArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        fetchIntent();

        TextView header = (TextView) findViewById(R.id.tvAppName);
        header.setText("History");

        recyclerView = (RecyclerView) findViewById(R.id.rv_history);
        recyclerView.setAdapter(new HistoryAdapter(this, historyModelArrayList));

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        navigationDrawer = new NavigationDrawerManager(this, null);
        navigationDrawer.initDrawer(this, backButtonListener);
    }

    private void fetchIntent() {
        historyModelArrayList = (ArrayList<HistoryModel>) getIntent().getExtras().get(Constants.KEY_HISTORY_LIST);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initHeader();
    }
}
