package model;

import java.util.ArrayList;


public class Epic extends Task {
    public final ArrayList<Integer> epicsSubTasks = new ArrayList<>();

    public Epic(String title, String description) {
        super(title, description, Status.NEW);
    }

    public Epic(int taskId, String title, String description) {
        super(taskId, title, description);
    }

    public ArrayList<Integer> getSubTasks() {
        return epicsSubTasks;
    }

    public void addSubTask(int id) {
        if (getSubTasks().contains(id)) {
            epicsSubTasks.add(id);
        }
    }

    public ArrayList getEpicsSubTasks() {
        return epicsSubTasks;
    }

    @Override
    public TaskType getTaskType() {
        return TaskType.Epic;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "subTasks=" + epicsSubTasks +
                ", taskId=" + taskId +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", taskStatus=" + taskStatus +
                '}';
    }

}
