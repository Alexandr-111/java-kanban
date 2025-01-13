import java.util.HashMap;
import java.util.ArrayList;

public class TaskManager {
    int counterId = 0;
    int foundId = 0; //Временно хранит id эпика в процессе обновления подзадачи
    HashMap<Integer, Task> tableForTasks = new HashMap<>();
    HashMap<Integer, Epic> tableForEpics = new HashMap<>();
    ArrayList<Integer> existingId = new ArrayList<>();

    public int getCounterId() {
        return counterId;
    }

    public void setCounterId() {
        counterId = counterId + 1;
    }

    public void setToZeroFoundId() {
        this.foundId = 0;
    }

    void createTask(String name, String description, String statusTask) {
        Status condition = Status.valueOf(statusTask);
        setCounterId();
        int id = getCounterId();
        tableForTasks.put(id, new Task(name, description, condition));
        existingId.add(id);
    }

    void updateTask(Integer id, String name, String description, String statusTask) {
        Status condition = Status.valueOf(statusTask);
        tableForTasks.put(id, new Task(name, description, condition));
    }

    void createEpic(String name, String description) {
        setCounterId();
        int id = getCounterId();
        tableForEpics.put(id, new Epic(name, description, Status.NEW));
        existingId.add(id);
    }

    void updateEpic(Integer id, String name, String description) {
        tableForEpics.put(id, new Epic(name, description, Status.NEW));

    }

    boolean createSubtask(int idEpic, String name, String description, String statusTask) {
        Status condition = Status.valueOf(statusTask);
        setCounterId();
        int id = getCounterId();
        if (checkIdEpic(idEpic)) {
            Epic temp = tableForEpics.get(idEpic);
            Subtask subtask = new Subtask(name, description, condition);
            temp.tableForSubtasks.put(id, subtask);
            existingId.add(id);
            calculateStatusEpic(idEpic);
            return true;
        }
        return false;
    }

    void updateSubtask(Integer id, String name, String description, String statusTask) {
        Status condition = Status.valueOf(statusTask);
        Integer idEpic = foundId;
        setToZeroFoundId();
        Epic temp = tableForEpics.get(idEpic);
        Subtask subtask = new Subtask(name, description, condition);
        temp.tableForSubtasks.put(id, subtask);
        calculateStatusEpic(idEpic);
    }

    void getListTasks() {
        System.out.println("Список всех задач");
        for (int element : tableForEpics.keySet()) {
            System.out.println("ЭПИК. Id = " + element);
            Epic obj = tableForEpics.get(element);
            System.out.println(obj);
            for (int key : obj.tableForSubtasks.keySet()) {
                System.out.println("Подзадача. Id = " + key);
                Subtask subtask = obj.tableForSubtasks.get(key);
                System.out.println(subtask);
            }
        }
        for (int elem : tableForTasks.keySet()) {
            System.out.println("ЗАДАЧА.  Id = " + elem);
            Task task = tableForTasks.get(elem);
            System.out.println(task);
        }
    }

    void removeAll() {
        tableForTasks.clear();
        tableForEpics.clear();
        existingId.clear();
    }

    boolean checkId(int search) {
        return existingId.contains(search);
    }

    boolean checkIdEpic(int idEpic) {
        return tableForEpics.containsKey(idEpic);
    }

    Task getById(Integer search) {
        for (Integer element : tableForEpics.keySet()) {
            if (element.equals(search)) {
                return tableForEpics.get(element);
            }
            Epic obj = tableForEpics.get(element);
            for (Integer key : obj.tableForSubtasks.keySet()) {
                if (key.equals(search)) {
                    return obj.tableForSubtasks.get(key);
                }
            }
        }
        for (Integer element : tableForTasks.keySet()) {
            if (element.equals(search)) {
                return tableForTasks.get(element);
            }
        }
        return null;
    }

    void removeById(Integer search) {
        for (Integer element : tableForEpics.keySet()) {
            if (element.equals(search)) {
                tableForEpics.remove(element);
                deleteId(search);
                return;
            }
            Epic obj = tableForEpics.get(element);
            for (Integer key : obj.tableForSubtasks.keySet()) {
                if (key.equals(search)) {
                    obj.tableForSubtasks.remove(key);
                    deleteId(search);
                    calculateStatusEpic(element);
                    return;
                }
            }
        }
        for (Integer element : tableForTasks.keySet()) {
            if (element.equals(search)) {
                tableForTasks.remove(element);
                deleteId(search);
            }
        }
    }

    void deleteId(int search) {
        int index = existingId.indexOf(search);
        existingId.remove(index);
    }

    String findOutClassObject(Integer search) {
        String type = "";
        for (Integer element : tableForEpics.keySet()) {
            if (element.equals(search)) {
                type = "Эпик";
                return type;
            }
            Epic obj = tableForEpics.get(element);
            for (Integer key : obj.tableForSubtasks.keySet()) {
                if (key.equals(search)) {
                    type = "Подзадача";
                    foundId = element;
                    return type;
                }
            }
        }
        for (Integer element : tableForTasks.keySet()) {
            if (element.equals(search)) {
                type = "Задача";
                return type;
            }
        }
        return type;
    }

    boolean isNoElements() {
        return existingId.isEmpty();
    }

    void calculateStatusEpic(int idEpic) {
        int counterNew = 0;
        int counterDone = 0;
        int counterProgress = 0;
        int size;
        for (Integer element : tableForEpics.keySet()) {
            if (element.equals(idEpic)) {
                Epic obj = tableForEpics.get(element);
                size = obj.tableForSubtasks.size();
                for (Integer k : obj.getTableForSubtasks().keySet()) {
                    Subtask sub = obj.getTableForSubtasks().get(k);
                    if (sub.getCondition() == Status.NEW) {
                        counterNew++;
                    }
                    if (sub.getCondition() == Status.DONE) {
                        counterDone++;
                    }
                    if (sub.getCondition() == Status.IN_PROGRESS) {
                        counterProgress++;
                    }
                }
                if (size == counterNew) {
                    obj.setCondition(Status.NEW);
                } else if (size == counterDone) {
                    obj.setCondition(Status.DONE);
                } else {
                    obj.setCondition(Status.IN_PROGRESS);
                }
                if (counterNew == 0 && counterDone == 0 && counterProgress == 0) {
                    obj.setCondition(Status.NEW);
                }
            }
        }
    }
}