package com.vakinc.testapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements NetworkRequester.OnNetworkResponseComplete {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences prefs = getSharedPreferences("gameData", MODE_PRIVATE);
        if(!prefs.getString("roomkey", "").equals(""))
        {
            GameManager gm = new GameManager();
            gm.checkGameActive(prefs.getString("roomkey", ""), this);
        }
    }

    public void newGame(View view) {
        Intent intent = new Intent(getApplicationContext(), NewGameActivity.class);
        startActivity(intent);
    }

    public void joinGame(View view) {
        Intent intent = new Intent(getApplicationContext(), JoinGameActivity.class);
        startActivity(intent);
    }

    public void MakeToast(String message)
    {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void networkResponse(JSONObject obj, int code) throws JSONException {
        if(code == 9 && obj.getBoolean("active"))
        {
            final SharedPreferences prefs = getSharedPreferences("gameData", MODE_PRIVATE);
            System.out.println("Read values: " + prefs.getString("player", "") + " " + prefs.getString("roomkey", ""));
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(getString(R.string.rejoin_game_prompt) + " " + prefs.getString("roomkey", "") + " ?")
                    .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent = new Intent(getApplicationContext(), GameActivity.class);
                            intent.putExtra("asa_roomkey", prefs.getString("roomkey", ""));
                            intent.putExtra("asa_player_name", prefs.getString("player", ""));
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    })
                    .setCancelable(false);
            AlertDialog rejoinDialog = builder.create();
            rejoinDialog.show();
        }
    }
}

