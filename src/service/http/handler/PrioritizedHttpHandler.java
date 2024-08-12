package service.http.handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import model.Task;
import service.TaskManager;

import java.io.IOException;
import java.util.List;

public class PrioritizedHttpHandler extends BaseHttpHandler {
    public PrioritizedHttpHandler(TaskManager manager, Gson gson) {
        super(manager,gson);
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        System.out.println("Началась обработка /Prioritized запроса от клиента.");
        try {
            if (httpExchange.getRequestMethod().equals("GET")) {
                List<Task> prioritized = manager.getPrioritizedTasks();
                String response = gson.toJson(prioritized);
                sendText(httpExchange, response);
            } else {
                writeResponse(httpExchange, "Такого запроса не существует", 404);
            }
        } catch (Exception e) {
            sendServerError(httpExchange);
        }
    }
}
