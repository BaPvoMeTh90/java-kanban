package service;

import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InMemoryHistoryManagerTest {

    TaskManager taskManager;

    @BeforeEach
    public void beforeEach() {
        taskManager = Managers.getDefault();
    }

    @Test
    @DisplayName("В Истории 3 объекта")
    public void shouldBe3Objects(){
        int taskId1 = taskManager.createTask(new Task("тет1", "1", Status.NEW));
        taskManager.getTask(taskId1);
        int epicId1 = taskManager.createEpic(new Epic("тет1", "3"));
        Epic epic1 = taskManager.getEpic(epicId1);
        int subTaskId2 = taskManager.createSubTask(new SubTask("тет1", "2", Status.NEW, epic1.getTaskId()));
        taskManager.getSubTask(subTaskId2);

        List<Task> history = taskManager.getHistory();

        int lengthShouldBe = 3;

        assertEquals(lengthShouldBe, history.size(), "Не все просмотры добавлены");
    }

    @Test
    @DisplayName("сохряняется 1 копия задачи")
    public void shouldBe1CopyOfTask() {
        int taskId1 = taskManager.createTask(new Task("тет2", "1", Status.NEW));
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
        int taskId1 = taskManager.createTask(new Task(" ", " ", Status.NEW));
        Task task1 = taskManager.getTask(taskId1);
        taskManager.deleteTask(taskId1);

        List<Task> history = taskManager.getHistory();

        int lengthShouldBee = 0;

        assertEquals(lengthShouldBee, history.size(), "В истории сохранен просмотр после даления");
    }

}