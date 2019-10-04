package com.vakinc.testapp;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class NewGameActivity extends AppCompatActivity implements NetworkRequester.OnNetworkResponseComplete {

    private String json_container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_game);
    }

    private String genRoomKey()
    {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random rand = new Random();
        StringBuilder key = new StringBuilder();

        for(int x = 0; x < 4; x++)
        {
            key.append(chars.charAt(rand.nextInt(chars.length())));
        }

        return key.toString();
    }

    public void createGame(View view)
    {
        EditText playerNameEntry = findViewById(R.id.player_name_entry);
        String playerName = playerNameEntry.getText().toString().trim();

        if (TextUtils.isEmpty(playerName)) {
            playerNameEntry.setError("Please enter your name.");
            playerNameEntry.requestFocus();
        }
        else
        {
            String roomkey = genRoomKey();
            GameManager gm = new GameManager();
            gm.addPlayer(playerName, roomkey, this);

            Intent intent = new Intent(getApplicationContext(), WaitingRoomActivity.class);
            intent.putExtra("asa_roomkey", roomkey);
            intent.putExtra("asa_player_name", playerName);
            startActivity(intent);
        }
    }

    @Override
    public void networkResponse(JSONObject obj, int code) throws JSONException {

    }
}
