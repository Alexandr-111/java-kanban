package handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import exceptions.ContainsKeyException;
import exceptions.TimeIntersectionsException;
import managers.TaskManager;
import tasks.Task;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class TasksHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager manager;

    public TasksHandler(TaskManager manager) {
        this.manager = manager;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        // Получим информацию об эндпоинте, к которому был запрос
        Endpoint endpoint = getEndpoint(httpExchange);
        switch (endpoint) {
            case GET_TASKS -> handlerForGetTasks(httpExchange);
            case GET_TASK_ID -> handlerForGetTaskById(httpExchange);
            case POST_TASK -> handlerForCreateTask(httpExchange);
            case POST_TASK_ID -> handlerForUpdateTaskById(httpExchange);
            case DELETE_TASK -> handlerForRemoveTask(httpExchange);
            case UNKNOWN -> httpExchange.sendResponseHeaders(404, 0);
        }
    }

    private enum Endpoint {
        GET_TASKS, GET_TASK_ID, POST_TASK, POST_TASK_ID, DELETE_TASK, UNKNOWN
    }

    private void handlerForGetTasks(HttpExchange httpExchange) throws IOException {
        try {
            ArrayList<Task> list = manager.getTasks();
            sendText(httpExchange, 200, list);
        } catch (RuntimeException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("Ошибка", "На сервере произошла ошибка, запрос не выполнен.");
            sendText(httpExchange, 500, errorResponse);
        } finally {
            httpExchange.close();
        }
    }

    private void handlerForGetTaskById(HttpExchange httpExchange) throws IOException {
        String[] path = httpExchange.getRequestURI().getPath().split("/");
        int id = Integer.parseInt(path[2]);
        Optional<Task> optTask = Optional.ofNullable(manager.getByIdTask(id));
        if (optTask.isEmpty()) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("Ошибка", "Задача с таким id не найдена.");
            sendText(httpExchange, 404, errorResponse);
        } else {
            Task task = optTask.get();
            sendText(httpExchange, 200, task);
        }
        httpExchange.close();
    }

    private void handlerForCreateTask(HttpExchange httpExchange) throws IOException {
        boolean isCompletion;
        InputStream inputStream = httpExchange.getRequestBody();
        String body = new String(inputStream.readAllBytes(), CHARSET);
        Task task = gson.fromJson(body, Task.class);
        try {
            isCompletion = manager.createTask(task);
            if (isCompletion) {
                httpExchange.sendResponseHeaders(201, 0);
            } else {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("Ошибка", "Не удалось создание задачи.");
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

    private void handlerForUpdateTaskById(HttpExchange httpExchange) throws IOException {
        boolean isCompletion;
        InputStream inputStream = httpExchange.getRequestBody();
        String body = new String(inputStream.readAllBytes(), CHARSET);
        Task task = gson.fromJson(body, Task.class);
        try {
            isCompletion = manager.updateTask(task);
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
            errorResponse.put("Ошибка", "Задача с таким id не найдена.");
            sendText(httpExchange, 404, errorResponse);
        } finally {
            httpExchange.close();
        }
    }

    private void handlerForRemoveTask(HttpExchange httpExchange) throws IOException {
        String[] path = httpExchange.getRequestURI().getPath().split("/");
        int id = Integer.parseInt(path[2]);
        boolean isCompletion = manager.removeByIdTask(id);
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
        if (method.equals("GET") && (path.length == 2) && path[1].equals("tasks")) {
            return Endpoint.GET_TASKS;
        } else if (method.equals("GET") && path.length == 3 && path[1].equals("tasks")) {
            return Endpoint.GET_TASK_ID;
        } else if (method.equals("POST") && path.length == 2 && path[1].equals("tasks")) {
            return Endpoint.POST_TASK;
        } else if (method.equals("POST") && path.length == 3 && path[1].equals("tasks")) {
            return Endpoint.POST_TASK_ID;
        } else if (method.equals("DELETE") && path.length == 3 && path[1].equals("tasks")) {
            return Endpoint.DELETE_TASK;
        } else {
            return Endpoint.UNKNOWN;
        }
    }
}