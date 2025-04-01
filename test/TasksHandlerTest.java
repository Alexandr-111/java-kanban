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
import java.util.List;

import static handlers.BaseHttpHandler.gson;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TasksHandlerTest {
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
    public void testAddTask() throws IOException, InterruptedException {
        Duration duration = Duration.ofMinutes(120L);
        LocalDateTime dateTime = LocalDateTime.of(2025, 5, 25, 1, 30);
        Task task = new Task("задача_5", "тестовая задача_5", Status.NEW, dateTime, duration);
        String taskJsonString = gson.toJson(task);
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(taskJsonString))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
        // проверяем, что создалась одна задача с корректным именем
        ArrayList<Task> tasksFromManager = manager.getTasks();
        assertEquals(1, tasksFromManager.size(), "Количество задач не соответствует ожидаемому");
        assertEquals("задача_5", tasksFromManager.get(0).getName(), "Неверное имя задачи");
        assertEquals("тестовая задача_5", tasksFromManager.get(0).getDescription(), "Неверное описание");
    }

    @Test
    public void testGetTaskById() throws IOException, InterruptedException {
        Duration duration = Duration.ofMinutes(120L);
        LocalDateTime dateTime = LocalDateTime.of(2025, 6, 5, 23, 30);
        Task task = new Task("Задача_23", "Описание954", Status.NEW, dateTime, duration);
        String taskJsonString = gson.toJson(task);
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(url)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(taskJsonString))
                .build();
        HttpResponse<String> response = client.send(request1, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());

        URI url2 = URI.create("http://localhost:8080/tasks/1");
        HttpRequest request2 = HttpRequest.newBuilder()
                .GET()
                .uri(url2)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "application/json")
                .build();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        response = client.send(request2, handler);
        String jsonResponse = response.body();
        Task task1 = gson.fromJson(jsonResponse, Task.class);
        assertEquals(200, response.statusCode());
        assertEquals("Задача_23", task1.getName(), "Неверное имя задачи");
        assertEquals("Описание954", task1.getDescription(), "Неверное описание");
        assertEquals(120, (int) task1.getDuration().toMinutes(), "Неверная продолжительность задачи");
        assertTrue(response.headers().firstValue("Content-Type").isPresent()
                && response.headers().firstValue("Content-Type").get().contains("application/json"));

        // Проверим случай, если введен id который не существует
        URI url3 = URI.create("http://localhost:8080/tasks/6");
        HttpRequest request3 = HttpRequest.newBuilder()
                .GET()
                .uri(url3)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "application/json")
                .build();
        HttpResponse.BodyHandler<String> handler3 = HttpResponse.BodyHandlers.ofString();
        response = client.send(request3, handler3);
        String jsonResponse3 = response.body();
        Task task3 = gson.fromJson(jsonResponse3, Task.class);
        assertEquals(404, response.statusCode());
    }

    @Test
    public void testUpdateTask() throws IOException, InterruptedException {
        Duration duration = Duration.ofMinutes(120L);
        LocalDateTime dateTime = LocalDateTime.of(2025, 5, 25, 1, 30);
        Task task = new Task("задача_5", "тестовая задача_5", Status.NEW, dateTime, duration);
        String taskJsonString = gson.toJson(task);
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(taskJsonString))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
        // проверяем, что создалась одна задача с корректным именем
        ArrayList<Task> tasksFromManager = manager.getTasks();
        assertEquals(1, tasksFromManager.size(), "Количество задач не соответствует ожидаемому");
        assertEquals("задача_5", tasksFromManager.get(0).getName(), "Неверное имя задачи");
        assertEquals("тестовая задача_5", tasksFromManager.get(0).getDescription(), "Неверное описание");

        Task task2 = new Task("Update", "Обновленная задача", Status.NEW, 1, dateTime, duration);
        String taskJsonString2 = gson.toJson(task2);
        URI url2 = URI.create("http://localhost:8080/tasks/1");
        HttpRequest request2 = HttpRequest.newBuilder()
                .uri(url2)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(taskJsonString2))
                .build();
        response = client.send(request2, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
        // проверяем, что обновилась задача
        ArrayList<Task> tasksFromManager2 = manager.getTasks();
        assertEquals(1, tasksFromManager2.size(), "Количество задач не соответствует ожидаемому");
        assertEquals("Update", tasksFromManager2.get(0).getName(), "Неверное имя задачи");
        assertEquals("Обновленная задача", tasksFromManager2.get(0).getDescription(), "Неверное описание");
    }

    @Test
    public void testGetTasks() throws IOException, InterruptedException {
        Duration duration = Duration.ofMinutes(120L);
        Duration duration2 = Duration.ofMinutes(390L);
        LocalDateTime dateTime = LocalDateTime.of(2025, 6, 10, 1, 30);
        LocalDateTime dateTime2 = LocalDateTime.of(2025, 6, 10, 7, 30);
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

        Task task2 = new Task("Вторая задача", "Описание568", Status.IN_PROGRESS, dateTime2, duration2);
        String taskJsonString2 = gson.toJson(task2);
        HttpRequest request2 = HttpRequest.newBuilder()
                .uri(url1)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(taskJsonString2))
                .build();
        response = client.send(request2, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());

        URI url2 = URI.create("http://localhost:8080/tasks");
        HttpRequest request3 = HttpRequest.newBuilder()
                .GET()
                .uri(url2)
                .version(HttpClient.Version.HTTP_1_1)
                .header("Accept", "application/json")
                .build();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        response = client.send(request3, handler);
        String jsonResponse3 = response.body();
        Type listType = new TypeToken<ArrayList<Task>>() {
        }.getType();
        List<Task> list = gson.fromJson(jsonResponse3, listType);
        assertEquals(2, list.size());
        assertEquals("Первая задача", list.get(0).getName(), "Неверное имя задачи");
        assertEquals("Вторая задача", list.get(1).getName(), "Неверное имя задачи");
        assertTrue(response.headers().firstValue("Content-Type").isPresent()
                && response.headers().firstValue("Content-Type").get().contains("application/json"));
    }

    @Test
    public void testDeleteTaskById() throws IOException, InterruptedException {
        Duration duration = Duration.ofMinutes(120L);
        LocalDateTime dateTime = LocalDateTime.of(2025, 6, 5, 23, 30);
        Task task = new Task("Задача_23", "Описание954", Status.NEW, dateTime, duration);
        String taskJsonString = gson.toJson(task);
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(url)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(taskJsonString))
                .build();
        HttpResponse<String> response = client.send(request1, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());

        URI url2 = URI.create("http://localhost:8080/tasks/1");
        HttpRequest request2 = HttpRequest.newBuilder()
                .DELETE()
                .uri(url2)
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        HttpResponse.BodyHandler<Void> handler2 = HttpResponse.BodyHandlers.discarding();
        HttpResponse<Void> response2 = client.send(request2, handler2);
        assertEquals(200, response2.statusCode());


        // Проверим случай, если введен id который не существует
        URI url3 = URI.create("http://localhost:8080/tasks/9");
        HttpRequest request3 = HttpRequest.newBuilder()
                .GET()
                .uri(url3)
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        HttpResponse.BodyHandler<Void> handler3 = HttpResponse.BodyHandlers.discarding();
        HttpResponse<Void> response3 = client.send(request3, handler3);
        assertEquals(404, response3.statusCode());
    }
}