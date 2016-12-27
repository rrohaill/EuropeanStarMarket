package com.star.market.europeanstarmarket.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.star.market.europeanstarmarket.R;
import com.star.market.europeanstarmarket.TextDrawable;
import com.star.market.europeanstarmarket.adapters.NavigationDrawerManager;
import com.star.market.europeanstarmarket.adapters.PreferenceConnector;
import com.star.market.europeanstarmarket.models.SubProductModel;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class BaseActivity extends FragmentActivity {

    private static AlertDialog dialog;
    protected NavigationDrawerManager navigationDrawer = null;
    protected View.OnClickListener backButtonListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            finish();
        }
    };
    private ProgressDialog loadingDialog;

    public static boolean haveInternet(Context con) {

        ConnectivityManager connectivity = (ConnectivityManager) con
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
        }
        return false;
    }

    public static void hideKeyboard(View view, Context con) {
        InputMethodManager imm = (InputMethodManager) con
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void showAlert(String title, String message, Context context) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);

        // set title
        alertDialogBuilder.setTitle(title);

        // set dialog message
        alertDialogBuilder
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, close
                        // current activity
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, just close
                        // the dialog box and do nothing
                        dialog.cancel();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
        setDialog(alertDialog);
    }

    public static void showAlertDialog(String title, String message, Context context) {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);

        // set title
        alertDialogBuilder.setTitle(title);

        // set dialog message
        alertDialogBuilder
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, close
                        // current activity
                        dialog.dismiss();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
        setDialog(alertDialog);
    }

    public static void showAlertDialog(String title, String message, Context context, DialogInterface.OnClickListener onClickListener) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);

        // set title
        alertDialogBuilder.setTitle(title);

        // set dialog message
        alertDialogBuilder
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("Ok", onClickListener);

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
        setDialog(alertDialog);
    }

    public static void showAlert(String title, String message, Context context, DialogInterface.OnClickListener onClickListener) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);

        // set title
        alertDialogBuilder.setTitle(title);

        // set dialog message
        alertDialogBuilder
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("Ok", onClickListener)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, just close
                        // the dialog box and do nothing
                        dialog.cancel();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
        setDialog(alertDialog);
    }

    public static void setDialog(AlertDialog d) {
        dialog = d;
    }

    public void addAutoKeyboardHideFunction(View parentLayout) {
        parentLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent ev) {
                hideKeyboard(view, getApplicationContext());
                return false;
            }
        });
    }

    public void initHeader() {
        ImageView ivCount = (ImageView) findViewById(R.id.iv_count);

        TextDrawable drawable = TextDrawable.builder()
                .buildRound(getCheckoutCount(), getResources().getColor(android.R.color
                        .holo_red_dark));

        ivCount.setImageDrawable(drawable);

        ImageView cart = (ImageView) findViewById(R.id.imageviewCart);
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String json = PreferenceConnector.readString(BaseActivity.this, PreferenceConnector.CHECKOUT_LIST, "");
                Gson gson = new Gson();

                if (!json.isEmpty()) {
                    startActivity(new Intent(BaseActivity.this, CheckoutActivity.class));
                }
            }
        });
    }

    public void dismissDialog() {
        dialog.dismiss();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void showLoading() {
        if (loadingDialog == null) {
            loadingDialog = new ProgressDialog(this, R.style.DialogTheme);
            loadingDialog.setTitle(getString(R.string.title_please_wait));
            if (Build.VERSION.SDK_INT < 21) {
                loadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            }
            loadingDialog.setMessage(getString(R.string.title_processing));
            loadingDialog.setCancelable(false);
            loadingDialog.setIndeterminate(false);
        }
        if (!loadingDialog.isShowing())
            loadingDialog.show();
    }

    public void hideLoading() {

        if (loadingDialog != null) {
            loadingDialog.dismiss();
            loadingDialog.cancel();
        }
    }

    public void processRequest() {

    }

    @Override
    public void onBackPressed() {
        if (navigationDrawer != null && navigationDrawer.isDrawerOpen()) {
            // if
            // drawer
            // is
            // open
            // then
            // close
            // it

            navigationDrawer.closeDrawer();
        } else {
            super.onBackPressed();
        }
    }

    public String getCheckoutCount() {
        ArrayList<SubProductModel> list = new ArrayList<>();
        String checkoutCount = "0";
        String json = PreferenceConnector.readString(this, PreferenceConnector.CHECKOUT_LIST, "");
        Gson gson = new Gson();

        if (!json.isEmpty()) {

            Type type = new TypeToken<ArrayList<SubProductModel>>() {
            }.getType();
            list = gson.fromJson(json, type);
        }

        if (!list.isEmpty())
            checkoutCount = list.size() + "";

        return checkoutCount;
    }

}
