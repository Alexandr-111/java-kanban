package server;

import com.sun.net.httpserver.HttpServer;
import handlers.*;
import managers.*;
import tasks.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.time.LocalDateTime;


public class HttpTaskServer {
    public static final int PORT = 8080;
    private final HttpServer server;
    private final TaskManager manager;

    public HttpTaskServer(TaskManager manager) {
        this.manager = manager;
        InetSocketAddress socket = new InetSocketAddress("localhost", PORT);
        try {
            server = HttpServer.create(socket, 0);
            server.createContext("/tasks", new TasksHandler(manager));
            server.createContext("/epics", new EpicsHandler(manager));
            server.createContext("/subtasks", new SubtasksHandler(manager));
            server.createContext("/history", new HistoryHandler(manager));
            server.createContext("/prioritized", new PriorityHandler(manager));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        HttpTaskServer taskServer = new HttpTaskServer(Managers.getDefault());
        taskServer.start();
        Duration duration = Duration.ofMinutes(120L);
        LocalDateTime dateTime = LocalDateTime.of(2025, 5, 25, 1, 30);
        Task task = new Task("название_5", "описание_5", Status.NEW, dateTime, duration);
        boolean isSuccessful = taskServer.manager.createTask(task);
        String serializedTask = BaseHttpHandler.gson.toJson(task);
        System.out.println(isSuccessful);
        System.out.println(serializedTask);
        taskServer.stop();
    }

    public void start() {
        this.server.start();
        System.out.println("Веб-сервер 'Менеджера задач' запущен на " + PORT + " порту!");
    }

    public void stop() {
        this.server.stop(0);
        System.out.println("Веб-сервер 'Менеджера задач' остановлен!");
    }
}