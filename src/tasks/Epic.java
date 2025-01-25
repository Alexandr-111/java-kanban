package tasks;

import java.util.ArrayList;

public class Epic extends Task {
    private final ArrayList<Integer> listOfId = new ArrayList<>();

    // Конструктор вызывается при создании задач
    public Epic(String name, String description, Status condition) {
        super(name, description, condition);
    }

    // Конструктор вызывается при обновлении задач, когда id уже известен
    public Epic(String name, String description, Status condition, int id) {
        super(name, description, condition, id);
    }

    public ArrayList<Integer> getListOfId() {
        return listOfId;
    }

    public void addIdSub(int idSub) {
        listOfId.add(idSub);
    }

    public void deleteIdSub(int idSub) {
        listOfId.remove(Integer.valueOf(idSub));
    }
}