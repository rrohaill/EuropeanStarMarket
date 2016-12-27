package com.star.market.europeanstarmarket.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.star.market.europeanstarmarket.R;
import com.star.market.europeanstarmarket.adapters.IOnItemClickCustomListner;
import com.star.market.europeanstarmarket.adapters.MainCategoryListAdapter;
import com.star.market.europeanstarmarket.adapters.NavigationDrawerManager;
import com.star.market.europeanstarmarket.adapters.PreferenceConnector;
import com.star.market.europeanstarmarket.models.SubCategoryModel;
import com.star.market.europeanstarmarket.models.SubProductModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class SubCategoryActivity extends BaseActivity implements IOnItemClickCustomListner, JSONCommunicationManager {

    private ArrayList<SubCategoryModel> listSubCategoryModels;
    private TextView header;
    private String headerName;
    private RecyclerView rvMainCategory;
    private int mPosition;
    private ArrayList<SubProductModel> listSubProductModels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_category);

        fetchIntent();
        initView();

    }

    private void initView() {
        header = (TextView) findViewById(R.id.tvAppName);
        header.setText(headerName + "");
        rvMainCategory = (RecyclerView) findViewById(R.id.rv_main_category);

        rvMainCategory.setAdapter(new MainCategoryListAdapter(this, listSubCategoryModels, this));

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        rvMainCategory.setLayoutManager(mLayoutManager);
        rvMainCategory.setItemAnimator(new DefaultItemAnimator());

        navigationDrawer = new NavigationDrawerManager(this, null);
        navigationDrawer.initDrawer(this, backButtonListener);

        initHeader();
    }

    private void fetchIntent() {
        headerName = (String) getIntent().getExtras().get(Constants.KEY_HEADER_NAME);
        listSubCategoryModels = (ArrayList<SubCategoryModel>) getIntent().getExtras().get(Constants.KEY_SUB_CATEGORIES);
    }

    @Override
    public void onItemClicked(View view, int position) {
        this.mPosition = position;
        processRequest();
    }

    @Override
    public void processRequest() {
        super.processRequest();

        NetworkCalls call = new NetworkCalls(this, this, Constants.subProductUrl);
        call.execute(Constants.subProductUrl, listSubCategoryModels.get(mPosition).getId(),
                PreferenceConnector.readString(SubCategoryActivity.this, PreferenceConnector.LOCATION, "0"));
    }

    @Override
    public void onPreRequest() {
        showLoading();
    }

    @Override
    public void onError(String s) {

        hideLoading();

        showAlert(getString(R.string.title_alert), s, this);
    }

    @Override
    public void onResponse(String response, JSONCommunicationManager jsonCommunicationManager) {

        try {

            Log.i("Response :", response);
            listSubProductModels = new ArrayList<>();
            JSONArray jsonArray = new JSONArray(response);
            Gson gson = new Gson();
            SubProductModel resp;

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject explrObject = jsonArray.getJSONObject(i);
                resp = gson.fromJson(explrObject.toString(), SubProductModel.class);
                listSubProductModels.add(resp);
            }

            hideLoading();
            onProcessNext(null);
        } catch (Exception e) {
            e.printStackTrace();
            hideLoading();
            showAlert(getString(R.string.title_alert), getString(R.string.FETCHING_DETAILS_PROBLEM), this);
        }
    }

    @Override
    public void onProcessNext(ArrayList<Object> listObject) {
        if (listSubProductModels.size() > 0) {
            Intent intent = new Intent(SubCategoryActivity.this, SubProductActivity.class);
            intent.putExtra(Constants.KEY_SUB_PRODUCT, listSubProductModels);
            intent.putExtra(Constants.KEY_HEADER_NAME, "Products");
            startActivity(intent);
        } else {
            showAlert(getString(R.string.title_alert), getString(R.string.FETCHING_DETAILS_PROBLEM), this);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        initHeader();
    }
}
