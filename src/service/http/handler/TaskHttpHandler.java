package service.http.handler;

import com.sun.net.httpserver.HttpExchange;
import model.Task;
import service.TaskManager;
import service.exeptions.IntersectionException;
import service.http.HttpTaskServer;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class TaskHttpHandler extends BaseHttpHandler {

    private final TaskManager taskManager;

    public TaskHttpHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        System.out.println("Началась обработка /Task запроса от клиента.");

        switch (httpExchange.getRequestMethod()) {
            case "POST":
                InputStream inputStream = httpExchange.getRequestBody();
                String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                Task postTask = HttpTaskServer.getGson().fromJson(body, Task.class);
                if (postTask.getTaskId() == 0 || postTask.getTaskId() == null) {
                    try {
                        taskManager.createTask(postTask);
                        writeResponse(httpExchange, "Задача создана.", 201);
                    } catch (IntersectionException e) {
                        sendHasInteractions(httpExchange);
                    } catch (Exception e) {
                        sendServerError(httpExchange);
                    }
                } else {
                    try {
                        taskManager.updateTask(postTask);
                        writeResponse(httpExchange, "Задача обновлена.", 201);
                    } catch (IntersectionException e) {
                        sendHasInteractions(httpExchange);
                    } catch (Exception e) {
                        sendServerError(httpExchange);
                    }
                }
            case "GET":
                Integer id = getIdFromPath(httpExchange.getRequestURI().getPath());
                if (id == null) {
                    try {
                        List<Task> tasks = taskManager.getTasks();
                        String response = HttpTaskServer.getGson().toJson(tasks);
                        sendText(httpExchange, response);
                    } catch (Exception e) {
                        sendServerError(httpExchange);
                    }
                } else {
                    try {
                        if (taskManager.getTask(id) != null) {
                            Task task = taskManager.getTask(id);
                            String response = HttpTaskServer.getGson().toJson(task);
                            sendText(httpExchange, response);
                        } else {
                            sendNotFound(httpExchange, "Task с id "+id+" отсутствует.");
                        }
                    } catch (Exception e) {
                        sendServerError(httpExchange);
                    }
                }
            case "DELETE":
                Integer deleteId = getIdFromPath(httpExchange.getRequestURI().getPath());
                try{
                    if(taskManager.getTask(deleteId) != null) {
                        taskManager.deleteTask(deleteId);
                        writeResponse(httpExchange, "Таск с id " + deleteId + "- удален.", 200);
                    } else {
                        sendNotFound(httpExchange, "Task с id "+deleteId+" отсутствует. Уточните id задачи и повторите запрос");
                    }
                } catch (Exception e) {
                    sendServerError(httpExchange);
                }
            default:
                try {
                    sendNotFound(httpExchange, "Такого запроса не существует");
                } catch (Exception e){
                    sendServerError(httpExchange);
                }
        }
    }
}
