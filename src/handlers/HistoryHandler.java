package handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import managers.TaskManager;
import tasks.Task;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HistoryHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager manager;

    public HistoryHandler(TaskManager manager) {
        this.manager = manager;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        try {

            String[] path = httpExchange.getRequestURI().getPath().split("/");
            String method = httpExchange.getRequestMethod();
            if (method.equals("GET") && (path.length == 2) && path[1].equals("history")) {
                List<Task> list = manager.getBrowsingHistory();
                sendText(httpExchange, 200, list);
            } else {
                httpExchange.sendResponseHeaders(404, 0);
            }
        } catch (RuntimeException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("Ошибка", "На сервере произошла ошибка, запрос не выполнен.");
            sendText(httpExchange, 500, errorResponse);
        } finally {
            httpExchange.close();
        }
    }
}