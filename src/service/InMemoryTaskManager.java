package service;

import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;
import service.exeptions.IntersectionException;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {

    protected HashMap<Integer, Task> tasks = new HashMap<>();
    protected HashMap<Integer, SubTask> subTasks = new HashMap<>();
    protected HashMap<Integer, Epic> epics = new HashMap<>();
    protected HistoryManager historyManager;
    private final Comparator<Task> taskComparator = Comparator.comparing(Task::getStartTime);
    protected Set<Task> prioritizedTasks = new TreeSet<>(taskComparator);

    protected int counter = 0;

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
            Task task = tasks.remove(id);
            historyManager.remove(id);
            prioritizedTasks.remove(task);
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
            changeEpicTiming(epic);
            historyManager.remove(id);
            prioritizedTasks.remove(subT);
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
                SubTask subT = subTasks.remove(numbers);
                prioritizedTasks.remove(subT);
            }
            epics.remove(id);
            historyManager.remove(id);
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
        if (!checkIntersections(task)) {
            addToPrioritizedTasks(task);
            task.setTaskId(generateId());
            tasks.put(task.getTaskId(), task);
            return task.getTaskId();
        } else {
            throw new IntersectionException("Найдено пересечение во времени");
        }
    }

    @Override
    public int createSubTask(SubTask subTask) {
        if (epics.containsKey(subTask.getEpicId())) {
            if (!checkIntersections(subTask)) {
                addToPrioritizedTasks(subTask);
                subTask.setTaskId(generateId());
                subTasks.put(subTask.getTaskId(), subTask);
                Epic epic = epics.get(subTask.getEpicId());
                epic.addSubTask(subTask.getTaskId());
                updateStatus(epic);
                changeEpicTiming(epic);
                return subTask.getTaskId();
            } else {
                throw new IntersectionException("Найдено пересечение во времени");
            }
        } else {
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
        for (Integer task : tasks.keySet()) {
            historyManager.remove(task);
            prioritizedTasks.remove(tasks.get(task));
        }
        tasks.clear();
    }

    @Override
    public void deleteAllSubTasks() {
        for (Epic epic : epics.values()) {
            epic.getSubTasks().clear();
            updateStatus(epic);
        }
        for (Integer subTask : subTasks.keySet()) {
            historyManager.remove(subTask);
            prioritizedTasks.remove(subTasks.get(subTask));
        }
        subTasks.clear();

    }

    @Override
    public void deleteAllEpic() {
        for (Integer subTask : subTasks.keySet()) {
            historyManager.remove(subTask);
            prioritizedTasks.remove(subTasks.get(subTask));
        }
        for (Integer epics : epics.keySet()) {
            historyManager.remove(epics);
        }
        epics.clear();
        subTasks.clear();
    }

    @Override
    public void updateTask(Task task) {
        if (tasks.containsKey(task.getTaskId()) && !checkIntersections(task)) {
            prioritizedTasks.removeIf(prioritizedTask -> prioritizedTask.equals(task));
            addToPrioritizedTasks(task);
            tasks.put(task.getTaskId(), task);
        } else {
            System.out.println("Такого Task нет");
        }
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        if (subTasks.containsKey(subTask.getTaskId()) && !checkIntersections(subTask) && epics.containsKey(subTask.getEpicId())) {
            prioritizedTasks.removeIf(prioritizedTask -> prioritizedTask.equals(subTask));
            addToPrioritizedTasks(subTask);
            subTasks.put(subTask.getTaskId(), subTask);
            updateStatus(epics.get(subTask.getEpicId()));
            changeEpicTiming(epics.get(subTask.getEpicId()));
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

    protected void changeEpicTiming(Epic epic) {
        if (epic.getSubTasks().isEmpty()) {
            epic.setDuration(Duration.ofMinutes(0));
            epic.setStartTime(null);
            epic.setEndTime(null);
            return;
        }
        epic.setStartTime(subTasks.get(epic.getSubTasks().get(0)).getStartTime());
        epic.setEndTime(subTasks.get(epic.getSubTasks().get(0)).getEndTime());
        Duration epicDuration = Duration.ofMinutes(0);
        for (Integer id : epic.getSubTasks()) {
            if (epic.getStartTime().isAfter(subTasks.get(id).getStartTime())) {
                epic.setStartTime(subTasks.get(id).getStartTime());
            }
            if (epic.getEndTime().isBefore((subTasks.get(id).getEndTime()))) {
                epic.setEndTime(subTasks.get(id).getEndTime());
            }
            epicDuration = epicDuration.plus(subTasks.get(id).getDuration());
        }
        epic.setDuration(epicDuration);
    }

    protected void addToPrioritizedTasks(Task task) {
            prioritizedTasks.add(task);
    }

    protected boolean checkIntersections(Task task) {

        LocalDateTime startOfTask = task.getStartTime();
        LocalDateTime endOfTask = task.getEndTime();


        return prioritizedTasks.stream()
                .filter(prioritizedTask -> prioritizedTask.getStartTime() != null)
                .filter(prioritizedTask -> !prioritizedTask.equals(task))
                .anyMatch(prioritizedTask ->

                        (prioritizedTask.getStartTime().isEqual(startOfTask) || prioritizedTask.getStartTime().isBefore(startOfTask)) && prioritizedTask.getEndTime().isAfter(startOfTask)
                                || (startOfTask.isEqual(prioritizedTask.getStartTime()) || startOfTask.isBefore(prioritizedTask.getStartTime())) && endOfTask.isAfter(prioritizedTask.getStartTime())

                );

    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return prioritizedTasks.stream().toList();
    }

}