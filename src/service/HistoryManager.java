package service;

import model.Task;

import java.util.List;

public interface HistoryManager {

    List<Task> getHistory();

    void remove(int id);

    void historyAdd(Task task);
}
