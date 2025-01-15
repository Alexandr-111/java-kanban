package tasks;

public class Epic extends Task {

    public Epic(String name, String description, Status condition) {
        super(name, description, condition);
    }

    @Override
    public String toString() {
        return "ЭПИК. "
                + "Название = '" + super.name + "', "
                + "Описание ='" + super.description + "', "
                + "Статус = " + super.condition + "\n";
    }
}