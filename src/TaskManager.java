import tasks.*;

import java.util.ArrayList;
import java.util.HashMap;


public class TaskManager {
    private int counterId = 0;
    private final HashMap<Integer, Epic> storageEpics = new HashMap<>();
    private final HashMap<Integer, Task> storageTasks = new HashMap<>();
    private final HashMap<Integer, Subtask> storageSubtasks = new HashMap<>();

    private int makeNewId() {
        counterId += 1;
        return counterId;
    }

    public void createTask(Task inputTask) {
        int id = makeNewId();
        inputTask.setId(id);
        storageTasks.put(id, inputTask);
    }

    public boolean updateTask(Task inputTask) {
        int id = inputTask.getId();
        if (storageTasks.containsKey(id)) {
            storageTasks.put(id, inputTask);
            return true;
        }
        return false;
    }

    public void createEpic(Epic inputEpic) {
        int id = makeNewId();
        inputEpic.setId(id);
        storageEpics.put(id, inputEpic);
    }

    public boolean updateEpic(Epic inputEpic) {
        int id = inputEpic.getId();
        if (storageEpics.containsKey(id)) {
            storageEpics.put(id, inputEpic);
            return true;
        }
        return false;
    }

    public boolean createSubtask(Subtask inputSubtask) {
        int idEpic = inputSubtask.getIdenEpic();
        if (storageEpics.containsKey(idEpic)) {
            int id = makeNewId();
            inputSubtask.setId(id);
            Epic temp = storageEpics.get(idEpic);
            temp.addIdSub(id);
            storageSubtasks.put(id, inputSubtask);
            calculateStatusEpic(idEpic);
            return true;
        }
        return false;
    }

    public boolean updateSubtask(Subtask inputSubtask) {
        int id = inputSubtask.getId();
        if (storageSubtasks.containsKey(id)) {
            int idEpic = inputSubtask.getIdenEpic();
            storageSubtasks.put(id, inputSubtask);
            calculateStatusEpic(idEpic);
            return true;
        }
        return false;
    }

    public int receiveIdEpic(int search) {
        Subtask current = storageSubtasks.get(search);
        return current.getIdenEpic();
    }

    public ArrayList<Epic> getEpics() {
        return new ArrayList<>(storageEpics.values());
    }

    public ArrayList<Task> getTasks() {
        return new ArrayList<>(storageTasks.values());
    }

    public ArrayList<Subtask> getSubtasks() {
        return new ArrayList<>(storageSubtasks.values());
    }

    public int removeByIdEpic(int search) {
        if (storageEpics.containsKey(search)) {
            Epic temp = storageEpics.get(search);
            ArrayList<Integer> del = temp.getListOfId();
            for (Integer r : del) {
                storageSubtasks.remove(r);
            }
            storageEpics.remove(search);
            return del.size();
        }
        return -1;
    }

    public boolean removeByIdTask(int search) {
        if (storageTasks.containsKey(search)) {
            storageTasks.remove(search);
            return true;
        }
        return false;
    }

    public boolean removeByIdSubtask(int search) {
        if (storageSubtasks.containsKey(search)) {
            Subtask current = storageSubtasks.get(search);
            int idEpic = current.getIdenEpic();
            Epic temp = storageEpics.get(idEpic);
            temp.deleteIdSub(search);
            storageSubtasks.remove(search);
            calculateStatusEpic(idEpic);
            return true;
        }
        return false;
    }

    public int findOutClassObject(int search) {
        int type = 0;
        if (storageEpics.containsKey(search)) {
            type = 1;
        }
        if (storageTasks.containsKey(search)) {
            type = 2;
        }
        if (storageSubtasks.containsKey(search)) {
            type = 3;
        }
        return type;
    }

    public Epic getByIdEpic(int search) {
        return storageEpics.get(search);
    }

    public Task getByIdTask(int search) {
        return storageTasks.get(search);
    }

    public Subtask getByIdSubtask(int search) {
        return storageSubtasks.get(search);
    }

    public boolean emptyList() {
        return storageEpics.isEmpty() && storageTasks.isEmpty() && storageSubtasks.isEmpty();
    }

    public void removeEpics() {
        storageEpics.clear();
        storageSubtasks.clear();
    }

    public void removeTasks() {
        storageTasks.clear();
    }

    private void calculateStatusEpic(int idEpic) {
        int sum = 0;
        int counterNew = 0;
        int counterDone = 0;
        Integer identifier;
        for (Integer element : storageSubtasks.keySet()) {
            Subtask temp = storageSubtasks.get(element);
            identifier = temp.getIdenEpic();
            if (identifier.equals(idEpic)) {
                Status c = temp.getCondition();
                sum++;
                if (c == Status.NEW) {
                    counterNew++;
                }
                if (c == Status.DONE) {
                    counterDone++;
                }
            }
            Epic currentEpic = storageEpics.get(idEpic);
            if (sum == counterNew) {
                currentEpic.setCondition(Status.NEW);
            } else if (sum == counterDone) {
                currentEpic.setCondition(Status.DONE);
            } else {
                currentEpic.setCondition(Status.IN_PROGRESS);
            }
        }
    }
}