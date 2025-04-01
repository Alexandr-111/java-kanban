package handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import managers.TaskManager;
import tasks.Task;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PriorityHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager manager;

    public PriorityHandler(TaskManager manager) {
        this.manager = manager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try (HttpExchange httpExchange = exchange) {
            List<Task> list = null;
            String[] path = httpExchange.getRequestURI().getPath().split("/");
            String method = httpExchange.getRequestMethod();
            try {
                if (method.equals(GET) && (path.length == 2) && path[1].equals("prioritized")) {
                    list = manager.getPrioritizedTasks();
                } else {
                    httpExchange.sendResponseHeaders(HttpStatus.NOT_FOUND.value(), 0);
                }
            } catch (RuntimeException e) {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("Ошибка", "На сервере произошла ошибка, запрос не выполнен.");
                sendText(httpExchange, HttpStatus.INTERNAL_SERVER_ERROR.value(), errorResponse);
            }
            sendText(httpExchange, HttpStatus.OK.value(), list);
        }
    }
}