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

public class WarehouseConsole {

    private static final Scanner scanner = new Scanner(System.in);

    // Метод для открытия нового склада
    public static void open() {
        try {
            // Парсинг данных из txt файлов
            ArrayList<Warehouse> warehouses = WarehouseParser.parse();
            ArrayList<Employee> employees = EmployeeParser.parse();

            // Генерация ID для склада
            int warehouseId = warehouses.isEmpty()
                    ? 1
                    : warehouses.stream()
                    .mapToInt(Warehouse::getId)
                    .max()
                    .orElse(0) + 1;

            // Запрос адреса склада
            System.out.println("Enter address of the warehouse: ");
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

            // Создание нового склада
            Warehouse warehouse = new Warehouse(warehouseId, employeeId, address);
            warehouses.add(warehouse);

            // Сохранение изменений в txt файлы
            boolean isSerialized = WarehouseParser.serialize(warehouses);
            if (isSerialized) {
                System.out.println("Warehouse with the ID: " + warehouseId + " was successfully opened.\nPress any key to return: ");
            } else {
                System.out.println("Error has occurred while serializing data.\nPress any key to return: ");
            }
        } catch (Exception e) {
            System.out.println("Error has occurred while parsing data: " + e.getMessage() + "\nPress any key to return: ");
        }
    }

    // Метод для закрытия склада
    public static void close() {
        try {
            // Парсинг данных из txt файлов
            ArrayList<Warehouse> warehouses = WarehouseParser.parse();
            ArrayList<SellingPoint> sellingPoints = SellingPointParser.parse();
            ArrayList<Cell> cells = CellParser.parse();

            // Запрос ID склада
            System.out.println("Enter ID of the warehouse you want to close: ");
            int warehouseId = scanner.nextInt();
            scanner.nextLine();

            // Поиск склада по ID
            Warehouse currWarehouse = findById(warehouseId, warehouses);
            if (currWarehouse == null) {
                System.out.println("Couldn't find warehouse with the ID: " + warehouseId + "\nPress any key to return: ");
                return;
            }

            // Проверка наличия непустых ячеек, если такие имеются,
            // то закрыть склад не удастся
            boolean isEmpty = true;
            for (Cell warehouseCell : currWarehouse.getCells()) {
                for (Cell cell : cells) {
                    if (warehouseCell.getId() == cell.getId()) {
                        if (cell.getQuantity() != 0) {
                            isEmpty = false;
                            break;
                        }
                    }
                }
            }

            if (!isEmpty) {
                System.out.println("The warehouse you want to close has products inside. Move them before closing.\nPress any key to return: ");
                return;
            }

            // Удаление склада из списка всех складов
            warehouses.remove(currWarehouse);

            // Сохранение изменений в txt файлы
            boolean isSerialized = WarehouseParser.serialize(warehouses);
            if (isSerialized) {
                System.out.println("Warehouse with the ID: " + warehouseId + " was successfully closed.\nPress any key to return: ");
            } else {
                System.out.println("Error has occurred while serializing data.\nPress any key to return: ");
            }
        } catch (Exception e) {
            System.out.println("Error has occurred while parsing data: " + e.getMessage() + "\nPress any key to return: ");
        }
    }

    // Метод для перемещения товаров из склада в пункт продаж
    public static void moveProduct() {
        try {
            // Парсинг данных из txt файлов
            ArrayList<Warehouse> warehouses = WarehouseParser.parse();
            ArrayList<Cell> cells = CellParser.parse();

            // Запрос ID склада
            System.out.println("Enter ID of the warehouse: ");
            int warehouseId = scanner.nextInt();
            scanner.nextLine();

            // Поиск склада по ID
            Warehouse currWarehouse = findById(warehouseId, warehouses);
            if (currWarehouse == null) {
                System.out.println("Couldn't find warehouse with the ID: " + warehouseId + "\nPress any key to return: ");
                return;
            }

            // Запрос ID пункта продаж
            System.out.println("Enter ID of the selling point you want to move product to: ");
            int sellingPointId = scanner.nextInt();
            scanner.nextLine();

            // Поиск пункта продаж по ID
            SellingPoint currSellingPoint = null;
            for (SellingPoint sellingPoint : currWarehouse.getSellingPoints()) {
                if (sellingPoint.getId() == sellingPointId) {
                    currSellingPoint = sellingPoint;
                    break;
                }
            }

            if (currSellingPoint == null) {
                System.out.println("Couldn't find selling point with the ID: " + sellingPointId + "\nPress any key to return: ");
                return;
            }

            // Запрос ID продукта
            System.out.println("Enter ID of the product you want to move to selling point: ");
            int productId = scanner.nextInt();
            scanner.nextLine();

            // Поиск ячейки текущего склада
            Cell currWarehouseCell = null;
            for (Cell cell : cells) {
                if (cell.getStorageId() == warehouseId && cell.getProductId() == productId) {
                    currWarehouseCell = cell;
                    break;
                }
            }

            if (currWarehouseCell == null) {
                System.out.println("Couldn't find warehouse cell.\nPress any key to return: ");
                return;
            }

            // Поиск ячейки пункта продаж, зарезервированной под данный продукт,
            // в случае нахождении ячейки, из ячейки склада вычитается количество,
            // а к ячейке пункта продаж пополняется
            int sellingPointCellId = -1;
            for (Cell cell : cells) {
                if (cell.getStorageId() == sellingPointId && cell.getProductId() == productId) {
                    if ((cell.getRemainingSpace()) >= currWarehouseCell.getQuantity()) {
                        cell.setQuantity(cell.getQuantity() + currWarehouseCell.getQuantity());
                        currWarehouseCell.setQuantity(0);
                        sellingPointCellId = cell.getId();
                        break;
                    }
                }
            }

            if (sellingPointCellId == -1) {
                System.out.println("Couldn't find a selling point cell for the product.\nPress any key to return: ");
                return;
            }

            // Сохранение изменений в txt файлы
            boolean isCellSerialized = CellParser.serialize(cells);
            if (isCellSerialized) {
                System.out.println("The product was successfully moved to selling point cell with the ID: " + sellingPointCellId + ".\nPress any key to return: ");
            } else {
                System.out.println("Error has occurred while serializing data.\nPress any key to return: ");
            }
        } catch (Exception e) {
            System.out.println("Error has occurred while parsing data: " + e.getMessage() + "\nPress any key to return: ");
        }
    }

