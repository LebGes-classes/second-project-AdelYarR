package parsers;

import entities.storage.Cell;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// Класс для парсинга и сериализации данных из txt файла в объект и обратно
public class CellParser {

    private static final String filePath = "src/files/cell.txt";

    // Метод для парсинга данных
    public static ArrayList<Cell> parse() throws FileNotFoundException, NumberFormatException {
        File file = new File(filePath);

        Scanner sc = new Scanner(file);

        ArrayList<Cell> cells = new ArrayList<>();
        // Переменная isStart требуется для того, чтобы не обрабатывать заголовок txt файла
        boolean isStart = true;
        // Читаем строки txt файла
        while (sc.hasNextLine()) {
            if (isStart) {
                // Пропуск заголовка
                sc.nextLine();
                isStart = false;
            } else {
                String input = sc.nextLine().trim();

                // Создание шаблона для извлечения информации из формата [...] [...] ...
                Pattern pattern = Pattern.compile("\\[(.*?)\\]");
                Matcher matcher = pattern.matcher(input);

                ArrayList<String> data = new ArrayList<>();
                while (matcher.find()) {
                    data.add(matcher.group(1));
                }

                // Присвоение полученных данных в свои типы
                int id = Integer.parseInt(data.get(0));
                int storageId = Integer.parseInt(data.get(1));
                int productId = Integer.parseInt(data.get(2));
                int capacity = Integer.parseInt(data.get(3));
                int quantity = Integer.parseInt(data.get(4));

                // Создание и добавление нового объекта в список
                Cell cell = new Cell(id, storageId, productId, capacity, quantity);
                cells.add(cell);
            }
        }

        return cells;
    }

    // Метод для сериализации данных
    public static boolean serialize(ArrayList<Cell> cells) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            // Запись заголовка файла
            writer.write("[id] [storage_id] [product_id] [capacity] [quantity]");
            writer.newLine();

            // Запись данных каждой ячейки
            for (Cell cell : cells) {
                writer.write(cell.toString());
                writer.newLine();
            }
            writer.flush();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
