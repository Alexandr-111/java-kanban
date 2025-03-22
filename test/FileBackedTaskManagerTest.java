import managers.FileBackedTaskManager;
import managers.Managers;
import org.junit.jupiter.api.*;
import tasks.*;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Files;
import java.time.Duration;
import java.time.LocalDateTime;

import static managers.FileBackedTaskManager.CS8;
import static org.junit.jupiter.api.Assertions.*;

public class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {

    @BeforeEach
    public void beforeEach() {
        try {
            Path p = Path.of("src\\resources\\save.csv");
            Files.writeString(p, "", CS8);
            manager = Managers.getDefaultManagerWithFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void createTaskCheck() {     // Метод для Unix-систем и  Windows-систем
        try {
            boolean a = false;
            boolean c = false;
            Duration duration = Duration.ofMinutes(390L);
            LocalDateTime dateTime = LocalDateTime.of(2025, 6, 20, 15, 10);
            boolean b = manager.createTask(new Task("Задача_10", "Описание568", Status.IN_PROGRESS,
                    dateTime, duration));
            assertTrue(b);
            String str_0 = """
                    id,type,name,status,description,startTime,duration,endTime,epicId\r
                    1,TASK,Задача_10,IN_PROGRESS,Описание568,2025-06-20T15:10,390,2025-06-20T21:40,\r
                    """;
            String str_1 = """
                    id,type,name,status,description,startTime,duration,endTime,epicId
                    1,TASK,Задача_10,IN_PROGRESS,Описание568,2025-06-20T15:10,390,2025-06-20T21:40,
                    """;
            String str_2 = Files.readString(manager.path, CS8);
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
    void removeByIdTaskCheck() {     // Метод для Unix-систем и  Windows-систем
        boolean a = false;
        boolean y = false;
        Duration duration = Duration.ofMinutes(390L);
        LocalDateTime dateTime = LocalDateTime.of(2025, 6, 20, 15, 10);
        try {
            boolean b = manager.createTask(new Task("Задача_10", "Описание568",
                    Status.IN_PROGRESS, dateTime, duration));
            assertTrue(b);
            boolean c = manager.removeByIdTask(1);
            assertTrue(c);
            String str_0 = "id,type,name,status,description,startTime,duration,endTime,epicId\r\n";
            String str_1 = "id,type,name,status,description,startTime,duration,endTime,epicId\n";
            String str_2 = Files.readString(manager.path, CS8);
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
    void updateTaskCheck() {     // Метод для Unix-систем и  Windows-систем
        try {
            boolean a = false;
            boolean y = false;
            Duration duration = Duration.ofMinutes(390L);
            LocalDateTime dateTime = LocalDateTime.of(2025, 6, 20, 15, 10);
            boolean b = manager.createTask(new Task("Задача_10", "Описание568",
                    Status.IN_PROGRESS, dateTime, duration));
            boolean c = manager.updateTask(new Task("Обновленное", "Описание500",
                    Status.DONE, 1, dateTime, duration));
            assertTrue(c && b);
            String str_0 = """
                    id,type,name,status,description,startTime,duration,endTime,epicId\r
                    1,TASK,Обновленное,DONE,Описание500,2025-06-20T15:10,390,2025-06-20T21:40,\r
                    """;
            String str_1 = """
                    id,type,name,status,description,startTime,duration,endTime,epicId
                    1,TASK,Обновленное,DONE,Описание500,2025-06-20T15:10,390,2025-06-20T21:40,
                    """;
            String str_2 = Files.readString(manager.path, CS8);
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
            Duration duration = Duration.ofMinutes(90L);
            Duration duration2 = Duration.ofMinutes(300L);
            LocalDateTime dateTime = LocalDateTime.of(2025, 4, 19, 12, 30);
            LocalDateTime dateTime2 = LocalDateTime.of(2025, 6, 19, 8, 30);
            boolean c = manager.createEpic(new Epic("название_5", "описание_5", Status.DONE));
            boolean a = manager.createTask(new Task("название_42", "описание_98",
                    Status.IN_PROGRESS, dateTime, duration));
            boolean b = manager.createSubtask(new Subtask(1, "название_5", "описание_71",
                    Status.NEW, 1, dateTime2, duration2));
            assertTrue(a && b && c);
            manager.removeByIdTask(2);
            String str_0 = """
                    id,type,name,status,description,startTime,duration,endTime,epicId\r
                    1,EPIC,название_5,NEW,описание_5,2025-06-19T08:30,300,2025-06-19T13:30,\r
                    3,SUBTASK,название_5,NEW,описание_71,2025-06-19T08:30,300,2025-06-19T13:30,1\r
                    """;

            String str_1 = """
                    id,type,name,status,description,startTime,duration,endTime,epicId
                    1,EPIC,название_5,NEW,описание_5,2025-06-19T08:30,300,2025-06-19T13:30,
                    3,SUBTASK,название_5,NEW,описание_71,2025-06-19T08:30,300,2025-06-19T13:30,1
                    """;
            String str_2 = Files.readString(manager.path, CS8);
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

    @Test
    void createEpicCheck() {     // Метод для Unix-систем и  Windows-систем
        try {
            boolean a = false;
            boolean c = false;
            boolean b = manager.createEpic(new Epic("Эпик_1", "Описание61", Status.NEW));
            assertTrue(b);
            String str_0 = """
                    id,type,name,status,description,startTime,duration,endTime,epicId\r
                    1,EPIC,Эпик_1,NEW,Описание61,\r
                    """;
            String str_1 = """
                    id,type,name,status,description,startTime,duration,endTime,epicId
                    1,EPIC,Эпик_1,NEW,Описание61,
                    """;
            String str_2 = Files.readString(manager.path, CS8);
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
    void createSubtaskCheck() {     // Метод для Unix-систем и  Windows-систем
        try {
            boolean a = false;
            boolean c = false;
            Duration duration = Duration.ofMinutes(30L);
            LocalDateTime dateTime = LocalDateTime.of(2025, 6, 20, 15, 30);
            boolean d = manager.createEpic(new Epic("Эпик_1", "Описание61", Status.NEW));
            boolean b = manager.createSubtask(new Subtask(1, "Задача_10", "Описание568",
                    Status.IN_PROGRESS, dateTime, duration));
            assertTrue(d && b);
            String str_0 = """
                    id,type,name,status,description,startTime,duration,endTime,epicId\r
                    1,EPIC,Эпик_1,IN_PROGRESS,Описание61,2025-06-20T15:30,30,2025-06-20T16:00,\r
                    2,SUBTASK,Задача_10,IN_PROGRESS,Описание568,2025-06-20T15:30,30,2025-06-20T16:00,1\r
                    """;
            String str_1 = """
                    id,type,name,status,description,startTime,duration,endTime,epicId
                    1,EPIC,Эпик_1,IN_PROGRESS,Описание61,2025-06-20T15:30,30,2025-06-20T16:00,
                    2,SUBTASK,Задача_10,IN_PROGRESS,Описание568,2025-06-20T15:30,30,2025-06-20T16:00,1
                    """;
            String str_2 = Files.readString(manager.path, CS8);
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
    void updateEpicCheck() {     // Метод для Unix-систем и  Windows-систем
        try {
            boolean a = false;
            boolean c = false;
            boolean d = manager.createEpic(new Epic("Эпик_100", "Описание487", Status.NEW));
            boolean b = manager.updateEpic(new Epic("Эпик_1", "Описание61", Status.NEW, 1));
            assertTrue(d && b);
            String str_0 = """
                    id,type,name,status,description,startTime,duration,endTime,epicId\r
                    1,EPIC,Эпик_1,NEW,Описание61,\r
                    """;
            String str_1 = """
                    id,type,name,status,description,startTime,duration,endTime,epicId
                    1,EPIC,Эпик_1,NEW,Описание61,
                    """;
            String str_2 = Files.readString(manager.path, CS8);
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
    void updateSubtaskCheck() {
        try {
            boolean a = false;
            boolean c = false;
            Duration duration = Duration.ofMinutes(30L);
            LocalDateTime dateTime = LocalDateTime.of(2025, 6, 20, 15, 30);
            boolean b = manager.createEpic(new Epic("Эпик_1", "Описание61", Status.NEW));
            boolean f = manager.createSubtask(new Subtask(1, "Задача_10", "Описание568",
                    Status.IN_PROGRESS, dateTime, duration));
            boolean d = manager.updateSubtask(new Subtask(1, "Обновление", "Описание5",
                    Status.IN_PROGRESS, 2, dateTime, duration));
            assertTrue(d && b && f);
            String str_0 = """
                    id,type,name,status,description,startTime,duration,endTime,epicId\r
                    1,EPIC,Эпик_1,IN_PROGRESS,Описание61,2025-06-20T15:30,30,2025-06-20T16:00,\r
                    2,SUBTASK,Обновление,IN_PROGRESS,Описание5,2025-06-20T15:30,30,2025-06-20T16:00,1\r                   
                    """;
            String str_1 = """
                    id,type,name,status,description,startTime,duration,endTime,epicId
                    1,EPIC,Эпик_1,IN_PROGRESS,Описание61,2025-06-20T15:30,30,2025-06-20T16:00,
                    2,SUBTASK,Обновление,IN_PROGRESS,Описание5,2025-06-20T15:30,30,2025-06-20T16:00,1       
                    """;
            String str_2 = Files.readString(manager.path, CS8);
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
    void removeByIdEpicCheck() {
        boolean a = false;
        boolean y = false;
        Duration duration = Duration.ofMinutes(390L);
        LocalDateTime dateTime = LocalDateTime.of(2025, 6, 20, 15, 10);
        try {
            boolean b = manager.createEpic(new Epic("Эпик_1", "Описание61", Status.NEW));
            boolean c = manager.createSubtask(new Subtask(1, "Задача_10", "Описание568",
                    Status.IN_PROGRESS, dateTime, duration));
            assertTrue(b && c);
            int number = manager.removeByIdEpic(1);
            assertEquals(1, number);
            String str_0 = "id,type,name,status,description,startTime,duration,endTime,epicId\r\n";
            String str_1 = "id,type,name,status,description,startTime,duration,endTime,epicId\n";
            String str_2 = Files.readString(manager.path, CS8);
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
    void removeByIdSubtaskCheck() {
        boolean a = false;
        boolean y = false;
        Duration duration = Duration.ofMinutes(390L);
        LocalDateTime dateTime = LocalDateTime.of(2025, 6, 20, 15, 10);
        try {
            boolean b = manager.createEpic(new Epic("Эпик_1", "Описание61", Status.NEW));
            boolean c = manager.createSubtask(new Subtask(1, "Задача_10", "Описание568",
                    Status.IN_PROGRESS, dateTime, duration));
            assertTrue(b && c);
            boolean f = manager.removeByIdSubtask(2);
            assertTrue(f);
            String str_0 = """ 
                    id,type,name,status,description,startTime,duration,endTime,epicId\r
                    1,EPIC,Эпик_1,IN_PROGRESS,Описание61,2025-06-20T15:10,0,2025-06-20T21:40,\r
                    """;
            String str_1 = """ 
                    id,type,name,status,description,startTime,duration,endTime,epicId
                    1,EPIC,Эпик_1,IN_PROGRESS,Описание61,2025-06-20T15:10,0,2025-06-20T21:40,
                    """;
            String str_2 = Files.readString(manager.path, CS8);
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