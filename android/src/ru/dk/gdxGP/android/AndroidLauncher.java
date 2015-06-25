package ru.dk.gdxGP.android;

import android.content.Intent;
import android.os.Bundle;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import ru.dk.gdxGP.GDXGameGP;

public class AndroidLauncher extends AndroidApplication {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        Intent intent = getIntent();
        String levelName = intent.getStringExtra("LEVEL_NAME");
        this.initialize(new GDXGameGP(levelName), config);
    }

}
