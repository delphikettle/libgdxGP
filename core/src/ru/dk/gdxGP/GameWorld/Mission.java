package ru.dk.gdxGP.GameWorld;

import com.badlogic.gdx.graphics.g2d.Batch;
import ru.dk.gdxGP.GameWorld.InterfacesForActions.MissionDrawer;
import ru.dk.gdxGP.GameWorld.Tasks.NullTask;
import ru.dk.gdxGP.GameWorld.Templates.MissionDrawerSet;

import java.util.LinkedList;

/**
 * Created by DK on 14.04.2015.
 */
public class Mission extends Task {
    private final LinkedList<Task> taskList = new LinkedList<Task>();
    private Task currentTask;
    private MissionDrawer missionDrawer = MissionDrawerSet.standardDrawer;

    public Mission(String title) {
        super(true, title);
        if(title.equals("Null"))this.addTask(new NullTask());
    }

    public void addTask(Task task) {
        this.taskList.add(task);
    }

    @Override
    public boolean check() {
        if (currentTask == null) {
            if (taskList.size() > 0)
                this.currentTask = taskList.pop();
        }
        if (currentTask != null) {
            if (currentTask.isAchieved())
                currentTask = null;
        }

        return (this.taskList.size() == 0 && currentTask == null);
    }

    public void setMissionDrawer(MissionDrawer missionDrawer) {
        this.missionDrawer = missionDrawer;
    }

    @Override
    public String getTaskText() {
        if (currentTask != null)
            return currentTask.getTaskText();
        else
            return "";
    }

    public void render(Batch batch) {
        this.missionDrawer.drawMission(this, batch);
    }
}
