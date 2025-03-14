import managers.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import tasks.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.stream.Stream;

import static managers.FileBackedTaskManager.CS8;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TaskManagerTest extends Tests {
    public static TaskManager managerA;
    public static TaskManager managerB;

    static Stream<TaskManager> argsFactory() {
        managerA = Managers.getDefault();
        managerB = Managers.getDefaultManagerWithFile();
        return Stream.of(managerA, managerB);
    }

    @BeforeEach
    public void beforeEach() {
        try {
            Path p = Path.of("src\\resources\\save.csv");
            Files.writeString(p, "", CS8);
            managerA.removeEpics();
            managerB.removeTasks();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @ParameterizedTest
    @MethodSource("argsFactory")
    void createTaskCheck(TaskManager manager) {
        Task inputTask = new Task("название_2", "описание_2", Status.NEW);
        assertTrue(manager.createTask(inputTask));
        ArrayList<Task> t = manager.getTasks();
        assertEquals(1, t.size());
        try {
            Path p = Path.of("src\\resources\\save.csv");
            Files.writeString(p, "", CS8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @ParameterizedTest
    @MethodSource("argsFactory")
    void updateTaskCheck(TaskManager manager) {
        Task inputTask = new Task("название_2", "описание_2", Status.DONE);
        manager.createTask(inputTask);
        Task updateInputTask = new Task("название_22", "описание_22", Status.NEW, 1);
        assertTrue(manager.updateTask(updateInputTask));
    }

    @ParameterizedTest
    @MethodSource("argsFactory")
    void removeByIdTaskCheck(TaskManager manager) {
        Task inputTask = new Task("название_2", "описание_2", Status.NEW);
        manager.createTask(inputTask);
        manager.removeTasks();
        assertTrue(manager.getTasks().isEmpty());
    }

    @ParameterizedTest
    @MethodSource("argsFactory")
    void createEpicCheck(TaskManager manager) {
        Epic inputEpic = new Epic("название_1", "описание_1", Status.NEW);
        assertTrue(manager.createEpic(inputEpic));
        ArrayList<Epic> e = manager.getEpics();
        assertEquals(1, e.size());
        try {
            Path p = Path.of("src\\resources\\save.csv");
            Files.writeString(p, "", CS8);
        } catch (IOException exp) {
            throw new RuntimeException(exp);
        }
    }

    @ParameterizedTest
    @MethodSource("argsFactory")
    void updateEpicCheck(TaskManager manager) {
        Epic inputEpic = new Epic("название_1", "описание_1", Status.NEW);
        assertTrue(manager.createEpic(inputEpic));
        Epic updateInputEpic = new Epic("название_11", "описание_11", Status.NEW, 1);
        assertTrue(manager.updateEpic(updateInputEpic));
    }

    @ParameterizedTest
    @MethodSource("argsFactory")
    void removeByIdEpicCheck(TaskManager manager) {
        Epic inputEpic = new Epic("название_1", "описание_1", Status.NEW);
        manager.createEpic(inputEpic);
        Subtask inputSubtask = new Subtask(1, "название_3", "описание_3", Status.DONE);
        manager.createSubtask(inputSubtask);
        manager.removeEpics();
        assertTrue(manager.getEpics().isEmpty() && manager.getSubtasks().isEmpty());
    }

    @ParameterizedTest
    @MethodSource("argsFactory")
    void createSubtaskCheck(TaskManager manager) {
        Epic inputEpic = new Epic("название_1", "описание_1", Status.NEW);
        manager.createEpic(inputEpic);
        Subtask inputSubtask = new Subtask(1, "название_3", "описание_3", Status.NEW);
        assertTrue(manager.createSubtask(inputSubtask));
        ArrayList<Subtask> s = manager.getSubtasks();
        assertEquals(1, s.size());
    }

    @ParameterizedTest
    @MethodSource("argsFactory")
    void updateSubtaskCheck(TaskManager manager) {
        Epic inputEpic = new Epic("название_1", "описание_1", Status.NEW);
        manager.createEpic(inputEpic);
        Subtask inputSubtask = new Subtask(1, "название_3", "описание_3", Status.NEW);
        manager.createSubtask(inputSubtask);
        Subtask updateInputSubtask = new Subtask(1, "название_33",
                "описание_33", Status.DONE, 2);
        assertTrue(manager.updateSubtask(updateInputSubtask));
    }

    @ParameterizedTest
    @MethodSource("argsFactory")
    void removeByIdSubtaskCheck(TaskManager manager) {
        Epic inputEpic = new Epic("название_7", "описание_7", Status.NEW);
        manager.createEpic(inputEpic);
        Subtask inputSubtask = new Subtask(1, "название_4", "описание_4", Status.IN_PROGRESS);
        manager.createSubtask(inputSubtask);
        manager.getByIdSubtask(2);
        manager.removeByIdSubtask(2);
        assertTrue(manager.getSubtasks().isEmpty());
    }

    @ParameterizedTest
    @MethodSource("argsFactory")
    void removeEpicsCheck(TaskManager manager) {
        Epic inputEpic = new Epic("название_1", "описание_1", Status.NEW);
        manager.createEpic(inputEpic);
        Subtask inputSubtask = new Subtask(1, "название_3", "описание_3", Status.DONE);
        manager.createSubtask(inputSubtask);
        manager.removeEpics();
        assertTrue(manager.getEpics().isEmpty() && manager.getSubtasks().isEmpty());
    }

    @ParameterizedTest
    @MethodSource("argsFactory")
    void removeTasksCheck(TaskManager manager) {
        Task inputTask = new Task("название_2", "описание_2", Status.NEW);
        manager.createTask(inputTask);
        manager.removeTasks();
        assertTrue(manager.getTasks().isEmpty());
    }

    @ParameterizedTest
    @MethodSource("argsFactory")
    void removeSubtasksCheck(TaskManager manager) {
        Epic inputEpic = new Epic("название_1", "описание_1", Status.NEW);
        manager.createEpic(inputEpic);
        Subtask inputSubtask = new Subtask(1, "название_3", "описание_3", Status.DONE);
        manager.createSubtask(inputSubtask);
        assertEquals(Status.DONE, inputEpic.getCondition());
        manager.removeSubtasks();
        assertTrue(manager.getSubtasks().isEmpty());
        assertEquals(Status.NEW, inputEpic.getCondition());
    }

    @ParameterizedTest
    @MethodSource("argsFactory")
    void calculateStatusEpicCheck(TaskManager manager) {
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

    @AfterEach
    public void afterEach() {
        try {
            Path p = Path.of("src\\resources\\save.csv");
            Files.writeString(p, "", CS8);
            managerA.removeEpics();
            managerB.removeTasks();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}