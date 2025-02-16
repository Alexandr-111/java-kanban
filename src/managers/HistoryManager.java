package managers;

import tasks.*;

import java.util.List;

public interface HistoryManager {

    void add(Task task);

    void remove(int id);

    List<Task> getHistory();

    boolean isKey(int key);
}