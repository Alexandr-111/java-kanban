package tasks;

public class TaskCSVFormat {
    private TaskCSVFormat() {
    }

    public static final String Dl = ",";

    public static String getHeader() {
        return "id,type,name,status,description,startTime,duration,endTime,epicId";
    }

    public static String toStringTask(Task task) {
        return task.getId() + Dl + TypesOfTasks.TASK + Dl + task.getName() + Dl + task.getCondition() + Dl
                + task.getDescription() + Dl + task.getStartTime() + Dl + task.getDuration().toMinutes()
                + Dl + task.getEndTime() + Dl;
    }

    public static String toStringEpic(Epic epic) {
        if (epic.getStartTime() == null) {
            return epic.getId() + Dl + TypesOfTasks.EPIC + Dl + epic.getName() + Dl + epic.getCondition() + Dl
                    + epic.getDescription() + Dl;
        } else {
            return epic.getId() + Dl + TypesOfTasks.EPIC + Dl + epic.getName() + Dl + epic.getCondition() + Dl
                    + epic.getDescription() + Dl + epic.getStartTime() + Dl + epic.getDuration().toMinutes()
                    + Dl + epic.getEndTime() + Dl;
        }
    }

    public static String toStringSubtask(Subtask subtask) {
        return subtask.getId() + Dl + TypesOfTasks.SUBTASK + Dl + subtask.getName() + Dl + subtask.getCondition()
                + Dl + subtask.getDescription() + Dl + subtask.getStartTime() + Dl
                + subtask.getDuration().toMinutes() + Dl + subtask.getEndTime() + Dl + subtask.getIdenEpic();
    }
}