package model;

import java.time.Duration;
import java.time.LocalDateTime;

public class SubTask extends Task {
    private final int epicId;

    public SubTask(int taskId, String title, String description, Status taskStatus, LocalDateTime startTime, Duration duration, int epicId) {
        super(taskId, title, description, taskStatus, startTime, duration);
        this.epicId = epicId;
    }

    public SubTask(String title, String description, Status taskStatus, LocalDateTime startTime, Duration duration, int epicId) {
        super(title, description, taskStatus, startTime, duration);
        this.epicId = epicId;
    }

    public SubTask(String title, String description, Status taskStatus, int epicId) {
        super(title, description, taskStatus);
        this.epicId = epicId;
    }

    public SubTask(String title, String description, int epicId) {
        super(title, description);
        this.epicId = epicId;
    }

    public SubTask(int taskId, String title, String description, Status taskStatus, int epicId) {
        super(taskId, title, description, taskStatus);
        this.epicId = epicId;
    }

    public SubTask(int taskId, String title, String description, int epicId) {
        super(taskId, title, description);
        this.epicId = epicId;
    }


    public Integer getEpicId() {
        return epicId;
    }

    @Override
    public TaskType getTaskType() {
        return TaskType.SUBTASK;
    }

    public String toFileString() {
        String[] string = {Integer.toString(getTaskId()),
                getTaskType().toString(),
                getTitle(),
                getTaskStatus().toString(),
                getDescription(),
                getEpicId().toString(),
                String.valueOf(getDuration().toMinutes()),           //id,type,name,status,description,epic, duration, startTime
                String.valueOf(getStartTime())
        };
        return String.join(",", string);
    }

    @Override
    public String toString() {
        return "SubTask{" +
                "epicId=" + epicId +
                ", taskId=" + taskId +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", taskStatus=" + taskStatus + '\'' +
                ", duration='" + getDuration().toMinutes() + '\'' +
                ", startTime='" + startTime +
                '}';
    }

}