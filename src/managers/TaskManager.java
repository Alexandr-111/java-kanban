package managers;

import tasks.*;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {

   boolean createTask(Task inputTask);

    boolean updateTask(Task inputTask);

    boolean createEpic(Epic inputEpic);

    boolean updateEpic(Epic inputEpic);

    boolean createSubtask(Subtask inputSubtask);

    boolean updateSubtask(Subtask inputSubtask);

    int receiveIdEpic(int search);

    List<Task> getBrowsingHistory();

    ArrayList<Epic> getEpics();

    ArrayList<Task> getTasks();

    ArrayList<Subtask> getSubtasks();

    int removeByIdEpic(int search);

    boolean removeByIdTask(int search);

    boolean removeByIdSubtask(int search);

    int findOutClassObject(int search);

    Epic getByIdEpic(int search);

    Task getByIdTask(int search);

    Subtask getByIdSubtask(int search);

    boolean emptyList();

    void removeEpics();

    void removeTasks();

    void removeSubtasks();
}