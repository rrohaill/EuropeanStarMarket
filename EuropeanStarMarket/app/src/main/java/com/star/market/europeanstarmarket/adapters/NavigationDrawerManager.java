package com.star.market.europeanstarmarket.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.StateListDrawable;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.star.market.europeanstarmarket.R;
import com.star.market.europeanstarmarket.activities.BaseActivity;
import com.star.market.europeanstarmarket.activities.Constants;
import com.star.market.europeanstarmarket.activities.JSONCommunicationManager;
import com.star.market.europeanstarmarket.activities.LoginActivity;
import com.star.market.europeanstarmarket.activities.MainCatagoryActivity;
import com.star.market.europeanstarmarket.activities.NetworkCalls;
import com.star.market.europeanstarmarket.activities.SubCategoryActivity;
import com.star.market.europeanstarmarket.models.MainCategoryModel;
import com.star.market.europeanstarmarket.models.NavDrawerItem;
import com.star.market.europeanstarmarket.models.SubCategoryModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;
import static com.star.market.europeanstarmarket.activities.Constants.listMainCategoryModel;

public class NavigationDrawerManager {
    ArrayList<MainCategoryModel> listCat = listMainCategoryModel;
    private ArrayList<SubCategoryModel> subCategoryModelArrayList;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private Context mMain;
    private ViewPager vpPager;
    private String[] navMenuTitles;
    private int[] navMenuIcons;
    private int mPosition;
//    = {R.drawable.btn_back_arrow_normal,
//            R.drawable.btn_back_arrow_normal,
//            R.drawable.btn_back_arrow_normal,
//            R.drawable.btn_back_arrow_normal, R.drawable.btn_back_arrow_normal,
//            R.drawable.btn_back_arrow_normal, R.drawable.btn_back_arrow_normal,
//            R.drawable.btn_back_arrow_normal};

    private ArrayList<NavDrawerItem> navDrawerItems;
    private NavDrawerListAdapter adapter;

    public NavigationDrawerManager(BaseActivity pMain, ViewPager vpPager) {
        mMain = pMain;
        this.vpPager = vpPager;
    }

//    private String[] fetchMainCategoryNames() {
//        String name[] = new String[Constants.listMainCategoryModel.size()];
//        ArrayList<MainCategoryModel> models = Constants.listMainCategoryModel;
//        for (int i = 0; i <= models.size(); i++) {
//            name[i] = models.get(i).getName();
//        }
//
//        return name;
//    }

