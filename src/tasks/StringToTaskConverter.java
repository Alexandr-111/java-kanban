package tasks;

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
        return new Task(param[2], param[4], status, Integer.parseInt(param[0]));
    }

    public static Epic fromStringToEpic(String[] param) {
        Status status = fromStringToStatus(param[3]);
        return new Epic(param[2], param[4], status, Integer.parseInt(param[0]));
    }

    public static Subtask fromStringToSubtask(String[] param) {
        Status status = fromStringToStatus(param[3]);
        return new Subtask(Integer.parseInt(param[5]), param[2], param[4], status, Integer.parseInt(param[0]));
    }
}