import java.io.*;
import java.util.*;

public class Main {
    static class TaskItem {
        String text;

        TaskItem(String text) {
            this.text = text;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    static ArrayList<TaskItem> tasks = new ArrayList<>();
    static Scanner scanner = new Scanner(System.in, "UTF-8");
    static String filename = "tasks.txt";

    public static void main(String[] args) {
        System.out.println("=== ПРОГРАММА СПИСОК ДЕЛ ===");
        loadTasks();

        while (true) {
            System.out.println("\nМЕНЮ:");
            System.out.println("1. Показать задачи");
            System.out.println("2. Добавить задачу");
            System.out.println("3. Удалить задачу");
            System.out.println("0. Выход");
            System.out.print("Выберите действие: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    showTasks();
                    break;
                case "2":
                    addTask();
                    break;
                case "3":
                    deleteTask();
                    break;
                case "0":
                    System.out.println("Сохранение задач...");
                    saveTasks();
                    System.out.println("Программа завершена. До свидания!");
                    scanner.close();
                    return;
                default:
                    System.out.println("Ошибка! Введите число от 0 до 3");
            }
        }
    }

    static void showTasks() {
        if (tasks.isEmpty()) {
            System.out.println("\nСписок задач пуст.");
        } else {
            System.out.println("\nВАШИ ЗАДАЧИ:");
            for (int i = 0; i < tasks.size(); i++) {
                System.out.println((i + 1) + ". " + tasks.get(i));
            }
        }
    }

    static void addTask() {
        System.out.print("\nВведите новую задачу: ");
        String text = scanner.nextLine().trim();

        if (text.isEmpty()) {
            System.out.println("Задача не может быть пустой!");
        } else {
            tasks.add(new TaskItem(text));
            saveTasks();
            System.out.println("Задача успешно добавлена!");
        }
    }

    static void deleteTask() {
        if (tasks.isEmpty()) {
            System.out.println("\nНет задач для удаления.");
            return;
        }

        showTasks();
        System.out.print("\nВведите номер задачи для удаления: ");

        try {
            int num = Integer.parseInt(scanner.nextLine().trim());

            if (num < 1 || num > tasks.size()) {
                System.out.println("Неверный номер! Введите число от 1 до " + tasks.size());
            } else {
                String removedText = tasks.get(num - 1).text;
                tasks.remove(num - 1);
                saveTasks();
                System.out.println("Задача удалена: \"" + removedText + "\"");
            }
        } catch (NumberFormatException e) {
            System.out.println("Ошибка! Нужно ввести число.");
        }
    }

    static void saveTasks() {
        try {
            PrintWriter writer = new PrintWriter(new FileWriter(filename, false));
            for (TaskItem task : tasks) {
                writer.println(task.text);
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("Ошибка при сохранении файла: " + e.getMessage());
        }
    }

    static void loadTasks() {
        File file = new File(filename);

        if (!file.exists()) {
            System.out.println("Файл с задачами не найден. Будет создан новый.");
            return;
        }

        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String line;
            int count = 0;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) {
                    tasks.add(new TaskItem(line));
                    count++;
                }
            }
            reader.close();

            if (count > 0) {
                System.out.println("Загружено задач: " + count);
            }
        } catch (IOException e) {
            System.out.println("Ошибка при загрузке файла: " + e.getMessage());
        }
    }
}


