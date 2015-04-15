package ru.dk.gdxGP.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import ru.dk.gdxGP.GameWorld.GameLevels;

import java.util.List;

/**
 * Created by DK on 23.03.2015.
 */
public class LevelListActivity extends Activity {
    private ListView lv;
    private ArrayAdapter<String> adapter;

    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        /*
        String[] values = new String[] { "Android", "iPhone", "WindowsMobile",
                "Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X",
                "Linux", "OS/2" };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.level_list_item, GameLevels.getNames(GameLevels.LEVELS,true));
        setListAdapter(adapter);
        */
        setContentView(R.layout.level_list);
        lv = (ListView) findViewById(R.id.levelList);
        if (lv == null) {
            System.err.println("Null lv");
            return;
        }
        final List<String> levelNames = GameLevels.getNames(GameLevels.LEVELS, true);
        adapter = new ArrayAdapter<String>(this, R.layout.level_list_item, levelNames);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View itemClicked, int position,
                                    long id) {
                Toast.makeText(getApplicationContext(), ((TextView) itemClicked).getText(),
                        Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LevelListActivity.this, AndroidLauncher.class);
                intent.putExtra("LEVEL_NAME", ((TextView) itemClicked).getText());
                startActivity(intent);
            }
        });
    }
/*
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        String item = (String) getListAdapter().getItem(position);
        Toast.makeText(this, item + " выбран", Toast.LENGTH_LONG).show();
    }
    */

}