import managers.FileBackedTaskManager;
import managers.Managers;
import org.junit.jupiter.api.*;
import tasks.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

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
        String str = "id,type,name,status,description,epic";
        String line = TaskCSVFormat.getHeader();
        assertEquals(str, line);
    }

    @Test
    void toStringTaskCheck() {
        boolean b = managerF.createTask(new Task("Новая задача", "Новое описание", Status.IN_PROGRESS));
        assertTrue(b);
        Task temp = managerF.getByIdTask(1);
        String str = "1,TASK,Новая задача,IN_PROGRESS,Новое описание,";
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