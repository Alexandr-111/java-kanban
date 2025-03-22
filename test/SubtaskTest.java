import managers.Managers;
import managers.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.*;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class SubtaskTest {
    private TaskManager manager;

    @BeforeEach
    public void beforeEach() {
        manager = Managers.getDefault();
    }

    @Test
    void getIdenEpicCheck() {
        Duration duration = Duration.ofMinutes(60L);
        LocalDateTime dateTime1 = LocalDateTime.of(2025, 5, 25, 12, 30);
        Epic inputEpic = new Epic("название_1", "описание_1", Status.NEW);
        manager.createEpic(inputEpic);
        Subtask inputSubtask2 = new Subtask(1, "название_3", "описание_3",
                Status.DONE, dateTime1, duration);
        manager.createSubtask(inputSubtask2);
        assertEquals(1, inputSubtask2.getIdenEpic());
    }
}