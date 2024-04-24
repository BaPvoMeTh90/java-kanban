package model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import service.Managers;
import service.TaskManager;

import static org.junit.jupiter.api.Assertions.*;


@DisplayName("Эпик")
class EpicTest {
    TaskManager taskManager = Managers.getDefault();
    Epic epic =new Epic("название","описание");
    int epicId = taskManager.createEpic(epic);
    Epic epicToComparison = new Epic(1,"название 1","Описание 1");

    @Test
    @DisplayName("Эпик должен совпадать с копией")
    void shouldBeEqualsToCopy (){
        assertEquals(epic, epicToComparison, "Сравнение по id не успешно");
    }



}