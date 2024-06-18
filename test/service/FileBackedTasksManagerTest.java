package service;

import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileBackedTasksManagerTest {

    TaskManager manager = Managers.getDefault();
    TaskManager loadedManager;
    @AfterEach


    @Test
    @DisplayName("сохраненные и загруженные таски одинаковые")
    public void saveAndLoadedTaskAreEqual() {
        int taskId1 = manager.createTask(new Task(" ", " ", Status.NEW));
        Task task1 = manager.getTask(taskId1);
        int taskId2 = manager.createEpic(new Epic(" ", " "));
        Epic task2 = manager.getEpic(taskId2);
        int taskId3 = manager.createSubTask(new SubTask("", "", Status.NEW, 2));
        SubTask task3 = manager.getSubTask(taskId3);
        loadedManager = FileBackedTasksManager.loadFromFile(new File("resources/toLoad.csv"));
        assertEquals(manager.getTask(taskId1).toString(), loadedManager.getTask(taskId1).toString(), "Таски не одинаковы");
        assertEquals(manager.getEpic(taskId2).toString(), loadedManager.getEpic(taskId2).toString(), "Таски не одинаковы");
        assertEquals(manager.getSubTask(taskId3).toString(), loadedManager.getSubTask(taskId3).toString(), "Таски не одинаковы");
    }

    @Test
    @DisplayName("сохранение и загрузку пустого файла")
    public void saveAbdLoadEmptyFile() throws IOException {
        loadedManager = FileBackedTasksManager.loadFromFile(File.createTempFile("resources/", "TestToLoad.csv"));
        List<Task> list = loadedManager.getTasks();
        int lengthShouldBe = 0;
        assertEquals(lengthShouldBe, list.size(), "загружн не пустой файл");
        int taskID = loadedManager.createTask(new Task("", "", Status.NEW));

        List<Task> list1 = loadedManager.getTasks();
        int lengthShouldBe1 = 1;
        assertEquals(lengthShouldBe1, list1.size(), "файл не записан");
    }
}
