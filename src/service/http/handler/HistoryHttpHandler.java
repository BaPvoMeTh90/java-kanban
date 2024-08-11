package service.http.handler;

import com.sun.net.httpserver.HttpExchange;
import model.Task;
import service.TaskManager;
import service.http.HttpTaskServer;

import java.io.IOException;
import java.util.List;

public class HistoryHttpHandler extends BaseHttpHandler {
    private final TaskManager taskManager;

    public HistoryHttpHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        System.out.println("Началась обработка /History запроса от клиента.");
        try {
            if (httpExchange.getRequestMethod().equals("GET")) {
                List<Task> history = taskManager.getHistory();
                String response = HttpTaskServer.getGson().toJson(history);
                sendText(httpExchange, response);
            } else {
                sendNotFound(httpExchange, "Такого запроса не существует");
            }
        } catch (Exception e) {
            sendServerError(httpExchange);
        }
    }
}
