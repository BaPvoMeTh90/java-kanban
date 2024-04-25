package service;

import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    TaskManager taskManager = Managers.getDefault();

    @Test
    @DisplayName("В Истории 3 объекта")
    public void shouldBe3Objects(){
        TaskManager taskManager = Managers.getDefault();
        int taskId1 = taskManager.createTask(new Task("1", "1", Status.NEW));
        Task task1 = taskManager.getTask(taskId1);
        int epicId1 = taskManager.createEpic(new Epic("3", "3"));
        Epic epic1 = taskManager.getEpic(epicId1);
        int subTaskId2 = taskManager.createSubTask(new SubTask("2", "2", Status.NEW, epic1.getTaskId()));
        Task task2 = taskManager.getSubTask(subTaskId2);
        List<Task> history =taskManager.getHistory();
        int lengthShouldBe = 3;

        assertEquals(lengthShouldBe, history.size(), "Не все просмотры добавлены");
    }

}