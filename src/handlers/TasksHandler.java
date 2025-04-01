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
            case UNKNOWN -> httpExchange.sendResponseHeaders(HttpStatus.NOT_FOUND.value(), 0);
        }
    }

    private enum Endpoint {
        GET_TASKS, GET_TASK_ID, POST_TASK, POST_TASK_ID, DELETE_TASK, UNKNOWN
    }

    private void handlerForGetTasks(HttpExchange exchange) throws IOException {
        try (HttpExchange httpExchange = exchange) {
            ArrayList<Task> list = null;
            try {
                list = manager.getTasks();
            } catch (RuntimeException e) {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("Ошибка", "На сервере произошла ошибка, запрос не выполнен.");
                sendText(httpExchange, HttpStatus.INTERNAL_SERVER_ERROR.value(), errorResponse);
            }
            sendText(httpExchange, HttpStatus.OK.value(), list);
        }
    }

    private void handlerForGetTaskById(HttpExchange exchange) throws IOException {
        try (HttpExchange httpExchange = exchange) {
            String[] path = httpExchange.getRequestURI().getPath().split("/");
            int id = Integer.parseInt(path[2]);
            Optional<Task> optTask = Optional.ofNullable(manager.getByIdTask(id));
            if (optTask.isEmpty()) {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("Ошибка", "Задача с таким id не найдена.");
                sendText(httpExchange, HttpStatus.NOT_FOUND.value(), errorResponse);
            } else {
                Task task = optTask.get();
                sendText(httpExchange, HttpStatus.OK.value(), task);
            }
        }
    }

    private void handlerForCreateTask(HttpExchange exchange) throws IOException {
        boolean isCompletion;
        try (HttpExchange httpExchange = exchange) {
            String body;
            try (InputStream inputStream = httpExchange.getRequestBody()) {
                body = new String(inputStream.readAllBytes(), CHARSET);
            }
            Task task = gson.fromJson(body, Task.class);
            try {
                isCompletion = manager.createTask(task);
                if (isCompletion) {
                    httpExchange.sendResponseHeaders(HttpStatus.CREATED.value(), 0);
                } else {
                    Map<String, String> errorResponse = new HashMap<>();
                    errorResponse.put("Ошибка", "Не удалось создание задачи.");
                    sendText(httpExchange, HttpStatus.INTERNAL_SERVER_ERROR.value(), errorResponse);
                }
            } catch (TimeIntersectionsException e) {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("Ошибка", "Задачи пересекаются по времени.");
                sendText(httpExchange, HttpStatus.NOT_ACCEPTABLE.value(), errorResponse);
            } catch (RuntimeException e) {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("Ошибка", "На сервере произошла ошибка, запрос не выполнен.");
                sendText(httpExchange, HttpStatus.INTERNAL_SERVER_ERROR.value(), errorResponse);
            }
        }
    }

    private void handlerForUpdateTaskById(HttpExchange exchange) throws IOException {
        boolean isCompletion;
        try (HttpExchange httpExchange = exchange) {
            String body;
            try (InputStream inputStream = httpExchange.getRequestBody()) {
                body = new String(inputStream.readAllBytes(), CHARSET);
            }
            Task task = gson.fromJson(body, Task.class);
            try {
                isCompletion = manager.updateTask(task);
                if (isCompletion) {
                    httpExchange.sendResponseHeaders(HttpStatus.CREATED.value(), 0);
                } else {
                    Map<String, String> errorResponse = new HashMap<>();
                    errorResponse.put("Ошибка", "На сервере произошла ошибка, запрос не выполнен.");
                    sendText(httpExchange, HttpStatus.INTERNAL_SERVER_ERROR.value(), errorResponse);
                }
            } catch (TimeIntersectionsException e) {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("Ошибка", "Задачи пересекаются по времени.");
                sendText(httpExchange, HttpStatus.NOT_ACCEPTABLE.value(), errorResponse);
            } catch (ContainsKeyException e) {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("Ошибка", "Задача с таким id не найдена.");
                sendText(httpExchange, HttpStatus.NOT_FOUND.value(), errorResponse);
            }
        }
    }

    private void handlerForRemoveTask(HttpExchange exchange) throws IOException {
        try (HttpExchange httpExchange = exchange) {
            String[] path = httpExchange.getRequestURI().getPath().split("/");
            int id = Integer.parseInt(path[2]);
            boolean isCompletion = manager.removeByIdTask(id);
            if (isCompletion) {
                httpExchange.sendResponseHeaders(HttpStatus.OK.value(), 0);
            } else {
                httpExchange.sendResponseHeaders(HttpStatus.NOT_FOUND.value(), 0);
            }
        }
    }

    private Endpoint getEndpoint(HttpExchange httpExchange) {
        String[] path = httpExchange.getRequestURI().getPath().split("/");
        String method = httpExchange.getRequestMethod();
        if (method.equals(GET) && (path.length == 2) && path[1].equals("tasks")) {
            return Endpoint.GET_TASKS;
        } else if (method.equals(GET) && path.length == 3 && path[1].equals("tasks")) {
            return Endpoint.GET_TASK_ID;
        } else if (method.equals(POST) && path.length == 2 && path[1].equals("tasks")) {
            return Endpoint.POST_TASK;
        } else if (method.equals(POST) && path.length == 3 && path[1].equals("tasks")) {
            return Endpoint.POST_TASK_ID;
        } else if (method.equals(DELETE) && path.length == 3 && path[1].equals("tasks")) {
            return Endpoint.DELETE_TASK;
        } else {
            return Endpoint.UNKNOWN;
        }
    }
}