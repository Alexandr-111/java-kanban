package adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import tasks.Epic;
import tasks.Status;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

// Адаптер для класса Epic, нужен для явного указания полей при сериализации, так как
//  без него возникает исключение IllegalArgumentException при попытке сериализовать объект.
//  Возможно из за одинаковых имен полей в этом классе и в родительском классе Task.

public class EpicAdapter extends TypeAdapter<Epic> {
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy");

    @Override
    public void write(JsonWriter writer, Epic epic) throws IOException {
        if (epic == null) {
            writer.nullValue();
            return;
        }
        writer.beginObject();
        writer.name("id").value(epic.getId());
        writer.name("name").value(epic.getName());
        writer.name("description").value(epic.getDescription());
        writer.name("condition").value(epic.getCondition().toString());

        writer.name("listOfId").beginArray();
        for (Integer id : epic.getListOfId()) {
            writer.value(id);
        }
        writer.endArray();

        writer.name("startTime").value(epic.getStartTime() != null ? epic.getStartTime().toString() : null);
        writer.name("endTime").value(epic.getEndTime() != null ? epic.getEndTime().toString() : null);
        writer.name("duration").value(epic.getDuration() != null ? epic.getDuration().toString() : null);

        writer.endObject();
    }

    @Override
    public Epic read(JsonReader reader) throws IOException {
        if (reader.peek() == JsonToken.NULL) {
            reader.nextNull();
            return null;
        }

        Epic epic;
        int id = 0;
        String name = null;
        String description = null;
        Status condition = null;
        ArrayList<Integer> listOfId = new ArrayList<>();
        LocalDateTime startTime = null;
        LocalDateTime endTime = null;
        Duration duration = null;


        reader.beginObject();
        while (reader.hasNext()) {
            String field = reader.nextName();
            switch (field) {
                case "id":
                    id = reader.nextInt();
                    break;
                case "name":
                    name = reader.nextString();
                    break;
                case "description":
                    description = reader.nextString();
                    break;
                case "condition":
                    condition = Status.valueOf(reader.nextString());
                    break;
                case "listOfId":
                    reader.beginArray();
                    while (reader.hasNext()) {
                        listOfId.add(reader.nextInt());
                    }
                    reader.endArray();
                    break;
                case "startTime":
                    String startTimeStr = reader.nextString();
                    startTime = (startTimeStr != null) ? LocalDateTime.parse(startTimeStr, dateFormatter) : null;
                    break;
                case "endTime":
                    String endTimeStr = reader.nextString();
                    endTime = (endTimeStr != null) ? LocalDateTime.parse(endTimeStr, dateFormatter) : null;
                    break;
                case "duration":
                    String durationStr = reader.nextString();
                    duration = (durationStr != null) ? Duration.parse(durationStr) : null;
                    break;
                default:
                    reader.skipValue(); // Пропускаем неизвестные поля
            }
        }
        reader.endObject();

        // Создаем объект Epic в зависимости от значения полей
        epic = Epic.selectConstructorEpic(name, description, condition, id, startTime, endTime, duration, listOfId);
        return epic;
    }
}