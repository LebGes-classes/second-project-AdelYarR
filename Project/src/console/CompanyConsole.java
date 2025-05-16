package console;

import entities.product.Product;
import entities.storage.Cell;
import entities.storage.selling_point.SellingPoint;
import entities.storage.warehouse.Warehouse;
import parsers.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import static utils.ArraySearch.findById;

public class CompanyConsole {

    private static final Scanner scanner = new Scanner(System.in);

    // Метод для вывода товаров, которые нуждаются в закупке
    public static void printProductInfo() {
        try {
            // Парсинг данных из JSON файлов
            ArrayList<Cell> cells = CellParser.parse();
            ArrayList<Product> products = ProductParser.parse();
            ArrayList<Warehouse> warehouses = WarehouseParser.parse();

            // Поиск ID товаров, для которых зарезервированы ячейки склада,
            // а также поиск ID товаров, которые закончились в ячейках склада
            Set<Integer> warehouseProductsId = new HashSet<>();
            Set<Integer> productsIdWithEmptyCells = new HashSet<>();
            for (Cell cell : cells) {
                boolean isWarehouseCell = false;
                boolean isOutOfStock = false;
                for (Warehouse warehouse : warehouses) {
                    if (cell.getStorageId() == warehouse.getId()) {
                        isWarehouseCell = true;
                    }
                    if (cell.getQuantity() == 0) {
                        isOutOfStock = true;
                    }
                }

                if (isWarehouseCell) {
                    warehouseProductsId.add(cell.getProductId());
                }
                if (isWarehouseCell && isOutOfStock) {
                    productsIdWithEmptyCells.add(cell.getProductId());
                }
            }

            // Вывод заголовка таблицы
            System.out.println("Products for purchase information");
            System.out.println("----------------------------------------------------------------------------------------------------");

            String headerFormat = "%-5s | %-20s | %-20s | %-20s%n";
            System.out.printf(headerFormat, "ID", "Name", "Manufacturer", "Purchase Price");
            System.out.println("----------------------------------------------------------------------------------------------------");

            String rowFormat = "%-5d | %-20s | %-20s | %-20s%n";

            // Поиск и вывод в консоль товаров, готовых к закупу
            for (Product product : products) {
                boolean isInWarehouse = warehouseProductsId.contains(product.getId());
                boolean isOutOfStock = productsIdWithEmptyCells.contains(product.getId());
                if (!isInWarehouse || isOutOfStock) {
                    System.out.printf(rowFormat, product.getId(), product.getName(), product.getManufacturer(), product.getPurchasePrice());
                }
            }

            System.out.println("----------------------------------------------------------------------------------------------------");

            System.out.println("Press any key to return: ");
        } catch (Exception e) {
            System.out.println("Error has occurred while parsing data: " + e.getMessage() + "\nPress any key to return: ");
        }
    }

    public static void purchaseProduct() {
        try {
            // Парсинг данных из JSON файлов
            ArrayList<Cell> cells = CellParser.parse();
            ArrayList<Product> products = ProductParser.parse();
            ArrayList<Warehouse> warehouses = WarehouseParser.parse();

            // Запрос ID товара
            System.out.println("Enter the ID of the product you want to purchase: ");
            int productId = scanner.nextInt();
            scanner.nextLine();

            // Поиск продукта по ID
            Product currProduct = findById(productId, products);

            if (currProduct == null) {
                System.out.println("Couldn't find product with the ID: " + productId + "\nPress any key to return: ");
                return;
            }

            // Запрос количества товаров для закупа
            System.out.println("Enter amount of products you want to purchase: ");
            int amount = scanner.nextInt();
            scanner.nextLine();

            // Запрос склада по ID
            System.out.println("Enter the ID of the warehouse you want to place products: ");
            int warehouseId = scanner.nextInt();
            scanner.nextLine();

            // Поиск склада по ID
            Warehouse currWarehouse = findById(warehouseId, warehouses);

            if (currWarehouse == null) {
                System.out.println("Couldn't find warehouse with the ID: " + warehouseId + "\nPress any key to return: ");
                return;
            }

            // Поиск ячейки склада, зарезервированной под данный товар
            Cell currCell = null;
            for (Cell cell : cells) {
                if (cell.getStorageId() == warehouseId && cell.getProductId() == productId) {
                    currCell = cell;
                    break;
                }
            }

            // Проверка, если зарезервированной ячейки не нашлось, ищем пустую
            // незарезервированную никаким товаром ячейку, и помещаем туда товар.
            // Если же зарезервированная ячейка нашлась, то пополняем её.
            if (currCell == null) {
                Cell emptyCell = null;
                for (Cell cell : cells) {
                    if (cell.getStorageId() == warehouseId && cell.getProductId() == 0) {
                        emptyCell = cell;
                        break;
                    }
                }

                // Проверка существования незарезервированной ячейки
                if (emptyCell == null) {
                    System.out.println("The warehouse doesn't have enough space to buy products.\nPress any key to return: ");
                    return;
                }

                // Проверка вместимости незарезервированной ячейки
                if (emptyCell.getCapacity() < amount) {
                    System.out.println("The warehouse cell doesn't have enough space to place products.\nPress any key to return: ");
                    return;
                }

                // Добавление товара в ячейку
                emptyCell.setProductId(productId);
                emptyCell.setQuantity(amount);

                // Сохранение данных в JSON файл
                boolean isCellSerialized = CellParser.serialize(cells);
                if (isCellSerialized) {
                    System.out.println("The product was successfully purchased and placed in cell with ID: " + emptyCell.getId() + ".\nPress any key to return: ");
                } else {
                    System.out.println("Error has occurred while serializing.\nPress any key to return: ");
                }
            } else {
                // Проверка вместимости незарезервированной ячейки
                if (currCell.getRemainingSpace() < amount) {
                    System.out.println("The warehouse cell doesn't have enough space to place products.\nPress any key to return: ");
                    return;
                }

                currCell.setQuantity(currCell.getQuantity() + amount);

                // Сохранение данных в JSON файл
                boolean isCellSerialized = CellParser.serialize(cells);
                if (isCellSerialized) {
                    System.out.println("The product was successfully purchased and placed in cell with ID: " + currCell.getId() + ".\nPress any key to return: ");
                } else {
                    System.out.println("Error has occurred while serializing.\nPress any key to return: ");
                }
            }
        } catch (Exception e) {
            System.out.println("Error has occurred while parsing data: " + e.getMessage() + "\nPress any key to return: ");
        }
    }

    // Метод для вывода общей доходности компании
    public static void printCompanyProfitInfo() {
        try {
            // Парсинг данных из JSON файлов
            ArrayList<SellingPoint> sellingPoints = SellingPointParser.parse();

            // Получение доходности с каждого пункта продаж
            double profit = 0;
            for (SellingPoint sellingPoint : sellingPoints) {
                profit += sellingPoint.getProfit();
            }

            // Вывод в консоль доходность
            System.out.println("The company's profit amount: " + profit + ".\nPress any key to return: ");
        } catch (Exception e) {
            System.out.println("Error has occurred while parsing data: " + e.getMessage() + "\nPress any key to return: ");
        }
    }
}
