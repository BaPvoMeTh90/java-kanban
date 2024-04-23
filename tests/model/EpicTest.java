package model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


@DisplayName("Эпик")
class EpicTest {
    static Epic epic =new Epic("название","описание");
    Epic epicToComparison = new Epic("название", "описание");

    @Test
    @DisplayName("Эпик должен совпадать с копией")
    void shouldBeEqualsToCopy (){
        assertEqualsTask (epic, epicToComparison, "Сравнение по: ");
    }

    private static void assertEqualsTask(Epic task, Epic taskToComparison, String text ){
        assertEquals(task.getDescription(), taskToComparison.getDescription(), text + " описанию.");
        assertEquals(task.getTaskId(), taskToComparison.getTaskId(), text + " Id.");
        assertEquals(task.getTitle(), taskToComparison.getTitle(), text + " названию.");
    }

}