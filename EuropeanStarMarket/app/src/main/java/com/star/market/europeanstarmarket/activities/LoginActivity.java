package com.star.market.europeanstarmarket.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.star.market.europeanstarmarket.R;
import com.star.market.europeanstarmarket.adapters.NavigationDrawerManager;
import com.star.market.europeanstarmarket.adapters.PreferenceConnector;
import com.star.market.europeanstarmarket.models.HistoryModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    boolean loginFlag = false;
    private TextInputEditText etUserName;
    private TextInputEditText etPassword;
    private Button btnLogin;
    private Button btnRegister;
    private Button btnHistory;
    private TextView tvLogedin;
    private LinearLayout llInput;
    private ArrayList<HistoryModel> listHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();

        navigationDrawer = new NavigationDrawerManager(this, null);
        navigationDrawer.initDrawer(this, backButtonListener);

        initHeader();

    }

    private void initView() {
        TextView header = (TextView) findViewById(R.id.tvAppName);
        header.setText("My Account");

        etUserName = (TextInputEditText) findViewById(R.id.et_user_id);
        etPassword = (TextInputEditText) findViewById(R.id.et_password);
        btnLogin = (Button) findViewById(R.id.btn_login);
        btnRegister = (Button) findViewById(R.id.btn_register);
        llInput = (LinearLayout) findViewById(R.id.ll_input);
        tvLogedin = (TextView) findViewById(R.id.tv_logedin);
        btnHistory = (Button) findViewById(R.id.btn_history);

        if (PreferenceConnector.readString(this, PreferenceConnector.USER_ID, "").isEmpty()) {
            llInput.setVisibility(View.VISIBLE);
            tvLogedin.setVisibility(View.GONE);
            btnHistory.setVisibility(View.GONE);
            loginFlag = true;
        } else {
            llInput.setVisibility(View.GONE);
            tvLogedin.setVisibility(View.VISIBLE);
            tvLogedin.setText("You are logged in as: ".concat(PreferenceConnector.readString(LoginActivity.this, PreferenceConnector.USER_ID, "")));
            btnLogin.setText("LOGOUT");
            btnHistory.setVisibility(View.VISIBLE);
        }

        etUserName.setOnClickListener(this);
        etPassword.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
        btnHistory.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                if (loginFlag) {
                    if (!checkEmpty(etUserName)) {
                        return;
                    } else {
                        loginCall();
                    }
                } else {
                    loginFlag = true;
                    llInput.setVisibility(View.VISIBLE);
                    tvLogedin.setVisibility(View.GONE);
                    btnLogin.setText("LOGIN");
                    PreferenceConnector.writeString(LoginActivity.this, PreferenceConnector.USER_ID, "");
                }
                break;
            case R.id.btn_register:
                startActivity(new Intent(LoginActivity.this, RegistrationActivity.class));
                break;
            case R.id.btn_history:
                historyCall();
                break;
        }
    }

    private void historyCall() {
        NetworkCalls call = new NetworkCalls(new JSONCommunicationManager() {
            @Override
            public void onResponse(String response, JSONCommunicationManager jsonCommunicationManager) {
                try {

                    Log.i("Response :", response);
                    listHistory = new ArrayList<>();
                    JSONArray jsonArray = new JSONArray(response);
                    Gson gson = new Gson();
                    HistoryModel resp;

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject explrObject = jsonArray.getJSONObject(i);
                        resp = gson.fromJson(explrObject.toString(), HistoryModel.class);
                        listHistory.add(resp);
                    }

                    hideLoading();
                    onProcessNext(null);
                } catch (Exception e) {
                    e.printStackTrace();
                    hideLoading();
                    showAlertDialog(getString(R.string.title_alert), getString(R.string.FETCHING_DETAILS_PROBLEM), LoginActivity.this);
                }
            }

            @Override
            public void onProcessNext(ArrayList<Object> listObject) {
                Intent intent = new Intent(LoginActivity.this, HistoryActivity.class);
                intent.putExtra(Constants.KEY_HISTORY_LIST, listHistory);
                startActivity(intent);
            }

            @Override
            public void onPreRequest() {
                showLoading();
            }

            @Override
            public void onError(String s) {
                hideLoading();

                if (s.contains("failure")) {
                    showAlertDialog(getString(R.string.title_alert), "No history found.", LoginActivity.this, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dismissDialog();
                        }
                    });
                } else {

                    showAlert(getString(R.string.title_alert), s, LoginActivity.this);
                }
            }
        }, this, Constants.historyUrl);
        call.execute(Constants.historyUrl, PreferenceConnector.readString(LoginActivity.this, PreferenceConnector.USER_ID, ""));
    }

    private void loginCall() {
        NetworkCalls call = new NetworkCalls(new JSONCommunicationManager() {
            @Override
            public void onResponse(String response, JSONCommunicationManager jsonCommunicationManager) {
                try {

                    Log.i("Response :", response);
                    JSONArray jsonArray = new JSONArray(response);

                    if (jsonArray.getJSONObject(0).has("userid")) {

                        String userid = jsonArray.getJSONObject(0).get("userid").toString();
                        PreferenceConnector.writeString(LoginActivity.this, PreferenceConnector.USER_ID, etUserName.getText().toString());
                        onProcessNext(null);
                    } else {
                        showAlertDialog(getString(R.string.title_alert), "Invalid Username or Password.", LoginActivity.this, new DialogInterface.OnClickListener() {
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
                    showAlertDialog(getString(R.string.title_alert), getString(R.string.FETCHING_DETAILS_PROBLEM), LoginActivity.this);
                }
            }

            @Override
            public void onProcessNext(ArrayList<Object> listObject) {
                showAlertDialog("Success", "Login Successful.", LoginActivity.this, new DialogInterface.OnClickListener() {
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

                if (s.contains("failure")) {
                    showAlertDialog(getString(R.string.title_alert), "Invalid Username or Password.", LoginActivity.this, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dismissDialog();
                        }
                    });
                } else {

                    showAlert(getString(R.string.title_alert), s, LoginActivity.this);
                }
            }
        }, this, Constants.loginUrl);
        call.execute(Constants.loginUrl, etUserName.getText().toString(), etPassword.getText().toString());
    }

    private boolean checkEmpty(TextInputEditText editText) {

        boolean flag = true;
        if (editText.getText().toString().isEmpty()) {
            flag = false;
            editText.setError("Field must not be empty.");
        }

        return flag;
    }

    @Override
    protected void onResume() {
        super.onResume();
        initHeader();
    }
}
