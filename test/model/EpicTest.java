package model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import service.Managers;
import service.TaskManager;

import static org.junit.jupiter.api.Assertions.assertEquals;


@DisplayName("Эпик")
class EpicTest {

    TaskManager taskManager;

    @BeforeEach
    public void beforeEach() {
        taskManager = Managers.getDefault();
    }

    @AfterEach
    public void afterEach() {
        taskManager.deleteAllTasks();
        taskManager.deleteAllSubTasks();
        taskManager.deleteAllEpic();
    }

    @Test
    @DisplayName("Эпик должен совпадать с копией")
    void shouldBeEqualsToCopy (){
        Epic epic = new Epic("название1", "описание1");
        int taskId = taskManager.createEpic(epic);
        Task taskToComparison = new Epic(1, "название", "описание");
        assertEquals(epic, taskToComparison, "Сравнение по id не успешно");
    }
}