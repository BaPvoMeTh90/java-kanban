package model;

public class SubTask extends Task {
    public SubTask(String title, String description, Status taskStatus, Epic epic) {
        super(title, description, taskStatus);
        this.epicId = epic.getTaskId();
    }

    private final int epicId;

    public int getEpicId() {
        return epicId;
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