package tasks;

import java.time.Duration;
import java.time.LocalDateTime;

public class StringToTaskConverter {
    private StringToTaskConverter() {
    }

    public static Status fromStringToStatus(String st) {
        Status status = null;
        if (st.equals("NEW")) {
            status = Status.NEW;
        } else if (st.equals("DONE")) {
            status = Status.DONE;
        } else if (st.equals("IN_PROGRESS")) {
            status = Status.IN_PROGRESS;
        }
        return status;
    }

    public static Task fromStringToTask(String[] param) {
        Status status = fromStringToStatus(param[3]);
        long minutes = Long.parseLong(param[6]);
        Duration duration = Duration.ofMinutes(minutes);
        return new Task(param[2], param[4], status, Integer.parseInt(param[0]), LocalDateTime.parse(param[5]),
                duration);
    }

    public static Epic fromStringToEpic(String[] param) {
        Status status = fromStringToStatus(param[3]);
        if (param.length == 5) {
            return new Epic(param[2], param[4], status, Integer.parseInt(param[0]));
        } else {
            long minutes = Long.parseLong(param[6]);
            Duration duration = Duration.ofMinutes(minutes);
            return new Epic(param[2], param[4], status, Integer.parseInt(param[0]), LocalDateTime.parse(param[5]),
                    duration, LocalDateTime.parse(param[7]));
        }
    }

    public static Subtask fromStringToSubtask(String[] param) {
        Status status = fromStringToStatus(param[3]);
        long minutes = Long.parseLong(param[6]);
        Duration duration = Duration.ofMinutes(minutes);
        return new Subtask(Integer.parseInt(param[8]), param[2], param[4], status, Integer.parseInt(param[0]),
                LocalDateTime.parse(param[5]), duration);
    }
}