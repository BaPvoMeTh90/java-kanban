package model;

public class SubTask extends Task {
    private final int epicId;

    public SubTask(String title, String description, Status taskStatus, int epicId) {
        super(title, description, taskStatus);
        this.epicId = epicId;
    }

    public SubTask(int taskId, String title, String description, Status taskStatus, int epicId) {
        super(taskId, title, description, taskStatus);
        this.epicId = epicId;
    }

    public Integer getEpicId() {
        return epicId;
    }

    @Override
    public TaskType getTaskType() {
        return TaskType.SubTask;
    }

    public String toFileString() {
        String[] string = {Integer.toString(getTaskId()),
                getTaskType().toString(),
                getTitle(),
                getTaskStatus().toString(),
                getDescription(),
                getEpicId().toString()};
        return String.join(",", string);
    }

    @Override
    public String toString() {
        return "SubTask{" +
                "epicId=" + epicId +
                ", taskId=" + taskId +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", taskStatus=" + taskStatus +
                '}';
    }

}