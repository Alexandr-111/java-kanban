import exceptions.ManagerSaveException;
import tasks.*;
import managers.*;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        FileBackedTaskManager managerF = null;
        try {
            managerF = Managers.getDefaultManagerWithFile();
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
        }
        if (managerF != null) {
            for (int i = 1; i < 4; i++) {
                managerF.createEpic(new Epic("Эпик_" + i, "Описание эпика", Status.NEW));
            }
            for (int i = 1; i < 4; i++) {
                managerF.createSubtask(new Subtask(i, "Подзадача_" + i, "Описание задачи", Status.DONE));
                managerF.createSubtask(new Subtask(i, "Подзадача_" + i * 6, "Описание 555", Status.NEW));
            }
            for (int i = 1; i < 3; i++) {
                managerF.createTask(new Task("Задача_" + i, "Описание задачи", Status.NEW));
            }
            //   Проверяем, что в файле сохраняется состоянии задач после их обновления
            managerF.updateTask(new Task("Новая задача", "Новое описание", Status.IN_PROGRESS, 10));
            managerF.updateSubtask(new Subtask(1, "Новая", "Описание7", Status.DONE, 5));

            //Удалим задачу и проверим, что она не будет сохраняться в файле
            managerF.removeByIdTask(11);

            // Удалим  эпик с двумя его подзадачами и убедимся, что в файле не сохраняется
            //  как сам эпик, так и все его подзадачи (id = 4 и id = 5)
            managerF.removeByIdEpic(1);

            // Напечатаем в консоли список всех задач в хэш-таблицах и убедимся,что в файле сохранились такие же задачи
            System.out.println("Список всех задач в managerF:");
            printE(managerF.getEpics());
            printS(managerF.getSubtasks());
            printT(managerF.getTasks());
            System.out.println("________________________________________________________________");

            // Создадим второй экземпляр менеджера FileBackedTaskManager, загрузим в него сохранение из файла save.csv
            // Выведем состояние хранилища в консоль и убедимся в том, что задачи в обоих менеджерах идентичны
            FileBackedTaskManager managerF2 = null;
            try {
              managerF2 = Managers.getDefaultManagerWithFile();
            } catch (ManagerSaveException e) {
                System.out.println(e.getMessage());
            }
            if (managerF2 != null) {
                System.out.println("Список всех задач managerF2:");
                printE(managerF2.getEpics());
                printS(managerF2.getSubtasks());
                printT(managerF2.getTasks());
            }
        }
    }

    static void printE(ArrayList<Epic> records) {
        System.out.println(records);
    }

    static void printT(ArrayList<Task> records) {
        System.out.println(records);
    }

    static void printS(ArrayList<Subtask> records) {
        System.out.println(records);
    }
}