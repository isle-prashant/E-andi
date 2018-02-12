package com.twosquares.e_mandi.managers;

import android.util.Log;
import android.widget.Toast;

import com.twosquares.e_mandi.datamodels.RowItem;
import com.twosquares.e_mandi.datamodels.User;
import com.twosquares.e_mandi.services.NetworkAsync;
import com.twosquares.e_mandi.services.NetworkAsyncInterface;
import com.twosquares.e_mandi.services.Response;
import com.twosquares.e_mandi.utils.RequestBuilder;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashMap;

import okhttp3.Request;

import static com.twosquares.e_mandi.datamodels.User.stars;
import static com.twosquares.e_mandi.views.DashboardActivity.dashBoardAdapter;
import static com.twosquares.e_mandi.views.DashboardActivity.rowItemList;
import static com.twosquares.e_mandi.views.DashboardActivity.swipeRefreshLayoutDashboard;
import static com.twosquares.e_mandi.views.MainActivity.ip;
import static com.twosquares.e_mandi.views.MainActivity.rowItems;

/**
 * Created by Prashant Kumar on 2/12/2018.
 */
public class DashboardManager extends Manager implements NetworkAsyncInterface {
    int DASHBOARD_REQUEST_CODE = 101;
    public void getDashboardData(){
        swipeRefreshLayoutDashboard.setRefreshing(true);
        RequestBuilder requestBuilder = new RequestBuilder();
        Request request = requestBuilder.createGetRequest("http://"+ip+"/index.json", null);
        new NetworkAsync(this,DASHBOARD_REQUEST_CODE).execute(request);
    }

    @Override
    public void onResponse(int requestCode, HashMap<Response, Object> response) {
        if (requestCode == DASHBOARD_REQUEST_CODE) {
            rowItems.clear();
            rowItemList.clear();
            String resBody = (String) response.get(Response.BODY);
            int responseCode = (int) response.get(Response.CODE);
            Log.d("response code", String.valueOf(responseCode));
            System.out.println(resBody);
            if (responseCode == 200){
                try {
                    jsonArray = new JSONArray(resBody);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        jsonObject = jsonArray.getJSONObject(i);
                        RowItem newItem = gson.fromJson(jsonObject.toString(), RowItem.class);
                        Boolean star = false;
                        if (stars.contains(jsonObject.getString("image_id"))) {
                            star = true;
                        }
                        newItem.setImportant(star);
                        rowItems.add(newItem);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                for (int i = 0; i < rowItems.size(); i ++){
                    if (rowItems.get(i).getOwner_id().equals(User.userId)) {
                        rowItemList.add(rowItems.get(i));
                    }
                }
                dashBoardAdapter.notifyDataSetChanged();
            } else {
                //TODO error handling
            }
        }

        swipeRefreshLayoutDashboard.setRefreshing(false);
    }
}
