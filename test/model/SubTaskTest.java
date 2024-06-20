package model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import service.ManagerSaveException;
import service.Managers;
import service.TaskManager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

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
        SubTask subTask = new SubTask("название", "описание", Status.NEW, epicId);
        int taskId = taskManager.createSubTask(subTask);
        Task taskToComparison = new SubTask(2, "название", "описание", Status.DONE, epicId);

        assertEquals(subTask, taskToComparison, "Сравнение по: ID");
    }

}