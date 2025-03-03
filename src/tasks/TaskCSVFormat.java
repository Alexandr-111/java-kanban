package tasks;

public class TaskCSVFormat {
    private TaskCSVFormat() {
    }

    public static final String Dl = ",";

    public static String getHeader() {
        return "id,type,name,status,description,epic";
    }

    public static String toStringTask(Task task) {
        return task.getId() + Dl + TypesOfTasks.TASK + Dl + task.getName() + Dl + task.getCondition() + Dl
                + task.getDescription() + Dl;
    }

    public static String toStringEpic(Epic epic) {
        return epic.getId() + Dl + TypesOfTasks.EPIC + Dl + epic.getName() + Dl + epic.getCondition() + Dl
                + epic.getDescription() + Dl;
    }

    public static String toStringSubtask(Subtask subtask) {
        return subtask.getId() + Dl + TypesOfTasks.SUBTASK + Dl + subtask.getName() + Dl + subtask.getCondition()
                + Dl + subtask.getDescription() + Dl + subtask.getIdenEpic();
    }
}