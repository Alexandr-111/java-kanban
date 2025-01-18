package tasks;

public class Subtask extends Task {
    private final int idenEpic;

    public Subtask(int idenEpic, String name, String description, Status condition, int id) {
        super(name, description, condition, id);
        this.idenEpic = idenEpic;
    }

    public int getIdenEpic() {
        return idenEpic;
    }

    @Override
    public String toString() {
        return "ПОДЗАДАЧА. "
                + "Id эпика = '" + this.idenEpic + "', "
                + "Название = '" + super.name + "', "
                + "Описание ='" + super.description + "', "
                + "Статус = '" + super.condition + "', "
                + "Id = '" + super.id + "'\n";
    }
}