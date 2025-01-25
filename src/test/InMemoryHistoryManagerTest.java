package test;

import tasks.*;
import managers.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    private final TaskManager manager = Managers.getDefault();

    @Test
    void addCheck() {
        for (int i = 1; i < 20; i++) {
            manager.createTask(new Task("название" + i, "описание" + i, Status.NEW));
            Task task = manager.getByIdTask(i);
        }
        assertEquals(10, manager.getManagerH().getHistory().size());
    }
}