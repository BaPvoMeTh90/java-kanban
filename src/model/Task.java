package model;


import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Task {
    protected int taskId;
    protected String title;
    protected String description;
    protected Status taskStatus;
    protected LocalDateTime startTime;
    protected Duration duration;

    public Task(int taskId,
                String title,
                String description,
                Status taskStatus,
                LocalDateTime startTime,
                Duration duration) {
        this.taskId = taskId;
        this.title = title;
        this.description = description;
        this.taskStatus = taskStatus;
        this.startTime = startTime;
        this.duration = duration;
    }

    public Task(String title, String description, Status taskStatus, LocalDateTime startTime, Duration duration) {
        this.title = title;
        this.description = description;
        this.taskStatus = taskStatus;
        this.startTime = startTime;
        this.duration = duration;
    }


    public Task(String title, String description, Status taskStatus) {
        this.title = title;
        this.description = description;
        this.taskStatus = taskStatus;
    }

    public Task(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public Task(int taskId, String title, String description, Status taskStatus) {
        this.taskId = taskId;
        this.title = title;
        this.description = description;
        this.taskStatus = taskStatus;
    }

    public Task(int taskId, String title, String description) {
        this.taskId = taskId;
        this.title = title;
        this.description = description;
    }

    public TaskType getTaskType() {
        return TaskType.TASK;
    }

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Status getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(Status taskStatus) {
        this.taskStatus = taskStatus;
    }

    public String toFileString() {
        String[] string = {Integer.toString(getTaskId()),
                getTaskType().toString(),
                getTitle(),
                getTaskStatus().toString(),
                getDescription(),
                "",                        //id,type,name,status,description,epic, duration, startTime
                String.valueOf(getDuration().toMinutes()),
                String.valueOf(getStartTime())};
        return String.join(",", string);
    }

    @Override
    public String toString() {
        return "Task{" +
                "taskId =" + taskId +
                ", title ='" + title + '\'' +
                ", description ='" + description + '\'' +
                ", taskStatus =" + taskStatus + '\'' +
                ", startTime =" + startTime + '\'' +
                ", Duration=" + duration.toMinutes() + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Task task)) return false;
        return taskId == task.taskId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(taskId);
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public LocalDateTime getEndTime() {
        return startTime.plus(duration);
    }
}