package tasks;

import java.time.Duration;
import java.time.LocalDateTime;

public class Subtask extends Task {
    private final int idenEpic;

    // Конструктор вызывается при создании задач
    public Subtask
    (int idenEpic, String name, String description, Status condition, LocalDateTime startTime, Duration duration) {
        super(name, description, condition, startTime, duration);
        this.idenEpic = idenEpic;
    }

    // Конструктор вызывается при обновлении задач, когда id уже известен
    public Subtask(int idenEpic, String name, String description, Status condition, int id,
                   LocalDateTime startTime, Duration duration) {
        super(name, description, condition, id, startTime, duration);
        this.idenEpic = idenEpic;
    }

    public int getIdenEpic() {
        return idenEpic;
    }

    @Override
    public String toString() {
        return "id ='" + this.id + "', "
                + "назв.='" + this.name + "', "
                + "опис.='" + this.description + "', "
                + "стат.='" + this.condition + "', "
                + "прод.='" + this.duration + "', "
                + "нач.='" + this.startTime + "', "
                + "кон.='" + this.endTime + "', "
                + "Idэпик.= '" + this.idenEpic + "'\n";
    }
}