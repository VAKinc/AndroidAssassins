package com.vakinc.testapp;

import android.app.Activity;
import org.json.JSONObject;

import java.util.HashMap;

public class GameManager implements NetworkRequester.OnNetworkResponseComplete {
    GameManager(){}

    void addPlayer(String player, String roomkey, Activity a)
    {
        HashMap<String, String> params = new HashMap<>();
        params.put("player", player);
        params.put("roomkey", roomkey);
        params.put("target", "NULL");

        NetworkRequester nr = new NetworkRequester(AssassinsAPI.URL_ADD_PLAYER, params, AssassinsAPI.CODE_POST_REQUEST, a);
        nr.execute();
    }

    void startGame(String roomkey, Activity a)
    {
        HashMap<String, String> params = new HashMap<>();
        params.put("roomkey", roomkey);

        NetworkRequester nr = new NetworkRequester(AssassinsAPI.URL_START_GAME, params, AssassinsAPI.CODE_POST_REQUEST, a);
        nr.execute();
    }

    void assassinateTarget(String player, String roomkey, Activity a)
    {
        HashMap<String, String> params = new HashMap<>();
        params.put("player", player);
        params.put("roomkey", roomkey);

        NetworkRequester nr = new NetworkRequester(AssassinsAPI.URL_ASSASSINATE_TARGET, params, AssassinsAPI.CODE_POST_REQUEST, a);
        nr.execute();
        checkGameActive(roomkey, a);
    }

    void checkGameActive(String roomkey, Activity a)
    {
        HashMap<String, String> params = new HashMap<>();
        params.put("roomkey", roomkey);

        NetworkRequester nr = new NetworkRequester(AssassinsAPI.URL_CHECK_GAME_ACTIVE, params, AssassinsAPI.CODE_POST_REQUEST, a);
        nr.execute();
    }

    @Override
    public void networkResponse(JSONObject obj, int code) {

    }
}
