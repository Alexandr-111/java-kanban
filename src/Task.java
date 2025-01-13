public class Task {
    String name;
    String description;
    Status condition;

    public Task(String name, String description, Status condition) {
        this.name = name;
        this.description = description;
        this.condition = condition;
    }

    public void setCondition(Status condition) {
        this.condition = condition;
    }

    public Status getCondition() {
        return condition;
    }

    @Override
    public String toString() {
        return "{ Название = '" + name + "', "
                + "Описание ='" + description + "', "
                + "Статус = " + condition
                + " }";
    }
}