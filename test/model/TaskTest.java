package model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import service.Managers;
import service.TaskManager;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Таск")
class TaskTest {

    TaskManager taskManager = Managers.getDefault();

    @Test
    @DisplayName("Таск должен совпадать с копией")
    void shouldBeEqualsToCopy (){
        Task task = new Task("название1", "описание1", Status.NEW, LocalDateTime.now(), Duration.ofMinutes(0));
        int taskId = taskManager.createTask(task);
        Task taskToComparison = new Task(1, "название", "описание", Status.DONE, LocalDateTime.now(), Duration.ofMinutes(0));
        assertEquals(task, taskToComparison, "Сравнение по: id");
    }
}