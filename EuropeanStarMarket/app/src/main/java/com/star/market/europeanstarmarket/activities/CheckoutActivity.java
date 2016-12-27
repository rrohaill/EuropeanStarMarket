package com.star.market.europeanstarmarket.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.star.market.europeanstarmarket.R;
import com.star.market.europeanstarmarket.adapters.IOnItemClickCustomListner;
import com.star.market.europeanstarmarket.adapters.NavigationDrawerManager;
import com.star.market.europeanstarmarket.adapters.PreferenceConnector;
import com.star.market.europeanstarmarket.adapters.SubProductListAdapter;
import com.star.market.europeanstarmarket.models.CheckoutModel;
import com.star.market.europeanstarmarket.models.SubProductModel;

import org.json.JSONArray;

import java.lang.reflect.Type;
import java.util.ArrayList;


public class CheckoutActivity extends BaseActivity implements IOnItemClickCustomListner, View.OnClickListener {

    private TextView header;
    private RecyclerView rvMainCategory;
    private int mPosition;
    private ArrayList<SubProductModel> list = new ArrayList<>();
    private SubProductListAdapter adapter;
    private Button btnOrder;
    private AppCompatSpinner spPayment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        checkoutList();
        initView();

    }

    private void checkoutList() {

        String json = PreferenceConnector.readString(this, PreferenceConnector.CHECKOUT_LIST, "");
        Gson gson = new Gson();

        if (!json.isEmpty()) {

            Type type = new TypeToken<ArrayList<SubProductModel>>() {
            }.getType();
            list = gson.fromJson(json, type);
        }
    }

    private void initView() {
        header = (TextView) findViewById(R.id.tvAppName);
        header.setText("Checkout");
        findViewById(R.id.imageviewCart).setVisibility(View.INVISIBLE);
        findViewById(R.id.iv_count).setVisibility(View.INVISIBLE);

        rvMainCategory = (RecyclerView) findViewById(R.id.rv_main_category);


        btnOrder = (Button) findViewById(R.id.btn_checkout);
        btnOrder.setOnClickListener(this);

        spPayment = (AppCompatSpinner) findViewById(R.id.sp_payment);
        spPayment.setPrompt("Cash on Delivery");

        addPaymentMethod();

        adapter = new SubProductListAdapter(this, list, this);
        rvMainCategory.setAdapter(adapter);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        rvMainCategory.setLayoutManager(mLayoutManager);
        rvMainCategory.setItemAnimator(new DefaultItemAnimator());

        navigationDrawer = new NavigationDrawerManager(this, null);
        navigationDrawer.initDrawer(this, backButtonListener);

    }

    private void addPaymentMethod() {
        ArrayList<String> list = new ArrayList<>();
        list.add("Cash on Delivery");
        list.add("Paypal");
        list.add("Credit Card");

        final ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, list);
        spPayment.setAdapter(adapter);

        spPayment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int position, long arg3) {
                if (position == 1 || position == 2) {
                    spPayment.setSelection(0);
                    showAlertDialog(getString(R.string.title_alert), "For now only cash on delivery is available.", CheckoutActivity.this);
                }
//                adapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    @Override
    public void onItemClicked(View view, int position) {

        list.remove(position);

        Gson gson = new Gson();
        String json = gson.toJson(list);

        PreferenceConnector.writeString(this, PreferenceConnector.CHECKOUT_LIST, json);

        adapter.notifyItemRemoved(position);
        adapter.notifyDataSetChanged();

    }

    @Override
    public void onClick(View view) {

        if (list != null && list.size() > 0) {

            if (PreferenceConnector.readString(CheckoutActivity.this, PreferenceConnector.USER_ID, "").isEmpty()) {
                showAlert(getString(R.string.title_alert), "Please login first to proceed.", CheckoutActivity.this, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dismissDialog();
                        startActivity(new Intent(CheckoutActivity.this, LoginActivity.class));
                        finish();
                    }
                });
            } else {

                ArrayList<CheckoutModel> checkoutModelArrayList = new ArrayList<>();

                for (int i = 0; i < list.size(); i++) {
                    SubProductModel model = list.get(i);
                    CheckoutModel checkoutModel = new CheckoutModel();
                    checkoutModel.setId(model.getId());
                    checkoutModel.setPrice(model.getPrice());
                    checkoutModel.setQuantity("1");
                    checkoutModelArrayList.add(checkoutModel);
                }

                Constants.checkoutList.clear();
                Constants.checkoutList.addAll(checkoutModelArrayList);

                orderCall();
            }
        } else {
            showAlertDialog(getString(R.string.title_alert), "Empty Cart", CheckoutActivity.this);
        }
    }

    private void orderCall() {
        new NetworkCalls(new JSONCommunicationManager() {
            @Override
            public void onResponse(String response, JSONCommunicationManager jsonCommunicationManager) {
                try {

                    Log.i("Response :", response);
//                    listSubProductModels = new ArrayList<>();
                    JSONArray jsonArray = new JSONArray(response);

                    if (jsonArray.getJSONObject(0).has("success")) {
                        onProcessNext(null);
                    } else {
                        showAlert(getString(R.string.title_alert), jsonArray.getJSONObject(0).get("response").toString(), CheckoutActivity.this, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dismissDialog();
                            }
                        });
                    }

                    hideLoading();
                    onProcessNext(null);
                } catch (Exception e) {
                    e.printStackTrace();
                    hideLoading();
                    showAlert(getString(R.string.title_alert), getString(R.string.FETCHING_DETAILS_PROBLEM), CheckoutActivity.this);
                }
            }

            @Override
            public void onProcessNext(ArrayList<Object> listObject) {
                initHeader();
                showAlert("Success", "Ordered Successfully.", CheckoutActivity.this, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dismissDialog();
                        Constants.checkoutList.clear();
                        PreferenceConnector.writeString(CheckoutActivity.this, PreferenceConnector.CHECKOUT_LIST, "");
                        finish();
                    }
                });

            }

            @Override
            public void onPreRequest() {
                showLoading();
            }

            @Override
            public void onError(String s) {
                hideLoading();

                showAlert(getString(R.string.title_alert), s, CheckoutActivity.this);
            }
        }, this, Constants.checkoutUrl).execute(Constants.checkoutUrl,
                PreferenceConnector.readString(CheckoutActivity.this, PreferenceConnector.USER_ID, ""),
                PreferenceConnector.readString(CheckoutActivity.this, PreferenceConnector.LOCATION, "0"));
    }
}
