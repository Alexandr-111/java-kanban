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
            case UNKNOWN -> httpExchange.sendResponseHeaders(404, 0);
        }
    }

    private enum Endpoint {
        GET_EPICS, GET_EPIC_ID, GET_EPIC_SUBTASK, POST_EPIC, DELETE_EPIC, UNKNOWN
    }

    private void handlerForGetEpics(HttpExchange httpExchange) throws IOException {
        try {
            ArrayList<Epic> list = manager.getEpics();
            sendText(httpExchange, 200, list);
        } catch (RuntimeException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("Ошибка", "На сервере произошла ошибка, запрос не выполнен.");
            sendText(httpExchange, 500, errorResponse);
        } finally {
            httpExchange.close();
        }
    }

    private void handlerForGetEpicById(HttpExchange httpExchange) throws IOException {
        String[] path = httpExchange.getRequestURI().getPath().split("/");
        int id = Integer.parseInt(path[2]);
        Optional<Epic> optEpic = Optional.ofNullable(manager.getByIdEpic(id));
        if (optEpic.isEmpty()) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("Ошибка", "Задача с таким id не найдена.");
            sendText(httpExchange, 404, errorResponse);
        } else {
            Epic epic = optEpic.get();
            sendText(httpExchange, 200, epic);
        }
        httpExchange.close();
    }

    private void handlerForCreateEpic(HttpExchange httpExchange) throws IOException {
        boolean isCompletion;
        InputStream inputStream = httpExchange.getRequestBody();
        String body = new String(inputStream.readAllBytes(), CHARSET);
        Epic epic = gson.fromJson(body, Epic.class);
        try {
            isCompletion = manager.createEpic(epic);
            if (isCompletion) {
                httpExchange.sendResponseHeaders(201, 0);
            } else {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("Ошибка", "Не удалось создание задачи.");
                sendText(httpExchange, 500, errorResponse);
            }
        } catch (RuntimeException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("Ошибка", "На сервере произошла ошибка, запрос не выполнен.");
            sendText(httpExchange, 500, errorResponse);
        } finally {
            httpExchange.close();
        }
    }

    private void handlerForGetSubtasksOfEpic(HttpExchange httpExchange) throws IOException {
        try {
            String[] path = httpExchange.getRequestURI().getPath().split("/");
            int idEpic = Integer.parseInt(path[2]);
            List<Subtask> list = manager.getSubtaskOfEpic(idEpic);
            sendText(httpExchange, 200, list);
        } catch (RuntimeException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("Ошибка", "На сервере произошла ошибка, запрос не выполнен.");
            sendText(httpExchange, 500, errorResponse);
        } finally {
            httpExchange.close();
        }
    }

    private void handlerForRemoveEpic(HttpExchange httpExchange) throws IOException {
        String[] path = httpExchange.getRequestURI().getPath().split("/");
        int id = Integer.parseInt(path[2]);
        int isCompletion = manager.removeByIdEpic(id);
        if (isCompletion == -1) {
            httpExchange.sendResponseHeaders(404, 0);
        } else {
            httpExchange.sendResponseHeaders(200, 0);
        }
        httpExchange.close();
    }

    private Endpoint getEndpoint(HttpExchange httpExchange) {
        String[] path = httpExchange.getRequestURI().getPath().split("/");
        String method = httpExchange.getRequestMethod();
        if (method.equals("GET") && (path.length == 2) && path[1].equals("epics")) {
            return EpicsHandler.Endpoint.GET_EPICS;
        } else if (method.equals("GET") && path.length == 3 && path[1].equals("epics")) {
            return EpicsHandler.Endpoint.GET_EPIC_ID;
        } else if (method.equals("GET") && path.length == 4 && path[1].equals("epics") && path[3].equals("subtasks")) {
            return EpicsHandler.Endpoint.GET_EPIC_SUBTASK;
        } else if (method.equals("POST") && path.length == 2 && path[1].equals("epics")) {
            return EpicsHandler.Endpoint.POST_EPIC;
        } else if (method.equals("DELETE") && path.length == 3 && path[1].equals("epics")) {
            return EpicsHandler.Endpoint.DELETE_EPIC;
        } else {
            return EpicsHandler.Endpoint.UNKNOWN;
        }
    }
}