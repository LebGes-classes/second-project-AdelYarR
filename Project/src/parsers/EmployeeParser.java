package parsers;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import entities.user.employee.Employee;

public class EmployeeParser {

    private static final String filePath = "src/files/employee.txt";

    public static ArrayList<Employee> parse() throws FileNotFoundException, NumberFormatException {
        File file = new File(filePath);

        Scanner sc = new Scanner(file);

        ArrayList<Employee> employees = new ArrayList<>();
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
                String fullName = data.get(2);
                String phoneNumber = data.get(3);
                String job = data.get(4);
                boolean isWork = Integer.parseInt(data.get(5)) == 1;

                Employee employee = new Employee(id, storageId, fullName, phoneNumber, job, isWork);
                employees.add(employee);
            }
        }

        return employees;
    }

    public static boolean serialize(ArrayList<Employee> employees) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("[id] [storage_id] [fullname] [phone_number] [job] [is_working]");
            writer.newLine();
            for (Employee em : employees) {
                writer.write(em.toString());
                writer.newLine();
            }
            writer.flush();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
