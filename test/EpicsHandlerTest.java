import managers.Managers;
import managers.TaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.HttpTaskServer;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static handlers.BaseHttpHandler.gson;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class EpicsHandlerTest {
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
    public void testAddEpic() throws IOException, InterruptedException {
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
        // проверяем, что создался один эпик с корректным именем
        ArrayList<Epic> epicsFromManager = manager.getEpics();
        assertEquals(1, epicsFromManager.size(), "Количество эпиков не соответствует ожидаемому");
        assertEquals("Эпик_1", epicsFromManager.get(0).getName(), "Неверное имя эпика");
        assertEquals("тестовый эпик_1", epicsFromManager.get(0).getDescription(), "Неверное описание");
    }

    @Test
    public void testGetEpicById() throws IOException, InterruptedException {
        Epic epic = new Epic("Эпик_10", "тестовый эпик_10", Status.NEW);
        String epicJsonString = gson.toJson(epic);
        URI url1 = URI.create("http://localhost:8080/epics");
        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(url1)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(epicJsonString))
                .build();
        HttpResponse<String> response = client.send(request1, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());

        URI url2 = URI.create("http://localhost:8080/epics/1");
        HttpRequest request2 = HttpRequest.newBuilder()
                .GET()
                .uri(url2)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "application/json")
                .build();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        response = client.send(request2, handler);
        String jsonResponse = response.body();
        Epic epic1 = gson.fromJson(jsonResponse, Epic.class);
        assertEquals(200, response.statusCode());
        assertEquals("Эпик_10", epic1.getName(), "Неверное имя задачи");
        assertEquals("тестовый эпик_10", epic1.getDescription(), "Неверное описание");
        ;
        assertTrue(response.headers().firstValue("Content-Type").isPresent()
                && response.headers().firstValue("Content-Type").get().contains("application/json"));
    }

    @Test
    public void testDeleteEpicById() throws IOException, InterruptedException {
        // Создадим  эпик
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

        // Создадим подзадачу
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

        // Удалим эпик
        URI url3 = URI.create("http://localhost:8080/epics/1");
        HttpRequest request3 = HttpRequest.newBuilder()
                .DELETE()
                .uri(url3)
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        HttpResponse.BodyHandler<Void> handler3 = HttpResponse.BodyHandlers.discarding();
        HttpResponse<Void> response3 = client.send(request3, handler3);
        assertEquals(200, response3.statusCode());
        assertEquals(0, manager.getEpics().size());
        assertEquals(0, manager.getSubtasks().size());

        // Проверим случай, если введен id которого нет.
        URI url4 = URI.create("http://localhost:8080/tasks/9");
        HttpRequest request4 = HttpRequest.newBuilder()
                .GET()
                .uri(url4)
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        HttpResponse.BodyHandler<Void> handler4 = HttpResponse.BodyHandlers.discarding();
        HttpResponse<Void> response4 = client.send(request4, handler4);
        assertEquals(404, response4.statusCode());
    }
}