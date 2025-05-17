package parsers;

import entities.storage.warehouse.Warehouse;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// Класс для парсинга данных из txt файла в объект
public class WarehouseParser {
    private static final String filePath = "src/files/warehouse.txt";

    public static ArrayList<Warehouse> parse() throws FileNotFoundException, NumberFormatException {
        File file = new File(filePath);

        Scanner sc = new Scanner(file);

        ArrayList<Warehouse> warehouses = new ArrayList<>();
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
                int employeeId = Integer.parseInt(data.get(1));
                String address = data.get(2);

                Warehouse warehouse = new Warehouse(id, employeeId, address);
                warehouses.add(warehouse);
            }
        }

        return warehouses;
    }

    public static boolean serialize(ArrayList<Warehouse> warehouses) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("[id] [employee_id] [address]");
            writer.newLine();
            for (Warehouse wh : warehouses) {
                writer.write(wh.toString());
                writer.newLine();
            }
            writer.flush();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
