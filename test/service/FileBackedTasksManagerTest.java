package service;

import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileBackedTasksManagerTest {

    TaskManager manager;
    TaskManager loadedManager;
    File file1;


    @BeforeEach
    public void beforeEach() throws IOException {
        file1 = File.createTempFile("resources", "testToLoad.csv");

        manager = new FileBackedTasksManager(Managers.getDefaultHistory(), file1);
    }

    @AfterEach
    public void afterEach() {
        try {
            if (Files.exists(file1.toPath())) {
                Files.delete(file1.toPath());
            }
            Files.createFile(file1.toPath());
        } catch (IOException e) {
            throw new ManagerSaveException("Файл Отсуствует");
        }
    }

    @Test
    @DisplayName("сохраненные и загруженные таски одинаковые")
    public void saveAndLoadedTaskAreEqual() {
        int taskId1 = manager.createTask(new Task(" ", " ", Status.NEW));
        Task task1 = manager.getTask(taskId1);
        int taskId2 = manager.createEpic(new Epic(" ", " "));
        Epic task2 = manager.getEpic(taskId2);
        int taskId3 = manager.createSubTask(new SubTask("", "", Status.NEW, 2));
        SubTask task3 = manager.getSubTask(taskId3);
        loadedManager = FileBackedTasksManager.loadFromFile(file1);
        assertEquals(manager.getTask(taskId1).toString(), loadedManager.getTask(taskId1).toString(), "Таски не одинаковы");
        assertEquals(manager.getEpic(taskId2).toString(), loadedManager.getEpic(taskId2).toString(), "Таски не одинаковы");
        assertEquals(manager.getSubTask(taskId3).toString(), loadedManager.getSubTask(taskId3).toString(), "Таски не одинаковы");
    }

    @Test
    @DisplayName("сохранение и загрузку пустого файла")
    public void saveAbdLoadEmptyFile() {
        loadedManager = FileBackedTasksManager.loadFromFile(file1);
        List<Task> list = loadedManager.getTasks();
        int lengthShouldBe = 0;
        assertEquals(lengthShouldBe, list.size(), "загружн не пустой файл");
        loadedManager.createTask(new Task("", "", Status.NEW));
        List<Task> list1 = loadedManager.getTasks();
        int lengthShouldBe1 = 1;
        assertEquals(lengthShouldBe1, list1.size(), "файл не записан");
    }
}
