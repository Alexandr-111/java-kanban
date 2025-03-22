import tasks.*;
import managers.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        FileBackedTaskManager managerF;
        managerF = Managers.getDefaultManagerWithFile();
        if (managerF != null) {
            managerF.createEpic(new Epic("Эпик_1", "Описание61", Status.NEW));
            managerF.createEpic(new Epic("Эпик_2", "Описание82", Status.NEW));

            // Напечатаем в консоли список всех задач и убедимся,что в файле сохранились такие же задачи
            System.out.println("Список всех эпиков в managerF:");
            printE(managerF.getEpics());
            System.out.println("________________________________________________________________");

            Duration duration1 = Duration.ofMinutes(120L);
            Duration duration2 = Duration.ofMinutes(180L);
            Duration duration3 = Duration.ofMinutes(240L);
            Duration duration4 = Duration.ofMinutes(300L);
            LocalDateTime dateTime1 = LocalDateTime.of(2025, 6, 18, 12, 10);
            LocalDateTime dateTime2 = LocalDateTime.of(2025, 6, 18, 23, 45);
            LocalDateTime dateTime3 = LocalDateTime.of(2025, 6, 19, 14, 20);
            LocalDateTime dateTime4 = LocalDateTime.of(2025, 6, 19, 8, 30);
            managerF.createSubtask(new Subtask(1, "Подзадача_1", "Описание22",
                    Status.DONE, dateTime1, duration1));
            managerF.createSubtask(new Subtask(1, "Подзадача_2", "Описание55",
                    Status.NEW, dateTime2, duration2));
            managerF.createSubtask(new Subtask(1, "Подзадача_3", "Описание16",
                    Status.DONE, dateTime3, duration3));
            managerF.createSubtask(new Subtask(2, "Подзадача_4", "Описание95",
                    Status.NEW, dateTime4, duration4));

            Duration duration5 = Duration.ofMinutes(390L);
            Duration duration6 = Duration.ofMinutes(420L);
            LocalDateTime dateTime5 = LocalDateTime.of(2025, 6, 20, 15, 10);
            LocalDateTime dateTime6 = LocalDateTime.of(2025, 6, 15, 23, 30);
            managerF.createTask(new Task("Задача_10", "Описание568",
                    Status.IN_PROGRESS, dateTime5, duration5));
            managerF.createTask(new Task("Задача_20", "Описание347",
                    Status.IN_PROGRESS, dateTime6, duration6));

            // Напечатаем в консоли список всех задач в хэш-таблицах
            System.out.println("Список всех задач в managerF:");
            printE(managerF.getEpics());
            printS(managerF.getSubtasks());
            printT(managerF.getTasks());
            System.out.println("________________________________________________________________");

            // Выведем в консоль список задач и подзадач в порядке приоритета
            System.out.println("Список задач в managerF, упорядоченных по времени выполнения:");
            printPT(managerF.getPrioritizedTasks());
            System.out.println("________________________________________________________________");

            // Проверим, что в файле и упорядоченном списке сохраняется состоянии задач после их обновления
            LocalDateTime dateTime7 = LocalDateTime.of(2029, 7, 1, 3, 30);
            LocalDateTime dateTime8 = LocalDateTime.of(2028, 10, 20, 14, 40);
            managerF.updateTask(new Task("Новая задача", "Описание№№№",
                    Status.IN_PROGRESS, 8, dateTime7, duration6));
            managerF.updateSubtask(new Subtask(1, "Новая", "Описание!",
                    Status.DONE, 5, dateTime8, duration3));

            System.out.println("Список после обновления задач (упорядоченный):");
            printPT(managerF.getPrioritizedTasks());
            System.out.println("________________________________________________________________");

            // Удалим задачу и подзадачу и проверим, что они не будет сохраняться в файле,
            // и не будут отображаться в упорядоченном списке задач
            managerF.removeByIdTask(7);
            managerF.removeByIdSubtask(3);

            // Удалим  эпик с его подзадачей и убедимся, что удален как сам эпик, так и его подзадача (id = 6)
            managerF.removeByIdEpic(2);

            System.out.println("Список после удаления (упорядоченный):");
            printPT(managerF.getPrioritizedTasks());
            System.out.println("________________________________________________________________");

            // Напечатаем в консоли список всех задач в хэш-таблицах и убедимся,что в файле сохранились такие же задачи
            System.out.println("Список всех задач в managerF:");
            printE(managerF.getEpics());
            printS(managerF.getSubtasks());
            printT(managerF.getTasks());
            System.out.println("________________________________________________________________");

            // Создадим задачу пересекающуюся по времени
            LocalDateTime dateTime9 = LocalDateTime.of(2029, 7, 1, 4, 50);
            managerF.createTask(new Task("Пересекающаяся", "Описание_735",
                    Status.IN_PROGRESS, dateTime9, duration2));
            System.out.println("________________________________________________________________");

            // Создадим второй экземпляр менеджера FileBackedTaskManager, загрузим в него из файла сохранение
            // Выведем состояние хранилища в консоль и убедимся в том, что задачи в обоих менеджерах идентичны
            FileBackedTaskManager managerF2;
            managerF2 = Managers.getDefaultManagerWithFile();
            if (managerF2 != null) {
                System.out.println("Список всех задач в managerF2:");
                printE(managerF2.getEpics());
                printS(managerF2.getSubtasks());
                printT(managerF2.getTasks());
                System.out.println("________________________________________________________________");

                // Проверим восстановился ли после загрузки из файла список упорядоченных по времени задач
                System.out.println("Список упорядоченных задач в managerF2:");
                printPT(managerF2.getPrioritizedTasks());
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

    static void printPT(List<Task> records) {
        System.out.println(records);
    }
}