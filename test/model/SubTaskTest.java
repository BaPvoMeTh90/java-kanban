package model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import service.Managers;
import service.TaskManager;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Субтаск")
class SubTaskTest {
    TaskManager taskManagerSubTask = Managers.getDefault();

    Epic epic = new Epic("название", "описание");
    int epicId = taskManagerSubTask.createEpic(epic);
    SubTask subTask = new SubTask("название", "описание", Status.NEW, epicId);
    int taskId = taskManagerSubTask.createSubTask(subTask);
    Task taskToComparison = new SubTask(2, "название", "описание", Status.DONE, epicId);



    @Test
    @DisplayName("Субтаск должен совпадать с копией")
    void shouldBeEqualsToCopy (){
        assertEquals(subTask, taskToComparison, "Сравнение по: ID");
    }

}