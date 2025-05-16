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

public class CellParser {

    private static final String filePath = "src/files/cell.txt";

    public static ArrayList<Cell> parse() throws FileNotFoundException, NumberFormatException {
        File file = new File(filePath);

        Scanner sc = new Scanner(file);

        ArrayList<Cell> cells = new ArrayList<>();
        boolean isStart = true;
        while (sc.hasNextLine()) {
            if (isStart) {
                sc.nextLine();
                isStart = false;
            } else {
                String input = sc.nextLine().trim();
                Pattern pattern = Pattern.compile("\\[(.*?)\\]");
                Matcher matcher = pattern.matcher(input);

                ArrayList<String> data = new ArrayList<>();
                while (matcher.find()) {
                    data.add(matcher.group(1));
                }

                int id = Integer.parseInt(data.get(0));
                int storageId = Integer.parseInt(data.get(1));
                int productId = Integer.parseInt(data.get(2));
                int capacity = Integer.parseInt(data.get(3));
                int quantity = Integer.parseInt(data.get(4));

                Cell cell = new Cell(id, storageId, productId, capacity, quantity);
                cells.add(cell);
            }
        }

        return cells;
    }

    public static boolean serialize(ArrayList<Cell> cells) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("[id] [storage_id] [product_id] [capacity] [quantity]");
            writer.newLine();
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
