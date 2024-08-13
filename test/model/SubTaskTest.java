package model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import service.exeptions.ManagerSaveException;
import service.Managers;
import service.TaskManager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Субтаск")
class SubTaskTest {
    TaskManager taskManager = Managers.getDefault();

    @AfterEach
    public void afterEach() {
        try {
            if (!Files.exists(Path.of("resources/toLoad.csv"))) {
                Files.createFile(Path.of("resources/toLoad.csv"));
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Отсутствует файл для записи данных");
        }
    }

    @Test
    @DisplayName("Субтаск должен совпадать с копией")
    void shouldBeEqualsToCopy (){
        Epic epic = new Epic("Субтаск1", "описание");
        int epicId = taskManager.createEpic(epic);
        SubTask subTask = new SubTask("название", "описание", Status.NEW, LocalDateTime.now(), Duration.ofMinutes(0), epicId);
        int taskId = taskManager.createSubTask(subTask);
        SubTask taskToComparison = taskManager.getSubTask(taskId);
        assertEquals(subTask, taskToComparison, "Сравнение по: ID");
    }

}