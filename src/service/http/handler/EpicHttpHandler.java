package service.http.handler;

import com.sun.net.httpserver.HttpExchange;
import model.Epic;
import model.SubTask;
import model.Task;
import service.TaskManager;
import service.exeptions.IntersectionException;
import service.http.HttpTaskServer;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class EpicHttpHandler extends BaseHttpHandler {
    private final TaskManager taskManager;

    public EpicHttpHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        System.out.println("Началась обработка /Epic запроса от клиента.");

        switch(httpExchange.getRequestMethod()) {
            case "POST":
                InputStream inputStream = httpExchange.getRequestBody();
                String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                Epic postTask = HttpTaskServer.getGson().fromJson(body, Epic.class);
                    try {
                        taskManager.createEpic(postTask);
                        writeResponse(httpExchange, "Задача создана.", 201);
                    } catch (IntersectionException e) {
                        sendHasInteractions(httpExchange);
                    } catch (Exception e) {
                        sendServerError(httpExchange);
                    }
            case "GET":
                Integer id = getIdFromPath(httpExchange.getRequestURI().getPath());
                String subPath = getSubPathFromPath(httpExchange.getRequestURI().getPath());
                if(id == null) {
                    List<Epic> tasks = taskManager.getEpics();
                    String response = HttpTaskServer.getGson().toJson(tasks);
                    sendText(httpExchange, response);
                } else if (subPath!=null) {
                    ArrayList<Integer> epicsSubTasks = taskManager.getEpic(id).getSubTasks();
                    String response = HttpTaskServer.getGson().toJson(epicsSubTasks);
                    sendText(httpExchange, response);
                } else {
                    if (taskManager.getEpic(id) != null) {
                        Task task = taskManager.getEpic(id);
                        String response = HttpTaskServer.getGson().toJson(task);
                        sendText(httpExchange, response);
                    } else {
                        sendNotFound(httpExchange, "Epic с id "+id+" отсутствует.");
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