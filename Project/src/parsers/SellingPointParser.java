package parsers;

import entities.storage.selling_point.SellingPoint;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SellingPointParser {
    private static final String filePath = "src/files/selling_point.txt";

    public static ArrayList<SellingPoint> parse() throws FileNotFoundException, NumberFormatException {
        File file = new File(filePath);

        Scanner sc = new Scanner(file);

        ArrayList<SellingPoint> sellingPoints = new ArrayList<>();
        boolean isStart = true;
        while (sc.hasNextLine()) {
            if (isStart) {
                String input = sc.nextLine().trim();
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
                int warehouseId = Integer.parseInt(data.get(1));
                int employeeId = Integer.parseInt(data.get(2));
                String address = data.get(3);
                double profit = Double.parseDouble(data.get(4));

                SellingPoint sellingPoint = new SellingPoint(id, warehouseId, employeeId, address, profit);
                sellingPoints.add(sellingPoint);
            }
        }

        return sellingPoints;
    }

    public static boolean serialize(ArrayList<SellingPoint> sellingPoints) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("[id] [warehouse_id] [employee_id] [address] [profit]");
            writer.newLine();
            for (SellingPoint sp : sellingPoints) {
                writer.write(sp.toString());
                writer.newLine();
            }
            writer.flush();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
