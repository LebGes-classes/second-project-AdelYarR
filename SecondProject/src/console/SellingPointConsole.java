package console;

import parsers.*;
import entities.product.Product;
import entities.storage.Cell;
import entities.storage.selling_point.SellingPoint;
import entities.storage.warehouse.Warehouse;
import entities.user.employee.Employee;

import java.util.ArrayList;
import java.util.Scanner;

import static utils.ArraySearch.findById;

public class SellingPointConsole {

    private static final Scanner scanner = new Scanner(System.in);

    // Метод для открытия нового пункта продаж
    public static void open() {
        try {
            // Парсинг данных из txt файлов
            ArrayList<SellingPoint> sellingPoints = SellingPointParser.parse();
            ArrayList<Warehouse> warehouses = WarehouseParser.parse();
            ArrayList<Employee> employees = EmployeeParser.parse();

            // Генерация ID для пункта продаж
            int sellingPointId = sellingPoints.isEmpty()
                    ? 1
                    : sellingPoints.stream()
                    .mapToInt(SellingPoint::getId)
                    .max()
                    .orElse(0) + 1;

            // Запрос адреса пункта продаж
            System.out.println("Enter address of the selling point: ");
            String address = scanner.nextLine();

            // Запрос ID ответственного лица
            System.out.println("Enter employee's ID: ");
            int employeeId = scanner.nextInt();
            scanner.nextLine();

            // Поиск сотрудника по ID
            Employee currEmployee = findById(employeeId, employees);
            if (currEmployee == null) {
                System.out.println("Couldn't find employee with the ID: " + employeeId + "\nPress any key to return: ");
                return;
            }

            // Запрос ID склада
            System.out.println("Enter warehouse ID: ");
            int warehouseId = scanner.nextInt();
            scanner.nextLine();

            // Поиск склада по ID
            Warehouse currWarehouse = findById(warehouseId, warehouses);
            if (currWarehouse == null) {
                System.out.println("Couldn't find warehouse with the ID: " + warehouseId + "\nPress any key to return: ");
                return;
            }

            // Создание нового пункта продаж
            SellingPoint sellingPoint = new SellingPoint(sellingPointId, warehouseId, employeeId, address, 0.00);
            sellingPoints.add(sellingPoint);

            // Сохранение изменений в txt файлы
            boolean isSerialized = SellingPointParser.serialize(sellingPoints);
            if (isSerialized) {
                System.out.println("Selling point with the ID: " + sellingPointId + " was successfully opened.\nPress any key to return: ");
            } else {
                System.out.println("Error has occurred while serializing data.\nPress any key to return: ");
            }
        } catch (Exception e) {
            System.out.println("Error has occurred while parsing data: " + e.getMessage() + "\nPress any key to return: ");
        }
    }

    // Метод для закрытия пункта продаж
    public static void close() {
        try {
            // Парсинг данных из txt файлов
            ArrayList<SellingPoint> sellingPoints = SellingPointParser.parse();
            ArrayList<Cell> cells = CellParser.parse();

            // Запрос ID пункта продаж
            System.out.println("Enter ID of the selling point you want to close: ");
            int sellingPointId = scanner.nextInt();
            scanner.nextLine();

            // Поиск пункта продаж по ID
            SellingPoint currSellingPoint = findById(sellingPointId, sellingPoints);
            if (currSellingPoint == null) {
                System.out.println("Couldn't find selling point with the ID: " + sellingPointId + "\nPress any key to return: ");
                return;
            }

            // Проверка наличия непустых ячеек, если такие имеются,
            // то закрыть пункт продаж не удастся
            boolean isEmpty = true;
            for (Cell sellingPointCell : currSellingPoint.getCells()) {
                for (Cell cell : cells) {
                    if (sellingPointCell.getId() == cell.getId()) {
                        if (cell.getQuantity() != 0) {
                            isEmpty = false;
                            break;
                        }
                    }
                }
            }

            if (!isEmpty) {
                System.out.println("The selling point you want to close has products inside. Move them before closing.\nPress any key to return: ");
                return;
            }

            // Удаление пункта продаж
            sellingPoints.remove(currSellingPoint);

            // Сохранение изменений в txt файлы
            boolean isSerialized = SellingPointParser.serialize(sellingPoints);
            if (isSerialized) {
                System.out.println("Selling point with the ID: " + sellingPointId + " was successfully closed.\nPress any key to return: ");
            } else {
                System.out.println("Error has occurred while serializing data.\nPress any key to return: ");
            }
        } catch (Exception e) {
            System.out.println("Error has occurred while parsing data: " + e.getMessage() + "\nPress any key to return: ");
        }
    }

