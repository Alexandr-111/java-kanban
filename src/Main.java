import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String name;
        String description;
        String statusTask;
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
                    manager.createEpic(name, description);
                } else if (command == 2) {
                    System.out.println("Введите название задачи");
                    name = scanner.nextLine();
                    System.out.println("Введите описание задачи");
                    description = scanner.nextLine();
                    System.out.println("Присвойте нужный статус");
                    System.out.println("Варианты:\n" + Status.NEW + "\n" + Status.IN_PROGRESS + "\n" + Status.DONE);
                    statusTask = scanner.nextLine();
                    manager.createTask(name, description, statusTask);
                } else if (command == 3) {
                    System.out.println("Введите id эпика");
                    int idEpic = scanner.nextInt();
                    scanner.nextLine();
                    System.out.println("Введите название подзадачи");
                    name = scanner.nextLine();
                    System.out.println("Введите описание подзадачи");
                    description = scanner.nextLine();
                    System.out.println("Присвойте нужный статус");
                    System.out.println("Варианты:\n" + Status.NEW + "\n" + Status.IN_PROGRESS + "\n" + Status.DONE);
                    statusTask = scanner.nextLine();
                    boolean b = manager.createSubtask(idEpic, name, description, statusTask);
                    if (b) {
                        System.out.println("Добавлена успешно");
                    } else {
                        System.out.println("Не существует эпика с таким id");
                    }
                } else if (command == 4) {
                    if (manager.isNoElements()) {
                        System.out.println("В списке нет ни одной задачи");
                    } else {
                        manager.getListTasks();
                    }
                } else if (command == 5) {
                    if (manager.isNoElements()) {
                        System.out.println("В списке нет ни одной задачи");
                    } else {
                        manager.removeAll();
                        System.out.println("Все задачи удалены");
                    }
                } else if (command == 6) {
                    System.out.println("Введите id задачи, которую нужно получить");
                    int search = scanner.nextInt();
                    scanner.nextLine();
                    if (manager.checkId(search)) {
                        Task a = manager.getById(search);
                        System.out.println(a);
                    } else {
                        System.out.println("Задачи с таким идентификатором не существует");
                    }
                } else if (command == 7) {
                    System.out.println("Введите id задачи, которую нужно обновить");
                    int search = scanner.nextInt();
                    scanner.nextLine();
                    if (manager.checkId(search)) {
                        String type = manager.findOutClassObject(search);
                        if (type.equals("Эпик")) {
                            System.out.println("Введите название эпика");
                            name = scanner.nextLine();
                            System.out.println("Введите описание эпика");
                            description = scanner.nextLine();
                            manager.updateEpic(search, name, description);

                        } else if (type.equals("Задача")) {
                            System.out.println("Введите название задачи");
                            name = scanner.nextLine();
                            System.out.println("Введите описание задачи");
                            description = scanner.nextLine();
                            System.out.println("Присвойте нужный статус");
                            System.out.println("Варианты:\n" + Status.NEW + "\n" + Status.IN_PROGRESS + "\n" + Status.DONE);
                            statusTask = scanner.nextLine();
                            manager.updateTask(search, name, description, statusTask);

                        } else if (type.equals("Подзадача")) {
                            System.out.println("Введите название подзадачи");
                            name = scanner.nextLine();
                            System.out.println("Введите описание подзадачи");
                            description = scanner.nextLine();
                            System.out.println("Присвойте нужный статус");
                            System.out.println("Варианты:\n" + Status.NEW + "\n" + Status.IN_PROGRESS + "\n" + Status.DONE);
                            statusTask = scanner.nextLine();
                            manager.updateSubtask(search, name, description, statusTask);
                        }
                    } else {
                        System.out.println("Задач с таким идентификатором не существует");
                    }
                } else if (command == 8) {
                    System.out.println("Введите id задачи, которую нужно удалить");
                    int search = scanner.nextInt();
                    scanner.nextLine();
                    if (manager.checkId(search)) {
                        manager.removeById(search);
                        System.out.println("Задача удалена");
                    } else {
                        System.out.println("Задач с таким идентификатором не существует");
                    }
                } else if (command == 0) {
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
}