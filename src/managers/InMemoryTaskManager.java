package managers;

import exceptions.ManagerSaveException;
import tasks.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {

    private int counterId = 0;
    protected final Map<Integer, Epic> storageEpics = new HashMap<>();
    protected final Map<Integer, Task> storageTasks = new HashMap<>();
    protected final Map<Integer, Subtask> storageSubtasks = new HashMap<>();
    private final HistoryManager managerH = Managers.getDefaultHistory();

    // компаратор для сортировки по времени
    private final Comparator<Task> comparator = Comparator.comparing(Task::getStartTime);

    // сет с упорядоченными по времени задачами
    protected final Set<Task> priority = new TreeSet<>(comparator);

    @Override
    public ArrayList<Epic> getEpics() {
        return new ArrayList<>(storageEpics.values());
    }

    @Override
    public ArrayList<Task> getTasks() {
        return new ArrayList<>(storageTasks.values());
    }

    @Override
    public ArrayList<Subtask> getSubtasks() {
        return new ArrayList<>(storageSubtasks.values());
    }

    public List<Task> getBrowsingHistory() {
        return managerH.getHistory();
    }

    public int getCounterId() {
        return counterId;
    }

    protected void setCounterId(int counterId) {
        this.counterId = counterId;
    }

    @Override
    public boolean createTask(Task inputTask) {
        boolean isAddingAllowed;
        if (!Objects.isNull(inputTask)) {
            int id = ++counterId;
            inputTask.setId(id);
            isAddingAllowed = addInPriority(inputTask);
            if (isAddingAllowed) {
                storageTasks.put(id, inputTask);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean updateTask(Task inputTask) {
        boolean isAddingAllowed;
        if (!Objects.isNull(inputTask)) {
            int id = inputTask.getId();
            if (storageTasks.containsKey(id)) {
                isAddingAllowed = addInPriority(inputTask);
                if (isAddingAllowed) {
                    storageTasks.put(id, inputTask);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean createEpic(Epic inputEpic) {
        if (!Objects.isNull(inputEpic)) {
            int id = ++counterId;
            inputEpic.setId(id);
            storageEpics.put(id, inputEpic);
            return true;
        }
        return false;
    }

    @Override
    public boolean updateEpic(Epic inputEpic) {
        if (!Objects.isNull(inputEpic)) {
            int id = inputEpic.getId();
            if (storageEpics.containsKey(id)) {
                storageEpics.put(id, inputEpic);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean createSubtask(Subtask inputSubtask) {
        boolean isAddingAllowed;
        if (!Objects.isNull(inputSubtask)) {
            int idEpic = inputSubtask.getIdenEpic();
            if (storageEpics.containsKey(idEpic)) {
                int id = ++counterId;
                inputSubtask.setId(id);
                isAddingAllowed = addInPriority(inputSubtask);
                if (isAddingAllowed) {
                    Epic temp = storageEpics.get(idEpic);
                    temp.addIdSub(id);
                    storageSubtasks.put(id, inputSubtask);
                    calculateStatusEpic(idEpic);
                    calculateDurationEpic(idEpic);
                    calculateTimeEpic(idEpic);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean updateSubtask(Subtask inputSubtask) {
        boolean isAddingAllowed;
        if (!Objects.isNull(inputSubtask)) {
            int id = inputSubtask.getId();
            if (storageSubtasks.containsKey(id)) {
                isAddingAllowed = addInPriority(inputSubtask);
                if (isAddingAllowed) {
                    int idEpic = inputSubtask.getIdenEpic();
                    storageSubtasks.put(id, inputSubtask);
                    calculateStatusEpic(idEpic);
                    calculateDurationEpic(idEpic);
                    calculateTimeEpic(idEpic);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public int receiveIdEpic(int search) {
        Subtask current = storageSubtasks.get(search);
        return current.getIdenEpic();
    }

    @Override
    public int removeByIdEpic(int search) {
        if (storageEpics.containsKey(search)) {
            Epic temp = storageEpics.get(search);
            ArrayList<Integer> del = temp.getListOfId();
            for (Integer item : del) {
                storageSubtasks.remove(item);
                if (managerH.existInBrowsingHistory(item)) {
                    managerH.remove(item);
                }
                priority.removeIf(element -> element.getId() == item);
            }
            storageEpics.remove(search);
            if (managerH.existInBrowsingHistory(search)) {
                managerH.remove(search);
            }

            return del.size();
        }
        return -1;
    }

    @Override
    public boolean removeByIdTask(int search) {
        if (storageTasks.containsKey(search)) {
            storageTasks.remove(search);
            if (managerH.existInBrowsingHistory(search)) {
                managerH.remove(search);
            }
            priority.removeIf(element -> element.getId() == search);
            return true;
        }
        return false;
    }

    @Override
    public boolean removeByIdSubtask(int search) {
        if (storageSubtasks.containsKey(search)) {
            Subtask current = storageSubtasks.get(search);
            int idEpic = current.getIdenEpic();
            Epic temp = storageEpics.get(idEpic);
            temp.deleteIdSub(search);
            storageSubtasks.remove(search);
            calculateStatusEpic(idEpic);
            calculateDurationEpic(idEpic);
            calculateTimeEpic(idEpic);
            if (managerH.existInBrowsingHistory(search)) {
                managerH.remove(search);
            }
            priority.removeIf(element -> element.getId() == search);
            return true;
        }
        return false;
    }

    @Override
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

    @Override
    public Epic getByIdEpic(int search) {
        managerH.add(takeSnapshot(search));
        return storageEpics.get(search);
    }

    @Override
    public Task getByIdTask(int search) {
        managerH.add(takeSnapshot(search));
        return storageTasks.get(search);
    }

    @Override
    public Subtask getByIdSubtask(int search) {
        managerH.add(takeSnapshot(search));
        return storageSubtasks.get(search);
    }

    @Override
    public boolean emptyList() {
        return storageEpics.isEmpty() && storageTasks.isEmpty() && storageSubtasks.isEmpty();
    }

    @Override
    public void removeEpics() {
        storageSubtasks.clear();
        storageEpics.clear();
    }

    @Override
    public void removeTasks() {
        storageTasks.clear();
    }

    @Override
    public void removeSubtasks() {
        storageSubtasks.clear();
        for (Integer element : storageEpics.keySet()) {
            Epic temp = storageEpics.get(element);
            temp.setCondition(Status.NEW);
            temp.getListOfId().clear();
        }
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
                Status status = temp.getCondition();
                sum++;
                if (status == Status.NEW) {
                    counterNew++;
                }
                if (status == Status.DONE) {
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

    //  Метод создает копию объекта, чтобы зафиксировать
    //  его состояние на тот момент, когда пользователь просматривает задачу
    private Task takeSnapshot(int search) {
        int markerClass = findOutClassObject(search);
        if (markerClass == 1) {
            Epic temp = storageEpics.get(search);
            if (temp.getStartTime() == null) {
                return new Epic(temp.getName(), temp.getDescription(), temp.getCondition(), temp.getId());
            } else {
                return new Epic(temp.getName(), temp.getDescription(), temp.getCondition(), temp.getId(),
                        temp.getStartTime(), temp.getDuration(), temp.getEndTime());
            }
        } else if (markerClass == 2) {
            Task temp = storageTasks.get(search);
            return new Task(temp.getName(), temp.getDescription(), temp.getCondition(), temp.getId(),
                    temp.getStartTime(), temp.getDuration());
        } else if (markerClass == 3) {
            Subtask temp = storageSubtasks.get(search);
            return new Subtask(temp.getIdenEpic(), temp.getName(), temp.getDescription(),
                    temp.getCondition(), temp.getId(), temp.getStartTime(), temp.getDuration());
        } else {
            return null;
        }
    }

    public List<Task> getPrioritizedTasks() {
        return priority.stream().
                toList();
    }

    private boolean validationTimeIntersections(Task task) {
        if (Objects.nonNull(task)) {
            LocalDateTime start1 = task.getStartTime();
            LocalDateTime end1 = task.getEndTime();

            // Фильтруем, чтобы не проверялось пересечение с той же самой задачей,
            // когда метод будет использоваться в методах обновления задач и подзадач.
            List<Task> filteredList = priority.stream()
                    .filter(item -> task.getId() != item.getId())
                    .toList();

            for (Task element : filteredList) {
                LocalDateTime start2 = element.getStartTime();
                LocalDateTime end2 = element.getEndTime();
                if ((start1.isAfter(start2) && start1.isBefore(end2))       // смещение назад
                        || (end1.isAfter(start2) && end1.isBefore(end2))    // смещение вперед
                        || (start1.isAfter(start2) && end1.isBefore(end2))  // вхождение
                        || (start1.isBefore(start2) && end1.isAfter(end2))  // поглощение
                        || (start1.equals(start2) && end1.equals(end2))) {  // совпадение
                    return false;
                }
            }
            return true;
        }
        throw new ManagerSaveException("В метод validationTimeIntersections передан null");
    }

    public boolean addInPriority(Task task) {
        boolean isNoIntersection;
        if (Objects.nonNull(task)) {
            if (priority.isEmpty()) {
                priority.add(task);
                return true;
            } else {
                isNoIntersection = validationTimeIntersections(task);
                if (isNoIntersection) {
                    priority.removeIf(element -> element.getId() == task.getId());
                    priority.add(task);
                    return true;
                } else {
                    System.out.println("Эта задача не может быть добавлена.\n" +
                            "На это время установлено выполнение другой задачи.");
                }
            }
        }
        return false;
    }

    private void calculateDurationEpic(int idEpic) {
        long sum = storageSubtasks.values().stream()
                .filter(temp -> temp.getIdenEpic() == idEpic)
                .mapToLong(temp -> temp.getDuration().toMinutes())
                .sum();

        Epic currentEpic = storageEpics.get(idEpic);
        Duration duration = Duration.ofMinutes(sum);
        currentEpic.setDuration(duration);
    }

    private void calculateTimeEpic(int idEpic) {
        Epic currentEpic = storageEpics.get(idEpic);
        List<Subtask> subtasks = storageSubtasks.values().stream()
                .filter(subtask -> subtask.getIdenEpic() == idEpic)
                .sorted(comparator)
                .toList();
        if (!subtasks.isEmpty()) {
            LocalDateTime startEpic = subtasks.get(0).getStartTime();
            LocalDateTime endEpic = subtasks.get(subtasks.size() - 1).getEndTime();
            currentEpic.setStartTime(startEpic);
            currentEpic.setEndTime(endEpic);
        }
    }
}