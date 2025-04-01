import managers.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.HttpTaskServer;
import tasks.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static handlers.BaseHttpHandler.gson;
import static org.junit.jupiter.api.Assertions.*;

public class SubtasksHandlerTest {

    HttpTaskServer taskServer;
    TaskManager manager;
    HttpClient client;

    @BeforeEach
    public void setUp() {
        this.manager = Managers.getDefault();
        this.taskServer = new HttpTaskServer(manager);
        taskServer.start();
        this.client = HttpClient.newHttpClient();
    }

    @AfterEach
    public void shutDown() {
        taskServer.stop();
    }

    @Test
    public void testAddSubtask() throws IOException, InterruptedException {
        // Создадим  эпик и потом подзадачу, так как без эпика подзадача не будет создана
        Epic epic = new Epic("Эпик_1", "тестовый эпик_1", Status.NEW);
        String epicJsonString = gson.toJson(epic);
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(epicJsonString))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());

        Duration duration1 = Duration.ofMinutes(120L);
        LocalDateTime dateTime1 = LocalDateTime.of(2025, 6, 18, 10, 30);
        Subtask subtask = new Subtask(1, "Подзадача_1", "Описание_85",
                Status.IN_PROGRESS, dateTime1, duration1);
        String subtaskJsonString = gson.toJson(subtask);
        URI url2 = URI.create("http://localhost:8080/subtasks");
        HttpRequest request2 = HttpRequest.newBuilder()
                .uri(url2)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(subtaskJsonString))
                .build();
        response = client.send(request2, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());

        // Проверяем, что создалась одна подзадача с корректным именем и описанием
        ArrayList<Subtask> subtaskFromManager = manager.getSubtasks();
        assertEquals(1, subtaskFromManager.size(), "Количество задач не соответствует ожидаемому");
        assertEquals("Подзадача_1", subtaskFromManager.get(0).getName(), "Неверное имя задачи");
        assertEquals("Описание_85", subtaskFromManager.get(0).getDescription(), "Неверное описание");
    }

    @Test
    public void testSubtaskById() throws IOException, InterruptedException {
        // Создадим  эпик и потом подзадачу, так как без эпика подзадача не будет создана
        Epic epic = new Epic("Эпик_1", "тестовый эпик_1", Status.NEW);
        String epicJsonString = gson.toJson(epic);
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(epicJsonString))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());

        Duration duration1 = Duration.ofMinutes(120L);
        LocalDateTime dateTime1 = LocalDateTime.of(2025, 6, 18, 10, 30);
        Subtask subtask = new Subtask(1, "Подзадача_1", "Описание_85",
                Status.IN_PROGRESS, dateTime1, duration1);
        String subtaskJsonString = gson.toJson(subtask);
        URI url2 = URI.create("http://localhost:8080/subtasks");
        HttpRequest request2 = HttpRequest.newBuilder()
                .uri(url2)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(subtaskJsonString))
                .build();
        response = client.send(request2, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());

        URI url3 = URI.create("http://localhost:8080/subtasks/2");
        HttpRequest request3 = HttpRequest.newBuilder()
                .GET()
                .uri(url3)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "application/json")
                .build();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        response = client.send(request3, handler);
        String jsonResponse = response.body();
        Subtask subtask3 = gson.fromJson(jsonResponse, Subtask.class);
        assertEquals(200, response.statusCode());
        assertEquals("Подзадача_1", subtask3.getName(), "Неверное имя подзадачи");
        assertEquals("Описание_85", subtask3.getDescription(), "Неверное описание");
        assertEquals(duration1.toMinutes(), subtask3.getDuration().toMinutes(), "Неверная продолжительность");
        assertTrue(response.headers().firstValue("Content-Type").isPresent()
                && response.headers().firstValue("Content-Type").get().contains("application/json"));
    }
}