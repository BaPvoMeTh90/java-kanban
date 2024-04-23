package model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Таск")
class TaskTest {

    Task task =new Task("название","описание");
    Task taskToComparison = new Task("название", "описание");

    @Test
    @DisplayName("Таск должен совпадать с копией")
    void shouldBeEqualsToCopy (){
        assertEqualsTask (task, taskToComparison, "Сравнение по: ");
    }

    private static void assertEqualsTask(Task task, Task taskToComparison, String text ){
        assertEquals(task.getDescription(), taskToComparison.getDescription(), text + " описанию.");
        assertEquals(task.getTaskId(), taskToComparison.getTaskId(), text + " Id.");
        assertEquals(task.getTitle(), taskToComparison.getTitle(), text + " названию.");
    }

}