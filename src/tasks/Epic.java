package tasks;

import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Integer> listOfId = new ArrayList<>();

    public Epic(String name, String description, Status condition, int id) {
        super(name, description, condition, id);
    }

    public ArrayList<Integer> getListOfId() {
        return listOfId;
    }

    @Override
    public String toString() {
        return "ЭПИК. "
                + "Название = '" + super.name + "', "
                + "Описание = '" + super.description + "', "
                + "Статус = '" + super.condition + "', "
                + "Id = '" + super.id + "'\n";
    }
}