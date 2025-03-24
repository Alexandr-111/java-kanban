import tasks.*;
import managers.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {
    private TaskManager manager;

    @BeforeEach
    public void beforeEach() {
        manager = Managers.getDefault();
    }

    @Test
    void getConditionCheck() {
        Duration duration = Duration.ofMinutes(60L);
        LocalDateTime dateTime = LocalDateTime.of(2025, 5, 25, 12, 30);
        manager.createTask(new Task("название_2", "описание_2", Status.NEW, dateTime, duration));
        Task task = manager.getByIdTask(1);
        assertEquals(Status.NEW, task.getCondition());
    }

    @Test
    void getDescriptionCheck() {
        Duration duration = Duration.ofMinutes(60L);
        LocalDateTime dateTime = LocalDateTime.of(2025, 5, 25, 12, 30);
        manager.createTask(new Task("название_2", "описание_2", Status.NEW, dateTime, duration));
        Task task = manager.getByIdTask(1);
        assertEquals("описание_2", task.getDescription());
    }

    @Test
    void getNameCheck() {
        Duration duration = Duration.ofMinutes(60L);
        LocalDateTime dateTime = LocalDateTime.of(2025, 5, 25, 12, 30);
        manager.createTask(new Task("Название", "описание_2", Status.NEW, dateTime, duration));
        Task task = manager.getByIdTask(1);
        assertEquals("Название", task.getName());
    }

    //  Тест проверяет неизменность задачи (по всем полям) при добавлении задачи в менеджер
    @Test
    void immutabilityTaskCheck() {
        Duration duration = Duration.ofMinutes(60L);
        LocalDateTime dateTime = LocalDateTime.of(2025, 5, 25, 12, 30);
        manager.createTask(new Task("название_2", "описание_2", Status.DONE, dateTime, duration));
        Task task = manager.getByIdTask(1);
        assertEquals("название_2", task.getName());
        assertEquals("описание_2", task.getDescription());
        assertEquals(Status.DONE, task.getCondition());
        assertEquals(60, (int) task.getDuration().toMinutes());
    }
}