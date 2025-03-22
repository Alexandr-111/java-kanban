import org.junit.jupiter.api.Test;
import tasks.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StringToTaskConverterTest {

    @Test
    void fromStringToStatusCheck() {
        String str = "NEW";
        Status stat = StringToTaskConverter.fromStringToStatus(str);
        Status statNew = Status.NEW;
        assertEquals(statNew, stat);
    }

    @Test
    void fromStringToTaskCheck() {
        String str = "10,TASK,Новая задача,IN_PROGRESS,Новое описание,2025-06-20T15:10,390,2025-06-20T21:40,";
        String[] param = str.split(",");
        Task temp = StringToTaskConverter.fromStringToTask(param);
        assertEquals(temp.getName(), "Новая задача");
        assertEquals(Status.IN_PROGRESS, temp.getCondition());
        assertEquals(10, temp.getId());
        assertEquals("Новое описание", temp.getDescription());
    }

    @Test
    void fromStringToEpicCheck() {
        String str = "15,EPIC,Эпик_1,NEW,Описание61,2025-06-20T15:30,30,2025-06-20T16:00,";
        String[] param = str.split(",");
        Epic temp = StringToTaskConverter.fromStringToEpic(param);
        assertEquals(temp.getName(), "Эпик_1");
        assertEquals(Status.NEW, temp.getCondition());
        assertEquals(15, temp.getId());
        assertEquals("Описание61", temp.getDescription());
    }

    @Test
    void fromStringToSubtaskCheck() {
        String str = "2,SUBTASK,Задача_10,IN_PROGRESS,Описание568,2025-06-20T15:30,30,2025-06-20T16:00,1";
        String[] param = str.split(",");
        Task temp = StringToTaskConverter.fromStringToSubtask(param);
        assertEquals(temp.getName(), "Задача_10");
        assertEquals(Status.IN_PROGRESS, temp.getCondition());
        assertEquals(2, temp.getId());
        assertEquals("Описание568", temp.getDescription());
        assertEquals(30, (int) temp.getDuration().toMinutes());
    }
}