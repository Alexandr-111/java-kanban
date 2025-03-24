import managers.*;
import org.junit.jupiter.api.Test;
import tasks.*;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {
    private final TaskManager manager = Managers.getDefault();

    @Test
    void addIdSubAndDeleteIdSubCheck() {
        Duration duration = Duration.ofMinutes(60L);
        LocalDateTime dateTime = LocalDateTime.of(2025, 5, 25, 12, 30);
        Epic inputEpic = new Epic("название_1", "описание_1", Status.NEW);
        manager.createEpic(inputEpic);
        Subtask inputSubtask = new Subtask(1, "название_3", "описание_3", Status.NEW,
                dateTime, duration);
        manager.createSubtask(inputSubtask);
        assertEquals(2, inputEpic.getListOfId().get(0));
        manager.removeByIdSubtask(2);
        assertTrue(inputEpic.getListOfId().isEmpty());
    }
}