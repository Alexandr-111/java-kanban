import tasks.*;
import managers.*;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {

        TaskManager manager = Managers.getDefault();
        for (int i = 1; i < 4; i++) {
            manager.createEpic(new Epic("Эпик_" + i, "Описание_" + i, Status.NEW));
        }
        for (int i = 1; i < 4; i++) {
            manager.createSubtask(new Subtask(i, "Подзадача", "Описание_" + i, Status.DONE));
            manager.createSubtask(new Subtask(i, "Подзадача", "Описание_" + (i + 2), Status.NEW));
        }
        for (int i = 1; i < 4; i++) {
            manager.createTask(new Task("Задача_" + i, "Описание_" + i, Status.NEW));
        }
        System.out.println("Список всех задач:");
        printE(manager.getEpics());
        printT(manager.getTasks());
        printS(manager.getSubtasks());

        System.out.println(manager.getByIdEpic(1));
        System.out.println(manager.getByIdEpic(3));
        System.out.println(manager.getByIdTask(10));
        System.out.println(manager.getByIdTask(12));
        System.out.println(manager.getByIdSubtask(7));
        System.out.println(manager.getByIdSubtask(9));

        // Проверяем, что в истории просмотров остаются оба состояния задачи,
        // предыдущая версия и версия после обновления

        manager.updateTask(new Task("Новая задача", "Новое описание", Status.IN_PROGRESS, 10));
        System.out.println(manager.getByIdTask(10));
        System.out.println("История просмотров:");
        System.out.println(manager.getManagerH().getHistory());
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