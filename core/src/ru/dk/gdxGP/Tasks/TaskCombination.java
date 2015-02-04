package ru.dk.gdxGP.Tasks;

import org.jetbrains.annotations.NotNull;
import ru.dk.gdxGP.Task;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Андрей on 23.01.2015.
 */
public class TaskCombination extends Task {
    public static final byte TC_AND=0,TC_OR=1,TC_NOT=2,TC_XOR=3;
    ArrayList<Task> tasks;
    byte flag;
    TaskCombination(@NotNull Task[] tasks,byte flag){
        this.tasks=new ArrayList<Task>(Arrays.<Task>asList(tasks));
        this.flag=flag;
    }
    public TaskCombination addTask(@NotNull Task task){
        tasks.add(task);
        return this;
    }
    @Override
    public boolean check() {
        boolean f;
        if(flag==TC_AND) f=true; else f=false;
        for(int i=0;i<tasks.size();i++){
            switch(flag){
                case TC_AND:
                    if(!tasks.get(i).isEnded()) return false;
                    break;
                case TC_OR:
                    if(tasks.get(i).isEnded()) return true;
                    break;
                case TC_XOR:
                    if(tasks.get(i).isEnded()&&f) return false;
                    if(tasks.get(i).isEnded()) f=true;
                    break;
                case TC_NOT:
                    return !tasks.get(i).isEnded();
                    //break;
                default: throw new IllegalArgumentException("flag cannot be "+flag);
            }
        }
        return f;

    }
}