    // Метод для перемещения товаров из пункта продаж в склад
    public static void moveProduct() {
        try {
            // Парсинг данных из txt файлов
            ArrayList<SellingPoint> sellingPoints = SellingPointParser.parse();
            ArrayList<Cell> cells = CellParser.parse();

            // Запрос ID пункта продаж
            System.out.println("Enter ID of the selling point: ");
            int sellingPointId = scanner.nextInt();
            scanner.nextLine();

            // Поиск пункта продаж по ID
            SellingPoint currSellingPoint = findById(sellingPointId, sellingPoints);
            if (currSellingPoint == null) {
                System.out.println("Couldn't find selling point with the ID: " + sellingPointId + "\nPress any key to return: ");
                return;
            }

            // Запрос ID товара
            System.out.println("Enter ID of the product you want to move back to warehouse: ");
            int productId = scanner.nextInt();
            scanner.nextLine();

            // Поиск ячейки текущего пункта продаж
            Cell currSellingPointCell = null;
            for (Cell cell : cells) {
                if (cell.getStorageId() == sellingPointId && cell.getProductId() == productId) {
                    currSellingPointCell = cell;
                }
            }

            if (currSellingPointCell == null) {
                System.out.println("Couldn't find selling point cell.\nPress any key to return: ");
                return;
            }

            // Поиск ячейки склада, зарезервированной под данный продукт,
            // в случае нахождении ячейки, из ячейки пункта продаж вычитается количество,
            // а к ячейке склада пополняется
            int warehouseCellId = -1;
            for (Cell cell : cells) {
                if (cell.getStorageId() == currSellingPoint.getWarehouseId() && cell.getProductId() == productId) {
                    if ((cell.getRemainingSpace()) >= currSellingPointCell.getQuantity()) {
                        cell.setQuantity(cell.getQuantity() + currSellingPointCell.getQuantity());
                        currSellingPointCell.setQuantity(0);
                        warehouseCellId = cell.getId();
                        break;
                    }
                }
            }

            if (warehouseCellId == -1) {
                System.out.println("Couldn't find a warehouse cell for the product.\nPress any key to return: ");
                return;
            }

            // Сохранение изменений в txt файлы
            boolean isCellSerialized = CellParser.serialize(cells);
            if (isCellSerialized) {
                System.out.println("The product was successfully moved to warehouse cell with the ID: " + warehouseCellId + ".\nPress any key to return: ");
            } else {
                System.out.println("Error has occurred while serializing data.\nPress any key to return: ");
            }
        } catch (Exception e) {
            System.out.println("Error has occurred while parsing data: " + e.getMessage() + "\nPress any key to return: ");
        }
    }

    // Метод для смены ответственного лица пункта продаж
    public static void changeResponsible() {
        try {
            // Парсинг данных из txt файлов
            ArrayList<SellingPoint> sellingPoints = SellingPointParser.parse();
            ArrayList<Employee> employees = EmployeeParser.parse();

            // Запрос ID пункта продаж
            System.out.println("Enter the ID of selling point: ");
            int sellingPointId = scanner.nextInt();
            scanner.nextLine();

            // Поиск пункта продаж по ID
            SellingPoint currSellingPoint = findById(sellingPointId, sellingPoints);
            if (currSellingPoint == null) {
                System.out.println("Couldn't find selling point with the ID: " + sellingPointId + "\nPress any key to return: ");
                return;
            }

            // Запрос ID ответственного лица
            System.out.println("Enter employee's ID: ");
            int employeeId = scanner.nextInt();
            scanner.nextLine();

            // Поиск сотрудника по ID
            Employee currEmployee = findById(employeeId, employees);
            if (currEmployee == null) {
                System.out.println("Couldn't find employee with the ID: " + employeeId + "\nPress any key to return: ");
                return;
            }

            // Смена ответственного лица у текущего пункта продаж
            currSellingPoint.setEmployeeId(employeeId);

            // Сохранение изменений в txt файлы
            boolean isSerialized = SellingPointParser.serialize(sellingPoints);
            if (isSerialized) {
                System.out.println("Selling point with the ID: " + sellingPointId + " has successfully changed the responsibility.\nPress any key to return: ");
            } else {
                System.out.println("Error has occurred while serializing data.\nPress any key to return: ");
            }
        } catch (Exception e) {
            System.out.println("Error has occurred while parsing data: " + e.getMessage() + "\nPress any key to return: ");
        }
    }

