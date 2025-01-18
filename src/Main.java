import tasks.*;

import java.util.ArrayList;
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
                    int id = manager.makeNewId();
                    Epic inputEpic = new Epic(name, description, condition, id);
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
                    int id = manager.makeNewId();
                    Task inputTask = new Task(name, description, condition, id);
                    manager.createTask(inputTask);
                } else if (command == 3) {
                    System.out.println("Введите id эпика");
                    int idEpic = scanner.nextInt();
                    scanner.nextLine();
                    System.out.println("Введите название подзадачи");
                    name = scanner.nextLine();
                    System.out.println("Введите описание подзадачи");
                    description = scanner.nextLine();
                    System.out.println("Присвойте нужный статус");
                    System.out.println("Возможные статусы:\n" + Status.NEW + "\n"
                            + Status.IN_PROGRESS + "\n" + Status.DONE);
                    statusTask = scanner.nextLine();
                    condition = Status.valueOf(statusTask);
                    int id = manager.makeNewId();
                    Subtask inputSubtask = new Subtask(idEpic, name, description, condition, id);
                    boolean b = manager.createSubtask(inputSubtask);
                    if (b) {
                        System.out.println("Подзадача успешно добавлена");
                    } else {
                        System.out.println("Не существует эпика с таким id");
                    }
                } else if (command == 4) {
                    if (manager.emptyList()) {
                        System.out.println("В списке нет ни одной задачи");
                    } else {
                        System.out.println("Список всех задач");
                        printE(manager.getEpics());
                        printT(manager.getTasks());
                        printS(manager.getSubtasks());
                    }
                } else if (command == 5) {
                    if (manager.emptyList()) {
                        System.out.println("В списке нет ни одной задачи");
                    } else {
                        manager.removeEpics();
                        manager.removeTasks();
                        manager.removeSubtasks();
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
                        Epic inputEpic = new Epic(name, description, condition, search);
                        boolean b = manager.updateEpic(inputEpic);
                        if (b) {
                            System.out.println("Эпик успешно обновлен");
                        }
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
                        Task inputTask = new Task(name, description, condition, search);
                        boolean b = manager.updateTask(inputTask);
                        if (b) {
                            System.out.println("Задача успешно обновлена");
                        }
                    } else if (type == 3) {
                        int idEpic = manager.receiveIdEpic(search);
                        System.out.println("Введите название подзадачи");
                        name = scanner.nextLine();
                        System.out.println("Введите описание подзадачи");
                        description = scanner.nextLine();
                        System.out.println("Присвойте нужный статус");
                        System.out.println("Варианты:\n" + Status.NEW + "\n"
                                + Status.IN_PROGRESS + "\n" + Status.DONE);
                        statusTask = scanner.nextLine();
                        condition = Status.valueOf(statusTask);
                        Subtask inputSubtask = new Subtask(idEpic, name, description, condition, search);
                        boolean b = manager.updateSubtask(inputSubtask);
                        if (b) {
                            System.out.println("Подзадача успешно обновлена");
                        }
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
                        int y = manager.removeByIdEpic(search);
                        if (y == -1) {
                            System.out.println("Не удалось провести удаление");
                        } else {
                            System.out.println("Удален эпик и " + y + " подзадачи");
                        }
                    } else if (type == 2) {
                        boolean b = manager.removeByIdTask(search);
                        if (b) {
                            System.out.println("Задача успешно удалена");
                        }
                    } else if (type == 3) {
                        boolean b = manager.removeByIdSubtask(search);
                        if (b) {
                            System.out.println("Подзадача успешно удалена");
                        }
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
        System.out.println("-----------------------------------------");
        System.out.println("\tПоехали!");
        System.out.println("\tВыберите опцию");
        System.out.println("\t1 - Создать новый эпик");
        System.out.println("\t2 - Создать новую задачу");
        System.out.println("\t3 - Добавить подзадачу в эпик");
        System.out.println("\t4 - Получить список всех задач");
        System.out.println("\t5 - Удалить все задачи");
        System.out.println("\t6 - Получить задачу по идентификатору");
        System.out.println("\t7 - Обновить задачу");
        System.out.println("\t8 - Удалить задачу по идентификатору");
        System.out.println("\t0 - Выйти из приложения");
        System.out.println("-----------------------------------------");
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