package service.http.handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import model.Task;
import service.TaskManager;
import service.exeptions.IntersectionException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class TaskHttpHandler extends BaseHttpHandler {
    public TaskHttpHandler(TaskManager manager, Gson gson) {
        super(manager, gson);
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        System.out.println("Началась обработка /Task запроса от клиента.");

        switch (httpExchange.getRequestMethod()) {
            case "POST":
                InputStream inputStream = httpExchange.getRequestBody();
                String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                if (!body.isEmpty()) {
                    Task postTask = gson.fromJson(body, Task.class);
                    if (postTask.getTaskId() == 0 || postTask.getTaskId() == null) {
                        try {
                            manager.createTask(postTask);
                            writeResponse(httpExchange, "Задача создана.", 201);
                        } catch (IntersectionException e) {
                            sendHasInteractions(httpExchange);
                        } catch (Exception e) {
                            sendServerError(httpExchange);
                        }
                    } else {
                        try {
                            manager.updateTask(postTask);
                            writeResponse(httpExchange, "Задача обновлена.", 201);
                        } catch (IntersectionException e) {
                            sendHasInteractions(httpExchange);
                        } catch (Exception e) {
                            sendServerError(httpExchange);
                        }
                    }
                } else {
                    throw new RuntimeException("Данные не переданы");
                }
            case "GET":
                Integer id = getIdFromPath(httpExchange.getRequestURI().getPath());
                if (id == null) {
                    try {
                        List<Task> tasks = manager.getTasks();
                        String response = gson.toJson(tasks);
                        sendText(httpExchange, response);
                    } catch (Exception e) {
                        sendServerError(httpExchange);
                    }
                } else {
                    try {
                        if (manager.getTask(id) != null) {
                            Task task = manager.getTask(id);
                            String response = gson.toJson(task);
                            sendText(httpExchange, response);
                        } else {
                            sendNotFound(httpExchange, "Task с id " + id + " отсутствует.");
                        }
                    } catch (Exception e) {
                        sendServerError(httpExchange);
                    }
                }
            case "DELETE":
                Integer deleteId = getIdFromPath(httpExchange.getRequestURI().getPath());
                try {
                    if (manager.getTask(deleteId) != null) {
                        manager.deleteTask(deleteId);
                        writeResponse(httpExchange, "Таск с id " + deleteId + "- удален.", 200);
                    } else {
                        sendNotFound(httpExchange, "Task с id " + deleteId + " отсутствует. Уточните id задачи и повторите запрос");
                    }
                } catch (Exception e) {
                    sendServerError(httpExchange);
                }
            default:
                try {
                    sendNotFound(httpExchange, "Такого запроса не существует");
                } catch (Exception e) {
                    sendServerError(httpExchange);
                }
        }
    }
}
