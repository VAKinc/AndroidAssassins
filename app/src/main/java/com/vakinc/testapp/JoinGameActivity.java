package com.vakinc.testapp;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

public class JoinGameActivity extends AppCompatActivity implements NetworkRequester.OnNetworkResponseComplete {

    String p;
    String k;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_game);
    }

    public void joinGame(View view){
        EditText playerNameEntry = findViewById(R.id.player_name_entry);
        String playerName = playerNameEntry.getText().toString().trim();

        EditText roomKeyEntry = findViewById(R.id.roomkey_entry);
        String roomkey = roomKeyEntry.getText().toString().toUpperCase().trim();

        if (TextUtils.isEmpty(playerName)) {
            playerNameEntry.setError("Please enter your name.");
            playerNameEntry.requestFocus();
        }
        else if (TextUtils.isEmpty(roomkey)) {
            roomKeyEntry.setError("Please enter a room key.");
            roomKeyEntry.requestFocus();
        }
        else
        {
            validateRoomKey(roomkey, this);
            p = playerName;
            k = roomkey;
        }
    }

    public void validateRoomKey(String roomkey, Activity a)
    {
        HashMap<String, String> params = new HashMap<>();
        params.put("roomkey", roomkey);

        NetworkRequester nr = new NetworkRequester(AssassinsAPI.URL_GET_PLAYERS_IN_GAME, params, AssassinsAPI.CODE_POST_REQUEST, a);
        nr.execute();
    }

    @Override
    public void networkResponse(JSONObject obj, int code) throws JSONException {
        if(code == 2)
        {
            if(obj.getBoolean("error"))
            {
                EditText roomKeyEntry = findViewById(R.id.roomkey_entry);
                roomKeyEntry.setError("Invalid room key.");
                roomKeyEntry.requestFocus();
            }
            else
            {
                GameManager gm = new GameManager();
                gm.checkGameActive(k, this);

            }
        }
        if(code == 9)
        {
            if(obj.getBoolean("active"))
            {
                EditText roomKeyEntry = findViewById(R.id.roomkey_entry);
                roomKeyEntry.setError("This game has already started.");
                roomKeyEntry.requestFocus();
            }
            else
            {
                GameManager gm = new GameManager();
                gm.addPlayer(p, k, this);
                Intent intent = new Intent(getApplicationContext(), WaitingRoomActivity.class);
                intent.putExtra("asa_roomkey", k);
                intent.putExtra("asa_player_name", p);
                startActivity(intent);
            }
        }
    }
}
