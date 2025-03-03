package managers;

import exceptions.ManagerSaveException;

public class Managers {
    private Managers() {
    }

    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static FileBackedTaskManager getDefaultManagerWithFile() throws ManagerSaveException {
        try {
            return FileBackedTaskManager.loadFromFile();
        } catch (ManagerSaveException e) {
            throw new ManagerSaveException("Произошла ошибка. Перезапустите программу.");
        }
    }
}