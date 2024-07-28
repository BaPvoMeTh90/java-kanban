package service;

import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("Менеджер сохранения задач в файл тест")
class FileBackedTasksManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    File temp;

    @BeforeEach
    void BeforeEach() throws IOException {
        temp = File.createTempFile("testToLoad.", "csv");
        taskManager = new FileBackedTasksManager(Managers.getDefaultHistory(), temp);
    }


    @Test
    @DisplayName("не загружается когда нет файла для загрузки")
    void whenLoadTasksAndFileNotExists() {
        assertThrows(ManagerLoadException.class, () -> {
            FileBackedTasksManager.loadFromFile(new File("Path/notExistPath"));
        }, "Не удалось загрузить данные");
    }

    @Test
    @DisplayName("сохраненные и загруженные таски одинаковые")
    public void saveAndLoadedTaskAreEqual() {
        int taskId1 = taskManager.createTask(new Task(" ", " ", Status.NEW, LocalDateTime.now(), Duration.ofMinutes(0)));
        Task task1 = taskManager.getTask(taskId1);
        int taskId2 = taskManager.createEpic(new Epic(" ", " "));
        Epic task2 = taskManager.getEpic(taskId2);
        int taskId3 = taskManager.createSubTask(new SubTask("", "", Status.NEW, LocalDateTime.now().plusMinutes(1), Duration.ofMinutes(0), 2));
        SubTask task3 = taskManager.getSubTask(taskId3);
        TaskManager loadedManager = FileBackedTasksManager.loadFromFile(temp);
        assertEquals(taskManager.getTask(taskId1).toString(), loadedManager.getTask(taskId1).toString(), "Таски не одинаковы");
        assertEquals(taskManager.getEpic(taskId2).toString(), loadedManager.getEpic(taskId2).toString(), "Таски не одинаковы");
        assertEquals(taskManager.getSubTask(taskId3).toString(), loadedManager.getSubTask(taskId3).toString(), "Таски не одинаковы");
    }

}
