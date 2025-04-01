package adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.Duration;


public class DurationAdapter extends TypeAdapter<Duration> {

    @Override
    public void write(JsonWriter jsonWriter, Duration duration) throws IOException {
        if (duration == null) {
            jsonWriter.nullValue();
            return;
        }
        long minutes = duration.toMinutes();
        jsonWriter.value(minutes);
    }

    @Override
    public Duration read(JsonReader jsonReader) throws IOException {
        try {
            long minutes = jsonReader.nextLong();
            return Duration.ofMinutes(minutes);
        } catch (NumberFormatException e) {
            throw new IOException(e);
        }
    }
}