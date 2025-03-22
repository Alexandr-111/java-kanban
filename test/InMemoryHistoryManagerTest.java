import tasks.*;
import managers.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    private TaskManager manager;

    @BeforeEach
    public void beforeEach() {
        manager = Managers.getDefault();
    }

    @Test
    void addCheck() {
        Duration duration = Duration.ofMinutes(30L);
        LocalDateTime dateTime = LocalDateTime.of(2025, 6, 20, 15, 10);
        for (int i = 1; i < 26; i++) {
            dateTime = dateTime.plusMinutes(60);
            manager.createTask(new Task("Задача_10", "Описание568", Status.IN_PROGRESS,
                    dateTime, duration));
            Task task = manager.getByIdTask(i);
        }
        assertEquals(25, manager.getBrowsingHistory().size());
    }

    @Test
    void shouldBeNoDuplicates() {
        Duration duration = Duration.ofMinutes(30L);
        LocalDateTime dateTime = LocalDateTime.of(2025, 6, 20, 15, 10);
        Duration duration2 = Duration.ofMinutes(180L);
        LocalDateTime dateTime2 = LocalDateTime.of(2025, 6, 20, 17, 10);
        Task inputTask1 = new Task("название_1", "описание_1", Status.NEW, dateTime, duration);
        manager.createTask(inputTask1);
        Task inputTask2 = new Task("название_2", "описание_2", Status.DONE, dateTime2, duration2);
        manager.createTask(inputTask2);
        manager.getByIdTask(1);
        manager.getByIdTask(2);
        manager.getByIdTask(2);
        manager.getByIdTask(1);
        assertEquals(2, manager.getBrowsingHistory().size());
    }

    @Test
    void taskShouldBeRemovedFromHistoryWhenTaskIsDeleted() {
        Duration duration = Duration.ofMinutes(30L);
        LocalDateTime dateTime = LocalDateTime.of(2025, 6, 20, 15, 10);
        Duration duration2 = Duration.ofMinutes(180L);
        LocalDateTime dateTime2 = LocalDateTime.of(2025, 6, 20, 17, 10);
        Task inputTask1 = new Task("название_1", "описание_1", Status.NEW, dateTime, duration);
        manager.createTask(inputTask1);
        Task inputTask2 = new Task("название_2", "описание_2", Status.DONE, dateTime2, duration2);
        manager.createTask(inputTask2);
        manager.getByIdTask(1);
        manager.getByIdTask(2);
        manager.removeByIdTask(1);
        assertEquals(1, manager.getBrowsingHistory().size());
    }
}