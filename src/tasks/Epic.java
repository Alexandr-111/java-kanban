package tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Epic extends Task {
    private final ArrayList<Integer> listOfId = new ArrayList<>();

    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Duration duration;

    // Конструктор вызывается при создании задач
    public Epic(String name, String description, Status condition) {
        super(name, description, condition);
    }

    // Конструктор вызывается при обновлении задач, когда id уже известен
    public Epic(String name, String description, Status condition, int id) {
        super(name, description, condition, id);
    }

    // Конструктор вызывается для создания эпика из строки, если у него уже есть подзадачи
    public Epic(String name, String description, Status condition, int id,
                LocalDateTime startTime, Duration duration, LocalDateTime endTime) {
        super(name, description, condition, id);
        this.startTime = startTime;
        this.duration = duration;
        this.endTime = endTime;
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    @Override
    public LocalDateTime getStartTime() {
        return startTime;
    }

    public ArrayList<Integer> getListOfId() {
        return listOfId;
    }

    @Override
    public Duration getDuration() {
        return duration;
    }

    public void addIdSub(int idSub) {
        listOfId.add(idSub);
    }

    public void deleteIdSub(int idSub) {
        listOfId.remove(Integer.valueOf(idSub));
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "id ='" + id + "', "
                + "назв.='" + this.name + "', "
                + "опис.='" + this.description + "', "
                + "стат.='" + this.condition + "', "
                + "прод.='" + this.duration + "', "
                + "нач.='" + this.startTime + "', "
                + "кон.='" + this.endTime + "'\n";
    }
}