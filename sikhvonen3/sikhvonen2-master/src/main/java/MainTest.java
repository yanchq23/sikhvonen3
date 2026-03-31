import org.junit.jupiter.api.*;
import java.io.*;

    @Test
    void showTasks_WhenEmpty_ShowsMessage() {
        Main.showTasks();
        assertTrue(out.toString().contains("Список задач пуст."));
    }

    @Test
    void showTasks_WithTasks_ShowsNumberedList() {
        Main.tasks.add(new Main.TaskItem("Купить хлеб"));
        Main.tasks.add(new Main.TaskItem("Позвонить врачу"));
        Main.showTasks();
        String output = out.toString();
        assertTrue(output.contains("1. Купить хлеб") && output.contains("2. Позвонить врачу"));
    }

    @Test
    void addTask_EmptyInput_ShowsErrorAndDoesNotAdd() {
        setInput("\n");
        Main.addTask();
        assertTrue(Main.tasks.isEmpty() && out.toString().contains("Задача не может быть пустой!"));
    }
//90
    @Test
    void addTask_ValidInput_AddsTaskAndSavesToFile() throws IOException {
        setInput("Полить цветы\n");
        Main.addTask();
        String fileContent = Files.readString(tempFile);
        assertTrue(Main.tasks.size() == 1 &&
                "Полить цветы".equals(Main.tasks.get(0).text) &&
                fileContent.contains("Полить цветы"));
    }

    @Test
    void deleteTask_WhenListEmpty_ShowsMessage() {
        Main.deleteTask();
        assertTrue(out.toString().contains("Нет задач для удаления."));
    }

    @Test
    void deleteTask_InvalidNumberFormat_ShowsError() {
        Main.tasks.add(new Main.TaskItem("Задача"));
        setInput("abc\n");
        Main.deleteTask();
        assertTrue(out.toString().contains("Ошибка! Нужно ввести число.") && Main.tasks.size() == 1);
    }

    @Test
    void deleteTask_NumberOutOfRange_ShowsError() {
        Main.tasks.add(new Main.TaskItem("Задача"));
        setInput("5\n");
        Main.deleteTask();
        assertTrue(out.toString().contains("Неверный номер! Введите число от 1 до 1") && Main.tasks.size() == 1);
    }

    @Test
    void deleteTask_ValidNumber_RemovesTaskAndUpdatesFile() throws IOException {
        Main.tasks.add(new Main.TaskItem("Удалить"));
        Main.tasks.add(new Main.TaskItem("Оставить"));
        setInput("1\n");
        Main.deleteTask();
        String fileContent = Files.readString(tempFile);
        assertTrue(Main.tasks.size() == 1 &&
                "Оставить".equals(Main.tasks.get(0).text) &&
                !fileContent.contains("Удалить") &&
                fileContent.contains("Оставить"));
    }

    @Test
    void saveTasks_WritesAllTasksToFile() throws IOException {
        Main.tasks.add(new Main.TaskItem("Первая"));
        Main.tasks.add(new Main.TaskItem("Вторая"));
        Main.saveTasks();
        String[] lines = Files.readString(tempFile).split("\n");
        assertArrayEquals(new String[]{"Первая", "Вторая"}, lines);
    }

    @Testvoid loadTasks_WhenFileNotExists_ShowsMessageAndStartsEmpty() {
        tempFile.toFile().delete();
        Main.loadTasks();
        assertTrue(Main.tasks.isEmpty() && out.toString().contains("Файл с задачами не найден. Будет создан новый."));
    }

    @Test
    void loadTasks_WithData_LoadsOnlyNonEmptyLines() throws IOException {
        Files.writeString(tempFile, "Запись 1\nЗапись 2\n\nЗапись 3\n");
        Main.loadTasks();
        assertEquals(3, Main.tasks.size());
        assertEquals("Запись 1", Main.tasks.get(0).text);
        assertEquals("Запись 2", Main.tasks.get(1).text);
        assertEquals("Запись 3", Main.tasks.get(2).text);
    }

    @Test
    void saveTasks_WhenIOException_PrintsError() {
        Main.filename = tempFile.getParent().toString();
        Main.tasks.add(new Main.TaskItem("Задача"));
        Main.saveTasks();
        assertTrue(out.toString().contains("Ошибка при сохранении файла"));
    }

    @Test
    void loadTasks_WhenIOException_PrintsError() {
        File f = tempFile.toFile();
        if (f.setReadable(false)) {
            Main.loadTasks();
            assertTrue(out.toString().contains("Ошибка при загрузке файла"));
            f.setReadable(true);
        } else {
            assertTrue(true);
        }
    }

    @Test
    void taskItemToString_ReturnsItsText() {
        Main.TaskItem item = new Main.TaskItem("Сходить в магазин");
        assertEquals("Сходить в магазин", item.toString());
    }
}