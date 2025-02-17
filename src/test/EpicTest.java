import managers.*;
import org.junit.jupiter.api.Test;
import tasks.*;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {
    private final TaskManager manager = Managers.getDefault();

    @Test
    void addIdSubAndDeleteIdSubCheck() {
        Epic inputEpic = new Epic("название_1", "описание_1", Status.NEW);
        manager.createEpic(inputEpic);
        Subtask inputSubtask = new Subtask(1, "название_3", "описание_3", Status.NEW);
        manager.createSubtask(inputSubtask);
        assertEquals(2, inputEpic.getListOfId().get(0));
        manager.removeByIdSubtask(2);
        assertTrue(inputEpic.getListOfId().isEmpty());
    }
}