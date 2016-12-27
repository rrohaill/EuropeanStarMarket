package com.star.market.europeanstarmarket.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.star.market.europeanstarmarket.R;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;

public class NetworkCalls extends AsyncTask<String, Integer, String> {

    JSONCommunicationManager communicationManager;
    Context context;
    String response;
    String webAddress = Constants.baseUrl;
    Boolean error = false;
    private boolean isInternetConnected = true;
    private HttpURLConnection urlConnection;

    public NetworkCalls(JSONCommunicationManager communicationManager, Context context) {
        this.communicationManager = communicationManager;
        this.context = context;
    }


    public NetworkCalls(JSONCommunicationManager communicationManager, Context context, String webAddress) {
        this.communicationManager = communicationManager;
        this.webAddress = Constants.baseUrl.concat(webAddress);
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        if (!((BaseActivity) context).haveInternet(context)) {
            ((BaseActivity) context).showAlertDialog(context.getString(R.string.title_alert),
                    context.getString(R.string.title_internet_problem),
                    context, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ((BaseActivity) context).dismissDialog();
                            ((BaseActivity) context).finish();
                        }
                    });
            isInternetConnected = false;
            return;
        } else {
            super.onPreExecute();
            communicationManager.onPreRequest();
        }
    }

    @Override
    protected String doInBackground(String... params) {
        if (isInternetConnected) {
            URL url;
            JsonObject jsonObject = new JsonObject();
            JsonArray jsonArray = new JsonArray();
            StringBuilder result = new StringBuilder();
            String userName = "username=";
            String password = "password=";
            String userNamePassword;

            try {

                if (params[0].equals(Constants.loginUrl)) {
                    userNamePassword = userName.concat(params[1].concat("&")).concat(password.concat(params[2]));
                    url = new URL(webAddress.concat(userNamePassword.trim()));
                } else if (params[0].equals(Constants.historyUrl)) {
                    url = new URL(webAddress.concat(params[1]));
                } else if (params[0].equals(Constants.registerUrl)) {
                    url = makeRegisterUrl(params);
                } else if (params[0].equals(Constants.checkoutUrl)) {

                    url = new URL(webAddress.concat("userid=" + params[1]).concat("&location=" + params[2]));


                    for (int i = 0; i < Constants.checkoutList.size(); i++) {
                        jsonObject.addProperty("id", Constants.checkoutList.get(i).getId());
                        jsonObject.addProperty("price", Constants.checkoutList.get(i).getPrice());
                        jsonObject.addProperty("quantity", Constants.checkoutList.get(i).getQuantity());
                        jsonArray.add(jsonObject);
                    }

                } else if (params[0].equals(Constants.subProductUrl)) {

                    url = new URL(webAddress.concat(params[1].concat("&location=" + params[2])));

                } else {
                    url = new URL(webAddress.concat(params[0]));
                }
                Log.d("URL", url.toString());

                Log.i("jsonObject", jsonArray.toString());

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setReadTimeout(10000 /* milliseconds */);
                urlConnection.setConnectTimeout(15000 /* milliseconds */);
                urlConnection.setDoOutput(true);

                if (params[0].equals(Constants.checkoutUrl)) {

                    if (params.length > 0) {
                        DataOutputStream wr = new DataOutputStream(urlConnection.getOutputStream());
                        wr.writeBytes(jsonArray.toString());
                        wr.flush();
                        wr.close();
                    }

                }


                InputStream in = new BufferedInputStream(urlConnection.getInputStream());

                BufferedReader reader = new BufferedReader(new InputStreamReader(in, "utf-8"), 8);

                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }


            } catch (FileNotFoundException e) {
                e.printStackTrace();
                error = true;
                response = context.getString(R.string.exception_invalid_response);
            } catch (SocketTimeoutException e) {
                e.printStackTrace();
                error = true;
                response = context.getString(R.string.exception_time_out);
            } catch (Exception e) {
                e.printStackTrace();
                error = true;
                response = context.getString(R.string.exception_invalid_response);
            } finally {
                urlConnection.disconnect();
            }


            response = result.toString();
            Log.i("response", response);

            if (response.contains("failure"))
                error = true;
            else if (response.isEmpty()) {
                error = true;
                response = context.getString(R.string.exception_invalid_response);
            }


            return response;
        } else {
            return null;
        }
    }


    private URL makeRegisterUrl(String[] params) {
        String register = "name=" + params[1] + "&password=" + params[2] + "&email=" + params[4] +
                "&phone=" + params[5] + "&address=" + params[6] + "&building=" + params[7] +
                "&street=" + params[8] + "&flatno=" + params[9] + "&gender=" + params[10] + "&promotions=" + params[11];
        URL url = null;
        try {
            url = new URL(webAddress.concat(register));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    @Override
    protected void onPostExecute(String response) {
        super.onPostExecute(response);
        if (response != null) {
            if (error) {
                communicationManager.onError(response);
            } else {
                communicationManager.onResponse(response, communicationManager);
            }
        }
    }
}
