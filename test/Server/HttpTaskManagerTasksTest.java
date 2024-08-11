package Server;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.Status;
import org.junit.jupiter.api.*;
import service.InMemoryTaskManager;
import service.Managers;
import service.TaskManager;
import service.http.HttpTaskServer;
import model.Task;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("HttpTaskManagerTasksTest")
public class HttpTaskManagerTasksTest {
    TaskManager manager = new InMemoryTaskManager(Managers.getDefaultHistory());
    HttpTaskServer taskServer = new HttpTaskServer(manager);
    Gson gson = HttpTaskServer.getGson();

    public HttpTaskManagerTasksTest() throws IOException {
    }

    @BeforeEach
    public void setUp() {
        manager.deleteAllTasks();
        manager.deleteAllSubTasks();
        manager.deleteAllEpic();
        taskServer.start();
    }

    @AfterEach
    public void shutDown() {
        taskServer.stop();
    }

    @Test
    @DisplayName("test task")
    public void testTaskCreate() throws IOException, InterruptedException {
        Task task = new Task("Test 2", "Testing task 2", Status.NEW, LocalDateTime.now().minusDays(15), Duration.ofMinutes(5));
        String taskJson = gson.toJson(task);

        HttpClient client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        List<Task> tasksFromManager = manager.getTasks();

        assertEquals(201, response.statusCode());
        assertNotNull(tasksFromManager, "Задачи не создана");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("Test 2", tasksFromManager.get(0).getTitle(), "Некорректное имя задачи");
    }

    @Test
    @DisplayName("Тест обновления задачи")
    public void testTaskUpdate() throws IOException, InterruptedException {
        Task task = new Task("testTaskUpdate", "", Status.NEW, LocalDateTime.now().minusDays(15), Duration.ofMinutes(5));
        manager.createTask(task);
        Task taskToServer = new Task(task.getTaskId(), "testTaskAfterUpdate", "", Status.NEW, LocalDateTime.now().minusDays(15), Duration.ofMinutes(5));

        String taskJson = gson.toJson(taskToServer);

        HttpClient client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode(),"rCode не верный");

        Task tasksFromManager = manager.getTask(1);

        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, manager.getTasks().size(), "Некорректное количество задач");
        assertEquals("testTaskAfterUpdate", tasksFromManager.getTitle(), "Некорректное имя задачи");
    }

    @Test
    @DisplayName("Тест получения задачи по Id")
    public void testTaskGetTaskById() throws IOException, InterruptedException {
        int taskID = manager.createTask (new Task(
                "testTaskGetTaskById",
                "",
                Status.NEW,
                LocalDateTime.now(),
                Duration.ofMinutes(0)));
        int overID = manager.createTask (new Task(
                "testTaskGetTaskById",
                "",
                Status.NEW,
                LocalDateTime.now().plusMinutes(5),
                Duration.ofMinutes(0)));

        HttpClient client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        URI url = URI.create("http://localhost:8080/tasks/1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Task gotTask = gson.fromJson(response.body(), Task.class);

        assertEquals(200, response.statusCode(), "rCode не верный");
        assertNotNull(gotTask, "Задачи не возвращаются");
        assertEquals("testTaskGetTaskById", gotTask.getTitle(), "Некорректное имя переданной задачи");
    }

    @Test
    @DisplayName("Тест получения всех задач")
    public void testTaskGetAllTasks() throws IOException, InterruptedException {
        int taskID = manager.createTask(new Task(
                "testTaskGetAllTasks",
                "",
                Status.NEW,
                LocalDateTime.now(),
                Duration.ofMinutes(0)));
        int overID = manager.createTask(new Task(
                "testTaskGetAllTasks2",
                "",
                Status.NEW,
                LocalDateTime.now().plusMinutes(5),
                Duration.ofMinutes(0)));

        HttpClient client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> getAllResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
        List<Task> gotTasks = gson.fromJson(getAllResponse.body(), new TypeToken<List<Task>>(){}.getType());

        assertEquals(200, getAllResponse.statusCode());
        assertEquals(2, gotTasks.size(), "Некорректное количество задач");
        assertEquals("testTaskGetAllTasks", gotTasks.getFirst().getTitle(), "Некорректное имя задачи");
    }

    @Test
    @DisplayName("Должно удалить задачу по id")
    public void shouldDeleteTaskByIdTest() throws IOException, InterruptedException {
        int taskID = manager.createTask(new Task(
                "shouldDeleteTaskByIdTest",
                "",
                Status.NEW,
                LocalDateTime.now(),
                Duration.ofMinutes(0)));
        int overID = manager.createTask(new Task(
                "shouldDeleteTaskByIdTest2",
                "",
                Status.NEW,
                LocalDateTime.now().plusMinutes(5),
                Duration.ofMinutes(0)));
        int overOneId = manager.createTask(new Task(
                "shouldDeleteTaskByIdTest3",
                "",
                Status.NEW,
                LocalDateTime.now().plusMinutes(10),
                Duration.ofMinutes(0)));
        int theOverOneID = manager.createTask(new Task(
                "shouldDeleteTaskByIdTest4",
                "",
                Status.NEW,
                LocalDateTime.now().plusMinutes(15),
                Duration.ofMinutes(0)));

        HttpClient client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        URI url = URI.create("http://localhost:8080/tasks/2");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> deleteResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
        List<Task> tasks = List.of(manager.getTask(1), manager.getTask(3), manager.getTask(4));

        assertEquals(200, deleteResponse.statusCode());
        assertEquals(tasks,manager.getTasks(),"Таска не удалена");
    }

    @Test
    @DisplayName("Тест истории просмотров")
    public void historyTest() throws IOException, InterruptedException {
        Task task = new Task("", "", Status.NEW, LocalDateTime.now(), Duration.ofMinutes(0));
        manager.createTask(task);
        int taskId = manager.getTasks().getFirst().getTaskId();
        manager.getTask(taskId);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/history");

        HttpRequest getHistoryRequest = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> getHistoryResponse = client.send(getHistoryRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, getHistoryResponse.statusCode());
        List<Task> gotHistory = gson.fromJson(getHistoryResponse.body(), new TypeToken<List<Task>>(){}.getType());
        assertEquals(1, gotHistory.size(), "Передана не коректная история");
        assertEquals("", gotHistory.getFirst().getTitle(), "Title не совпадает");
    }

    @Test
    @DisplayName("Тест приоритизированных задач")
    public void prioritizedTest() throws IOException, InterruptedException {
        Task task = new Task("Test 2", "Testing task 2", Status.NEW, LocalDateTime.now().minusDays(15), Duration.ofMinutes(5));
        manager.createTask(task);
        int taskId = manager.getTasks().getFirst().getTaskId();
        manager.getTask(taskId);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/prioritized");

        HttpRequest getPrioritizedRequest = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> getPrioritizedResponse = client.send(getPrioritizedRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, getPrioritizedResponse.statusCode());
        List<Task> gotPrioritized = gson.fromJson(getPrioritizedResponse.body(), new TypeToken<List<Task>>(){}.getType());
        assertEquals(1, gotPrioritized.size(), "Некорректное количество задач в списке приоритета");
        assertEquals("Test 2", gotPrioritized.getFirst().getTitle(), "Некорректное имя задачи");
    }
}