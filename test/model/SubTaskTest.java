package model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import service.Managers;
import service.TaskManager;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Субтаск")
class SubTaskTest {
    TaskManager taskManager = Managers.getDefault();

    Epic epic = new Epic("название", "описание");
    int epicId = taskManager.createEpic(epic);
    SubTask subTask =new SubTask(2,"название1","описание1", Status.NEW, epic.getTaskId());
    SubTask subTaskToComparison =new SubTask("название","описание", Status.NEW, epic.getTaskId());
    int SubTaskId= taskManager.createSubTask(subTaskToComparison);


    @Test
    @DisplayName("Субтаск должен совпадать с копией")
    void shouldBeEqualsToCopy (){
        assertEquals(subTask, subTaskToComparison, "Сравнение по: ID");
    }


}