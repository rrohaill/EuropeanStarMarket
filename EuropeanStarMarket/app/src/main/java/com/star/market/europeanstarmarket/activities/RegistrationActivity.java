package com.star.market.europeanstarmarket.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.AppCompatSpinner;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.star.market.europeanstarmarket.R;
import com.star.market.europeanstarmarket.adapters.NavigationDrawerManager;

import org.json.JSONArray;

import java.util.ArrayList;

public class RegistrationActivity extends BaseActivity implements View.OnClickListener {

    private AppCompatSpinner genderSpinner;
    private AppCompatSpinner promotionsSpinner;
    private String gender = "male";
    private TextInputEditText etUserName, etPassword,
            etCPassword, etEmail,
            etPhone, etAddress,
            etStreet, etBuilding,
            etFlatNo;
    private Button btnRegister;
    private String promotions = "Mobile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        initView();


    }

    private void initView() {
        TextView header = (TextView) findViewById(R.id.tvAppName);
        header.setText("Registration");
        findViewById(R.id.imageviewCart).setVisibility(View.INVISIBLE);
        findViewById(R.id.iv_count).setVisibility(View.INVISIBLE);

        genderSpinner = (AppCompatSpinner) findViewById(R.id.sp_gender);
        promotionsSpinner = (AppCompatSpinner) findViewById(R.id.sp_promotions);
        etUserName = (TextInputEditText) findViewById(R.id.et_user_id);
        etPassword = (TextInputEditText) findViewById(R.id.et_password);
        etCPassword = (TextInputEditText) findViewById(R.id.et_c_password);
        etEmail = (TextInputEditText) findViewById(R.id.et_email);
        etAddress = (TextInputEditText) findViewById(R.id.et_address);
        etBuilding = (TextInputEditText) findViewById(R.id.et_building);
        etFlatNo = (TextInputEditText) findViewById(R.id.et_flat);
        etPhone = (TextInputEditText) findViewById(R.id.et_phone);
        etStreet = (TextInputEditText) findViewById(R.id.et_street);

        btnRegister = (Button) findViewById(R.id.btn_register);

        btnRegister.setOnClickListener(this);

        genderSpinner.setPrompt("Gender");
        addGender();
        addPromotions();

        navigationDrawer = new NavigationDrawerManager(this, null);
        navigationDrawer.initDrawer(this, backButtonListener);
    }

    private void addPromotions() {
        ArrayList<String> list = new ArrayList<>();
        list.add("Mobile");
        list.add("Email");

        final ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, list);
        promotionsSpinner.setAdapter(adapter);

        promotionsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int position, long arg3) {
                if (position == 0) {
                    promotions = "Mobile";
                } else if (position == 1) {
                    promotions = "Email";
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }


    public void addGender() {

        ArrayList<String> list = new ArrayList<>();
        list.add("Male");
        list.add("Female");
        list.add("Other");

        final ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, list);
        genderSpinner.setAdapter(adapter);

        genderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int position, long arg3) {
                if (position == 0) {
                    gender = "male";
                } else if (position == 1) {
                    gender = "female";
                } else {
                    gender = "other";
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    @Override
    public void onClick(View view) {
        boolean flag = true;

        if (!checkEmpty(etAddress)) {
            flag = false;
        }
        if (!checkEmpty(etBuilding)) {
            flag = false;
        }
        if (!checkEmpty(etCPassword)) {
            flag = false;
        }
        if (!checkEmpty(etEmail)) {
            flag = false;
        }
        if (!checkEmpty(etFlatNo)) {
            flag = false;
        }
        if (!checkEmpty(etPassword)) {
            flag = false;
        }
        if (!checkEmpty(etPhone)) {
            flag = false;
        }
        if (!checkEmpty(etStreet)) {
            flag = false;
        }
        if (!checkEmpty(etUserName)) {
            flag = false;
        }
        if (!etPassword.getText().toString().equals(etCPassword.getText().toString())) {
            showAlertDialog(getString(R.string.title_alert), "Password and Confirm-Password must be same.", RegistrationActivity.this);
            flag = false;
        }

        if (flag) {

            if (etPassword.getText().toString().equals(etCPassword.getText().toString())) {
                registerCall();
            } else {
                showAlert(getString(R.string.title_alert), "Password and Confirm password should be same.", this);
            }
        }
    }

    private void registerCall() {
        NetworkCalls call = new NetworkCalls(new JSONCommunicationManager() {
            @Override
            public void onResponse(String response, JSONCommunicationManager jsonCommunicationManager) {
                try {

                    Log.i("Response :", response);
                    JSONArray jsonArray = new JSONArray(response);

                    if (jsonArray.getJSONObject(0).has("success")) {
                        onProcessNext(null);
                    } else {
                        showAlert(getString(R.string.title_alert), jsonArray.getJSONObject(0).get("response").toString(), RegistrationActivity.this, new DialogInterface.OnClickListener() {
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
                    showAlert(getString(R.string.title_alert), getString(R.string.FETCHING_DETAILS_PROBLEM), RegistrationActivity.this);
                }
            }

            @Override
            public void onProcessNext(ArrayList<Object> listObject) {
                showAlert("Success", "Registered Successfully.", RegistrationActivity.this, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dismissDialog();
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

                showAlert(getString(R.string.title_alert), s, RegistrationActivity.this);
            }
        }, this, Constants.registerUrl);
        call.execute(Constants.registerUrl, etUserName.getText().toString(),
                etPassword.getText().toString(),
                etCPassword.getText().toString(),
                etEmail.getText().toString(),
                etPhone.getText().toString(),
                etAddress.getText().toString(),
                etBuilding.getText().toString(),
                etStreet.getText().toString(),
                etFlatNo.getText().toString(),
                gender, promotions);
    }

    private boolean checkEmpty(TextInputEditText editText) {

        boolean flag = true;
        if (editText.getText().toString().isEmpty()) {
            flag = false;
            editText.setError("Field must not be empty.");
        }

        return flag;
    }

}
