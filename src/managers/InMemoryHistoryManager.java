package managers;

import tasks.*;

import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    static final int MAX_SIZE_LIST = 10;
    private final List<Task> history = new LinkedList<>();

    @Override
    public LinkedList<Task> getHistory() {
        LinkedList<Task> historyCopy = new LinkedList<>();
        historyCopy.addAll(history);
        return historyCopy;
    }

    @Override
    public void add(Task task) {
        if (history.size() == MAX_SIZE_LIST) {
            history.remove(0);
        }
        history.add(task);
    }
}