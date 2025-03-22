import managers.TaskManager;
import org.junit.jupiter.api.Test;

public abstract class TaskManagerTest<T extends TaskManager> {
    protected T manager;

    @Test
    abstract void createTaskCheck();

    @Test
    abstract void updateTaskCheck();

    @Test
    abstract void removeByIdTaskCheck();

    @Test
    abstract void createEpicCheck();

    @Test
    abstract void createSubtaskCheck();

    @Test
    abstract void updateEpicCheck();

    @Test
    abstract void updateSubtaskCheck();

    @Test
    abstract void removeByIdEpicCheck();

    @Test
    abstract void removeByIdSubtaskCheck();
}