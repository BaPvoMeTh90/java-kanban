package service;

import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("ИнМемориТаскМенеджерТест")
class InMemoryTaskManagerTest {

    TaskManager taskManager;
    Task task;
    Epic epic;

    SubTask subTask;
    @BeforeEach
    public void createManager() {
        taskManager = Managers.getDefault();

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

        subTask = new SubTask("SubTask create", "Test createSubTask description", Status.NEW, epic.getTaskId());
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
    public void shouldAddAngGetById(){
        task = new Task("Test create", "Test createTask description", Status.NEW);
        final int taskId = taskManager.createTask(task);
        epic = new Epic("Epic create", "Test createEpic description");
        final int epicId = taskManager.createEpic(epic);
        subTask = new SubTask("SubTask create", "Test createSubTask description", Status.NEW, epic.getTaskId());
        final int subTaskId = taskManager.createSubTask(subTask);

        assertEquals(task, taskManager.getTask(taskId), "Сохнаренные задачи не совпадают");
        assertEquals(epic, taskManager.getEpic(epicId), "Сохнаренные Эпики не совпадают");
        assertEquals(subTask, taskManager.getSubTask(subTaskId), "Сохнаренные СубТаски не совпадают");
    }



    @Test
    @DisplayName("Сравнение созданной и сохраненной задач")
    public void equalsTask() {
        task = new Task("Test create", "Test createTask description", Status.NEW);
        final int taskId = taskManager.createTask(task);

        final Task savedTask = taskManager.getTask(taskId);

        assertEquals(task.getDescription(), savedTask.getDescription(),  "По описанию.");
        assertEquals(task.getTaskId(), savedTask.getTaskId(),  "По Id.");
        assertEquals(task.getTitle(), savedTask.getTitle(),  "По названию.");
        assertEquals(task.getTaskStatus(), savedTask.getTaskStatus(),  "По статусу.");
    }
    @Test
    @DisplayName("Сравнение созданного и сохраненного Эпиков")
    public void equalsEpic() {
        epic = new Epic("Test create", "Test createTask description");
        final int taskId = taskManager.createEpic(epic);
        subTask = new SubTask("SubTask create", "Test createSubTask description", Status.NEW, epic.getTaskId());
        final int subTaskId = taskManager.createSubTask(subTask);

        final Epic savedEpic = taskManager.getEpic(taskId);

        assertEquals(epic.getDescription(), savedEpic.getDescription(),  "По описанию.");
        assertEquals(epic.getTaskId(), savedEpic.getTaskId(),  "По Id.");
        assertEquals(epic.getTitle(), savedEpic.getTitle(),  "По названию.");
        assertEquals(epic.getTaskStatus(), savedEpic.getTaskStatus(),  "По статусу.");
        assertEquals(epic.getSubTasks(), savedEpic.getSubTasks(),  "По СубТаскам.");
    }

    @Test
    @DisplayName("Задача с генерируемым и ручным ID конфликтуют")
    public void shouldNotConflictGeneratedAndManualID(){
        task= new Task("Название","описание",Status.NEW);
        int taskID = taskManager.createTask(task);
        Task newTask = new Task(1,"название1","ОПисание 1", Status.NEW);
        int newTaskID = taskManager.createTask(newTask);
        assertEquals(2, taskManager.getTasks().size(), "обе задачи в списке");
    }

    @Test
    @DisplayName("Эпик не добавить в себя подзадачей")
    public void shouldNotBeAddedAsSubTask(){
        Epic epic = new Epic(0, "Epic 1", "Testing epic 1");
        epic.addSubTask(0);
        assertEquals(0, epic.epicsSubTasks.size(), "epic should not add itself as subtask");
    }

    @Test
    @DisplayName("СубТаску не добавить в себя Эпиком")
    public void shouldNotBeAddedAsEpic(){
        int ids = taskManager.createSubTask(new SubTask(1, " ", " ", Status.NEW, 1));
        assertEquals(null, taskManager.getSubTask(ids), "СубТаск добавлен В эпик суб таски");

    }
}