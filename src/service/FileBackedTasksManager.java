package service;

import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.Duration;
import java.time.LocalDateTime;

public class FileBackedTasksManager extends InMemoryTaskManager {

    public FileBackedTasksManager(HistoryManager historyManager) {
        super(historyManager);
    }

    private File file = new File("resources", "toLoad.csv");

    private final String firstString = "id,type,name,status,description,epic, duration, startTime";

    public FileBackedTasksManager(HistoryManager historyManager, File file) {
        super(historyManager);
        this.file = file;
    }

    public static FileBackedTasksManager loadFromFile(File file) {
        FileBackedTasksManager manager = new FileBackedTasksManager(Managers.getDefaultHistory(), file);
        int maxID = 0;
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {

            String line = bufferedReader.readLine();
            while (bufferedReader.ready()) {
                line = bufferedReader.readLine();
                if (line.isEmpty()) {
                    break;
                }

                Task task = fromString(line);
                if (task.getTaskId() > maxID) {
                    maxID = task.getTaskId();
                }
                switch (task.getTaskType()) {
                    case TASK:
                        manager.tasks.put(task.getTaskId(), task);
                        manager.addToPrioritizedTasks(task);
                        break;
                    case SUBTASK:
                        manager.subTasks.put(task.getTaskId(), (SubTask) task);
                        manager.addToPrioritizedTasks(task);
                        break;
                    case EPIC:
                        manager.epics.put(task.getTaskId(), (Epic) task);
                        break;
                }
            }
        } catch (IOException e) {
            throw new ManagerLoadException("Не удалось считать данные из файла.");
        }
        for (SubTask st : manager.subTasks.values()) {
            manager.epics.get(st.getEpicId()).addSubTask(st.getTaskId());
        }
        manager.counter = (maxID);
        return manager;
    }

    public void save() {
        try {
            if (!Files.exists(file.toPath())) {
                Files.createFile(file.toPath());
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Отсутствует файл для записи данных");
        }

        try (FileWriter writer = new FileWriter(file, StandardCharsets.UTF_8)) {
            writer.write(firstString + "\n");

            for (Task task : getTasks()) {
                writer.write(task.toFileString() + "\n");
            }

            for (Epic epic : getEpics()) {
                writer.write(epic.toFileString() + "\n");
            }

            for (SubTask subtask : getSubTasks()) {
                writer.write(subtask.toFileString() + "\n");
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка сохранения в файл");
        }
    }

    private static Task fromString(String value) {
        String[] file = value.split(",");
        int id = Integer.parseInt(file[0]);
        String type = file[1];
        String title = file[2];
        Status status = Status.valueOf(file[3].toUpperCase());
        String description = file[4];
        Integer subsEpic = null;
        Duration duration = Duration.ofMinutes(Long.parseLong(file[6]));
        LocalDateTime startTime = LocalDateTime.parse(file[7]);
        //id,type,name,status,description,epic, duration, startTime

        if (type.equals("SUBTASK")) {
            subsEpic = Integer.parseInt(file[5]);
        }
        if (type.equals("EPIC")) {
            Epic epic = new Epic(title, description);
            epic.setTaskId(id);
            epic.setTaskStatus(status);
            return epic;
        } else if (type.equals("SUBTASK")) {
            SubTask subtask = new SubTask(title, description, status, startTime, duration, subsEpic);
            subtask.setTaskId(id);
            return subtask;
        } else {
            Task task = new Task(title, description, status, startTime, duration);
            task.setTaskId(id);
            return task;
        }
    }

    @Override
    public int createTask(Task task) {
        super.createTask(task);
        save();
        return task.getTaskId();
    }

    @Override
    public int createSubTask(SubTask subTask) {
        super.createSubTask(subTask);
        save();
        return subTask.getTaskId();
    }

    @Override
    public int createEpic(Epic epic) {
        super.createEpic(epic);
        save();
        return epic.getTaskId();
    }

    @Override
    public void deleteTask(int id) {
        super.deleteTask(id);
        save();
    }

    @Override
    public void deleteSubTask(int id) {
        super.deleteSubTask(id);
        save();
    }

    @Override
    public void deleteEpic(int id) {
        super.deleteEpic(id);
        save();
    }


    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void deleteAllSubTasks() {
        super.deleteAllSubTasks();
        save();
    }

    @Override
    public void deleteAllEpic() {
        super.deleteAllEpic();
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    public void updateSubTask(SubTask subTask) {
        super.updateSubTask(subTask);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }
}
