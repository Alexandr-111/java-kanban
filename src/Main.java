import tasks.*;

import java.util.HashMap;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String name;
        String description;
        String statusTask;
        Status condition;
        int command;
        Scanner scanner = new Scanner(System.in);
        TaskManager manager = new TaskManager();
        while (true) {
            print();
            if (scanner.hasNextInt()) {
                command = scanner.nextInt();
                scanner.nextLine();
                if (command == 1) {
                    System.out.println("Введите название эпика");
                    name = scanner.nextLine();
                    System.out.println("Введите описание эпика");
                    description = scanner.nextLine();
                    condition = Status.NEW;
                    Epic inputEpic = new Epic(name, description, condition);
                    manager.createEpic(inputEpic);
                } else if (command == 2) {
                    System.out.println("Введите название задачи");
                    name = scanner.nextLine();
                    System.out.println("Введите описание задачи");
                    description = scanner.nextLine();
                    System.out.println("Присвойте нужный статус");
                    System.out.println("Варианты:\n" + Status.NEW + "\n" + Status.IN_PROGRESS + "\n" + Status.DONE);
                    statusTask = scanner.nextLine();
                    condition = Status.valueOf(statusTask);
                    Task inputTask = new Task(name, description, condition);
                    manager.createTask(inputTask);
                } else if (command == 3) {
                    System.out.println("Введите id эпика");
                    int idEpic = scanner.nextInt();
                    scanner.nextLine();
                    if (manager.checkIdEpic(idEpic)) {
                        System.out.println("Введите название подзадачи");
                        name = scanner.nextLine();
                        System.out.println("Введите описание подзадачи");
                        description = scanner.nextLine();
                        System.out.println("Присвойте нужный статус");
                        System.out.println("Возможные статусы:\n" + Status.NEW + "\n"
                                + Status.IN_PROGRESS + "\n" + Status.DONE);
                        statusTask = scanner.nextLine();
                        condition = Status.valueOf(statusTask);
                        Subtask inputSubtask = new Subtask(idEpic, name, description, condition);
                        manager.createSubtask(idEpic, inputSubtask);
                        System.out.println("Подзадача успешно добавлена");
                    } else {
                        System.out.println("Не существует эпика с таким id");
                    }
                } else if (command == 4) {
                    if (manager.isNoSubtask() && manager.isNoTask() && manager.isNoEpics()) {
                        System.out.println("В списке нет ни одной задачи");
                    } else {
                        System.out.println("Список всех задач");
                        printE(manager.getEpics());
                        printT(manager.getTasks());
                        printS(manager.getSubtasks());
                    }
                } else if (command == 5) {
                    if (manager.isNoSubtask() && manager.isNoTask() && manager.isNoEpics()) {
                        System.out.println("В списке нет ни одной задачи");
                    } else {
                        manager.removeAll();
                        System.out.println("Все задачи удалены");
                    }
                } else if (command == 6) {
                    int type;
                    System.out.println("Введите id задачи, которую нужно получить");
                    int search = scanner.nextInt();
                    scanner.nextLine();
                    type = manager.findOutClassObject(search);
                    if (type == 1) {
                        Epic a = manager.getByIdEpic(search);
                        System.out.println(a);
                    } else if (type == 2) {
                        Task a = manager.getByIdTask(search);
                        System.out.println(a);
                    } else if (type == 3) {
                        Subtask a = manager.getByIdSubtask(search);
                        System.out.println(a);
                    } else {
                        System.out.println("Задачи с таким идентификатором не существует");
                    }
                } else if (command == 7) {
                    System.out.println("Введите id задачи, которую нужно обновить");
                    int type;
                    int search = scanner.nextInt();
                    scanner.nextLine();
                    type = manager.findOutClassObject(search);
                    if (type == 1) {
                        System.out.println("Введите название эпика");
                        name = scanner.nextLine();
                        System.out.println("Введите описание эпика");
                        description = scanner.nextLine();
                        condition = Status.NEW;
                        Epic inputEpic = new Epic(name, description, condition);
                        manager.updateEpic(search, inputEpic);
                        System.out.println("Задача обновлена");
                    } else if (type == 2) {
                        System.out.println("Введите название задачи");
                        name = scanner.nextLine();
                        System.out.println("Введите описание задачи");
                        description = scanner.nextLine();
                        System.out.println("Присвойте нужный статус");
                        System.out.println("Возможные статусы:\n" + Status.NEW + "\n"
                                + Status.IN_PROGRESS + "\n" + Status.DONE);
                        statusTask = scanner.nextLine();
                        condition = Status.valueOf(statusTask);
                        Task inputTask = new Task(name, description, condition);
                        manager.updateTask(search, inputTask);
                        System.out.println("Задача обновлена");
                    } else if (type == 3) {
                        int idenEpic = manager.receiveIdenEpic(search);
                        System.out.println("Введите название подзадачи");
                        name = scanner.nextLine();
                        System.out.println("Введите описание подзадачи");
                        description = scanner.nextLine();
                        System.out.println("Присвойте нужный статус");
                        System.out.println("Варианты:\n" + Status.NEW + "\n"
                                + Status.IN_PROGRESS + "\n" + Status.DONE);
                        statusTask = scanner.nextLine();
                        condition = Status.valueOf(statusTask);
                        Subtask inputSubtask = new Subtask(idenEpic, name, description, condition);
                        manager.updateSubtask(search, inputSubtask, idenEpic);
                        System.out.println("Задача обновлена");
                    } else {
                        System.out.println("Задач с таким идентификатором не существует");
                    }
                } else if (command == 8) {
                    System.out.println("Введите id задачи, которую нужно удалить");
                    int type;
                    int search = scanner.nextInt();
                    scanner.nextLine();
                    type = manager.findOutClassObject(search);
                    if (type == 1) {
                      int b =  manager.removeByIdEpic(search);
                        System.out.println("Удален эпик и " + b + " подзадачи");
                    } else if (type == 2) {
                        manager.removeByIdTask(search);
                        System.out.println("Задача удалена");
                    } else if (type == 3) {
                        int idenEpic = manager.receiveIdenEpic(search);
                        manager.removeByIdSubtask(search, idenEpic);
                        System.out.println("Подзадача удалена");
                    } else {
                        System.out.println("Задачи с таким идентификатором не существует");
                    }
                } else if (command == 0) {
                    System.out.println("Программа завершена");
                    break;
                } else {
                    System.out.println("Такой команды нет.");
                }
            } else {
                System.out.println("Для выбора команды используйте только цифры");
                scanner.nextLine();
            }
        }
    }

    static void print() {
        System.out.println("---------------------------------------");
        System.out.println("Поехали!");
        System.out.println("Выберите опцию");
        System.out.println("1 - Создать новый эпик");
        System.out.println("2 - Создать новую задачу");
        System.out.println("3 - Добавить подзадачу в эпик");
        System.out.println("4 - Получить список всех задач");
        System.out.println("5 - Удалить все задачи");
        System.out.println("6 - Получить задачу по идентификатору");
        System.out.println("7 - Обновить задачу");
        System.out.println("8 - Удалить задачу по идентификатору");
        System.out.println("0 - Выйти из приложения");
        System.out.println("---------------------------------------");
    }

    static void printE(HashMap<Integer, Epic> storageEpics) {
        System.out.println(storageEpics);
    }

    static void printT(HashMap<Integer, Task> storageTasks) {
        System.out.println(storageTasks);
    }

    static void printS(HashMap<Integer, Subtask> storageSubtasks) {
        System.out.println(storageSubtasks);
    }
}