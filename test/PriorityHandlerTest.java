import com.google.gson.reflect.TypeToken;
import managers.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.HttpTaskServer;
import tasks.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static handlers.BaseHttpHandler.gson;
import static org.junit.jupiter.api.Assertions.*;

public class PriorityHandlerTest {
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
    public void testGetPrioritizedList() throws IOException, InterruptedException {
        Duration duration = Duration.ofMinutes(120L);
        Duration duration2 = Duration.ofMinutes(390L);
        Duration duration3 = Duration.ofMinutes(420L);
        LocalDateTime dateTime = LocalDateTime.of(2025, 6, 10, 1, 30);
        LocalDateTime dateTime2 = LocalDateTime.of(2025, 6, 10, 7, 30);
        LocalDateTime dateTime3 = LocalDateTime.of(2025, 6, 11, 23, 0);
        Task task1 = new Task("Первая задача", "Описание954", Status.NEW, dateTime, duration);
        String taskJsonString1 = gson.toJson(task1);
        URI url1 = URI.create("http://localhost:8080/tasks");
        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(url1)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(taskJsonString1))
                .build();
        HttpResponse<String> response = client.send(request1, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());

        Task task3 = new Task("Третья задача", "Описание347", Status.IN_PROGRESS, dateTime3, duration3);
        String taskJsonString3 = gson.toJson(task3);
        HttpRequest request3 = HttpRequest.newBuilder()
                .uri(url1)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(taskJsonString3))
                .build();
        response = client.send(request3, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());

        Task task2 = new Task("Вторая задача", "Описание568", Status.IN_PROGRESS, dateTime2, duration2);
        String taskJsonString2 = gson.toJson(task2);
        HttpRequest request2 = HttpRequest.newBuilder()
                .uri(url1)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(taskJsonString2))
                .build();
        response = client.send(request2, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());

        URI url2 = URI.create("http://localhost:8080/prioritized");
        HttpRequest request4 = HttpRequest.newBuilder()
                .GET()
                .uri(url2)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "application/json")
                .build();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        response = client.send(request4, handler);
        String jsonResponse = response.body();
        Type listType = new TypeToken<ArrayList<Task>>() {
        }.getType();
        List<Task> list = gson.fromJson(jsonResponse, listType);
        assertEquals(3, list.size());
        assertEquals("Первая задача", list.get(0).getName(), "Неверное имя задачи");
        assertEquals("Вторая задача", list.get(1).getName(), "Неверное имя задачи");
        assertEquals("Третья задача", list.get(2).getName(), "Неверное имя задачи");
        assertTrue(response.headers().firstValue("Content-Type").isPresent()
                && response.headers().firstValue("Content-Type").get().contains("application/json"));
    }
}