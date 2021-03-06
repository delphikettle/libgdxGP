package ru.dk.gdxGP.GameWorld.Tasks;

import ru.dk.gdxGP.GameWorld.Task;

import java.util.ArrayList;
import java.util.Arrays;

public class TaskCombination extends Task {
    public static final byte TC_AND = 0, TC_OR = 1, TC_NOT = 2, TC_XOR = 3;
    private final ArrayList<Task> tasks;
    private final byte flag;

    public TaskCombination(Task[] tasks, byte flag, boolean once) {
        super();
        this.tasks = new ArrayList<Task>(Arrays.asList(tasks));
        this.flag = flag;
    }

    public TaskCombination addTask(Task task) {
        tasks.add(task);
        return this;
    }

    @Override
    public boolean check() {
        boolean f = flag == TC_AND;
        for (Task task : tasks) {
            switch (flag) {
                case TC_AND:
                    if (!task.isAchieved()) return false;
                    break;
                case TC_OR:
                    if (task.isAchieved()) return true;
                    break;
                case TC_XOR:
                    if (task.isAchieved() && f) return false;
                    if (task.isAchieved()) f = true;
                    break;
                case TC_NOT:
                    return !task.isAchieved();
                default:
                    throw new IllegalArgumentException("flag cannot be " + flag);
            }
        }
        return f;

    }

    @Override
    public void pause() {
        for (int i = 0; i < tasks.size(); i++) {
            tasks.get(0).pause();
        }
    }

    @Override
    public void resume() {
        for (int i = 0; i < tasks.size(); i++) {
            tasks.get(0).resume();
        }
    }
}
