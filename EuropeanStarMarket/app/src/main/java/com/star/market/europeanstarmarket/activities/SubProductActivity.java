package com.star.market.europeanstarmarket.activities;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.star.market.europeanstarmarket.R;
import com.star.market.europeanstarmarket.adapters.IOnItemClickCustomListner;
import com.star.market.europeanstarmarket.adapters.NavigationDrawerManager;
import com.star.market.europeanstarmarket.adapters.PreferenceConnector;
import com.star.market.europeanstarmarket.adapters.SubProductListAdapter;
import com.star.market.europeanstarmarket.models.SubProductModel;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class SubProductActivity extends BaseActivity implements IOnItemClickCustomListner {

    private ArrayList<SubProductModel> listSubProduct;
    private TextView header;
    private String headerName;
    private RecyclerView rvMainCategory;
    private int mPosition;
    private EditText etSearch;
    private SubProductListAdapter adapter;
    private ArrayList<SubProductModel> filteredList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_product);

        fetchIntent();
        initView();

    }

    private void initView() {
        header = (TextView) findViewById(R.id.tvAppName);
        header.setText(headerName + "");
        rvMainCategory = (RecyclerView) findViewById(R.id.rv_main_category);

        etSearch = (EditText) findViewById(R.id.et_search);

        filteredList = listSubProduct;
        adapter = new SubProductListAdapter(this, listSubProduct, this);
        rvMainCategory.setAdapter(adapter);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        rvMainCategory.setLayoutManager(mLayoutManager);
        rvMainCategory.setItemAnimator(new DefaultItemAnimator());

        searchListener();

        navigationDrawer = new NavigationDrawerManager(this, null);
        navigationDrawer.initDrawer(this, backButtonListener);

        initHeader();

        addAutoKeyboardHideFunction(findViewById(R.id.activity_main_catagory));
    }


    private void searchListener() {
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence query, int i, int i1, int i2) {
                query = query.toString().toLowerCase();

                filteredList = new ArrayList<>();

                for (int j = 0; j < listSubProduct.size(); j++) {

                    final String text = listSubProduct.get(j).getName().toLowerCase();
                    if (text.contains(query)) {

                        filteredList.add(listSubProduct.get(j));
                    }
                }

                rvMainCategory.setLayoutManager(new LinearLayoutManager(SubProductActivity.this));
                adapter = new SubProductListAdapter(SubProductActivity.this, filteredList, SubProductActivity.this);
                rvMainCategory.setAdapter(adapter);
                adapter.notifyDataSetChanged();  // data set changed
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void fetchIntent() {
        headerName = (String) getIntent().getExtras().get(Constants.KEY_HEADER_NAME);
        listSubProduct = (ArrayList<SubProductModel>) getIntent().getExtras().get(Constants.KEY_SUB_PRODUCT);
    }

    @Override
    public void onItemClicked(View view, int position) {

        ArrayList<SubProductModel> list = new ArrayList<>();
        String json = PreferenceConnector.readString(this, PreferenceConnector.CHECKOUT_LIST, "");
        Gson gson = new Gson();

        if (!json.isEmpty()) {

            Type type = new TypeToken<ArrayList<SubProductModel>>() {
            }.getType();
            list = gson.fromJson(json, type);
        }
        list.add(filteredList.get(position));
        json = gson.toJson(list);

        PreferenceConnector.writeString(this, PreferenceConnector.CHECKOUT_LIST, json);

        showAlertDialog("Success", filteredList.get(position).getName().concat(" added to cart"), this);

        initHeader();

    }

    @Override
    protected void onResume() {
        super.onResume();
        initHeader();
    }
}
