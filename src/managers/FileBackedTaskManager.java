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

    private void save() {
        try (BufferedWriter bufferW = new BufferedWriter(new FileWriter(path.toFile(), CS8))) {
            bufferW.write(TaskCSVFormat.getHeader());
            bufferW.newLine();
            bufferW.flush();
            for (Map.Entry<Integer, Epic> element : this.storageEpics.entrySet()) {
                Epic epic = element.getValue();
                bufferW.write(TaskCSVFormat.toStringEpic(epic));
                bufferW.newLine();
                bufferW.flush();
            }
            for (Map.Entry<Integer, Subtask> element : this.storageSubtasks.entrySet()) {
                Subtask subtask = element.getValue();
                bufferW.write(TaskCSVFormat.toStringSubtask(subtask));
                bufferW.newLine();
                bufferW.flush();
            }
            for (Map.Entry<Integer, Task> element : this.storageTasks.entrySet()) {
                Task task = element.getValue();
                bufferW.write(TaskCSVFormat.toStringTask(task));
                bufferW.newLine();
                bufferW.flush();
            }
        } catch (IOException exp) {
            throw new ManagerSaveException();
        }
    }

    @Override
    public boolean createTask(Task inputTask) {
        boolean b = super.createTask(inputTask);
        save();
        return b;
    }

    @Override
    public boolean createEpic(Epic inputEpic) {
        boolean b = super.createEpic(inputEpic);
        save();
        return b;
    }

    @Override
    public boolean createSubtask(Subtask inputSubtask) {
        boolean b = super.createSubtask(inputSubtask);
        save();
        return b;
    }

    @Override
    public boolean updateTask(Task inputTask) {
        boolean b = super.updateTask(inputTask);
        save();
        return b;
    }

    @Override
    public boolean updateEpic(Epic inputEpic) {
        boolean b = super.updateEpic(inputEpic);
        save();
        return b;
    }

    @Override
    public boolean updateSubtask(Subtask inputSubtask) {
        boolean b = super.updateSubtask(inputSubtask);
        save();
        return b;
    }

    @Override
    public boolean removeByIdTask(int search) {
        boolean b = super.removeByIdTask(search);
        save();
        return b;
    }

    @Override
    public int removeByIdEpic(int search) {
        int b = super.removeByIdEpic(search);
        save();
        return b;
    }

    @Override
    public boolean removeByIdSubtask(int search) {
        boolean b = super.removeByIdSubtask(search);
        save();
        return b;
    }

    private static void makeNewFile(Path path) throws ManagerSaveException {
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
                    } else if (param[1].equals("EPIC")) {
                        Epic epic = StringToTaskConverter.fromStringToEpic(param);
                        managerF.storageEpics.put(Integer.parseInt(param[0]), epic);
                    } else if (param[1].equals("SUBTASK")) {
                        Subtask subtask = StringToTaskConverter.fromStringToSubtask(param);
                        managerF.storageSubtasks.put(Integer.parseInt(param[0]), subtask);
                        // Добавляем id подзадачи в список подзадач ее эпика
                        Epic temp = managerF.storageEpics.get(Integer.parseInt(param[5]));
                        temp.addIdSub(Integer.parseInt(param[0]));
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