import managers.TaskManager;

public abstract class Tests {
    abstract void createTaskCheck(TaskManager manager);

    abstract void updateTaskCheck(TaskManager manager);

    abstract void removeByIdTaskCheck(TaskManager manager);

    abstract void createEpicCheck(TaskManager manager);

    abstract void updateEpicCheck(TaskManager manager);

    abstract void removeByIdEpicCheck(TaskManager manager);

    abstract void removeByIdSubtaskCheck(TaskManager manager);

    abstract void updateSubtaskCheck(TaskManager manager);

    abstract void createSubtaskCheck(TaskManager manager);

    abstract void removeSubtasksCheck(TaskManager manager);

    abstract void removeTasksCheck(TaskManager manager);

    abstract void removeEpicsCheck(TaskManager manager);

    abstract void calculateStatusEpicCheck(TaskManager manager);
}