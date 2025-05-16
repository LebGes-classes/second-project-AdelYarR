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
            ArrayList<SellingPoint> sellingPoints = SellingPointParser.parse();
            ArrayList<Warehouse> warehouses = WarehouseParser.parse();
            ArrayList<Employee> employees = EmployeeParser.parse();

            int sellingPointId = sellingPoints.getLast().getId() + 1;

            System.out.println("Enter address of the selling point: ");
            String address = scanner.nextLine();

            System.out.println("Enter employee's ID: ");
            int employeeId = scanner.nextInt();
            scanner.nextLine();

            Employee currEmployee = findById(employeeId, employees);
            if (currEmployee == null) {
                System.out.println("Couldn't find employee with the ID: " + employeeId + "\nPress any key to return: ");
                return;
            }

            System.out.println("Enter warehouse ID: ");
            int warehouseId = scanner.nextInt();
            scanner.nextLine();

            Warehouse currWarehouse = findById(warehouseId, warehouses);
            if (currWarehouse == null) {
                System.out.println("Couldn't find warehouse with the ID: " + warehouseId + "\nPress any key to return: ");
                return;
            }

            SellingPoint sellingPoint = new SellingPoint(sellingPointId, warehouseId, employeeId, address, 0.00);
            sellingPoints.add(sellingPoint);

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
            ArrayList<SellingPoint> sellingPoints = SellingPointParser.parse();
            ArrayList<Cell> cells = CellParser.parse();

            System.out.println("Enter ID of the selling point you want to close: ");
            int sellingPointId = scanner.nextInt();
            scanner.nextLine();

            SellingPoint currSellingPoint = findById(sellingPointId, sellingPoints);
            if (currSellingPoint == null) {
                System.out.println("Couldn't find selling point with the ID: " + sellingPointId + "\nPress any key to return: ");
                return;
            }

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

            sellingPoints.remove(currSellingPoint);

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
            ArrayList<SellingPoint> sellingPoints = SellingPointParser.parse();
            ArrayList<Cell> cells = CellParser.parse();

            System.out.println("Enter ID of the selling point: ");
            int sellingPointId = scanner.nextInt();
            scanner.nextLine();

            SellingPoint currSellingPoint = findById(sellingPointId, sellingPoints);
            if (currSellingPoint == null) {
                System.out.println("Couldn't find selling point with the ID: " + sellingPointId + "\nPress any key to return: ");
                return;
            }

            System.out.println("Enter ID of the product you want to move back to warehouse: ");
            int productId = scanner.nextInt();
            scanner.nextLine();

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

            int warehouseCellId = 0;
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

            if (warehouseCellId == 0) {
                System.out.println("Couldn't find a warehouse cell for the product.\nPress any key to return: ");
                return;
            }

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
            ArrayList<SellingPoint> sellingPoints = SellingPointParser.parse();
            ArrayList<Employee> employees = EmployeeParser.parse();

            System.out.println("Enter the ID of selling point: ");
            int sellingPointId = scanner.nextInt();
            scanner.nextLine();

            SellingPoint currSellingPoint = findById(sellingPointId, sellingPoints);
            if (currSellingPoint == null) {
                System.out.println("Couldn't find selling point with the ID: " + sellingPointId + "\nPress any key to return: ");
                return;
            }

            System.out.println("Enter employee's ID: ");
            int employeeId = scanner.nextInt();
            scanner.nextLine();

            Employee currEmployee = findById(employeeId, employees);
            if (currEmployee == null) {
                System.out.println("Couldn't find employee with the ID: " + employeeId + "\nPress any key to return: ");
                return;
            }

            currSellingPoint.setEmployeeId(employeeId);

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
            ArrayList<SellingPoint> sellingPoints = SellingPointParser.parse();

            System.out.println("Enter the ID of selling point: ");
            int sellingPointId = scanner.nextInt();
            scanner.nextLine();

            SellingPoint currSellingPoint = findById(sellingPointId, sellingPoints);
            if (currSellingPoint == null) {
                System.out.println("Couldn't find selling point with the ID: " + sellingPointId + "\nPress any key to return: ");
                return;
            }

            System.out.println("Selling Point information");
            System.out.println("------------------------------------------------------------------------------------------------------");

            String headerFormat = "%-5s | %-15s | %-25s | %-50s%n";
            System.out.printf(headerFormat, "ID", "Warehouse ID", "Responsible Employee ID", "Address");
            System.out.println("------------------------------------------------------------------------------------------------------");

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
            ArrayList<SellingPoint> sellingPoints = SellingPointParser.parse();
            ArrayList<Cell> cells = CellParser.parse();
            ArrayList<Product> products = ProductParser.parse();

            System.out.println("Enter the ID of selling point: ");
            int sellingPointId = scanner.nextInt();
            scanner.nextLine();

            SellingPoint currSellingPoint = findById(sellingPointId, sellingPoints);
            if (currSellingPoint == null) {
                System.out.println("Couldn't find selling point with the ID: " + sellingPointId + "\nPress any key to return: ");
                return;
            }

            System.out.println("Selling Point products information");
            System.out.println("----------------------------------------------------------------------------------------------------");

            String headerFormat = "%-5s | %-20s | %-20s | %-20s | %-10s | %-6s%n";
            System.out.printf(headerFormat, "ID", "Name", "Manufacturer", "Purchase Price", "Sale Price", "Amount");
            System.out.println("----------------------------------------------------------------------------------------------------");

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
            ArrayList<SellingPoint> sellingPoints = SellingPointParser.parse();

            System.out.println("Enter the ID of selling point: ");
            int sellingPointId = scanner.nextInt();
            scanner.nextLine();

            SellingPoint currSellingPoint = findById(sellingPointId, sellingPoints);
            if (currSellingPoint == null) {
                System.out.println("Couldn't find selling point with the ID: " + sellingPointId + "\nPress any key to return: ");
                return;
            }

            System.out.println("The profit of the selling point is " + currSellingPoint.getProfit() + ".\nPress any key to return: ");
        } catch (Exception e) {
            System.out.println("Error has occurred while parsing data: " + e.getMessage() + "\nPress any key to return: ");
        }
    }
}