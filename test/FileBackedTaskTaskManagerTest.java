import managers.FileBackedTaskManager;
import managers.Managers;
import org.junit.jupiter.api.*;
import tasks.*;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Files;

import static managers.FileBackedTaskManager.CS8;
import static org.junit.jupiter.api.Assertions.*;

public class FileBackedTaskTaskManagerTest extends TaskManagerTest {
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
    void createTaskAndWriteCheck() {     // Метод для Unix-систем и  Windows-систем
        try {
            boolean a = false;
            boolean c = false;
            boolean b = managerF.createTask(new Task("название_2", "описание_2", Status.NEW));
            assertTrue(b);
            String str_0 = """
                    id,type,name,status,description,epic\r
                    1,TASK,название_2,NEW,описание_2,\r
                    """;
            String str_1 = """
                    id,type,name,status,description,epic
                    1,TASK,название_2,NEW,описание_2,
                    """;
            String str_2 = Files.readString(managerF.path, CS8);
            if (str_0.equals(str_2)) {
                a = true;
            }
            if (str_1.equals(str_2)) {
                c = true;
            }
            assertNotSame(a, c);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void removeByIdTaskAndWriteCheck() {     // Метод для Unix-систем и  Windows-систем
        boolean a = false;
        boolean y = false;
        try {
            boolean b = managerF.createTask(new Task("название_5", "описание_6", Status.DONE));
            assertTrue(b);
            boolean c = managerF.removeByIdTask(1);
            assertTrue(c);
            String str_0 = "id,type,name,status,description,epic\r\n";
            String str_1 = "id,type,name,status,description,epic\n";
            String str_2 = Files.readString(managerF.path, CS8);
            if (str_0.equals(str_2)) {
                a = true;
            }
            if (str_1.equals(str_2)) {
                y = true;
            }
            assertNotSame(a, y);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void updateTaskAndWriteCheck() {     // Метод для Unix-систем и  Windows-систем
        try {
            boolean a = false;
            boolean y = false;
            boolean b = managerF.createTask(new Task("название_6", "описание_7", Status.IN_PROGRESS));
            assertTrue(b);
            boolean c = managerF.updateTask(new Task("название_10", "описание_20", Status.DONE, 1));
            assertTrue(c);
            String str_0 = """
                    id,type,name,status,description,epic\r
                    1,TASK,название_10,DONE,описание_20,\r
                    """;
            String str_1 = """
                    id,type,name,status,description,epic
                    1,TASK,название_10,DONE,описание_20,
                    """;
            String str_2 = Files.readString(managerF.path, CS8);
            if (str_0.equals(str_2)) {
                a = true;
            }
            if (str_1.equals(str_2)) {
                y = true;
            }
            assertNotSame(a, y);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void saveCheck() {     // Метод для Unix-систем и  Windows-систем
        try {
            boolean x = false;
            boolean y = false;
            boolean c = managerF.createEpic(new Epic("название_5", "описание_5", Status.DONE));
            boolean a = managerF.createTask(new Task("название_42", "описание_98", Status.IN_PROGRESS));
            boolean b = managerF.createSubtask
                    (new Subtask(1, "название_5", "описание_71", Status.NEW, 1));
            assertTrue(a && b && c);
            managerF.removeByIdTask(2);
            String str_0 = """
                    id,type,name,status,description,epic\r
                    1,EPIC,название_5,NEW,описание_5,\r
                    3,SUBTASK,название_5,NEW,описание_71,1\r
                    """;
            String str_1 = """
                    id,type,name,status,description,epic
                    1,EPIC,название_5,NEW,описание_5,
                    3,SUBTASK,название_5,NEW,описание_71,1
                    """;
            String str_2 = Files.readString(managerF.path, CS8);
            if (str_0.equals(str_2)) {
                x = true;
            }
            if (str_1.equals(str_2)) {
                y = true;
            }
            assertNotSame(x, y);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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