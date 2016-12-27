package com.star.market.europeanstarmarket.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.star.market.europeanstarmarket.R;
import com.star.market.europeanstarmarket.adapters.IOnItemClickCustomListner;
import com.star.market.europeanstarmarket.adapters.MainCategoryListAdapter;
import com.star.market.europeanstarmarket.adapters.NavigationDrawerManager;
import com.star.market.europeanstarmarket.models.MainCategoryModel;
import com.star.market.europeanstarmarket.models.SubCategoryModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class MainCatagoryActivity extends BaseActivity implements IOnItemClickCustomListner, JSONCommunicationManager {

    private ArrayList<MainCategoryModel> listMainCategoryModel;
    private Integer[] mThumbIds;
    private RecyclerView rvMainCategory;
    private int mPosition;
    private ArrayList<SubCategoryModel> subCategoryModelArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_catagory);

        rvMainCategory = (RecyclerView) findViewById(R.id.rv_main_category);

        fetchMainCategory();

        loadImages();

        rvMainCategory.setAdapter(new MainCategoryListAdapter(this, listMainCategoryModel, mThumbIds, this));

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        rvMainCategory.setLayoutManager(mLayoutManager);
        rvMainCategory.setItemAnimator(new DefaultItemAnimator());

        initHeader();

    }


    private void fetchMainCategory() {
        try {
            listMainCategoryModel = new ArrayList<>();
            JSONObject obj = new JSONObject(loadJSONFromAsset());
            JSONArray jsonArray = obj.getJSONArray("HomecatelogArray");
            Gson gson = new Gson();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject explrObject = jsonArray.getJSONObject(i);
                MainCategoryModel resp = gson.fromJson(explrObject.toString(), MainCategoryModel.class);
                listMainCategoryModel.add(resp);
            }
            Constants.listMainCategoryModel.clear();
            Constants.listMainCategoryModel.addAll(listMainCategoryModel);

            navigationDrawer = new NavigationDrawerManager(this, null);
            navigationDrawer.initDrawer(this, null);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getAssets().open("main_category.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    private void loadImages() {
        int size = listMainCategoryModel.size();

        mThumbIds = new Integer[size];
        for (int i = 0; i < size; i++) {
            mThumbIds[i] = getResources().getIdentifier(
                    listMainCategoryModel.get(i).getImage()
                            .replace(".png", ""), "drawable",
                    getPackageName());
            // mThumbIds_hover[i] = mMain.getResources().getIdentifier(
            // category.getCategory().get(i).getIcon().replace(".png", "")
            // + "_pressed", "drawable", mMain.getPackageName());
        }
    }

    @Override
    public void onItemClicked(View view, int position) {
        this.mPosition = position;
        processRequest();
    }

    @Override
    public void processRequest() {
        super.processRequest();

        NetworkCalls call = new NetworkCalls(this, this, Constants.subCatUrl);
        call.execute(listMainCategoryModel.get(mPosition).getId());
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
            subCategoryModelArrayList = new ArrayList<>();
            JSONArray jsonArray = new JSONArray(response);
            Gson gson = new Gson();
            SubCategoryModel resp;

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject explrObject = jsonArray.getJSONObject(i);
                resp = gson.fromJson(explrObject.toString(), SubCategoryModel.class);
                subCategoryModelArrayList.add(resp);
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
        if (subCategoryModelArrayList.size() > 0) {
            Intent intent = new Intent(MainCatagoryActivity.this, SubCategoryActivity.class);
            intent.putExtra(Constants.KEY_SUB_CATEGORIES, subCategoryModelArrayList);
            intent.putExtra(Constants.KEY_HEADER_NAME, listMainCategoryModel.get(mPosition).getName());
            startActivity(intent);
        } else {
            showAlert(getString(R.string.title_alert), getString(R.string.FETCHING_DETAILS_PROBLEM), this);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        navigationDrawer.closeDrawer();
        initHeader();
    }
}
