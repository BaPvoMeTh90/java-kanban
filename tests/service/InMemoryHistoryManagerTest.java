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
        int taskId1 = taskManager.createTask(new Task("1", "1", Status.NEW));
        Task task1 = taskManager.getTask(taskId1);
        int epicId1 = taskManager.createEpic(new Epic("3", "3"));
        Epic epic1 = taskManager.getEpic(epicId1);
        int subTaskId2 = taskManager.createSubTask(new SubTask("2", "2", Status.NEW, epic1.getTaskId()));
        Task task2 = taskManager.getSubTask(subTaskId2);

        List<Task> history = taskManager.getHistory();

        int lengthShouldBe = 3;

        assertEquals(lengthShouldBe, history.size(), "Не все просмотры добавлены");
    }

    @Test
    @DisplayName("сохряняется 1 копия задачи")
    public void shouldBe1CopyOfTask() {
        int taskId1 = taskManager.createTask(new Task("1", "1", Status.NEW));
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
        int taskId1 = taskManager.createTask(new Task("1", "1", Status.NEW));
        taskManager.getTask(taskId1);
        taskManager.deleteTask(1);

        List<Task> history = taskManager.getHistory();

        int lengthShouldBe = 0;

        assertEquals(lengthShouldBe, history.size(), "В истории сохранен просмотр после даления");
    }

}