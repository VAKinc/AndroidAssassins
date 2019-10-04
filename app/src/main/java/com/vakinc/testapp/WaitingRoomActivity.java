package com.vakinc.testapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.List;

public class WaitingRoomActivity extends AppCompatActivity implements NetworkRequester.OnNetworkResponseComplete {

    private static String playerName;
    private static String roomkey;
    private static String str = "";
    private TextView player_list;
    private TextView roomkeyTV;
    private HashMap<String, String> params = new HashMap<>();

    private Handler handler = new Handler();
    private Runnable updateTextView = new Runnable() {
        @Override
        public void run() {
            NetworkRequester nr = new NetworkRequester(AssassinsAPI.URL_GET_PLAYERS_IN_GAME, params, AssassinsAPI.CODE_POST_REQUEST, WaitingRoomActivity.this);
            nr.execute();
            player_list.setText(String.format("%s%s", getString(R.string.player_list), str));
            handler.postDelayed(updateTextView, 2000);
        }
    };

    private Handler waitHandler = new Handler();
    private Runnable waitForGameStart = new Runnable() {
        @Override
        public void run() {
            NetworkRequester nr = new NetworkRequester(AssassinsAPI.URL_CHECK_GAME_ACTIVE, params, AssassinsAPI.CODE_POST_REQUEST, WaitingRoomActivity.this);
            nr.execute();
            waitHandler.postDelayed(waitForGameStart, 2000);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_room);

        Intent intent = getIntent();
        roomkey = intent.getStringExtra("asa_roomkey");
        playerName = intent.getStringExtra("asa_player_name");
        roomkeyTV = findViewById(R.id.waitingroomkey);
        roomkeyTV.setText(String.format("%s%s", getString(R.string.roomkey), roomkey));
        str = playerName;

        player_list = findViewById(R.id.player_list_lobby);
        params.put("roomkey", roomkey);

        handler.post(updateTextView);
        waitHandler.post(waitForGameStart);
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    protected void onPause() {
        super.onPause();


        SharedPreferences prefs = getSharedPreferences("gameData", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("player", playerName);
        editor.putString("roomkey", roomkey);
        editor.commit();
        System.out.println("Wrote values: " + prefs.getString("player", "") + " " + prefs.getString("roomkey", ""));
    }

    private static void updatePlayerList(JSONArray players) throws JSONException {
        str = "";
        for(int i = 0; i < players.length(); i++)
        {
            JSONObject obj = players.getJSONObject(i);
            str = str + "\n" + obj.getString("player");
        }
    }

    public void startGame(View view)
    {
        GameManager gm = new GameManager();
        gm.startGame(roomkey, this);

        handler.removeCallbacks(updateTextView);
        waitHandler.removeCallbacks(waitForGameStart);

        Intent intent = new Intent(getApplicationContext(), GameActivity.class);
        intent.putExtra("asa_roomkey", roomkey);
        intent.putExtra("asa_player_name", playerName);
        startActivity(intent);
    }

    @Override
    public void networkResponse(JSONObject obj, int code) throws JSONException {
        if(code == 2)
        {
            updatePlayerList(obj.getJSONArray("players"));
        }
        if(code == 9 && obj.getBoolean("active"))
        {
            handler.removeCallbacks(updateTextView);
            waitHandler.removeCallbacks(waitForGameStart);

            Intent intent = new Intent(getApplicationContext(), GameActivity.class);
            intent.putExtra("asa_roomkey", roomkey);
            intent.putExtra("asa_player_name", playerName);
            startActivity(intent);
        }
    }
}