    public void initDrawer(Context context,
                           View.OnClickListener drawerIconListner) {
        mDrawerLayout = (DrawerLayout) ((BaseActivity) mMain).findViewById(R.id.parent_layout);
        mDrawerList = (ListView) ((BaseActivity) mMain).findViewById(R.id.list_slidermenu);

        navDrawerItems = new ArrayList<NavDrawerItem>();

        navMenuIcons = new int[listCat.size() + 2];
        navMenuIcons[0] = R.drawable.my_list_drawer;
        navMenuIcons[1] = R.drawable.my_account_drawer;

        for (int i = 2; i <= listCat.size(); i++) {
            navMenuIcons[i] = context.getResources().getIdentifier(listCat.get(i - 2).getImage().replace(".png", "_drawer"), "drawable",
                    context.getPackageName());
        }


        navMenuTitles = new String[2 + listCat.size()];
        navMenuTitles[0] = "List";
        navMenuTitles[1] = "My Account";
        for (int i = 2; i <= listCat.size(); i++) {
            navMenuTitles[i] = listCat.get(i - 2).getName();
        }

        // adding nav drawer items to array
        for (int i = 0; i < navMenuTitles.length; i++) {
            navDrawerItems.add(new NavDrawerItem(navMenuTitles[i],
                    navMenuIcons[i]));
        }

        mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

        // drawer image
        ImageView btn_drawer = (ImageView) ((BaseActivity) mMain).findViewById(R.id.drawerImage);
        if (drawerIconListner == null) {
            btn_drawer.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    mDrawerLayout.openDrawer(Gravity.LEFT);
                }
            });
        } else {
            btn_drawer.setOnClickListener(drawerIconListner);

            StateListDrawable states = new StateListDrawable();
            states.addState(
                    new int[]{android.R.attr.state_pressed},
                    mMain.getResources().getDrawable(
                            R.drawable.btn_back_arrow_pressed));
            states.addState(
                    new int[]{android.R.attr.state_focused},
                    mMain.getResources().getDrawable(
                            R.drawable.btn_back_arrow_normal));
            states.addState(
                    new int[]{},
                    mMain.getResources().getDrawable(
                            R.drawable.btn_back_arrow_normal));
            btn_drawer.setImageDrawable(states);

        }

        // setting the nav drawer list adapter
        adapter = new NavDrawerListAdapter(mMain.getApplicationContext(),
                navDrawerItems);
        mDrawerList.setAdapter(adapter);

        // enabling action bar app icon and behaving it as toggle button
        // getActionBar().setDisplayHomeAsUpEnabled(true);
        // getActionBar().setHomeButtonEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(((BaseActivity) mMain), mDrawerLayout,
                R.drawable.btn_menu, // nav menu toggle icon
                R.string.app_name, // nav drawer open - description for
                // accessibility
                R.string.app_name // nav drawer close - description for
                // accessibility
        ) {
            public void onDrawerClosed(View view) {
                // getActionBar().setTitle(mTitle);
                // calling onPrepareOptionsMenu() to show action bar icons
                ActivityCompat.invalidateOptionsMenu(((BaseActivity) mMain));
            }

            public void onDrawerOpened(View drawerView) {
                // getActionBar().setTitle(mDrawerTitle);
                // calling onPrepareOptionsMenu() to hide action bar icons
                // invalidateOptionsMenu();
                ActivityCompat.invalidateOptionsMenu(((BaseActivity) mMain));
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

    }

    public void closeDrawer() {
        mDrawerLayout.closeDrawers();

    }

    public boolean isDrawerOpen() {
        return mDrawerLayout.isDrawerOpen(Gravity.LEFT);
    }

    public void processRequest() {

        NetworkCalls call = new NetworkCalls(new JSONCommunicationManager() {
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

                    ((BaseActivity) mMain).hideLoading();
                    onProcessNext(null);
                } catch (Exception e) {
                    e.printStackTrace();
                    ((BaseActivity) mMain).hideLoading();
                    ((BaseActivity) mMain).showAlert(mMain.getString(R.string.title_alert), mMain.getString(R.string.FETCHING_DETAILS_PROBLEM), mMain);
                }
            }

            @Override
            public void onProcessNext(ArrayList<Object> listObject) {
                if (subCategoryModelArrayList.size() > 0) {
                    Intent intent = new Intent(mMain, SubCategoryActivity.class);
                    intent.putExtra(Constants.KEY_SUB_CATEGORIES, subCategoryModelArrayList);
                    intent.putExtra(Constants.KEY_HEADER_NAME, listMainCategoryModel.get(mPosition - 2).getName());
                    mMain.startActivity(intent);
                } else {
                    ((BaseActivity) mMain).showAlert(mMain.getString(R.string.title_alert), mMain.getString(R.string.FETCHING_DETAILS_PROBLEM), mMain);
                }
            }

            @Override
            public void onPreRequest() {
                closeDrawer();
                ((BaseActivity) mMain).showLoading();
            }

            @Override
            public void onError(String s) {
                ((BaseActivity) mMain).hideLoading();

                ((BaseActivity) mMain).showAlert(mMain.getString(R.string.title_alert), s, mMain);

            }
        }, ((BaseActivity) mMain), Constants.subCatUrl);
        call.execute(listMainCategoryModel.get(mPosition - 2).getId());
    }


    private class SlideMenuClickListener implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            mPosition = position;

            if (position == 0) {
                closeDrawer();
                mMain.startActivity(new Intent(mMain, MainCatagoryActivity.class).addFlags(FLAG_ACTIVITY_CLEAR_TOP));
                ((BaseActivity) mMain).finish();
            } else if (position == 1) {
                closeDrawer();
                mMain.startActivity(new Intent(mMain, LoginActivity.class));
            } else if (position > 1) {
                closeDrawer();
                processRequest();
            }

//            Intent intent;

            // display view for selected nav drawer item
//            if (position == 0) {// home slider
//                closeDrawer();
//                if (vpPager != null) {// if home page
//                    vpPager.setCurrentItem(ApplicationData.homePagePosition);
//                } else {// if inner screen
//                    mMain.goToMainMenu();
//                }
//            } else if (position == 1) {// my profile
//                if (!(mMain instanceof MyProfileActivity)) {
//                    intent = new Intent(mMain, MyProfileActivity.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                    mMain.startActivity(intent);
//                }
//                closeDrawer();
//            } else if (position == 2) {// change pass
//                intent = new Intent(mMain,
//                        MyAccountChangePasswordActivity.class);
//                mMain.startActivity(intent);
//                closeDrawer();
//
//            } else if (position == 3) {// change mpin
//                //TODO enable this after 6th may release.
////                Toast.makeText(mMain, "Comming Soon", Toast.LENGTH_SHORT).show();
//                intent = new Intent(mMain, SelectBankMPINChangeActivity.class);
//                mMain.startActivity(intent);
//                closeDrawer();
//
//            } else if (position == 4) {// settings
//                intent = new Intent(mMain, SettingsActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                mMain.startActivity(intent);
//                closeDrawer();
//
//            } else if (position == 5) {// contact us
//                intent = new Intent(mMain, ContactUsActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                mMain.startActivity(intent);
//                closeDrawer();
//
//            } else if (position == 6) {// FAQs
//                intent = new Intent(mMain, FAQActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                mMain.startActivity(intent);
//                closeDrawer();
//
//            } else if (position == 7) {// signout
//                mMain.signOut();
//            } else {
//
//                AppToastMaker.showShortToast(mMain, "Feature not implemented.");
//            }
        }

    }


}
