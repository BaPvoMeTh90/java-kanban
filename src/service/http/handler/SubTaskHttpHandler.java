package service.http.handler;

import com.sun.net.httpserver.HttpExchange;
import model.SubTask;
import model.Task;
import service.TaskManager;
import service.exeptions.IntersectionException;
import service.http.HttpTaskServer;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class SubTaskHttpHandler extends BaseHttpHandler {
    private final TaskManager taskManager;

    public SubTaskHttpHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        System.out.println("Началась обработка /SubTask запроса от клиента.");

        switch(httpExchange.getRequestMethod()) {
            case "POST":
                InputStream inputStream = httpExchange.getRequestBody();
                String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                SubTask postTask = HttpTaskServer.getGson().fromJson(body, SubTask.class);
                if (postTask.getTaskId() == null || postTask.getTaskId() == 0) {
                    try {
                        taskManager.createSubTask(postTask);
                        writeResponse(httpExchange, "Задача создана.", 201);
                    } catch (IntersectionException e) {
                        sendHasInteractions(httpExchange);
                    } catch (Exception e) {
                        sendServerError(httpExchange);
                    }
                } else {
                    try {
                        taskManager.updateSubTask(postTask);
                        writeResponse(httpExchange, "Задача обновлена.", 201);
                    } catch (IntersectionException e) {
                        sendHasInteractions(httpExchange);
                    } catch (Exception e) {
                        sendServerError(httpExchange);
                    }
                }
            case "GET":
                Integer id = getIdFromPath(httpExchange.getRequestURI().getPath());
                if(id == null) {
                    try {
                        List<SubTask> tasks = taskManager.getSubTasks();
                        String response = HttpTaskServer.getGson().toJson(tasks);
                        sendText(httpExchange, response);
                    }catch (Exception e) {
                        sendServerError(httpExchange);
                    }
                }else {
                    try{
                        if (taskManager.getSubTask(id) != null) {
                            SubTask task = taskManager.getSubTask(id);
                            String response = HttpTaskServer.getGson().toJson(task);
                            sendText(httpExchange, response);
                        } else {
                            sendNotFound(httpExchange, "SubTask с id "+id+" отсутствует.");
                        }
                    }catch (Exception e) {
                        sendServerError(httpExchange);
                    }
                }
            case "DELETE":
                Integer deleteId = getIdFromPath(httpExchange.getRequestURI().getPath());
                try{
                    if(taskManager.getSubTask(deleteId) != null) {
                        taskManager.deleteTask(deleteId);
                        writeResponse(httpExchange, "SubTask с id " + deleteId + "- удален.", 200);
                    } else {
                        sendNotFound(httpExchange, "SubTask с id "+deleteId+" отсутствует. Уточните id задачи и повторите запрос");
                    }
                } catch (Exception e) {
                    sendServerError(httpExchange);
                }
            default:
                try {
                    sendNotFound(httpExchange, "Такого запроса не существует");
                }catch (Exception e){
                    sendServerError(httpExchange);
                }
        }
    }
}
