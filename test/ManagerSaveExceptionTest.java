import exceptions.ManagerSaveException;
import managers.*;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class ManagerSaveExceptionTest {
    Path document = Path.of("W:\\src\\resources\\document.csv");

    @Test
    public void exceptionMakeNewFileCheck() {
        assertThrows(ManagerSaveException.class, () -> FileBackedTaskManager.makeNewFile(document),
                "Expected ManagerSaveException for invalid Path");
    }
}