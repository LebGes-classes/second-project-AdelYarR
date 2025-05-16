package parsers;

import entities.product.Product;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.Collections;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProductParser {
    private static final String filePath = "src/files/product.txt";

    public static ArrayList<Product> parse() throws FileNotFoundException, NumberFormatException {
        File file = new File(filePath);

        Scanner sc = new Scanner(file);

        ArrayList<Product> products = new ArrayList<>();
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
                String name = data.get(1);
                String manufacturer = data.get(2);
                double purchasePrice = Double.parseDouble(data.get(3));
                double salePrice = Double.parseDouble(data.get(4));

                Product product = new Product(id, name, manufacturer, purchasePrice, salePrice);
                products.add(product);
            }
        }

        Collections.sort(products, new Comparator<Product>() {
           public int compare(Product p1, Product p2) {
               return Integer.compare(p1.getId(), p2.getId());
           }
        });
        return products;
    }

    public static boolean serialize(ArrayList<Product> products) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("[id] [name] [manufacturer] [purchase_price] [sale_price]");
            writer.newLine();
            for (Product pr : products) {
                writer.write(pr.toString());
                writer.newLine();
            }
            writer.flush();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
