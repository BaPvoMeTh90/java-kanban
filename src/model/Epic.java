package model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;


public class Epic extends Task {
    private LocalDateTime endTime;
    private final ArrayList<Integer> epicsSubTasks = new ArrayList<>();

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
        epicsSubTasks.add(id);
    }

    @Override
    public TaskType getTaskType() {
        return TaskType.EPIC;
    }

    @Override
    public Duration getDuration() {
        if (duration != null) {
            return duration;
        } else {
            return Duration.ofMinutes(0);
        }
    }

    @Override
    public String toString() {
        return "Epic{" +
                "subTasks=" + epicsSubTasks + '\'' +
                ", taskId=" + taskId + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", taskStatus=" + taskStatus +
                '}';
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
}
