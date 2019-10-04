package com.vakinc.testapp;

import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class GameOverActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        SharedPreferences prefs = getSharedPreferences("gameData", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("player", "");
        editor.putString("roomkey", "");
        editor.commit();
    }
}
