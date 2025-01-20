package tasks;

public class Task {
    protected final String name;
    protected final String description;
    protected Status condition;
    protected int id = 0;

    // Конструктор вызывается при создании задач
    public Task(String name, String description, Status condition) {
        this.name = name;
        this.description = description;
        this.condition = condition;

    }

    // Конструктор вызывается при обновлении задач, когда id уже известен
    public Task(String name, String description, Status condition, int id) {
        this.name = name;
        this.description = description;
        this.condition = condition;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCondition(Status condition) {
        this.condition = condition;
    }

    public Status getCondition() {
        return condition;
    }

    @Override
    public String toString() {
        return "ЗАДАЧА. " +
                "Название = '" + name + "', "
                + "Описание = '" + description + "', "
                + "Статус = '" + condition + "', "
                + "Id = '" + this.id + "'\n";
    }
}