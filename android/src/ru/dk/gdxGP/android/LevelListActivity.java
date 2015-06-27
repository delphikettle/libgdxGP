package ru.dk.gdxGP.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import ru.dk.gdxGP.GameWorld.GameLevels;

import java.util.List;

public class LevelListActivity extends Activity {
    private ListView lv;
    private ArrayAdapter<String> adapter;

    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.level_list);
        lv = (ListView) findViewById(R.id.levelList);
        final List<String> levelNames = GameLevels.getLevelsNames(false);
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

}