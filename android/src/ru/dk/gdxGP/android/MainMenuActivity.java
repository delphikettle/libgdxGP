package ru.dk.gdxGP.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.badlogic.gdx.graphics.Texture;
import ru.dk.gdxGP.GDXGameGP;

public class MainMenuActivity extends Activity implements View.OnClickListener {
    Button startButton, helpButton, exitButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);
        GDXGameGP.assetManager.load("images/logo.png", Texture.class);
        GDXGameGP.assetManager.load("images/loadBall.png", Texture.class);
        startButton = (Button) findViewById(R.id.newGameButton);
        helpButton = (Button) findViewById(R.id.helpButton);
        exitButton = (Button) findViewById(R.id.exitButton);
        startButton.setOnClickListener(this);
        helpButton.setOnClickListener(this);
        exitButton.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.newGameButton:
                Intent intent = new Intent(MainMenuActivity.this, LevelListActivity.class);
                startActivity(intent);
                break;
            case R.id.helpButton:
                Toast.makeText(this, "Tap Start button", Toast.LENGTH_SHORT).show();
                break;
            case R.id.exitButton:
                System.exit(0);
                break;
        }

    }
}