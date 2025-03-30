package handlers;

import adapters.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import tasks.Epic;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;

public class BaseHttpHandler {
    protected static final Charset CHARSET = StandardCharsets.UTF_8;
    public static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .registerTypeAdapter(Epic.class, new EpicAdapter())
            .create();

    // Метод для отправки заголовков и тела ответа (формат JSON)
    protected void sendText(HttpExchange httpExchange, int code, Object obj) throws IOException {
        String jsonStr = gson.toJson(obj);
        byte[] bytes = jsonStr.getBytes(CHARSET);
        httpExchange.getResponseHeaders().set("Content-Type", "application/json");
        httpExchange.sendResponseHeaders(code, bytes.length);
        try (OutputStream outputStream = httpExchange.getResponseBody()) {
            outputStream.write(bytes);
        }
    }
}