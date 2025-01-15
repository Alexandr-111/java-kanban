import tasks.*;

import java.util.ArrayList;
import java.util.HashMap;


public class TaskManager {
    private int counterId = 0;
    private final HashMap<Integer, Epic> storageEpics = new HashMap<>();
    private final HashMap<Integer, Task> storageTasks = new HashMap<>();
    private final HashMap<Integer, Subtask> storageSubtasks = new HashMap<>();

    public int getCounterId() {
        return counterId;
    }

    public void setCounterId() {
        counterId = counterId + 1;
    }

    public void createTask(Task inputTask) {
        setCounterId();
        int id = getCounterId();
        storageTasks.put(id, inputTask);
    }

    public void createEpic(Epic inputEpic) {
        setCounterId();
        int id = getCounterId();
        storageEpics.put(id, inputEpic);
    }

    public void createSubtask(int idEpic, Subtask inputSubtask) {
        setCounterId();
        int id = getCounterId();
        storageSubtasks.put(id, inputSubtask);
        calculateStatusEpic(idEpic);
    }

    public void updateTask(int id, Task inputTask) {
        storageTasks.put(id, inputTask);
    }

    public void updateEpic(int id, Epic inputEpic) {
        storageEpics.put(id, inputEpic);
    }

    public void updateSubtask(int id, Subtask inputSubtask, int idEpic) {
        storageSubtasks.put(id, inputSubtask);
        calculateStatusEpic(idEpic);
    }

    public int receiveIdenEpic(int search) {
        Subtask currentEpic = storageSubtasks.get(search);
        return currentEpic.getIdenEpic();
    }

    public HashMap<Integer, Epic> getEpics() {
        return storageEpics;
    }

    public HashMap<Integer, Task> getTasks() {
        return storageTasks;
    }

    public HashMap<Integer, Subtask> getSubtasks() {
        return storageSubtasks;
    }

    public void removeAll() {
        storageTasks.clear();
        storageEpics.clear();
        storageSubtasks.clear();
    }

    public boolean checkIdTask(int idTask) {
        return storageTasks.containsKey(idTask);
    }

    public boolean checkIdSubtask(int idSubtask) {
        return storageSubtasks.containsKey(idSubtask);
    }

    public boolean checkIdEpic(int idEpic) {
        return storageEpics.containsKey(idEpic);
    }

    public int removeByIdEpic(int search) {
        storageEpics.remove(search);
        return deleteSubtask(search);
    }

    public void removeByIdTask(int search) {
        storageTasks.remove(search);
    }

    public void removeByIdSubtask(int search, int idEpic) {
        storageSubtasks.remove(search);
        calculateStatusEpic(idEpic);
    }

    // При удалении эпика метод удаляет все его подзадачи
    public int deleteSubtask(int idEpic) {
        Integer identifier;
        ArrayList<Integer> del = new ArrayList<>();
        for (Integer element : storageSubtasks.keySet()) {
            Subtask temp = storageSubtasks.get(element);
            identifier = temp.getIdenEpic();
            if (identifier.equals(idEpic)) {
                del.add(element);
            }
        }
        for (int i = 0; i < del.size(); i++) {
            int r = del.get(i);
            storageSubtasks.remove(r);
        }
        return del.size();
    }

    public int findOutClassObject(int search) {
        int type = 0;
        if (checkIdEpic(search)) {
            type = 1;
        }
        if (checkIdTask(search)) {
            type = 2;
        }
        if (checkIdSubtask(search)) {
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

    public boolean isNoEpics() {
        return storageEpics.isEmpty();
    }

    public boolean isNoTask() {
        return storageTasks.isEmpty();
    }

    public boolean isNoSubtask() {
        return storageSubtasks.isEmpty();
    }


    public void calculateStatusEpic(int idEpic) {
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