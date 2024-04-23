package model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Субтаск")
class SubTaskTest {

    Epic epic = new Epic("название", "описание");
    SubTask subTask =new SubTask("название","описание", Status.NEW, epic);
    SubTask subTaskToComparison =new SubTask("название","описание", Status.NEW, epic);


    @Test
    @DisplayName("Субтаск должен совпадать с копией")
    void shouldBeEqualsToCopy (){
        assertEqualsTask (subTask, subTaskToComparison, "Сравнение по: ");
    }

    private static void assertEqualsTask(SubTask subTask, SubTask subTaskToComparison, String text ){
        assertEquals(subTask.getDescription(), subTaskToComparison.getDescription(), text + " описанию.");
        assertEquals(subTask.getTaskId(), subTaskToComparison.getTaskId(), text + " Id.");
        assertEquals(subTask.getTitle(), subTaskToComparison.getTitle(), text + " названию.");
        assertEquals(subTask.getEpicId(), subTaskToComparison.getEpicId(), text + " принадлежности к эпику.");
    }

}