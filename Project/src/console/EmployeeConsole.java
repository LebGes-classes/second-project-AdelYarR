package console;

import parsers.EmployeeParser;
import parsers.SellingPointParser;
import parsers.WarehouseParser;
import entities.storage.Storage;
import entities.storage.selling_point.SellingPoint;
import entities.storage.warehouse.Warehouse;
import entities.user.employee.Employee;

import static utils.ArraySearch.findById;

import java.util.Scanner;
import java.util.ArrayList;

public class EmployeeConsole {

    private static final Scanner scanner = new Scanner(System.in);

    // Метод для вывода информации о сотруднике
    public static void printInfo() {
        try {
            // Парсинг данных из JSON файла
            ArrayList<Employee> employees = EmployeeParser.parse();

            // Вывод в консоль заголовка таблицы
            System.out.println("Employee information");
            System.out.println("---------------------------------------------------------------------------------------------");

            String headerFormat = "%-5s | %-20s | %-15s | %-30s | %-5s%n";
            System.out.printf(headerFormat, "ID", "Full Name", "Phone Number", "Job", "Storage ID");
            System.out.println("---------------------------------------------------------------------------------------------");

            // Установление формата для вывода строк таблицы в консоль
            String rowFormat = "%-5d | %-20s | %-15s | %-30s | %-5d%n";

            // Поиск и вывод сотрудников в консоль
            for (Employee em : employees) {
                if (em.isWorking()) {
                    System.out.printf(rowFormat, em.getId(), em.getFullName(), em.getPhoneNumber(), em.getJob(), em.getStorageId());
                }
            }

            System.out.println("---------------------------------------------------------------------------------------------");

            System.out.println("Press any key to return: ");
        } catch (Exception e) {
            System.out.println("Error has occurred while parsing data: " + e.getMessage() + "\nPress any key to return: ");
        }
    }

    // Метод для изменения должности и места работы сотрудника
    public static void changePosition() {
        try {
            // Парсинг данных из JSON файлов
            ArrayList<Employee> employees = EmployeeParser.parse();
            ArrayList<Warehouse> warehouses = WarehouseParser.parse();
            ArrayList<SellingPoint> sellingPoints = SellingPointParser.parse();
            ArrayList<Storage> storages = new ArrayList<>();
            storages.addAll(warehouses);
            storages.addAll(sellingPoints);

            // Запрос ID сотрудника
            System.out.println("Enter the ID of the employee you want to change position: ");
            int employeeId = scanner.nextInt();
            scanner.nextLine();

            // Поиск сотрудника по ID
            Employee currEmployee = findById(employeeId, employees);
            if (currEmployee == null) {
                System.out.println("Couldn't find employee with the ID: " + employeeId + "\nPress any key to return: ");
                return;
            }

            // Запрос изменённой должности сотрудника
            System.out.println("Enter employee's new position: ");
            String job = scanner.nextLine();

            // Запрос ID изменённого места работы сотрудника
            System.out.println("Enter storage ID: ");
            int storageId = scanner.nextInt();
            scanner.nextLine();

            // Поиск места работы по ID
            Storage currStorage = findById(storageId, storages);
            if (currStorage == null) {
                System.out.println("Couldn't find storage with the ID: " + storageId + "\nPress any key to return: ");
                return;
            }

            // Изменение должности и места работы сотрудника
            currEmployee.manageEmployee(job, storageId);

            // Сохранение изменений в JSON файл
            boolean isSerialized = EmployeeParser.serialize(employees);
            if (isSerialized) {
                System.out.println("Employee with the ID: " + employeeId + " has successfully changed position.\nPress any key to return: ");
            } else {
                System.out.println("Error has occurred while serializing data.\nPress any key to return: ");
            }
        } catch (Exception e) {
            System.out.println("Error has occurred while parsing data: " + e.getMessage() + "\nPress any key to return: ");
        }
    }

    // Метод для найма сотрудника
    public static void hireEmployee() {
        try {
            // Парсинг данных из JSON файлов
            ArrayList<Employee> employees = EmployeeParser.parse();
            ArrayList<Warehouse> warehouses = WarehouseParser.parse();
            ArrayList<SellingPoint> sellingPoints = SellingPointParser.parse();
            ArrayList<Storage> storages = new ArrayList<>();
            storages.addAll(warehouses);
            storages.addAll(sellingPoints);

            // Создание нового ID для нанимаемого сотрудника
            int employeeId = employees.isEmpty() ? 1 : employees.getLast().getId() + 1;

            // Запрос полного имени нанимаемого сотрудника
            System.out.println("Enter employee's full name: ");
            String fullName = scanner.nextLine();

            // Запрос номера телефона нанимаемого сотрудника
            System.out.println("Enter employee's phone number: ");
            String phoneNumber = scanner.nextLine();

            // Запрос должности для нанимаемого сотрудника
            System.out.println("Enter employee's job: ");
            String job = scanner.nextLine();

            // Запрос ID места работы для нанимаемого сотрудника
            System.out.println("Enter storage ID: ");
            int storageId = scanner.nextInt();
            scanner.nextLine();

            // Поиск места работы по ID
            Storage currStorage = findById(storageId, storages);
            if (currStorage == null) {
                System.out.println("Couldn't find storage with the ID: " + storageId + "\nPress any key to return: ");
                return;
            }

            // Добавление нанимаемого сотрудника в список всех сотрудников
            Employee newEmployee = new Employee(employeeId, storageId, fullName, phoneNumber, job, true);
            employees.add(newEmployee);

            // Сохранение изменений в JSON файл
            boolean isSerialized = EmployeeParser.serialize(employees);
            if (isSerialized) {
                System.out.println("Employee with the ID: " + employeeId + " has been successfully hired.\nPress any key to return: ");
            } else {
                System.out.println("Error has occurred while serializing data.\nPress any key to return: ");
            }
        } catch (Exception e) {
            System.out.println("Error has occurred while parsing data: " + e.getMessage() + "\nPress any key to return: ");
        }
    }

    public static void fireEmployee() {;
        try {
            // Парсинг данных из JSON файла
            ArrayList<Employee> employees = EmployeeParser.parse();

            // Запрос ID сотрудника для увольнения
            System.out.println("Enter employee's ID you want to remove: ");
            int employeeId = scanner.nextInt();
            scanner.nextLine();

            // Поиск сотрудника по ID
            Employee currEmployee = findById(employeeId, employees);
            if (currEmployee == null) {
                System.out.println("Couldn't find employee with the ID: " + employeeId + "\nPress any key to return: ");
                return;
            }

            // Увольнение сотрудника
            currEmployee.fireEmployee();

            // Сохранение данных в JSON файл
            boolean isSerialized = EmployeeParser.serialize(employees);
            if (isSerialized) {
                System.out.println("Employee with the ID: " + employeeId + " has been successfully fired.\nPress any key to return: ");
            } else {
                System.out.println("Error has occurred while serializing data.\nPress any key to return: ");
            }
        } catch (Exception e) {
            System.out.println("Error has occurred while parsing data: " + e.getMessage() + "\nPress any key to return: ");
        }
    }
}
