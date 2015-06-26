package ru.dk.gdxGP.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainMenuActivity extends Activity implements View.OnClickListener {
    private Button startButton;
    private Button helpButton;
    private Button exitButton;
    private Button settingsButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState==null){
            Intent logoIntent = new Intent(MainMenuActivity.this, LogoActivity.class);
            startActivity(logoIntent);
        }
        setContentView(R.layout.main_menu);
        startButton = (Button) findViewById(R.id.newGameButton);
        helpButton = (Button) findViewById(R.id.helpButton);
        exitButton = (Button) findViewById(R.id.exitButton);
        settingsButton = (Button) findViewById(R.id.settingsButton);
        startButton.setOnClickListener(this);
        helpButton.setOnClickListener(this);
        exitButton.setOnClickListener(this);
        settingsButton.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.newGameButton:
                Intent levelListIntent = new Intent(MainMenuActivity.this, LevelListActivity.class);
                startActivity(levelListIntent);
                break;
            case R.id.helpButton:
                Toast.makeText(this, "Tap Start button", Toast.LENGTH_SHORT).show();
                break;
            case R.id.exitButton:
                System.exit(0);
                break;
            case R.id.settingsButton:
                Intent settingsIntent = new Intent(MainMenuActivity.this, SettingsActivity.class);
                startActivity(settingsIntent);
                break;
        }

    }
}