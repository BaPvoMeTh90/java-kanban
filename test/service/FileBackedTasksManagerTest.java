package service;

import model.Status;
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
    public void beforeEach() {
        file1 = new File("resources", "testToLoad.csv");
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
    @DisplayName("сохранение  загрузка нескольких задач")
    public void saveTasks() {
        manager.createTask(new Task("", "", Status.NEW));
        manager.createTask(new Task("", "", Status.NEW));
        loadedManager = FileBackedTasksManager.loadFromFile(file1);
        List<Task> list = loadedManager.getTasks();
        int lengthShouldBe = 2;
        assertEquals(lengthShouldBe, list.size(), "не совпадает сохранение");
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
