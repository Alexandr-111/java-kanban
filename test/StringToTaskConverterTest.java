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
        String str = "10,TASK,Новая задача,IN_PROGRESS,Новое описание,";
        String[] param = str.split(",");
        Task temp = StringToTaskConverter.fromStringToTask(param);
        assertEquals(temp.getName(), "Новая задача");
        assertEquals(Status.IN_PROGRESS, temp.getCondition());
        assertEquals(10, temp.getId());
        assertEquals("Новое описание", temp.getDescription());
    }
}