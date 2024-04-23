package service;

import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {

    TaskManager taskManager;
    Task task;
    Epic epic;
    SubTask subTask;
    @BeforeEach
    public void createManager() {
        taskManager = Managers.getDefault();

    }
    @AfterEach
    public void clearHistory(){
        List<Task> history = taskManager.getHistory();
        history.clear();
    }

    @Test
    @DisplayName("задача должна создаться")
    void shouldBeCreatedTask() {

        task = new Task("Test create", "Test createTask description", Status.NEW);
        final int taskId = taskManager.createTask(task);

        final Task savedTask = taskManager.getTask(taskId);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");

        final List<Task> tasks = taskManager.getTasks();

        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.get(0), "Задачи не совпадают.");
    }

    @Test
    @DisplayName("Эпик должен создаться")
    void shouldBeCreatedEpic() {

        epic = new Epic("Epic create", "Test createEpic description");
        final int epicId = taskManager.createEpic(epic);

        final Task savedEpic = taskManager.getEpic(epicId);

        assertNotNull(savedEpic, "Задача не найдена.");
        assertEquals(epic, savedEpic, "Задачи не совпадают.");

        final List<Epic> epics = taskManager.getEpics();

        assertNotNull(epics, "Задачи не возвращаются.");
        assertEquals(1, epics.size(), "Неверное количество задач.");
        assertEquals(epic, epics.get(0), "Задачи не совпадают.");
    }

    @Test
    @DisplayName("СубТаск должен создаться")
    void shouldBeCreatedSubTask() {

        epic = new Epic("Epic create", "Test createEpic description");
        final int epicId = taskManager.createEpic(epic);

        subTask = new SubTask("SubTask create", "Test createSubTask description", Status.NEW, epic);
        final int subTaskId = taskManager.createSubTask(subTask);

        final Task savedSubTask = taskManager.getSubTask(subTaskId);

        assertNotNull(savedSubTask, "Задача не найдена.");
        assertEquals(subTask, savedSubTask, "Задачи не совпадают.");

        final List<SubTask> subTasks = taskManager.getSubTasks();

        assertNotNull(subTasks, "Задачи не возвращаются.");
        assertEquals(1, subTasks.size(), "Неверное количество задач.");
        assertEquals(subTask, subTasks.get(0), "Задачи не совпадают.");
    }
    @Test
    @DisplayName("Добавление в список и поиск по ID")
    public void ShouldAddAngGetById(){
        task = new Task("Test create", "Test createTask description", Status.NEW);
        final int taskId = taskManager.createTask(task);
        epic = new Epic("Epic create", "Test createEpic description");
        final int epicId = taskManager.createEpic(epic);
        subTask = new SubTask("SubTask create", "Test createSubTask description", Status.NEW, epic);
        final int subTaskId = taskManager.createSubTask(subTask);

        assertEquals(task, taskManager.getTask(taskId), "Сохнаренные задачи не совпадают");
        assertEquals(epic, taskManager.getEpic(epicId), "Сохнаренные Эпики не совпадают");
        assertEquals(subTask, taskManager.getSubTask(subTaskId), "Сохнаренные СубТаски не совпадают");
    }

    @Test
    @DisplayName("История сохраняет изменения просмотренных задач")
    public void ShouldBeSavedModifiedTasks(){
        int taskId1 = taskManager.createTask(new Task("1", "1", Status.NEW));
        Task task1 = taskManager.getTask(taskId1);
        Task task2= task1;
        task2.setDescription("Изменили описаине");
        taskManager.getTask(taskId1);
        List<Task> after = taskManager.getHistory();
        assertEquals(after.get(0),task1, "исходная таска не в истории");
        assertEquals(after.get(1),task2, "обновленная таска не в истории");
    }


}