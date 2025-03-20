import managers.FileBackedTaskManager;
import managers.Managers;
import org.junit.jupiter.api.*;
import tasks.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDateTime;

import static managers.FileBackedTaskManager.CS8;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TaskCSVFormatTest {
    private FileBackedTaskManager managerF;

    @BeforeEach
    public void beforeEach() {
        try {
            Path p = Path.of("src\\resources\\save.csv");
            Files.writeString(p, "", CS8);
            managerF = Managers.getDefaultManagerWithFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void getHeaderCheck() {
        String str = "id,type,name,status,description,startTime,duration,endTime,epicId";
        String line = TaskCSVFormat.getHeader();
        assertEquals(str, line);
    }

    @Test
    void toStringTaskCheck() {
        Duration duration = Duration.ofMinutes(390L);
        LocalDateTime dateTime = LocalDateTime.of(2025, 6, 20, 15, 10);
        boolean b = managerF.createTask(new Task("Задача_10", "Описание568",
                Status.IN_PROGRESS, dateTime, duration));
        assertTrue(b);
        Task temp = managerF.getByIdTask(1);
        String str = "1,TASK,Задача_10,IN_PROGRESS,Описание568,2025-06-20T15:10,390,2025-06-20T21:40,";
        String result = TaskCSVFormat.toStringTask(temp);
        assertEquals(str, result);
    }

    @Test
    void toStringEpicCheck() {
        boolean b = managerF.createEpic(new Epic("Эпик_3", "Описание этого эпика", Status.DONE));
        assertTrue(b);
        Epic temp = managerF.getByIdEpic(1);
        String str = "1,EPIC,Эпик_3,DONE,Описание этого эпика,";
        String result = TaskCSVFormat.toStringEpic(temp);
        assertEquals(str, result);
    }

    @AfterAll
    public static void afterAll() {
        try {
            Path p = Path.of("src\\resources\\save.csv");
            Files.writeString(p, "", CS8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}