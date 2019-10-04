package com.vakinc.testapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class GameActivity extends AppCompatActivity implements NetworkRequester.OnNetworkResponseComplete {

    private static String playerName;
    private static String roomkey;
    private static String str = "";
    private static String tarstring = "";
    private boolean dead = false;
    private TextView player_list;
    private TextView target;
    private HashMap<String, String> params = new HashMap<>();

    //TODO: Error if server is down.

    private Handler handler = new Handler();
    private Runnable updateTextView = new Runnable() {
        @Override
        public void run() {
            NetworkRequester nr = new NetworkRequester(AssassinsAPI.URL_GET_PLAYERS_IN_GAME, params, AssassinsAPI.CODE_POST_REQUEST, GameActivity.this);
            nr.execute();
            player_list.setText(String.format("%s%s", getString(R.string.player_list), str));
            handler.postDelayed(updateTextView, 2000);
        }
    };

    private Handler tarHandler = new Handler();
    private Runnable updateTargetView = new Runnable() {
        @Override
        public void run() {
            NetworkRequester nr = new NetworkRequester(AssassinsAPI.URL_GET_TARGET_FOR, params, AssassinsAPI.CODE_POST_REQUEST, GameActivity.this);
            nr.execute();
            target.setText(String.format("%s%s", getString(R.string.target), tarstring));
            tarHandler.postDelayed(updateTargetView, 2000);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Intent intent = getIntent();
        roomkey = intent.getStringExtra("asa_roomkey");
        playerName = intent.getStringExtra("asa_player_name");

        TextView roomkeyTV = findViewById(R.id.active_key);
        target = findViewById(R.id.target);
        player_list = findViewById(R.id.active_players);
        params.put("player", playerName);
        params.put("roomkey", roomkey);

        roomkeyTV.setText(String.format("%s%s", getString(R.string.roomkey), roomkey));
        handler.post(updateTextView);
        tarHandler.post(updateTargetView);
    }

    private static void updatePlayerList(JSONArray players) throws JSONException {
        str = "";
        for(int i = 0; i < players.length(); i++)
        {
            JSONObject obj = players.getJSONObject(i);
            if(i == 0)
            {
                str = str + " " + obj.getString("player");
            }
            else if(i == players.length() - 1)
            {
                str = str + ", " + obj.getString("player") + ".";
            }
            else
            {
                str = str + ", " + obj.getString("player");
            }
        }
    }

    private static void updateTarget(String target) {
        tarstring = target;
    }

    public void assassinate(View view)
    {
        GameManager gm = new GameManager();
        gm.assassinateTarget(playerName, roomkey, this);
    }

    private boolean checkLiving(JSONArray players) throws JSONException {
        for(int i = 0; i < players.length(); i++)
        {
            JSONObject o = players.getJSONObject(i);
            if(o.getString("player").equals(playerName))
            {
                return true;
            }
        }
        return false;
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
        System.out.println("Wrote values: " +  prefs.getString("player", "") + " " + prefs.getString("roomkey", ""));
    }

    @Override
    public void networkResponse(JSONObject obj, int code) throws JSONException {
        if(code == 8)
        {
            if(dead) {tarHandler.removeCallbacks(updateTargetView);}
            else
            {
                updateTarget(obj.getString("target"));
            }
        }

        if(code == 2)
        {
            if(obj.getBoolean("error"))
            {
                handler.removeCallbacks(updateTextView);
                tarHandler.removeCallbacks(updateTargetView);

                Intent intent = new Intent(getApplicationContext(), GameOverActivity.class);
                startActivity(intent);
            }
            else if(!checkLiving(obj.getJSONArray("players")))
            {
                dead = true;
                target.setText(R.string.dead);
                Button button = findViewById(R.id.target_assassinated_button);
                button.setVisibility(View.GONE);
                updatePlayerList(obj.getJSONArray("players"));
            }
            else
            {
                updatePlayerList(obj.getJSONArray("players"));
            }
        }
    }
}
