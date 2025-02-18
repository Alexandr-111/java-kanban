import tasks.*;
import managers.InMemoryTaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {
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
    void createTaskCheck() {
        Task inputTask = new Task("название_2", "описание_2", Status.NEW);
        assertTrue(manager.createTask(inputTask));
        ArrayList<Task> t = manager.getTasks();
        assertEquals(1, t.size());
    }

    @Test
    void updateTaskCheck() {
        Task inputTask = new Task("название_2", "описание_2", Status.DONE);
        manager.createTask(inputTask);
        Task updateInputTask = new Task("название_22", "описание_22", Status.NEW, 1);
        assertTrue(manager.updateTask(updateInputTask));
    }

    @Test
    void createEpicCheck() {
        Epic inputEpic = new Epic("название_1", "описание_1", Status.NEW);
        assertTrue(manager.createEpic(inputEpic));
        ArrayList<Epic> e = manager.getEpics();
        assertEquals(1, e.size());
    }

    @Test
    void updateEpicCheck() {
        Epic inputEpic = new Epic("название_1", "описание_1", Status.NEW);
        assertTrue(manager.createEpic(inputEpic));
        Epic updateInputEpic = new Epic("название_11", "описание_11", Status.NEW, 1);
        assertTrue(manager.updateEpic(updateInputEpic));
    }

    @Test
    void createSubtaskCheck() {
        Epic inputEpic = new Epic("название_1", "описание_1", Status.NEW);
        manager.createEpic(inputEpic);
        Subtask inputSubtask = new Subtask(1, "название_3", "описание_3", Status.NEW);
        assertTrue(manager.createSubtask(inputSubtask));
        ArrayList<Subtask> s = manager.getSubtasks();
        assertEquals(1, s.size());
    }

    @Test
    void updateSubtaskCheck() {
        Epic inputEpic = new Epic("название_1", "описание_1", Status.NEW);
        manager.createEpic(inputEpic);
        Subtask inputSubtask = new Subtask(1, "название_3", "описание_3", Status.NEW);
        manager.createSubtask(inputSubtask);
        Subtask updateInputSubtask = new Subtask(1, "название_33",
                "описание_33", Status.DONE, 2);
        assertTrue(manager.updateSubtask(updateInputSubtask));
    }

    @Test
    void removeByIdEpicCheck() {
        Epic inputEpic = new Epic("название_1", "описание_1", Status.NEW);
        manager.createEpic(inputEpic);
        Subtask inputSubtask = new Subtask(1, "название_3", "описание_3", Status.DONE);
        manager.createSubtask(inputSubtask);
        Subtask inputSubtask2 = new Subtask(1, "название_4", "описание_4", Status.IN_PROGRESS);
        manager.createSubtask(inputSubtask2);
        assertEquals(2, manager.removeByIdEpic(1));
        assertTrue(manager.getEpics().isEmpty());
        assertTrue(manager.getSubtasks().isEmpty());
    }

    @Test
    void removeByIdTaskCheck() {
        Task inputTask = new Task("название_2", "описание_2", Status.NEW);
        manager.createTask(inputTask);
        manager.getByIdTask(1);
        manager.removeByIdTask(1);
        assertTrue(manager.getTasks().isEmpty());
    }

    @Test
    void removeByIdSubtaskCheck() {
        Epic inputEpic = new Epic("название_7", "описание_7", Status.NEW);
        manager.createEpic(inputEpic);
        Subtask inputSubtask = new Subtask(1, "название_4", "описание_4", Status.IN_PROGRESS);
        manager.createSubtask(inputSubtask);
        manager.getByIdSubtask(2);
        manager.removeByIdSubtask(2);
        assertTrue(manager.getSubtasks().isEmpty());
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
    void removeEpicsCheck() {
        Epic inputEpic = new Epic("название_1", "описание_1", Status.NEW);
        manager.createEpic(inputEpic);
        Subtask inputSubtask = new Subtask(1, "название_3", "описание_3", Status.DONE);
        manager.createSubtask(inputSubtask);
        manager.removeEpics();
        assertTrue(manager.getEpics().isEmpty() && manager.getSubtasks().isEmpty());
    }

    @Test
    void removeTasksCheck() {
        Task inputTask = new Task("название_2", "описание_2", Status.NEW);
        manager.createTask(inputTask);
        manager.removeTasks();
        assertTrue(manager.getTasks().isEmpty());
    }

    @Test
    void removeSubtasksCheck() {
        Epic inputEpic = new Epic("название_1", "описание_1", Status.NEW);
        manager.createEpic(inputEpic);
        Subtask inputSubtask = new Subtask(1, "название_3", "описание_3", Status.DONE);
        manager.createSubtask(inputSubtask);
        assertEquals(Status.DONE, inputEpic.getCondition());
        manager.removeSubtasks();
        assertTrue(manager.getSubtasks().isEmpty());
        assertEquals(Status.NEW, inputEpic.getCondition());
    }

    @Test
    void calculateStatusEpicCheck() {
        Epic inputEpic = new Epic("название_1", "описание_1", Status.NEW);
        manager.createEpic(inputEpic);
        Subtask inputSubtask = new Subtask(1, "название_3", "описание_3", Status.DONE);
        manager.createSubtask(inputSubtask);
        assertEquals(Status.DONE, inputEpic.getCondition());
        Subtask inputSubtask2 = new Subtask(1, "название_4", "описание_4", Status.IN_PROGRESS);
        manager.createSubtask(inputSubtask2);
        assertEquals(Status.IN_PROGRESS, inputEpic.getCondition());
        manager.removeSubtasks();
        assertEquals(Status.NEW, inputEpic.getCondition());
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