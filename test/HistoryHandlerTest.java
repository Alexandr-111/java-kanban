import com.google.gson.reflect.TypeToken;
import managers.Managers;
import managers.TaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.HttpTaskServer;
import tasks.Status;
import tasks.Task;

import java.io.IOException;
import java.lang.reflect.Type;
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

public class HistoryHandlerTest {
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
    public void testGetHistoryList() throws IOException, InterruptedException {
        //  Создаем две задачи
        Duration duration = Duration.ofMinutes(120L);
        Duration duration3 = Duration.ofMinutes(420L);
        LocalDateTime dateTime = LocalDateTime.of(2025, 6, 10, 1, 30);
        LocalDateTime dateTime3 = LocalDateTime.of(2025, 6, 11, 23, 0);
        Task task1 = new Task("Первая задача", "Описание954", Status.NEW, dateTime, duration);
        String taskJsonString1 = gson.toJson(task1);
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(taskJsonString1))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());

        Task task3 = new Task("Третья задача", "Описание347", Status.IN_PROGRESS, dateTime3, duration3);
        String taskJsonString3 = gson.toJson(task3);
        HttpRequest request3 = HttpRequest.newBuilder()
                .uri(url)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(taskJsonString3))
                .build();
        HttpResponse<String> response3 = client.send(request3, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response3.statusCode());

        // Получаем одну задачу по ID, чтобы она записалась в истории просмотров
        URI url2 = URI.create("http://localhost:8080/tasks/2");
        request = HttpRequest.newBuilder()
                .GET()
                .uri(url2)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "application/json")
                .build();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        response = client.send(request, handler);
        assertEquals(200, response.statusCode());
        assertTrue(response.headers().firstValue("Content-Type").isPresent()
                && response.headers().firstValue("Content-Type").get().contains("application/json"));

        // Получаем историю просмотров
        URI url3 = URI.create("http://localhost:8080/history");
        HttpRequest request4 = HttpRequest.newBuilder()
                .GET()
                .uri(url3)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "application/json")
                .build();
        HttpResponse.BodyHandler<String> handler2 = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response4 = client.send(request4, handler2);
        String jsonResponse2 = response4.body();
        Type listType = new TypeToken<ArrayList<Task>>() {
        }.getType();
        ArrayList<Task> list = gson.fromJson(jsonResponse2, listType);
        assertEquals(1, list.size());
        assertEquals("Третья задача", list.get(0).getName(), "Неверное имя задачи");
        assertEquals("Описание347", list.get(0).getDescription(), "Неверное описание");
        assertTrue(response.headers().firstValue("Content-Type").isPresent()
                && response.headers().firstValue("Content-Type").get().contains("application/json"));
    }
}