package handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import managers.TaskManager;
import tasks.Epic;
import tasks.Subtask;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class EpicsHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager manager;

    public EpicsHandler(TaskManager manager) {
        this.manager = manager;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        // Получим информацию об эндпоинте, к которому был запрос
        EpicsHandler.Endpoint endpoint = getEndpoint(httpExchange);
        switch (endpoint) {
            case GET_EPICS -> handlerForGetEpics(httpExchange);
            case GET_EPIC_ID -> handlerForGetEpicById(httpExchange);
            case GET_EPIC_SUBTASK -> handlerForGetSubtasksOfEpic(httpExchange);
            case POST_EPIC -> handlerForCreateEpic(httpExchange);
            case DELETE_EPIC -> handlerForRemoveEpic(httpExchange);
            case UNKNOWN -> httpExchange.sendResponseHeaders(HttpStatus.NOT_FOUND.value(), 0);
        }
    }

    private enum Endpoint {
        GET_EPICS, GET_EPIC_ID, GET_EPIC_SUBTASK, POST_EPIC, DELETE_EPIC, UNKNOWN
    }

    private void handlerForGetEpics(HttpExchange exchange) throws IOException {
        try (HttpExchange httpExchange = exchange) {
            ArrayList<Epic> list = null;
            try {
                list = manager.getEpics();
            } catch (RuntimeException e) {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("Ошибка", "На сервере произошла ошибка, запрос не выполнен.");
                sendText(httpExchange, HttpStatus.INTERNAL_SERVER_ERROR.value(), errorResponse);
            }
            sendText(httpExchange, HttpStatus.OK.value(), list);
        }
    }


    private void handlerForGetEpicById(HttpExchange exchange) throws IOException {
        try (HttpExchange httpExchange = exchange) {
            String[] path = httpExchange.getRequestURI().getPath().split("/");
            int id = Integer.parseInt(path[2]);
            Optional<Epic> optEpic = Optional.ofNullable(manager.getByIdEpic(id));
            if (optEpic.isEmpty()) {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("Ошибка", "Задача с таким id не найдена.");
                sendText(httpExchange, HttpStatus.NOT_FOUND.value(), errorResponse);
            } else {
                Epic epic = optEpic.get();
                sendText(httpExchange, HttpStatus.OK.value(), epic);
            }
        }
    }

    private void handlerForCreateEpic(HttpExchange exchange) throws IOException {
        boolean isCompletion;
        try (HttpExchange httpExchange = exchange) {
            String body;
            try (InputStream inputStream = httpExchange.getRequestBody()) {
                body = new String(inputStream.readAllBytes(), CHARSET);
            }
            Epic epic = gson.fromJson(body, Epic.class);
            try {
                isCompletion = manager.createEpic(epic);
                if (isCompletion) {
                    httpExchange.sendResponseHeaders(HttpStatus.CREATED.value(), 0);
                } else {
                    Map<String, String> errorResponse = new HashMap<>();
                    errorResponse.put("Ошибка", "Не удалось создание задачи.");
                    sendText(httpExchange, HttpStatus.INTERNAL_SERVER_ERROR.value(), errorResponse);
                }
            } catch (RuntimeException e) {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("Ошибка", "На сервере произошла ошибка, запрос не выполнен.");
                sendText(httpExchange, HttpStatus.INTERNAL_SERVER_ERROR.value(), errorResponse);
            }
        }
    }

    private void handlerForGetSubtasksOfEpic(HttpExchange exchange) throws IOException {
        List<Subtask> list;
        try (HttpExchange httpExchange = exchange) {
            String[] path = httpExchange.getRequestURI().getPath().split("/");
            int idEpic = Integer.parseInt(path[2]);
            try {
                list = manager.getSubtaskOfEpic(idEpic);
            } catch (RuntimeException e) {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("Ошибка", "На сервере произошла ошибка, запрос не выполнен.");
                sendText(httpExchange, HttpStatus.INTERNAL_SERVER_ERROR.value(), errorResponse);
                return;
            }
            sendText(httpExchange, HttpStatus.OK.value(), list);
        }
    }

    private void handlerForRemoveEpic(HttpExchange exchange) throws IOException {
        try (HttpExchange httpExchange = exchange) {
            String[] path = httpExchange.getRequestURI().getPath().split("/");
            int id = Integer.parseInt(path[2]);
            int isCompletion = manager.removeByIdEpic(id);
            if (isCompletion == -1) {
                httpExchange.sendResponseHeaders(HttpStatus.NOT_FOUND.value(), 0);
            } else {
                httpExchange.sendResponseHeaders(HttpStatus.OK.value(), 0);
            }
        }
    }

    private Endpoint getEndpoint(HttpExchange httpExchange) {
        String[] path = httpExchange.getRequestURI().getPath().split("/");
        String method = httpExchange.getRequestMethod();
        if (method.equals(GET) && (path.length == 2) && path[1].equals("epics")) {
            return EpicsHandler.Endpoint.GET_EPICS;
        } else if (method.equals(GET) && path.length == 3 && path[1].equals("epics")) {
            return EpicsHandler.Endpoint.GET_EPIC_ID;
        } else if (method.equals(GET) && path.length == 4 && path[1].equals("epics") && path[3].equals("subtasks")) {
            return EpicsHandler.Endpoint.GET_EPIC_SUBTASK;
        } else if (method.equals(POST) && path.length == 2 && path[1].equals("epics")) {
            return EpicsHandler.Endpoint.POST_EPIC;
        } else if (method.equals(DELETE) && path.length == 3 && path[1].equals("epics")) {
            return EpicsHandler.Endpoint.DELETE_EPIC;
        } else {
            return EpicsHandler.Endpoint.UNKNOWN;
        }
    }
}