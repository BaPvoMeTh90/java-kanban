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
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Менеджер истории тест")
class InMemoryHistoryManagerTest {

    private TaskManager taskManager;

    public Task createTask() {
        return new Task("", "", Status.NEW, LocalDateTime.now(), Duration.ofMinutes(0));
    }

    @BeforeEach
    void BeforeEach() throws IOException {
        File temp = new File("resources", "testToLoad.csv");
        taskManager = new FileBackedTasksManager(Managers.getDefaultHistory(), temp);
    }

    @AfterEach
    public void afterEach() throws IOException {
        Files.delete(Path.of("resources/testToLoad.csv"));
    }

    @Test
    @DisplayName("В Истории 3 объекта")
    public void shouldBe3Objects(){
        int taskId1 = taskManager.createTask(createTask());
        taskManager.getTask(taskId1);
        int epicId1 = taskManager.createEpic(new Epic("", ""));
        Epic epic1 = taskManager.getEpic(epicId1);
        int subTaskId2 = taskManager.createSubTask(new SubTask("", "", Status.NEW, LocalDateTime.now(), Duration.ofMinutes(0), epicId1));
        taskManager.getSubTask(subTaskId2);

        List<Task> history = taskManager.getHistory();

        int lengthShouldBe = 3;

        assertEquals(lengthShouldBe, history.size(), "Не все просмотры добавлены");
    }

    @Test
    @DisplayName("сохряняется 1 копия задачи при дублировании")
    public void shouldBe1CopyOfTask() {
        int taskId1 = taskManager.createTask(createTask());
        taskManager.getTask(taskId1);
        taskManager.getTask(taskId1);
        taskManager.getTask(taskId1);

        List<Task> history = taskManager.getHistory();

        int lengthShouldBe = 1;

        assertEquals(lengthShouldBe, history.size(), "В истории несколько просмотров таски");
    }

    @Test
    @DisplayName("Задача исчезает из истории после удаления")
    public void disappearFromTheHistoryAfterDeletion() {
        int taskId1 = taskManager.createTask(createTask());
        Task task1 = taskManager.getTask(taskId1);
        taskManager.deleteTask(taskId1);

        List<Task> history = taskManager.getHistory();

        int lengthShouldBee = 0;

        assertEquals(lengthShouldBee, history.size(), "В истории сохранен просмотр после даления");
    }

    @Test
    @DisplayName("удаление истории из начала, серидины и конца списка")
    public void removalFromHeadBodyTail() {
        int taskId1 = taskManager.createTask(createTask());
        int taskId2 = taskManager.createTask(new Task("", "", Status.NEW, LocalDateTime.now().plusMinutes(1), Duration.ofMinutes(0)));
        int taskId3 = taskManager.createTask(new Task("", "", Status.NEW, LocalDateTime.now().plusMinutes(2), Duration.ofMinutes(0)));
        int taskId4 = taskManager.createTask(new Task("", "", Status.NEW, LocalDateTime.now().plusMinutes(3), Duration.ofMinutes(0)));
        int taskId5 = taskManager.createTask(new Task("", "", Status.NEW, LocalDateTime.now().plusMinutes(4), Duration.ofMinutes(0)));
        Task task1 = taskManager.getTask(taskId1);
        Task task2 = taskManager.getTask(taskId2);
        Task task3 = taskManager.getTask(taskId3);
        Task task4 = taskManager.getTask(taskId4);
        Task task5 = taskManager.getTask(taskId5);
        taskManager.deleteTask(taskId1);
        assertEquals(taskManager.getHistory(), List.of
                        (taskManager.getTask(2), taskManager.getTask(3), taskManager.getTask(4), taskManager.getTask(5)),
                "удаление с головы не удалось");
        taskManager.deleteTask(taskId5);
        assertEquals(taskManager.getHistory(), List.of(taskManager.getTask(2), taskManager.getTask(3), taskManager.getTask(4)), "удаление с хвоста не удалось");
        taskManager.deleteTask(taskId3);
        assertEquals(taskManager.getHistory(), List.of(taskManager.getTask(2), taskManager.getTask(4)), "удаление из середины не удалось");
    }

    @Test
    @DisplayName("Загружает с пустой историей задач")
    public void loadFromEmptyFile() {
        taskManager.createTask(createTask());
        int LengthShouldBe = 0;
        assertEquals(taskManager.getHistory().size(), LengthShouldBe, "история не пуста");
    }

}