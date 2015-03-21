package ru.dk.gdxGP.android;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class MainMenuActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);
    }

    public void menuButtonClick(View view) {
        switch (view.getId()){
            case R.id.exitButton:
                System.exit(0);
                break;
        }
    }
}