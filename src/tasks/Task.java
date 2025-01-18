package tasks;

public class Task {
    protected final String name;
    protected final String description;
    protected Status condition;
    protected final int id;

    public Task(String name, String description, Status condition, int id) {
        this.name = name;
        this.description = description;
        this.condition = condition;
        this.id = id;

    }

    public int getId() {
        return id;
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