import tasks.*;

import java.util.ArrayList;
import java.util.HashMap;


public class TaskManager {
    private int counterId = 0;
    private final HashMap<Integer, Epic> storageEpics = new HashMap<>();
    private final HashMap<Integer, Task> storageTasks = new HashMap<>();
    private final HashMap<Integer, Subtask> storageSubtasks = new HashMap<>();

    public int makeNewId() {
        counterId += 1;
        return counterId;
    }

    public void createTask(Task inputTask) {
        int id = inputTask.getId();
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
        int id = inputEpic.getId();
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
            int id = inputSubtask.getId();
            Epic temp = storageEpics.get(idEpic);
            ArrayList<Integer> list = temp.getListOfId();
            list.add(id);
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
        ArrayList<Epic> records = new ArrayList<>();
        for (Integer element : storageEpics.keySet()) {
            records.add(storageEpics.get(element));
        }
        return records;
    }

    public ArrayList<Task> getTasks() {
        ArrayList<Task> records = new ArrayList<>();
        for (Integer element : storageTasks.keySet()) {
            records.add(storageTasks.get(element));
        }
        return records;
    }

    public ArrayList<Subtask> getSubtasks() {
        ArrayList<Subtask> records = new ArrayList<>();
        for (Integer element : storageSubtasks.keySet()) {
            records.add(storageSubtasks.get(element));
        }
        return records;
    }

    public int removeByIdEpic(int search) {
        if (storageEpics.containsKey(search)) {
            storageEpics.remove(search);
            Integer identifier;
            ArrayList<Integer> del = new ArrayList<>();
            for (Integer element : storageSubtasks.keySet()) {
                Subtask temp = storageSubtasks.get(element);
                identifier = temp.getIdenEpic();
                if (identifier.equals(search)) {
                    del.add(element);
                }
            }
            for (int i = 0; i < del.size(); i++) {
                int r = del.get(i);
                storageSubtasks.remove(r);
            }
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
            ArrayList<Integer> list = temp.getListOfId();
            list.remove(Integer.valueOf(search));
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
    }

    public void removeTasks() {
        storageTasks.clear();
    }

    public void removeSubtasks() {
        storageSubtasks.clear();
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