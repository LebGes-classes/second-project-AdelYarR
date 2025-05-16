package parsers;

import entities.user.consumer.Consumer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConsumerParser {
    private static final String filePath = "src/files/consumer.txt";

    public static ArrayList<Consumer> parse() throws FileNotFoundException, NumberFormatException {
        File file = new File(filePath);

        Scanner sc = new Scanner(file);

        ArrayList<Consumer> consumers = new ArrayList<>();
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
                String fullName = data.get(1);
                String phoneNumber = data.get(2);

                Consumer consumer = new Consumer(id, fullName, phoneNumber);
                consumers.add(consumer);
            }
        }

        return consumers;
    }

    public static boolean serialize(ArrayList<Consumer> consumers) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("[id] [fullname] [phone_number]");
            writer.newLine();
            for (Consumer consumer : consumers) {
                writer.write(consumer.toString());
                writer.newLine();
            }
            writer.flush();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
