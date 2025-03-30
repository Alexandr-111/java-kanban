package handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import exceptions.ContainsKeyException;
import exceptions.TimeIntersectionsException;
import managers.TaskManager;
import tasks.Subtask;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class SubtasksHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager manager;

    public SubtasksHandler(TaskManager manager) {
        this.manager = manager;

    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        // Получим информацию об эндпоинте, к которому был запрос
        Endpoint endpoint = getEndpoint(httpExchange);
        switch (endpoint) {
            case GET_SUBTASK -> handlerForGetSubtasks(httpExchange);
            case GET_SUBTASK_ID -> handlerForGetSubtaskById(httpExchange);
            case POST_SUBTASK -> handlerForCreateSubtask(httpExchange);
            case POST_SUBTASK_ID -> handlerForUpdateSubtaskById(httpExchange);
            case DELETE_SUBTASK -> handlerForRemoveSubtask(httpExchange);
            case UNKNOWN -> httpExchange.sendResponseHeaders(404, 0);
        }
    }

    private enum Endpoint {
        GET_SUBTASK, GET_SUBTASK_ID, POST_SUBTASK, POST_SUBTASK_ID, DELETE_SUBTASK, UNKNOWN
    }

    private void handlerForGetSubtasks(HttpExchange httpExchange) throws IOException {
        try {
            ArrayList<Subtask> list = manager.getSubtasks();
            sendText(httpExchange, 200, list);
        } catch (RuntimeException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("Ошибка", "На сервере произошла ошибка, запрос не выполнен.");
            sendText(httpExchange, 500, errorResponse);
        } finally {
            httpExchange.close();
        }
    }

    private void handlerForGetSubtaskById(HttpExchange httpExchange) throws IOException {
        String[] path = httpExchange.getRequestURI().getPath().split("/");
        int id = Integer.parseInt(path[2]);
        Optional<Subtask> optSubtask = Optional.ofNullable(manager.getByIdSubtask(id));
        if (optSubtask.isEmpty()) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("Ошибка", "Подзадача с таким id не найдена.");
            sendText(httpExchange, 404, errorResponse);
        } else {
            Subtask subtask = optSubtask.get();
            sendText(httpExchange, 200, subtask);
        }
        httpExchange.close();
    }

    private void handlerForCreateSubtask(HttpExchange httpExchange) throws IOException {
        boolean isCompletion;
        InputStream inputStream = httpExchange.getRequestBody();
        String body = new String(inputStream.readAllBytes(), CHARSET);
        Subtask subtask = gson.fromJson(body, Subtask.class);
        try {
            isCompletion = manager.createSubtask(subtask);
            if (isCompletion) {
                httpExchange.sendResponseHeaders(201, 0);
            } else {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("Ошибка", "Не удалось создание подзадачи.");
                sendText(httpExchange, 500, errorResponse);
            }
        } catch (TimeIntersectionsException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("Ошибка", "Задачи пересекаются по времени.");
            sendText(httpExchange, 406, errorResponse);
        } catch (RuntimeException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("Ошибка", "На сервере произошла ошибка, запрос не выполнен.");
            sendText(httpExchange, 500, errorResponse);
        } finally {
            httpExchange.close();
        }
    }

    private void handlerForUpdateSubtaskById(HttpExchange httpExchange) throws IOException {
        boolean isCompletion;
        InputStream inputStream = httpExchange.getRequestBody();
        String body = new String(inputStream.readAllBytes(), CHARSET);
        Subtask subtask = gson.fromJson(body, Subtask.class);
        try {
            isCompletion = manager.updateSubtask(subtask);
            if (isCompletion) {
                httpExchange.sendResponseHeaders(201, 0);
            } else {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("Ошибка", "На сервере произошла ошибка, запрос не выполнен.");
                sendText(httpExchange, 500, errorResponse);
            }
        } catch (TimeIntersectionsException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("Ошибка", "Задачи пересекаются по времени.");
            sendText(httpExchange, 406, errorResponse);
        } catch (ContainsKeyException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("Ошибка", "Подзадача с таким id не найдена.");
            sendText(httpExchange, 404, errorResponse);
        } finally {
            httpExchange.close();
        }
    }

    private void handlerForRemoveSubtask(HttpExchange httpExchange) throws IOException {
        String[] path = httpExchange.getRequestURI().getPath().split("/");
        int id = Integer.parseInt(path[2]);
        boolean isCompletion = manager.removeByIdSubtask(id);
        if (isCompletion) {
            httpExchange.sendResponseHeaders(200, 0);
        } else {
            httpExchange.sendResponseHeaders(404, 0);
        }
        httpExchange.close();
    }

    private Endpoint getEndpoint(HttpExchange httpExchange) {
        String[] path = httpExchange.getRequestURI().getPath().split("/");
        String method = httpExchange.getRequestMethod();
        if (method.equals("GET") && (path.length == 2) && path[1].equals("subtasks")) {
            return Endpoint.GET_SUBTASK;
        } else if (method.equals("GET") && path.length == 3 && path[1].equals("subtasks")) {
            return Endpoint.GET_SUBTASK_ID;
        } else if (method.equals("POST") && path.length == 2 && path[1].equals("subtasks")) {
            return Endpoint.POST_SUBTASK;
        } else if (method.equals("POST") && path.length == 3 && path[1].equals("subtasks")) {
            return Endpoint.POST_SUBTASK_ID;
        } else if (method.equals("DELETE") && path.length == 3 && path[1].equals("subtasks")) {
            return Endpoint.DELETE_SUBTASK;
        } else {
            return Endpoint.UNKNOWN;
        }
    }
}