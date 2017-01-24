package com.star.market.europeanstarmarket.activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;

import com.star.market.europeanstarmarket.R;
import com.star.market.europeanstarmarket.adapters.PreferenceConnector;

import static com.star.market.europeanstarmarket.activities.BaseActivity.setDialog;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class SplashActivity extends Activity {

//    private String staticUdid = "42a22e8d6fb6c1e6";
private String staticUdid = "6454c4b6a1a6dda5";

    // Galaxy s6 edge
//    private String staticUdid = "6111e8750d45f0b9";

    private String udid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fullscreen);

        udid = Settings.Secure.getString(
                SplashActivity.this.getContentResolver(),
                Settings.Secure.ANDROID_ID);

        Thread timerThread = new Thread() {
            public void run() {
                try {
                    sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    if (staticUdid.equals(udid)) {

                        if (PreferenceConnector.readBoolean(SplashActivity.this, PreferenceConnector.FIRST_RUN, true)) {
                            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                            startActivity(intent);
                        } else {
                            startActivity(new Intent(SplashActivity.this, MainCatagoryActivity.class));
                        }
                    } else {
                        showDialog();
                    }
                }
            }
        };
        timerThread.start();
    }

    private void showDialog() {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {


                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        SplashActivity.this);

                // set title
                alertDialogBuilder.setTitle(getString(R.string.title_alert));

                // set dialog message
                alertDialogBuilder
                        .setMessage("You are not allowed to view this app.")
                        .setCancelable(false)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();
                            }
                        });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();
                setDialog(alertDialog);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (staticUdid.equals(udid)) {
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.slide_in_left);
            finish();
        }

    }

    @Override
    public void onBackPressed() {
    }
}
