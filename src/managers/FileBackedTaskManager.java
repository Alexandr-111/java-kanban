package managers;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import exceptions.*;
import tasks.*;

public class FileBackedTaskManager extends InMemoryTaskManager implements TaskManager {

    public static final String FILE_SAVE = "src\\resources\\save.csv";
    public static final Charset CS8 = StandardCharsets.UTF_8;
    public Path path;

    public FileBackedTaskManager(String str) {
        if (Objects.nonNull(str)) {
            this.path = Path.of(str);
        }
    }

    // Метод для записи строки
    private void writeLine(BufferedWriter bufferW, String line) {
        try {
            bufferW.write(line);
            bufferW.newLine();
            bufferW.flush();
        } catch (IOException e) {
            throw new ManagerSaveException();
        }
    }

    private void save() {
        try (BufferedWriter bufferW = new BufferedWriter(new FileWriter(path.toFile(), CS8))) {
            bufferW.write(TaskCSVFormat.getHeader());
            bufferW.newLine();
            bufferW.flush();
            this.storageEpics.values().stream()
                    .map(TaskCSVFormat::toStringEpic)
                    .forEach(line -> writeLine(bufferW, line));

            this.storageSubtasks.values().stream()
                    .map(TaskCSVFormat::toStringSubtask)
                    .forEach(line -> writeLine(bufferW, line));

            this.storageTasks.values().stream()
                    .map(TaskCSVFormat::toStringTask)
                    .forEach(line -> writeLine(bufferW, line));
        } catch (IOException exp) {
            throw new ManagerSaveException();
        }
    }

    @Override
    public boolean createTask(Task inputTask) {
        boolean isSuccessfulCompletion = super.createTask(inputTask);
        save();
        return isSuccessfulCompletion;
    }

    @Override
    public boolean createEpic(Epic inputEpic) {
        boolean isSuccessfulCompletion = super.createEpic(inputEpic);
        save();
        return isSuccessfulCompletion;
    }

    @Override
    public boolean createSubtask(Subtask inputSubtask) {
        boolean isSuccessfulCompletion = super.createSubtask(inputSubtask);
        save();
        return isSuccessfulCompletion;
    }

    @Override
    public boolean updateTask(Task inputTask) {
        boolean isSuccessfulCompletion = super.updateTask(inputTask);
        save();
        return isSuccessfulCompletion;
    }

    @Override
    public boolean updateEpic(Epic inputEpic) {
        boolean isSuccessfulCompletion = super.updateEpic(inputEpic);
        save();
        return isSuccessfulCompletion;
    }

    @Override
    public boolean updateSubtask(Subtask inputSubtask) {
        boolean isSuccessfulCompletion = super.updateSubtask(inputSubtask);
        save();
        return isSuccessfulCompletion;
    }

    @Override
    public boolean removeByIdTask(int search) {
        boolean isSuccessfulCompletion = super.removeByIdTask(search);
        save();
        return isSuccessfulCompletion;
    }

    @Override
    public int removeByIdEpic(int search) {
        int deletedSubtasks = super.removeByIdEpic(search);
        save();
        return deletedSubtasks;
    }

    @Override
    public boolean removeByIdSubtask(int search) {
        boolean isSuccessfulCompletion = super.removeByIdSubtask(search);
        save();
        return isSuccessfulCompletion;
    }

    public static void makeNewFile(Path path) throws ManagerSaveException {
        if (Files.notExists(path)) {
            try {
                Files.createFile(path);
            } catch (IOException e) {
                throw new ManagerSaveException();
            }
        }
    }

    public static FileBackedTaskManager loadFromFile() {
        FileBackedTaskManager managerF;
        try {
            managerF = new FileBackedTaskManager(FILE_SAVE);
            makeNewFile(managerF.path);
            if (Objects.nonNull(managerF.path)) {
                String str = Files.readString(managerF.path, CS8);
                String[] arraySave = str.split("\r\n");
                int max = 0;
                for (int i = 1; i < arraySave.length; i++) {
                    String line = arraySave[i];
                    String[] param = line.split(",");
                    if (param[1].equals("TASK")) {
                        Task task = StringToTaskConverter.fromStringToTask(param);
                        managerF.storageTasks.put(Integer.parseInt(param[0]), task);
                        // Добавляем в сет с задачами и подзачами упорядоченными по времени выполнения
                        managerF.priority.add(task);
                    } else if (param[1].equals("EPIC")) {
                        Epic epic = StringToTaskConverter.fromStringToEpic(param);
                        managerF.storageEpics.put(Integer.parseInt(param[0]), epic);
                    } else if (param[1].equals("SUBTASK")) {
                        Subtask subtask = StringToTaskConverter.fromStringToSubtask(param);
                        managerF.storageSubtasks.put(Integer.parseInt(param[0]), subtask);
                        // Добавляем id подзадачи в список подзадач ее эпика
                        Epic temp = managerF.storageEpics.get(Integer.parseInt(param[8]));
                        temp.addIdSub(Integer.parseInt(param[0]));
                        // Добавляем в сет с задачами и подзачами упорядоченными по времени выполнения
                        managerF.priority.add(subtask);
                    }
                    if (Integer.parseInt(param[0]) > max) {
                        max = Integer.parseInt(param[0]);
                    }
                }
                // Восстанавливаем значение поля counterId
                managerF.setCounterId(max);
            }
        } catch (IOException e) {
            throw new ManagerSaveException();
        }
        return managerF;
    }
}