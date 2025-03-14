import tasks.*;
import managers.InMemoryTaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskTaskManagerTest extends TaskManagerTest {

    private InMemoryTaskManager manager;

    @BeforeEach
    public void beforeEach() {
        manager = new InMemoryTaskManager();
    }

    @Test
    void makeIdCheck() {
        manager.createTask(new Task("название_5", "описание_5", Status.NEW));
        assertEquals(1, manager.getCounterId());
        manager.createTask(new Task("название_6", "описание_6", Status.DONE));
        assertEquals(2, manager.getCounterId());
    }

    @Test
    void receiveIdEpicCheck() {
        Epic inputEpic = new Epic("название_7", "описание_7", Status.NEW);
        manager.createEpic(inputEpic);
        Subtask inputSubtask = new Subtask(1, "название_3", "описание_3", Status.DONE);
        manager.createSubtask(inputSubtask);
        assertEquals(1, manager.receiveIdEpic(2));
    }

    @Test
    void findOutClassObjectCheck() {
        Task inputTask = new Task("название_6", "описание_6", Status.DONE);
        manager.createTask(inputTask);
        assertEquals(2, manager.findOutClassObject(1));
    }

    @Test
    void getByIdEpicCheck() {
        Epic inputEpic = new Epic("название_7", "описание_7", Status.NEW);
        manager.createEpic(inputEpic);
        assertEquals(inputEpic, manager.getByIdEpic(1));
    }

    @Test
    void getByIdTaskCheck() {
        Task inputTask = new Task("название_2", "описание_2", Status.NEW);
        manager.createTask(inputTask);
        assertEquals(inputTask, manager.getByIdTask(1));
    }

    @Test
    void getByIdSubtaskCheck() {
        Epic inputEpic = new Epic("название_1", "описание_1", Status.NEW);
        manager.createEpic(inputEpic);
        Subtask inputSubtask = new Subtask(1, "название_3", "описание_3", Status.DONE);
        manager.createSubtask(inputSubtask);
        assertEquals(inputSubtask, manager.getByIdSubtask(2));
    }

    @Test
    void emptyListCheck() {
        assertTrue(manager.emptyList());
    }

    @Test
    void takeSnapshotCheck() {
        Task inputTask = new Task("Первое", "описание_1", Status.DONE);
        manager.createTask(inputTask);
        Task task = manager.getByIdTask(1);
        assertEquals("Первое", task.getName());
        Task inputTask2 = new Task("Второе", "описание_2", Status.IN_PROGRESS, 1);
        manager.updateTask(inputTask2);
        task = manager.getByIdTask(1);
        assertEquals("Второе", task.getName());
        Task snapshot = manager.getBrowsingHistory().get(0);
        assertEquals("Второе", snapshot.getName());
        assertEquals(1, manager.getBrowsingHistory().size());
    }
}