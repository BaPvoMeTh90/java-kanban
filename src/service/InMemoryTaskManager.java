package service;

import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;

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
            tasks.remove(id);
            historyManager.remove(id);
            prioritizedTasks.removeIf(task -> task.getTaskId() == id);
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
            prioritizedTasks.removeIf(task -> Objects.equals(task.getTaskId(), id));
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
                prioritizedTasks.removeIf(task -> Objects.equals(task.getTaskId(), numbers));
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
        addToPrioritizedTasks(task);
        task.setTaskId(generateId());
        tasks.put(task.getTaskId(), task);
        return task.getTaskId();
    }

    @Override
    public int createSubTask(SubTask subTask) {
        if (epics.containsKey(subTask.getEpicId())) {
            subTask.setTaskId(generateId());
            subTasks.put(subTask.getTaskId(), subTask);
            addToPrioritizedTasks(subTask);
            Epic epic = epics.get(subTask.getEpicId());
            epic.addSubTask(subTask.getTaskId());
            updateStatus(epic);
            changeEpicTiming(epic);
            return subTask.getTaskId();
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
        changeEpicTiming(epic);
        return epic.getTaskId();
    }

    @Override
    public void deleteAllTasks() {
        for (Integer tasks : tasks.keySet()) {
            historyManager.remove(tasks);
        }
        tasks.clear();
    }

    @Override
    public void deleteAllSubTasks() {
        for (Epic epic : epics.values()) {
            epic.getSubTasks().clear();
            updateStatus(epic);
        }
        for (Integer subTasks : subTasks.keySet()) {
            historyManager.remove(subTasks);
        }
        subTasks.clear();

    }

    @Override
    public void deleteAllEpic() {
        for (Integer subTasks : subTasks.keySet()) {
            historyManager.remove(subTasks);
        }
        for (Integer epics : epics.keySet()) {
            historyManager.remove(epics);
        }
        epics.clear();
        subTasks.clear();
    }

    @Override
    public void updateTask(Task task) {
        if (tasks.containsKey(task.getTaskId())) {
            addToPrioritizedTasks(task);
            int taskId = task.getTaskId();
            prioritizedTasks.removeIf(prioritizedTask -> prioritizedTask.equals(task));
            addToPrioritizedTasks(task);
        } else {
            System.out.println("Такого задания нет");
        }
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        if (subTasks.containsKey(subTask.getTaskId()) && epics.containsKey(subTask.getEpicId())) {
            subTasks.put(subTask.getTaskId(), subTask);
            prioritizedTasks.removeIf(prioritizedTask -> prioritizedTask.equals(subTask));
            addToPrioritizedTasks(subTask);
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
            changeEpicTiming(oldEpic);
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

    public void addToPrioritizedTasks(Task task) {
        boolean isIntersection = checkIntersections(task);
        if (!isIntersection) {
            prioritizedTasks.add(task);
        } else {
            throw new IntersectionException("Найдено пересечение во времени");
        }
    }

    public boolean checkIntersections(Task task) {

        LocalDateTime startOfTask = task.getStartTime();
        LocalDateTime endOfTask = task.getEndTime();

        return prioritizedTasks.stream()
                .filter(prioritizedTask -> prioritizedTask.getStartTime() != null)
                .anyMatch(prioritizedTask -> !endOfTask.isBefore(prioritizedTask.getStartTime())
                        && !prioritizedTask.getEndTime().isBefore(startOfTask));
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return prioritizedTasks.stream().toList();
    }

}