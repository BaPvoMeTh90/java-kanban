package service;

import model.Status;
import model.SubTask;
import model.Task;
import model.Epic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {

    private final HashMap<Integer, Task> tasks= new HashMap<>();
    private final HashMap<Integer, SubTask> subTasks= new HashMap<>();
    private final HashMap<Integer, Epic> epics= new HashMap<>();
    private final HistoryManager historyManager;

    int counter = 0;

    public InMemoryTaskManager(HistoryManager historyManager) {
        this.historyManager = historyManager;
    }

    @Override
    public List<Task> getHistory() {
       return historyManager.getHistory();
    }
    private int generateId() {
        return ++counter;
    }

    @Override
    public Task getTask(int id) {
        historyManager.historyAdd(tasks.get(id));
        return tasks.get(id);

    }

    @Override
    public SubTask getSubTask(int id) {
        historyManager.historyAdd(subTasks.get(id));
        return subTasks.get(id);
    }

    @Override
    public Epic getEpic(int id) {
        historyManager.historyAdd(epics.get(id));
        return epics.get(id);

    }

    @Override
    public void deleteTask(int id) {
        if (tasks.containsKey(id)) {
            tasks.remove(id);
        } else {
            System.out.println("Такой задачи нет");
        }
    }

    @Override
    public void deleteSubTask(int id) {
        if (subTasks.containsKey(id)) {
            SubTask subT = subTasks.remove(id);
            Epic epic = epics.get(subT.getEpicId());
            epic.getSubTasks().remove((Integer) id);
            updateStatus(epic);
        } else {
            System.out.println("Такой Подзадачи нет");
        }
    }

    @Override
    public void deleteEpic(int id) {
        if (epics.containsKey(id)) {
            Epic epic = epics.get(id);
            List<Integer> subTasksOfEpic = epic.getSubTasks();
            for (Integer numbers : subTasksOfEpic) {
                subTasks.remove(numbers);
            }
            epics.remove(id);
        } else {
            System.out.println("Такой задачи нет");
        }
    }

    @Override
    public List<Task> getTasks() {
        List<Task> listOfTasks = new ArrayList<>();
        for (int id : tasks.keySet()) {
            listOfTasks.add(tasks.get(id));
        }
        return listOfTasks;
    }

    @Override
    public List<SubTask> getSubTasks() {
        List<SubTask> listOfSubTasks = new ArrayList<>();
        for (int id : subTasks.keySet()) {
            listOfSubTasks.add(subTasks.get(id));
        }
        return listOfSubTasks;
    }

    @Override
    public List<Epic> getEpics() {
        List<Epic> listOfEpics = new ArrayList<>();
        for (int id : epics.keySet()) {
            listOfEpics.add(epics.get(id));
        }
        return listOfEpics;
    }

    @Override
    public int createTask(Task task) {
        task.setTaskId(generateId());
        tasks.put(task.getTaskId(), task);
        return task.getTaskId();
    }

    @Override
    public int createSubTask(SubTask subTask) {
        if (epics.containsKey(subTask.getEpicId())) {
            subTask.setTaskId(generateId());
            subTasks.put(subTask.getTaskId(), subTask);
            Epic epic = epics.get(subTask.getEpicId());
            epic.addSubTask(subTask.getTaskId());
            updateStatus(epic);
            return subTask.getTaskId();
        }else {
            System.out.println("Эпик отсутствует");
            return 0;
        }
    }

    @Override
    public int createEpic(Epic epic) {
        epic.setTaskId(generateId());
        epics.put(epic.getTaskId(), epic);
        updateStatus(epic);
        return epic.getTaskId();
    }

    @Override
    public void deleteAllTasks() {
        tasks.clear();

    }

    @Override
    public void deleteAllSubTasks() {
        for (Epic epic : epics.values()) {
            epic.getSubTasks().clear();
            updateStatus(epic);
        }
        subTasks.clear();

    }

    @Override
    public void deleteAllEpic() {
        epics.clear();
        subTasks.clear();
    }

    @Override
    public void updateTask(Task task) {
        if (tasks.containsKey(task.getTaskId())) {
            int taskId = task.getTaskId();
            tasks.put(taskId, task);
        } else {
            System.out.println("Такого задания нет");
        }
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        if (subTasks.containsKey(subTask.getTaskId()) && epics.containsKey(subTask.getEpicId())) {
            int taskId = subTask.getTaskId();
            subTasks.put(taskId, subTask);
            updateStatus(epics.get(subTask.getEpicId()));
        } else {
            System.out.println("Такого подзадания или эпика нет");
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        if (epics.containsKey(epic.getTaskId())) {
            int taskId = epic.getTaskId();
            Epic oldEpic = epics.get(taskId);
            oldEpic.setTitle(epic.getTitle());
            oldEpic.setDescription(epic.getDescription());
        } else {
            System.out.println("Такого эпика нет");
        }
    }

    private void updateStatus(Epic epic) {
        List<Integer> statusOfSubTasks = epic.getSubTasks();
        int countOfDone = 0;
        int countOfNew = 0;
        if (statusOfSubTasks.isEmpty()) {
            epic.setTaskStatus(Status.NEW);
            return;
        } else {
            for (int count : statusOfSubTasks)
                if (subTasks.get(count).getTaskStatus().equals(Status.DONE)) {
                    countOfDone++;
                } else if (subTasks.get(count).getTaskStatus().equals(Status.NEW)) {
                    countOfNew++;
                }
        }
        if (countOfDone == statusOfSubTasks.size()) {
            epic.setTaskStatus(Status.DONE);
        } else if (countOfNew == statusOfSubTasks.size()) {
            epic.setTaskStatus(Status.NEW);
        } else {
            epic.setTaskStatus(Status.IN_PROGRESS);
        }
    }

}