package com.star.market.europeanstarmarket.activities;

import com.star.market.europeanstarmarket.models.CheckoutModel;
import com.star.market.europeanstarmarket.models.MainCategoryModel;

import java.util.ArrayList;

/**
 * Created by rohai on 11/11/2016.
 */

public class Constants {

    public static final String baseUrl = "http://europeanstarmarket.com/";
    public static final String subCatUrl = "subcat.asmx/Getsubcat?catid=";
    public static final String subProductUrl = "product.asmx/Getproduct?subcatid=";
    public static final String loginUrl = "login.asmx/Getloginresponse?";
    public static final String registerUrl = "register.asmx/processregister?";
    public static final String checkoutUrl = "checkout.asmx/Getcheckout?";
    public static final String historyUrl = "history.asmx/Gethistory?userid=";

    public static final String KEY_SUB_CATEGORIES = "key_sub_categories";
    public static final String KEY_HEADER_NAME = "key_header_name";
    public static final String KEY_SUB_PRODUCT = "key_sub_product";
    public static final String KEY_HISTORY_LIST = "key_history_list";

    public static ArrayList<MainCategoryModel> listMainCategoryModel = new ArrayList<>();
    public static ArrayList<CheckoutModel> checkoutList = new ArrayList<>();
}
