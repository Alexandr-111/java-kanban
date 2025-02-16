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
        for (int i = 1; i < 3; i++) {
            manager.createTask(new Task("Задача_" + i, "Описание_" + i, Status.NEW));
        }
        System.out.println("Список всех задач:");
        printE(manager.getEpics());
        printT(manager.getTasks());
        printS(manager.getSubtasks());

        // Проверяем, что в истории просмотров остается только
        // один просмотр одной и той же задачи
        System.out.println(manager.getByIdEpic(1));
        System.out.println(manager.getByIdEpic(3));
        System.out.println(manager.getByIdTask(10));
        System.out.println(manager.getByIdEpic(1));
        System.out.println(manager.getByIdSubtask(5));
        System.out.println(manager.getByIdSubtask(4));
        System.out.println(manager.getByIdSubtask(9));
        System.out.println(manager.getByIdSubtask(5));

        //   Проверяем, что в истории просмотров остается состояние задачи
        //   именно на момент последнего просмотра, а не предыдущего.
        manager.updateTask(new Task("Новая задача", "Новое описание", Status.IN_PROGRESS, 10));
        System.out.println(manager.getByIdTask(10));
        System.out.println("История просмотров:");
        System.out.println(manager.getBrowsingHistory());

        //Удалим задачу, которая есть в истории, и проверим, что при печати она не будет выводиться
        manager.removeByIdTask(10);
        System.out.println("История просмотров:");
        System.out.println(manager.getBrowsingHistory());

        //Удалим подзадачу, которая есть в истории, и проверим, что при печати она не будет выводиться
        manager.removeByIdSubtask(9);
        System.out.println("История просмотров:");
        System.out.println(manager.getBrowsingHistory());

        // Удалим  эпик с двумя его подзадачами и убедимся, что из истории
        // удалился как сам эпик, так и все его подзадачи (id = 4 и id = 5)
        manager.removeByIdEpic(1);
        System.out.println("История просмотров:");
        System.out.println(manager.getBrowsingHistory());
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