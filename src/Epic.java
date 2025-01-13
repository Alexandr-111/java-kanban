import java.util.HashMap;

public class Epic extends Task {
    public Epic(String name, String description, Status condition) {
        super(name, description, condition);
    }

    HashMap<Integer, Subtask> tableForSubtasks = new HashMap<>();

    public HashMap<Integer, Subtask> getTableForSubtasks() {
        return tableForSubtasks;
    }
}