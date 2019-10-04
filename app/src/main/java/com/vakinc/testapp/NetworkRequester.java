package com.vakinc.testapp;

import android.app.Activity;
import android.os.AsyncTask;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class NetworkRequester extends AsyncTask<Void, Void, String> {
    String url;
    HashMap<String, String> params;
    int requestCode;
    OnNetworkResponseComplete act;


    NetworkRequester(String url, HashMap<String, String> params, int requestCode, Activity act) {
        this.url = url;
        this.params = params;
        this.requestCode = requestCode;
        this.act = (OnNetworkResponseComplete) act;
    }

    public interface OnNetworkResponseComplete{
        void networkResponse(JSONObject obj, int code) throws JSONException;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        try {
            JSONObject object = new JSONObject(result);
            act.networkResponse(object, object.getInt("responsecode"));
            System.out.println(object.getString("message"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected String doInBackground(Void... voids) {
        RequestHandler requestHandler = new RequestHandler();
        if(requestCode == AssassinsAPI.CODE_POST_REQUEST)
            return requestHandler.sendPost(url, params);
        if(requestCode == AssassinsAPI.CODE_GET_REQUEST)
            return requestHandler.sendGet(url);
        return null;
    }
}