package tasks;

public class Subtask extends Task {
    private final int idenEpic;

    // Конструктор вызывается при создании задач
    public Subtask(int idenEpic, String name, String description, Status condition) {
        super(name, description, condition);
        this.idenEpic = idenEpic;
    }

    // Конструктор вызывается при обновлении задач, когда id уже известен
    public Subtask(int idenEpic, String name, String description, Status condition, int id) {
        super(name, description, condition, id);
        this.idenEpic = idenEpic;
    }

    public int getIdenEpic() {
        return idenEpic;
    }

    @Override
    public String toString() {
        return "Id эпика = '" + this.idenEpic + "', "
                + "Название = '" + super.name + "', "
                + "Описание ='" + super.description + "', "
                + "Статус = '" + super.condition + "', "
                + "Id = '" + super.id + "'\n";
    }
}