    // Метод для вывода информации о пункте продаж
    public static void printInfo() {
        try {
            // Парсинг данных из txt файлов
            ArrayList<SellingPoint> sellingPoints = SellingPointParser.parse();

            // Запрос ID пункта продаж
            System.out.println("Enter the ID of selling point: ");
            int sellingPointId = scanner.nextInt();
            scanner.nextLine();

            // Поиск пункта продаж по ID
            SellingPoint currSellingPoint = findById(sellingPointId, sellingPoints);
            if (currSellingPoint == null) {
                System.out.println("Couldn't find selling point with the ID: " + sellingPointId + "\nPress any key to return: ");
                return;
            }

            // Вывод в консоль заголовка таблицы
            System.out.println("Selling Point information");
            System.out.println("------------------------------------------------------------------------------------------------------");

            String headerFormat = "%-5s | %-15s | %-25s | %-50s%n";
            System.out.printf(headerFormat, "ID", "Warehouse ID", "Responsible Employee ID", "Address");
            System.out.println("------------------------------------------------------------------------------------------------------");

            // Установление формата таблицы и вывод информации о пункте продаж
            String rowFormat = "%-5d | %-15d | %-25d | %-50s%n";
            System.out.printf(rowFormat, currSellingPoint.getId(), currSellingPoint.getWarehouseId(), currSellingPoint.getEmployeeId(), currSellingPoint.getAddress());
            System.out.println("------------------------------------------------------------------------------------------------------");

            System.out.println("Press any key to return: ");
        } catch (Exception e) {
            System.out.println("Error has occurred while parsing data: " + e.getMessage() + "\nPress any key to return: ");
        }
    }

    // Метод для вывода информации о товарах доступных к закупке
    public static void printProductsInfo() {
        try {
            // Парсинг данных из txt файлов
            ArrayList<SellingPoint> sellingPoints = SellingPointParser.parse();
            ArrayList<Cell> cells = CellParser.parse();
            ArrayList<Product> products = ProductParser.parse();

            // Запрос ID пункта продаж
            System.out.println("Enter the ID of selling point: ");
            int sellingPointId = scanner.nextInt();
            scanner.nextLine();

            // Поиск пункта продаж по ID
            SellingPoint currSellingPoint = findById(sellingPointId, sellingPoints);
            if (currSellingPoint == null) {
                System.out.println("Couldn't find selling point with the ID: " + sellingPointId + "\nPress any key to return: ");
                return;
            }

            // Вывод в консоль заголовок таблицы
            System.out.println("Selling Point products information");
            System.out.println("----------------------------------------------------------------------------------------------------");

            String headerFormat = "%-5s | %-20s | %-20s | %-20s | %-10s | %-6s%n";
            System.out.printf(headerFormat, "ID", "Name", "Manufacturer", "Purchase Price", "Sale Price", "Amount");
            System.out.println("----------------------------------------------------------------------------------------------------");

            // Установление формата таблицы и поиск товаров в ячейках пункта продаж
            String rowFormat = "%-5d | %-20s | %-20s | %-20s | %-10s | %-6d%n";
            for (Cell cell : cells) {
                if (cell.getStorageId() == sellingPointId) {
                    for (Product product : products) {
                        if (cell.getProductId() == product.getId() && cell.getQuantity() != 0) {
                            System.out.printf(rowFormat, product.getId(), product.getName(), product.getManufacturer(), product.getPurchasePrice(), product.getSalePrice(), cell.getQuantity());
                        }
                    }
                }
            }

            System.out.println("----------------------------------------------------------------------------------------------------");

            System.out.println("Press any key to return: ");
        } catch (Exception e) {
            System.out.println("Error has occurred while parsing data: " + e.getMessage() + "\nPress any key to return: ");
        }
    }

    // Метод для вывода информации о доходности пункта продаж
    public static void printProfitInfo() {
        try {
            // Парсинг данных из txt файлов
            ArrayList<SellingPoint> sellingPoints = SellingPointParser.parse();

            // Запрос ID пункта продаж
            System.out.println("Enter the ID of selling point: ");
            int sellingPointId = scanner.nextInt();
            scanner.nextLine();

            // Поиск пункта продаж по ID
            SellingPoint currSellingPoint = findById(sellingPointId, sellingPoints);
            if (currSellingPoint == null) {
                System.out.println("Couldn't find selling point with the ID: " + sellingPointId + "\nPress any key to return: ");
                return;
            }

            // Вывод в консоль информации о доходности пункта продаж
            System.out.println("The profit of the selling point is " + currSellingPoint.getProfit() + ".\nPress any key to return: ");
        } catch (Exception e) {
            System.out.println("Error has occurred while parsing data: " + e.getMessage() + "\nPress any key to return: ");
        }
    }
}