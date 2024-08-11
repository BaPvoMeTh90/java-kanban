package service.http.handler;

import com.sun.net.httpserver.HttpExchange;
import model.Task;
import service.TaskManager;
import service.http.HttpTaskServer;

import java.io.IOException;
import java.util.List;

public class PrioritizedHttpHandler extends BaseHttpHandler {
    private final TaskManager taskManager;

    public PrioritizedHttpHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        System.out.println("Началась обработка /Prioritized запроса от клиента.");
        try{
            if (httpExchange.getRequestMethod().equals("GET")) {
                List<Task> prioritized = taskManager.getPrioritizedTasks();
                String response = HttpTaskServer.getGson().toJson(prioritized);
                sendText(httpExchange, response);
            } else {
                writeResponse(httpExchange, "Такого запроса не существует", 404);
            }
        } catch (Exception e){
            sendServerError(httpExchange);
        }
    }
}
