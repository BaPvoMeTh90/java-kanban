package model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import service.Managers;
import service.TaskManager;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Таск")
class TaskTest {

    TaskManager taskManagerTask = Managers.getDefault();
    Task task =new Task("название1","описание1",Status.NEW);
    int taskId = taskManagerTask.createTask(task);
    Task taskToComparison = new Task(1,"название","описание",Status.DONE);

    @Test
    @DisplayName("Таск должен совпадать с копией")
    void shouldBeEqualsToCopy (){
        assertEquals(task, taskToComparison, "Сравнение по: id");
    }



}