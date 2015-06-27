package ru.dk.gdxGP.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainMenuActivity extends Activity implements View.OnClickListener {
    private ImageButton startButton;
    private ImageButton helpButton;
    private ImageButton tutorialButton;
    private ImageButton settingsButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState==null){
            Intent logoIntent = new Intent(MainMenuActivity.this, LogoActivity.class);
            startActivity(logoIntent);
        }
        setContentView(R.layout.main_menu);
        startButton = (ImageButton) findViewById(R.id.newGameButton);
        helpButton = (ImageButton) findViewById(R.id.helpButton);
        tutorialButton = (ImageButton) findViewById(R.id.tutorialButton);
        settingsButton = (ImageButton) findViewById(R.id.settingsButton);
        startButton.setOnClickListener(this);
        helpButton.setOnClickListener(this);
        tutorialButton.setOnClickListener(this);
        settingsButton.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.newGameButton:
                Intent levelListIntent = new Intent(MainMenuActivity.this, LevelListActivity.class);
                startActivity(levelListIntent);
                break;
            case R.id.tutorialButton:
                Intent tutorialListIntent = new Intent(MainMenuActivity.this, TutorialListActivity.class);
                startActivity(tutorialListIntent);
                break;
            case R.id.helpButton:
                Toast.makeText(this, "Tap Start button", Toast.LENGTH_SHORT).show();
                break;
            case R.id.settingsButton:
                Intent settingsIntent = new Intent(MainMenuActivity.this, SettingsActivity.class);
                startActivity(settingsIntent);
                break;
        }

    }
}