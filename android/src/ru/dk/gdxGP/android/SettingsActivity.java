package ru.dk.gdxGP.android;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SeekBar;
import ru.dk.gdxGP.Settings;


public class SettingsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        SeekBar musicVolumeChanger = (SeekBar) findViewById(R.id.MusicVolSeekBar);
        musicVolumeChanger.setProgress((int) (100*Settings.getMusicVolume()));
        Log.i("Volume",""+(int) (100*Settings.getMusicVolume()));
        musicVolumeChanger.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Settings.setMusicVolume(progress*0.01f);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
}
