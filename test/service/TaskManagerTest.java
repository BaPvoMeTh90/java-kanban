package service;

import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Таск Менеджер Тест")
abstract class TaskManagerTest<T extends TaskManager> {
    protected T taskManager;

    @Test
    @DisplayName("задача должна создаться")
    void shouldBeCreatedTask() {

        Task task = new Task("Test create", "Test createTask description", Status.NEW, LocalDateTime.now(), Duration.ofMinutes(0));
        int taskId = taskManager.createTask(task);

        Task savedTask = taskManager.getTask(taskId);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");

        List<Task> tasks = taskManager.getTasks();

        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.get(0), "Задачи не совпадают.");
    }

    @Test
    @DisplayName("Эпик должен создаться")
    void shouldBeCreatedEpic() {

        Epic epic = new Epic("", "");
        int epicId = taskManager.createEpic(epic);

        Task savedEpic = taskManager.getEpic(epicId);

        assertNotNull(savedEpic, "Задача не найдена.");
        assertEquals(epic, savedEpic, "Задачи не совпадают.");

        List<Epic> epics = taskManager.getEpics();

        assertNotNull(epics, "Задачи не возвращаются.");
        assertEquals(1, epics.size(), "Неверное количество задач.");
        assertEquals(epic, epics.get(0), "Задачи не совпадают.");
    }

    @Test
    @DisplayName("СубТаск должен создаться")
    void shouldBeCreatedSubTask() {

        Epic epic = new Epic(" ", "  ");
        int epicId = taskManager.createEpic(epic);

        SubTask subTask = new SubTask("", "", Status.NEW, LocalDateTime.now(), Duration.ofMinutes(0), epicId);
        int subTaskId = taskManager.createSubTask(subTask);

        Task savedSubTask = taskManager.getSubTask(subTaskId);

        assertNotNull(savedSubTask, "Задача не найдена.");
        assertEquals(subTask, savedSubTask, "Задачи не совпадают.");

        List<SubTask> subTasks = taskManager.getSubTasks();

        assertNotNull(subTasks, "Задачи не возвращаются.");
        assertEquals(1, subTasks.size(), "Неверное количество задач.");
        assertEquals(subTask, subTasks.get(0), "Задачи не совпадают.");
    }

    @Test
    @DisplayName("Добавление в список и поиск по ID")
    public void shouldAddAngGetById() {
        Task task = new Task("", "", Status.NEW, LocalDateTime.now(), Duration.ofMinutes(0));
        int taskId = taskManager.createTask(task);
        Epic epic = new Epic("", "");
        int epicId = taskManager.createEpic(epic);
        SubTask subTask = new SubTask("", "", Status.NEW, LocalDateTime.now().plusMinutes(1), Duration.ofMinutes(0), epicId);
        int subTaskId = taskManager.createSubTask(subTask);

        assertEquals(task, taskManager.getTask(taskId), "Сохнаренные задачи не совпадают");
        assertEquals(epic, taskManager.getEpic(epicId), "Сохнаренные Эпики не совпадают");
        assertEquals(subTask, taskManager.getSubTask(subTaskId), "Сохнаренные СубТаски не совпадают");
    }

    @Test
    @DisplayName("Сравнение созданной и сохраненной задач")
    public void equalsTask() {
        Task task = new Task(" ", "", Status.NEW, LocalDateTime.now(), Duration.ofMinutes(0));
        int taskId = taskManager.createTask(task);

        Task savedTask = taskManager.getTask(taskId);

        assertEquals(task.getDescription(), savedTask.getDescription(), "По описанию.");
        assertEquals(task.getTaskId(), savedTask.getTaskId(), "По Id.");
        assertEquals(task.getTitle(), savedTask.getTitle(), "По названию.");
        assertEquals(task.getTaskStatus(), savedTask.getTaskStatus(), "По статусу.");
    }

    @Test
    @DisplayName("Сравнение созданного и сохраненного Эпиков")
    public void equalsEpic() {
        Epic epic = new Epic("", "");
        int taskId = taskManager.createEpic(epic);
        SubTask subTask = new SubTask("", "", Status.NEW, LocalDateTime.now(), Duration.ofMinutes(0), taskId);
        int subTaskId = taskManager.createSubTask(subTask);

        Epic savedEpic = taskManager.getEpic(taskId);

        assertEquals(epic.getDescription(), savedEpic.getDescription(), "По описанию.");
        assertEquals(epic.getTaskId(), savedEpic.getTaskId(), "По Id.");
        assertEquals(epic.getTitle(), savedEpic.getTitle(), "По названию.");
        assertEquals(epic.getTaskStatus(), savedEpic.getTaskStatus(), "По статусу.");
        assertEquals(epic.getSubTasks(), savedEpic.getSubTasks(), "По СубТаскам.");
    }

    @Test
    @DisplayName("Задача с генерируемым и ручным ID конфликтуют")
    public void shouldNotConflictGeneratedAndManualID() {
        Task task = new Task("", "", Status.NEW, LocalDateTime.now(), Duration.ofMinutes(0));
        int taskID = taskManager.createTask(task);
        Task newTask = new Task(1, "", "", Status.NEW, LocalDateTime.now().plusMinutes(1), Duration.ofMinutes(0));
        int newTaskID = taskManager.createTask(newTask);
        assertEquals(2, taskManager.getTasks().size(), "обе задачи в списке");
    }

    @Test
    @DisplayName("Эпик корректно меняет статус")
    void EpicShouldChangeStatus() {
        int epic = taskManager.createEpic(new Epic("", ""));
        assertEquals(taskManager.getEpic(epic).getTaskStatus(), Status.NEW, "Статус пустого эпикане NEW");
        taskManager.createSubTask(new SubTask("", "", Status.NEW, LocalDateTime.now(), Duration.ofMinutes(0), epic));
        taskManager.createSubTask(new SubTask("", "", Status.NEW, LocalDateTime.now().plusMinutes(1), Duration.ofMinutes(0), epic));
        assertEquals(taskManager.getEpic(epic).getTaskStatus(), Status.NEW, "статус эпика не NEW, с NEW субТасками");
        taskManager.deleteAllSubTasks();
        taskManager.createSubTask(new SubTask("", "", Status.DONE, LocalDateTime.now().plusMinutes(2), Duration.ofMinutes(0), epic));
        taskManager.createSubTask(new SubTask("", "", Status.DONE, LocalDateTime.now().plusMinutes(3), Duration.ofMinutes(0), epic));
        assertEquals(taskManager.getEpic(epic).getTaskStatus(), Status.DONE, "статус эпика не DONE, с DONE субТасками");
        taskManager.deleteAllSubTasks();
        taskManager.createSubTask(new SubTask("", "", Status.NEW, LocalDateTime.now().plusMinutes(4), Duration.ofMinutes(0), epic));
        taskManager.createSubTask(new SubTask("", "", Status.DONE, LocalDateTime.now().plusMinutes(5), Duration.ofMinutes(0), epic));
        assertEquals(taskManager.getEpic(epic).getTaskStatus(), Status.IN_PROGRESS, "статус эпика не IN_PROGRESS, с NEW и DONE субТасками");
        taskManager.deleteAllSubTasks();
        taskManager.createSubTask(new SubTask("", "", Status.IN_PROGRESS, LocalDateTime.now().plusMinutes(6), Duration.ofMinutes(0), epic));
        taskManager.createSubTask(new SubTask("", "", Status.IN_PROGRESS, LocalDateTime.now().plusMinutes(7), Duration.ofMinutes(0), epic));
        assertEquals(taskManager.getEpic(epic).getTaskStatus(), Status.IN_PROGRESS, "статус эпика не IN_PROGRESS, с IN_PROGRESS субТасками");

    }

    @Test
    @DisplayName("Проверка наложения времени работает коректно")
    void shouldCheckPrioritization() {
        Task task = new Task("", "", Status.NEW, LocalDateTime.of(2024, 7, 10, 15, 0), Duration.ofMinutes(10));
        taskManager.createTask(task);
        Task task2 = new Task("", "", Status.NEW, LocalDateTime.of(2024, 7, 10, 15, 5), Duration.ofMinutes(10));
        assertThrows(IntersectionException.class, () -> taskManager.createTask(task2));
    }


}
