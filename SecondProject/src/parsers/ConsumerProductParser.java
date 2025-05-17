package parsers;

import entities.user.consumer.ConsumerProduct;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// Класс для парсинга данных из txt файла в объект
public class ConsumerProductParser {

    private static final String filePath = "src/files/consumer_product.txt";

    public static ArrayList<ConsumerProduct> parse() throws FileNotFoundException, NumberFormatException {
        File file = new File(filePath);

        Scanner sc = new Scanner(file);

        ArrayList<ConsumerProduct> consumerProducts = new ArrayList<>();
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
                int consumerId = Integer.parseInt(data.get(1));
                int productId = Integer.parseInt(data.get(2));
                int quantity = Integer.parseInt(data.get(3));

                ConsumerProduct consumerProduct = new ConsumerProduct(id, consumerId, productId, quantity);
                consumerProducts.add(consumerProduct);
            }
        }

        return consumerProducts;
    }

    public static boolean serialize(ArrayList<ConsumerProduct> consumerProducts) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("[id] [consumer_id] [product_id] [quantity]");
            writer.newLine();
            for (ConsumerProduct consumerProduct : consumerProducts) {
                writer.write(consumerProduct.toString());
                writer.newLine();
            }
            writer.flush();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
