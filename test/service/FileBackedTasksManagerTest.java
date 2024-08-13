package service;

import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import service.exeptions.ManagerLoadException;

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
        assertEquals(taskManager.getTask(taskId1).getTaskId(), loadedManager.getTask(taskId1).getTaskId(), "Таски не одинаковы по ID");
        assertEquals(taskManager.getTask(taskId1).getTitle(), loadedManager.getTask(taskId1).getTitle(), "Таски не одинаковы по Title");
        assertEquals(taskManager.getTask(taskId1).getDescription(), loadedManager.getTask(taskId1).getDescription(), "Таски не одинаковы по Description");
        assertEquals(taskManager.getTask(taskId1).getTaskStatus(), loadedManager.getTask(taskId1).getTaskStatus(), "Таски не одинаковы по Status");
        assertEquals(taskManager.getTask(taskId1).getStartTime(), loadedManager.getTask(taskId1).getStartTime(), "Таски не одинаковы по StartTime");
        assertEquals(taskManager.getTask(taskId1).getDuration(), loadedManager.getTask(taskId1).getDuration(), "Таски не одинаковы по Duration");
        assertEquals(taskManager.getEpic(taskId2).getTaskId(), loadedManager.getEpic(taskId2).getTaskId(), "Эпики не одинаковы по ID");
        assertEquals(taskManager.getEpic(taskId2).getTitle(), loadedManager.getEpic(taskId2).getTitle(), "Эпики не одинаковы по Title");
        assertEquals(taskManager.getEpic(taskId2).getDescription(), loadedManager.getEpic(taskId2).getDescription(), "Эпики не одинаковы по Description()");
        assertEquals(taskManager.getEpic(taskId2).getTaskStatus(), loadedManager.getEpic(taskId2).getTaskStatus(), "Эпики не одинаковы по TaskStatus");
        assertEquals(taskManager.getEpic(taskId2).getSubTasks(), loadedManager.getEpic(taskId2).getSubTasks(), "Эпики не одинаковы по SubTasks");
        assertEquals(taskManager.getEpic(taskId2).getStartTime(), loadedManager.getEpic(taskId2).getStartTime(), "Эпики не одинаковы по StartTime");
        assertEquals(taskManager.getEpic(taskId2).getDuration(), loadedManager.getEpic(taskId2).getDuration(), "Эпики не одинаковы по Duration");
        assertEquals(taskManager.getSubTask(taskId3).getTaskId(), loadedManager.getSubTask(taskId3).getTaskId(), "СубТаски не одинаковы по TaskId");
        assertEquals(taskManager.getSubTask(taskId3).getTitle(), loadedManager.getSubTask(taskId3).getTitle(), "СубТаски не одинаковы по Title");
        assertEquals(taskManager.getSubTask(taskId3).getDescription(), loadedManager.getSubTask(taskId3).getDescription(), "СубТаски не одинаковы по Description");
        assertEquals(taskManager.getSubTask(taskId3).getTaskStatus(), loadedManager.getSubTask(taskId3).getTaskStatus(), "СубТаски не одинаковы по TaskStatus");
        assertEquals(taskManager.getSubTask(taskId3).getEpicId(), loadedManager.getSubTask(taskId3).getEpicId(), "СубТаски не одинаковы по EpicId");
        assertEquals(taskManager.getSubTask(taskId3).getStartTime(), loadedManager.getSubTask(taskId3).getStartTime(), "СубТаски не одинаковы по StartTime");
        assertEquals(taskManager.getSubTask(taskId3).getDuration(), loadedManager.getSubTask(taskId3).getDuration(), "СубТаски не одинаковы по Duration");
        assertEquals(taskManager.getPrioritizedTasks(), loadedManager.getPrioritizedTasks(), "Сортированные списки не одинаковы");
    }

}
