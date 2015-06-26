package ru.dk.gdxGP.android;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.SeekBar;
import ru.dk.gdxGP.utils.Settings;


public class SettingsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        SeekBar musicVolumeChanger = (SeekBar) findViewById(R.id.MusicVolSeekBar);
        musicVolumeChanger.setProgress((int) (100*Settings.getCurrentSettings().getMusicVolume()));
        Log.i("Volume",""+(int) (100*Settings.getCurrentSettings().getMusicVolume()));
        musicVolumeChanger.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Settings.getCurrentSettings().setMusicVolume(progress*0.01f);
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
