package tests;

import tasks.*;
import managers.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    private TaskManager manager;

    @BeforeEach
    public void beforeEach() {
        manager = Managers.getDefault();
    }

    @Test
    void addCheck() {
        for (int i = 1; i < 26; i++) {
            manager.createTask(new Task("название" + i, "описание" + i, Status.NEW));
            Task task = manager.getByIdTask(i);
        }
        assertEquals(25, manager.getBrowsingHistory().size());
    }

    @Test
    void shouldBeNoDuplicates() {
        Task inputTask1 = new Task("название_1", "описание_1", Status.NEW);
        manager.createTask(inputTask1);
        Task inputTask2 = new Task("название_2", "описание_2", Status.DONE);
        manager.createTask(inputTask2);
        manager.getByIdTask(1);
        manager.getByIdTask(2);
        manager.getByIdTask(2);
        manager.getByIdTask(1);
        assertEquals(2, manager.getBrowsingHistory().size());
    }

    @Test
    void taskShouldBeRemovedFromHistoryWhenTaskIsDeleted() {
        Task inputTask1 = new Task("название_1", "описание_1", Status.NEW);
        manager.createTask(inputTask1);
        Task inputTask2 = new Task("название_2", "описание_2", Status.DONE);
        manager.createTask(inputTask2);
        manager.getByIdTask(1);
        manager.getByIdTask(2);
        manager.removeByIdTask(1);
        assertEquals(1, manager.getBrowsingHistory().size());
    }
}