package model;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task{
private  ArrayList <SubTask> subTasks = new ArrayList<>();

    public Epic(String title, String description) {
        super(title, description);
    }


    public ArrayList<SubTask> getSubTasks() {
        return subTasks;
    }

    public void addSubTask(SubTask subTask){
        subTasks.add(subTask);
    }

    public Status updateStatus() {
        int sumOfDone = 0;
        boolean newOne = true;
        for(SubTask subTask : subTasks) {
            if (subTask.getTaskStatus().equals(Status.DONE)) {
                ++sumOfDone;
            } else if (subTask.getTaskStatus().equals(Status.IN_PROGRESS)) {
                newOne = false;
            }
        }
        if(!newOne){
            return Status.IN_PROGRESS;
        }else if(sumOfDone == subTasks.size()){
            return Status.DONE;
        }
        return Status.NEW;
    }
}
