package com.star.market.europeanstarmarket.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.star.market.europeanstarmarket.R;
import com.star.market.europeanstarmarket.adapters.PreferenceConnector;

public class MainActivity extends Activity implements View.OnClickListener {

    private ImageButton ibLocation1;
    private ImageButton ibLocation2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        ibLocation1 = (ImageButton) findViewById(R.id.ib_location1);
        ibLocation2 = (ImageButton) findViewById(R.id.ib_location2);

        ibLocation1.setOnClickListener(this);
        ibLocation2.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent = null;
        PreferenceConnector.writeBoolean(MainActivity.this, PreferenceConnector.FIRST_RUN, false);
        switch (view.getId()) {
            case R.id.ib_location1:
                PreferenceConnector.writeString(MainActivity.this, PreferenceConnector.LOCATION, "0");
                intent = new Intent(this, MainCatagoryActivity.class);
                break;
            case R.id.ib_location2:
                PreferenceConnector.writeString(MainActivity.this, PreferenceConnector.LOCATION, "0");
                intent = new Intent(this, MainCatagoryActivity.class);
                break;
        }
        startActivity(intent);
        finish();
    }
}
