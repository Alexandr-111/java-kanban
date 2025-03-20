package tasks;

import java.time.Duration;
import java.time.LocalDateTime;

public class Task {
    protected final String name;
    protected final String description;
    protected Status condition;
    protected int id = 0;
    protected LocalDateTime startTime;
    protected LocalDateTime endTime;
    protected Duration duration;


    // Конструктор вызывается при создании задач
    public Task(String name, String description, Status condition, LocalDateTime startTime, Duration duration) {
        this.name = name;
        this.description = description;
        this.condition = condition;
        this.startTime = startTime;
        this.duration = duration;
        this.endTime = startTime.plusMinutes(duration.toMinutes());
    }

    // Конструктор вызывается при обновлении задач, когда id уже известен
    public Task(String name, String description, Status condition, int id, LocalDateTime startTime,
                Duration duration) {
        this.name = name;
        this.description = description;
        this.condition = condition;
        this.id = id;
        this.startTime = startTime;
        this.duration = duration;
        this.endTime = startTime.plusMinutes(duration.toMinutes());
    }

    // Родительские конструкторы для создания и обновления Эпика,
    // без параметров LocalDateTime startTime и Duration duration
    public Task(String name, String description, Status condition) {
        this.name = name;
        this.description = description;
        this.condition = condition;
    }

    public Task(String name, String description, Status condition, int id) {
        this.name = name;
        this.description = description;
        this.condition = condition;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getId() {
        return id;
    }

    public Duration getDuration() {
        return duration;
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

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        long continuance = duration.toMinutes();
        return startTime.plusMinutes(duration.toMinutes());
    }

    @Override
    public String toString() {
        return "id ='" + this.id + "', "
                + "назв.='" + this.name + "', "
                + "опис.= " + this.description + "', "
                + "стат.='" + this.condition + "', "
                + "прод.='" + this.duration + "', "
                + "нач.='" + this.startTime + "', "
                + "кон.='" + this.endTime + "'\n";
    }
}