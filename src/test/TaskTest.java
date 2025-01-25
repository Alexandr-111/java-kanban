package test;

import tasks.*;
import managers.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {
    private TaskManager manager;

    @BeforeEach
    public void beforeEach() {
        manager = Managers.getDefault();
    }

    @Test
    void getConditionCheck() {
        manager.createTask(new Task("название_7", "описание_8", Status.NEW));
        Task task = manager.getByIdTask(1);
        assertEquals(Status.NEW, task.getCondition());
    }

    @Test
    void getDescriptionCheck() {
        manager.createTask(new Task("название_8", "описание_9", Status.NEW));
        Task task = manager.getByIdTask(1);
        assertEquals("описание_9", task.getDescription());
    }

    @Test
    void getNameCheck() {
        manager.createTask(new Task("Название", "описание_8", Status.NEW));
        Task task = manager.getByIdTask(1);
        assertEquals("Название", task.getName());
    }

    //  Тест проверяет неизменность задачи (по всем полям) при добавлении задачи в менеджер
    @Test
    void immutabilityTaskCheck() {
        manager.createTask(new Task("название_6", "описание_6", Status.DONE));
        Task task = manager.getByIdTask(1);
        assertEquals("название_6", task.getName());
        assertEquals("описание_6", task.getDescription());
        assertEquals(Status.DONE, task.getCondition());
    }
}