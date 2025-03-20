import managers.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {
    @BeforeEach
    public void beforeEach() {
        manager = new InMemoryTaskManager();
    }

    @Test
    void createTaskCheck() {
        Duration duration = Duration.ofMinutes(60L);
        LocalDateTime dateTime = LocalDateTime.of(2025, 5, 25, 12, 30);
        Task inputTask = new Task("название_2", "описание_2", Status.NEW, dateTime, duration);
        assertTrue(manager.createTask(inputTask));
        ArrayList<Task> t = manager.getTasks();
        assertEquals(1, t.size());
    }

    @Test
    void updateTaskCheck() {
        Duration duration = Duration.ofMinutes(60L);
        LocalDateTime dateTime = LocalDateTime.of(2025, 5, 25, 12, 30);
        Task inputTask = new Task("название_2", "описание_2", Status.DONE, dateTime, duration);
        manager.createTask(inputTask);
        Task updateInputTask = new Task
                ("название_22", "описание_22", Status.NEW, 1, dateTime, duration);
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
        Duration duration = Duration.ofMinutes(60L);
        LocalDateTime dateTime = LocalDateTime.of(2025, 5, 25, 12, 30);
        Epic inputEpic = new Epic("название_1", "описание_1", Status.NEW);
        manager.createEpic(inputEpic);
        Subtask inputSubtask = new Subtask
                (1, "название_3", "описание_3", Status.NEW, dateTime, duration);
        assertTrue(manager.createSubtask(inputSubtask));
        ArrayList<Subtask> s = manager.getSubtasks();
        assertEquals(1, s.size());
    }

    @Test
    void updateSubtaskCheck() {
        Duration duration = Duration.ofMinutes(60L);
        LocalDateTime dateTime = LocalDateTime.of(2025, 5, 25, 12, 30);
        Epic inputEpic = new Epic("название_1", "описание_1", Status.NEW);
        manager.createEpic(inputEpic);
        Subtask inputSubtask = new Subtask
                (1, "название_3", "описание_3", Status.NEW, dateTime, duration);
        manager.createSubtask(inputSubtask);
        Subtask updateInputSubtask = new Subtask(1, "название_33",
                "описание_33", Status.DONE, 2, dateTime, duration);
        assertTrue(manager.updateSubtask(updateInputSubtask));
    }

    @Test
    void removeByIdEpicCheck() {
        Duration duration = Duration.ofMinutes(60L);
        LocalDateTime dateTime = LocalDateTime.of(2025, 5, 25, 12, 30);
        Duration duration2 = Duration.ofMinutes(120L);
        LocalDateTime dateTime2 = LocalDateTime.of(2025, 5, 25, 18, 30);
        Epic inputEpic = new Epic("название_1", "описание_1", Status.NEW);
        manager.createEpic(inputEpic);
        Subtask inputSubtask = new Subtask
                (1, "название_3", "описание_3", Status.DONE, dateTime, duration);
        manager.createSubtask(inputSubtask);
        Subtask inputSubtask2 = new Subtask
                (1, "название_4", "описание_4", Status.IN_PROGRESS, dateTime2, duration2);
        manager.createSubtask(inputSubtask2);
        assertEquals(2, manager.removeByIdEpic(1));
        assertTrue(manager.getEpics().isEmpty());
        assertTrue(manager.getSubtasks().isEmpty());
    }

    @Test
    void removeByIdTaskCheck() {
        Duration duration = Duration.ofMinutes(60L);
        LocalDateTime dateTime = LocalDateTime.of(2025, 5, 25, 12, 30);
        Task inputTask = new Task("название_2", "описание_2", Status.NEW, dateTime, duration);
        manager.createTask(inputTask);
        manager.getByIdTask(1);
        manager.removeByIdTask(1);
        assertTrue(manager.getTasks().isEmpty());
    }

    @Test
    void removeByIdSubtaskCheck() {
        Duration duration = Duration.ofMinutes(60L);
        LocalDateTime dateTime = LocalDateTime.of(2025, 5, 25, 12, 30);
        Epic inputEpic = new Epic("название_7", "описание_7", Status.NEW);
        manager.createEpic(inputEpic);
        Subtask inputSubtask = new Subtask
                (1, "название_4", "описание_4", Status.IN_PROGRESS, dateTime, duration);
        manager.createSubtask(inputSubtask);
        manager.getByIdSubtask(2);
        manager.removeByIdSubtask(2);
        assertTrue(manager.getSubtasks().isEmpty());
    }

    @Test
    void receiveIdEpicCheck() {
        Duration duration = Duration.ofMinutes(60L);
        LocalDateTime dateTime = LocalDateTime.of(2025, 5, 25, 12, 30);
        Epic inputEpic = new Epic("название_7", "описание_7", Status.NEW);
        manager.createEpic(inputEpic);
        Subtask inputSubtask = new Subtask
                (1, "название_3", "описание_3", Status.DONE, dateTime, duration);
        manager.createSubtask(inputSubtask);
        assertEquals(1, manager.receiveIdEpic(2));
    }

    @Test
    void findOutClassObjectCheck() {
        Duration duration = Duration.ofMinutes(60L);
        LocalDateTime dateTime = LocalDateTime.of(2025, 5, 25, 12, 30);
        Task inputTask = new Task("название_6", "описание_6", Status.DONE, dateTime, duration);
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
        Duration duration = Duration.ofMinutes(60L);
        LocalDateTime dateTime = LocalDateTime.of(2025, 5, 25, 12, 30);
        Task inputTask = new Task("название_2", "описание_2", Status.NEW, dateTime, duration);
        manager.createTask(inputTask);
        assertEquals(inputTask, manager.getByIdTask(1));
    }

    @Test
    void getByIdSubtaskCheck() {
        Duration duration = Duration.ofMinutes(60L);
        LocalDateTime dateTime = LocalDateTime.of(2025, 5, 25, 12, 30);
        Epic inputEpic = new Epic("название_1", "описание_1", Status.NEW);
        manager.createEpic(inputEpic);
        Subtask inputSubtask = new Subtask(1, "название_3", "описание_3",
                Status.DONE, dateTime, duration);
        manager.createSubtask(inputSubtask);
        assertEquals(inputSubtask, manager.getByIdSubtask(2));
    }

    @Test
    void emptyListCheck() {
        assertTrue(manager.emptyList());
    }

    @Test
    void removeEpicsCheck() {
        Duration duration = Duration.ofMinutes(60L);
        LocalDateTime dateTime = LocalDateTime.of(2025, 5, 25, 12, 30);
        Epic inputEpic = new Epic("название_1", "описание_1", Status.NEW);
        manager.createEpic(inputEpic);
        Subtask inputSubtask = new Subtask(1, "название_3", "описание_3",
                Status.DONE, dateTime, duration);
        manager.createSubtask(inputSubtask);
        manager.removeEpics();
        assertTrue(manager.getEpics().isEmpty() && manager.getSubtasks().isEmpty());
    }

    @Test
    void removeTasksCheck() {
        Duration duration = Duration.ofMinutes(60L);
        LocalDateTime dateTime = LocalDateTime.of(2025, 5, 25, 12, 30);
        Task inputTask = new Task("название_2", "описание_2", Status.NEW, dateTime, duration);
        manager.createTask(inputTask);
        manager.removeTasks();
        assertTrue(manager.getTasks().isEmpty());
    }

    @Test
    void removeSubtasksCheck() {
        Duration duration = Duration.ofMinutes(60L);
        LocalDateTime dateTime = LocalDateTime.of(2025, 5, 25, 12, 30);
        Epic inputEpic = new Epic("название_1", "описание_1", Status.NEW);
        manager.createEpic(inputEpic);
        Subtask inputSubtask = new Subtask(1, "название_3", "описание_3",
                Status.DONE, dateTime, duration);
        manager.createSubtask(inputSubtask);
        assertEquals(Status.DONE, inputEpic.getCondition());
        manager.removeSubtasks();
        assertTrue(manager.getSubtasks().isEmpty());
        assertEquals(Status.NEW, inputEpic.getCondition());
    }

    @Test
    void calculateStatusEpicCheck() {
        Duration duration = Duration.ofMinutes(60L);
        LocalDateTime dateTime1 = LocalDateTime.of(2025, 5, 25, 12, 30);
        LocalDateTime dateTime2 = LocalDateTime.of(2025, 5, 26, 10, 30);
        Epic inputEpic = new Epic("название_1", "описание_1", Status.NEW);
        manager.createEpic(inputEpic);
        Subtask inputSubtask = new Subtask(1, "название_3", "описание_3",
                Status.DONE, dateTime1, duration);
        manager.createSubtask(inputSubtask);
        assertEquals(Status.DONE, inputEpic.getCondition());
        Subtask inputSubtask2 = new Subtask(1, "название_4", "описание_4",
                Status.IN_PROGRESS, dateTime2, duration);
        manager.createSubtask(inputSubtask2);
        assertEquals(Status.IN_PROGRESS, inputEpic.getCondition());
        manager.removeSubtasks();
        assertEquals(Status.NEW, inputEpic.getCondition());
    }

    @Test
    void takeSnapshotCheck() {
        Duration duration = Duration.ofMinutes(60L);
        LocalDateTime dateTime = LocalDateTime.of(2025, 5, 25, 12, 30);
        Task inputTask = new Task("Первое", "описание_1", Status.DONE,
                dateTime, duration);
        manager.createTask(inputTask);
        Task task = manager.getByIdTask(1);
        assertEquals("Первое", task.getName());
        Task inputTask2 = new Task("Второе", "описание_2", Status.IN_PROGRESS,
                1, dateTime, duration);
        manager.updateTask(inputTask2);
        task = manager.getByIdTask(1);
        assertEquals("Второе", task.getName());
        Task snapshot = manager.getBrowsingHistory().get(0);
        assertEquals("Второе", snapshot.getName());
        assertEquals(1, manager.getBrowsingHistory().size());
    }

    @Test
    void makeIdCheck() {
        Duration duration = Duration.ofMinutes(60L);
        LocalDateTime dateTime = LocalDateTime.of(2025, 5, 25, 12, 30);
        Duration duration2 = Duration.ofMinutes(120L);
        LocalDateTime dateTime2 = LocalDateTime.of(2025, 5, 25, 15, 30);
        manager.createTask(new Task("название_5", "описание_5", Status.NEW, dateTime, duration));
        assertEquals(1, manager.getCounterId());
        manager.createTask(new Task("название_6", "описание_6", Status.DONE, dateTime2, duration2));
        assertEquals(2, manager.getCounterId());
    }

    @Test
    void addInPriorityCheck() {
        Duration duration = Duration.ofMinutes(60L);
        LocalDateTime dateTime = LocalDateTime.of(2025, 5, 25, 12, 30);
        Duration duration2 = Duration.ofMinutes(60L);
        LocalDateTime dateTime2 = LocalDateTime.of(2025, 5, 25, 12, 45);
        boolean a = manager.createTask
                (new Task("название_2", "описание_2", Status.NEW, dateTime, duration));
        boolean b = manager.createTask
                (new Task("название_3", "описание_3", Status.NEW, dateTime2, duration2));
        assertTrue(a && b);
        assertEquals(1, manager.getPrioritizedTasks().size());
    }
}