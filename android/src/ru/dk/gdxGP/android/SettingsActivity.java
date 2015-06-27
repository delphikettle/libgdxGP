package ru.dk.gdxGP.android;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Switch;
import ru.dk.gdxGP.utils.Settings;


public class SettingsActivity extends Activity {

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        SeekBar musicVolumeChanger = (SeekBar) findViewById(R.id.MusicVolSeekBar);
        musicVolumeChanger.setProgress((int) (100 * Settings.getCurrentSettings().getMusicVolume()));
        musicVolumeChanger.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Settings.getCurrentSettings().setMusicVolume(progress * 0.01f);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        SeekBar soundVolumeChanger = (SeekBar) findViewById(R.id.AudioVolSeekBar);
        soundVolumeChanger.setProgress((int) (100 * Settings.getCurrentSettings().getSoundVolume()));
        soundVolumeChanger.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Settings.getCurrentSettings().setSoundVolume(progress * 0.01f);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        Switch debugSwitch = (Switch) findViewById(R.id.debugSwitch);
        debugSwitch.setChecked(Settings.getCurrentSettings().isDebug());
        debugSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Settings.getCurrentSettings().setDebug(!Settings.getCurrentSettings().isDebug());
            }
        });

    }
}
