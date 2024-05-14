package service;

import model.Epic;
import model.SubTask;
import model.Task;

import java.util.List;

public interface TaskManager{
    List<Task> getHistory();

    Task getTask(int id);

    SubTask getSubTask(int id);

    Epic getEpic(int id);

    void deleteTask(int id);

    void deleteSubTask(int id);

    void deleteEpic(int id);

    List<Task> getTasks();

    List<SubTask> getSubTasks();

    List<Epic> getEpics();

    int createTask(Task task);

    int createSubTask(SubTask subTask);

    int createEpic(Epic epic);

    void deleteAllTasks();

    void deleteAllSubTasks();

    void deleteAllEpic();

    void updateTask(Task task);

    void updateSubTask(SubTask subTask);

    void updateEpic(Epic epic);
}