    // Метод для смены ответственного лица на складе
    public static void changeResponsible() {
        try {
            // Парсинг данных из txt файлов
            ArrayList<Warehouse> warehouses = WarehouseParser.parse();
            ArrayList<Employee> employees = EmployeeParser.parse();

            // Запрос ID склада
            System.out.println("Enter the ID of the warehouse: ");
            int warehouseId = scanner.nextInt();
            scanner.nextLine();

            // Поиск склада по ID
            Warehouse currWarehouse = findById(warehouseId, warehouses);
            if (currWarehouse == null) {
                System.out.println("Couldn't find warehouse with the ID: " + warehouseId + "\nPress any key to return: ");
                return;
            }

            // Запрос ID сотрудника
            System.out.println("Enter employee's ID: ");
            int employeeId = scanner.nextInt();
            scanner.nextLine();

            // Поиск сотрудника по ID
            Employee currEmployee = findById(employeeId, employees);
            if (currEmployee == null) {
                System.out.println("Couldn't find employee with the ID: " + employeeId + "\nPress any key to return: ");
                return;
            }

            // Смена ответственного лица у текущего склада
            currWarehouse.setEmployeeId(employeeId);

            // Сохранение изменений в txt файлы
            boolean isSerialized = WarehouseParser.serialize(warehouses);
            if (isSerialized) {
                System.out.println("Warehouse with the ID: " + warehouseId + " has successfully changed the responsibility.\nPress any key to return: ");
            } else {
                System.out.println("Error has occurred while serializing data.\nPress any key to return: ");
            }
        } catch (Exception e) {
            System.out.println("Error has occurred while parsing data: " + e.getMessage() + "\nPress any key to return: ");
        }
    }

    // Метод для вывода информации о складе
    public static void printInfo() {
        try {
            // Парсинг данных из txt файлов
            ArrayList<Warehouse> warehouses = WarehouseParser.parse();

            // Запрос ID склада
            System.out.println("Enter the ID of the warehouse: ");
            int warehouseId = scanner.nextInt();
            scanner.nextLine();

            // Поиск склада по ID
            Warehouse currWarehouse = findById(warehouseId, warehouses);
            if (currWarehouse == null) {
                System.out.println("Couldn't find warehouse with the ID: " + warehouseId + "\nPress any key to return: ");
                return;
            }

            // Вывод в консоль заголовка таблицы
            System.out.println("Warehouse information");
            System.out.println("------------------------------------------------------------------------------------------------------");

            String headerFormat = "%-5s | %-25s | %-50s%n";
            System.out.printf(headerFormat, "ID", "Responsible Employee ID", "Address");
            System.out.println("------------------------------------------------------------------------------------------------------");

            // Установление формата таблицы и вывод информации о складе
            String rowFormat = "%-5d | %-25d | %-50s%n";
            System.out.printf(rowFormat, currWarehouse.getId(), currWarehouse.getEmployeeId(), currWarehouse.getAddress());
            System.out.println("------------------------------------------------------------------------------------------------------");

            System.out.println("Press any key to return: ");
        } catch (Exception e) {
            System.out.println("Error has occurred while parsing data: " + e.getMessage() + "\nPress any key to return: ");
        }
    }

    // Метод для вывода информации о продуктах на складе
    public static void printProductsInfo() {
        try {
            // Парсинг данных из txt файлов
            ArrayList<Warehouse> warehouses = WarehouseParser.parse();
            ArrayList<Cell> cells = CellParser.parse();
            ArrayList<Product> products = ProductParser.parse();

            // Запрос ID склада
            System.out.println("Enter the ID of the warehouse: ");
            int warehouseId = scanner.nextInt();
            scanner.nextLine();

            // Поиск склада по ID
            Warehouse currWarehouse = findById(warehouseId, warehouses);
            if (currWarehouse == null) {
                System.out.println("Couldn't find warehouse with the ID: " + warehouseId + "\nPress any key to return: ");
                return;
            }

            // Выводит в консоль заголовка таблицы
            System.out.println("Warehouse products information");
            System.out.println("----------------------------------------------------------------------------------------------------");

            String headerFormat = "%-5s | %-20s | %-20s | %-20s | %-10s | %-6s%n";
            System.out.printf(headerFormat, "ID", "Name", "Manufacturer", "Purchase Price", "Sale Price", "Amount");
            System.out.println("----------------------------------------------------------------------------------------------------");

            // Установление формата таблицы и поиск товаров в ячейках склада
            String rowFormat = "%-5d | %-20s | %-20s | %-20s | %-10s | %-6d%n";
            for (Cell cell : cells) {
                if (cell.getStorageId() == warehouseId) {
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
}
