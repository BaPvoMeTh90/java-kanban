package service.http.handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import model.SubTask;
import service.TaskManager;
import service.exeptions.IntersectionException;
import service.http.HttpTaskServer;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class SubTaskHttpHandler extends BaseHttpHandler {
    public SubTaskHttpHandler(TaskManager manager, Gson gson) {
        super(manager, gson);
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        System.out.println("Началась обработка /SubTask запроса от клиента.");

        switch (httpExchange.getRequestMethod()) {
            case "POST":
                InputStream inputStream = httpExchange.getRequestBody();
                String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                if (!body.isEmpty()) {
                    SubTask postTask = gson.fromJson(body, SubTask.class);
                    if (postTask.getTaskId() == null || postTask.getTaskId() == 0) {
                        try {
                            manager.createSubTask(postTask);
                            writeResponse(httpExchange, "Задача создана.", 201);
                        } catch (IntersectionException e) {
                            sendHasInteractions(httpExchange);
                        } catch (Exception e) {
                            sendServerError(httpExchange);
                        }
                    } else {
                        try {
                            manager.updateSubTask(postTask);
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
                        List<SubTask> tasks = manager.getSubTasks();
                        String response = gson.toJson(tasks);
                        sendText(httpExchange, response);
                    } catch (Exception e) {
                        sendServerError(httpExchange);
                    }
                } else {
                    try {
                        if (manager.getSubTask(id) != null) {
                            SubTask task = manager.getSubTask(id);
                            String response = gson.toJson(task);
                            sendText(httpExchange, response);
                        } else {
                            sendNotFound(httpExchange, "SubTask с id " + id + " отсутствует.");
                        }
                    } catch (Exception e) {
                        sendServerError(httpExchange);
                    }
                }
            case "DELETE":
                Integer deleteId = getIdFromPath(httpExchange.getRequestURI().getPath());
                try {
                    if (manager.getSubTask(deleteId) != null) {
                        manager.deleteTask(deleteId);
                        writeResponse(httpExchange, "SubTask с id " + deleteId + "- удален.", 200);
                    } else {
                        sendNotFound(httpExchange, "SubTask с id " + deleteId + " отсутствует. Уточните id задачи и повторите запрос");
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
