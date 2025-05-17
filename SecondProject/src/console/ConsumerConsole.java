package console;

import parsers.*;
import entities.product.Product;
import entities.storage.Cell;
import entities.storage.selling_point.SellingPoint;
import entities.user.consumer.Consumer;
import entities.user.consumer.ConsumerProduct;

import java.util.Scanner;

import java.util.ArrayList;

import static utils.ArraySearch.findById;

public class ConsumerConsole {

    private static final Scanner scanner = new Scanner(System.in);

    // Метод для покупки товаров покупателем
    public static void purchaseProduct() {
        try {
            // Парсинг данных из txt файлов
            ArrayList<Consumer> consumers = ConsumerParser.parse();
            ArrayList<ConsumerProduct> consumerProducts = ConsumerProductParser.parse();
            ArrayList<Product> products = ProductParser.parse();
            ArrayList<SellingPoint> sellingPoints = SellingPointParser.parse();
            ArrayList<Cell> cells = CellParser.parse();

            // Запрос ID покупателя
            System.out.println("Enter the ID of the consumer: ");
            int consumerId = scanner.nextInt();
            scanner.nextLine();

            // Поиск покупателя по ID
            Consumer currConsumer = findById(consumerId, consumers);
            if (currConsumer == null) {
                System.out.println("Couldn't find consumer with the ID: " + consumerId + "\nPress any key to return: ");
                return;
            }

            // Запрос ID пункта продаж
            System.out.println("Enter the ID of the selling point you want to buy product from: ");
            int sellingPointId = scanner.nextInt();
            scanner.nextLine();

            // Поиск пункта продаж по ID
            SellingPoint currSellingPoint = findById(sellingPointId, sellingPoints);
            if (currSellingPoint == null) {
                System.out.println("Couldn't find selling point with the ID: " + sellingPointId + "\nPress any key to return: ");
                return;
            }

            // Запрос ID товара
            System.out.println("Enter the ID of the product you want to buy: ");
            int productId = scanner.nextInt();
            scanner.nextLine();

            // Поиск ячейки с товаром в данном пункте продажи
            Cell currCell = null;
            for (Cell cell : cells) {
                if (cell.getStorageId() == sellingPointId && cell.getProductId() == productId) {
                    currCell = cell;
                    break;
                }
            }

            if (currCell == null) {
                System.out.println("Couldn't find cell with the product.\nPress any key to return: ");
                return;
            }

            // Поиск товара по ID в данных ячейках
            Product currProduct = findById(currCell.getProductId(), products);

            if (currProduct == null) {
                System.out.println("Couldn't find product with the ID: " + productId + "\nPress any key to return: ");
                return;
            }

            // Запрос количество товаров для покупки
            System.out.println("Enter the amount of products you want to buy: ");
            int quantity = scanner.nextInt();
            scanner.nextLine();

            // Проверка есть ли в ячейке достаточно свободного места
            if (currCell.getQuantity() - quantity < 0) {
                System.out.println("Couldn't buy this amount of products, there are only " + currCell.getQuantity() + " of products available.\nPress any key to return: ");
                return;
            }

            // Создание нового ID для товара, купленным покупателем
            int consumerProductId = consumerProducts.isEmpty()
                    ? 1
                    : consumerProducts.stream()
                    .mapToInt(ConsumerProduct::getId)
                    .max()
                    .orElse(0) + 1;

            // Добавление нового товара, купленным покупателем, список всех купленных продуктов
            ConsumerProduct newConsumerProduct = new ConsumerProduct(consumerProductId, consumerId, productId, quantity);
            consumerProducts.add(newConsumerProduct);

            currCell.setQuantity(currCell.getQuantity() - quantity);

            // Изменение доходности пункта продаж, прибавляем бюджет
            double profit = quantity * currProduct.getMarginal();
            currSellingPoint.setProfit(currSellingPoint.getProfit() + profit);

            // Сохранение изменений в txt файлы
            boolean isConsumerProductSerialized = ConsumerProductParser.serialize(consumerProducts);
            boolean isCellSerialized = CellParser.serialize(cells);
            boolean isSPSerialized = SellingPointParser.serialize(sellingPoints);
            if (isConsumerProductSerialized && isCellSerialized && isSPSerialized) {
                System.out.println("Product with the ID " + productId + " was successfully purchased.\nPress any key to return: ");
            } else {
                System.out.println("Error has occurred while serializing.\nPress any key to return: ");
            }
        } catch (Exception e) {
            System.out.println("Error has occurred while purchasing product: " + e.getMessage() + "\nPress any key to return: ");
        }
    }

    // Метод для возврата купленного товара покупателем
    public static void returnProduct() {
        Scanner scanner = new Scanner(System.in);
        try {
            // Парсинг данных из txt файлов
            ArrayList<Consumer> consumers = ConsumerParser.parse();
            ArrayList<ConsumerProduct> consumerProducts = ConsumerProductParser.parse();
            ArrayList<Product> products = ProductParser.parse();
            ArrayList<SellingPoint> sellingPoints = SellingPointParser.parse();
            ArrayList<Cell> cells = CellParser.parse();

            // Запрос ID покупателя
            System.out.println("Enter the ID of the consumer: ");
            int consumerId = scanner.nextInt();
            scanner.nextLine();

            // Поиск покупателя по ID
            Consumer currConsumer = findById(consumerId, consumers);
            if (currConsumer == null) {
                System.out.println("Couldn't find consumer with the ID: " + consumerId + "\nPress any key to return: ");
                return;
            }

            // Запрос ID пункта продаж, куда требуется вернуть товар
            System.out.println("Enter the ID of the selling point you want to return product to: ");
            int sellingPointId = scanner.nextInt();
            scanner.nextLine();

            // Поиск пункта продаж по ID
            SellingPoint currSellingPoint = findById(sellingPointId, sellingPoints);
            if (currSellingPoint == null) {
                System.out.println("Couldn't find selling point with the ID: " + sellingPointId + "\nPress any key to return: ");
                return;
            }

            // Запрос ID товара
            System.out.println("Enter the ID of the product you want to return: ");
            int productId = scanner.nextInt();
            scanner.nextLine();

            // Поиск купленного покупателем товара
            ConsumerProduct currConsumerProduct = null;
            for (ConsumerProduct consumerProduct : consumerProducts) {
                if (consumerProduct.getConsumerId() == consumerId && consumerProduct.getProductId() == productId) {
                    currConsumerProduct = consumerProduct;
                    break;
                }
            }

            if (currConsumerProduct == null) {
                System.out.println("Couldn't find product with the ID: " + productId + "\nPress any key to return: ");
                return;
            }

            // Поиск ячейки для товара в данном пункте продажи
            Cell currCell = null;
            for (Cell cell : cells) {
                if (cell.getStorageId() == sellingPointId && cell.getProductId() == productId) {
                    currCell = cell;
                    break;
                }
            }

            if (currCell == null) {
                System.out.println("Couldn't find cell for the product.\nPress any key to return: ");
                return;
            }

            // Поиск товара по ID
            Product currProduct = findById(currCell.getProductId(), products);

            if (currProduct == null) {
                System.out.println("Couldn't find product with the ID: " + productId + "\nPress any key to return: ");
                return;
            }

            // Запрос количество товаров для покупки
            System.out.println("Enter the amount of products you want to return: ");
            int quantity = scanner.nextInt();
            scanner.nextLine();

            // Проверка есть ли в ячейке достаточно свободного места
            if (currCell.getQuantity() + quantity > currCell.getCapacity()) {
                System.out.println("Couldn't return this amount of products, capacity left only for " + currCell.getRemainingSpace() + " products.\nPress any key to return: ");
                return;
            }

            // Изменение количество купленного товара покупателем
            currConsumerProduct.setQuantity(currConsumerProduct.getQuantity() - quantity);
            if (currConsumerProduct.getQuantity() == 0) {
                consumerProducts.remove(currConsumerProduct);
            }

            currCell.setQuantity(currCell.getQuantity() + quantity);

            // Изменение доходности пункта продаж, вычитаем бюджет
            double profit = quantity * currProduct.getMarginal();
            currSellingPoint.setProfit(currSellingPoint.getProfit() - profit);

            // Сохранение изменений в txt файлы
            boolean isConsumerProductSerialized = ConsumerProductParser.serialize(consumerProducts);
            boolean isCellSerialized = CellParser.serialize(cells);
            boolean isSPSerialized = SellingPointParser.serialize(sellingPoints);
            if (isConsumerProductSerialized && isCellSerialized && isSPSerialized) {
                System.out.println("Product with the ID " + productId + " was successfully returned.\nPress any key to return: ");
            } else {
                System.out.println("Error has occurred while serializing.\nPress any key to return: ");
            }
        } catch (Exception e) {
            System.out.println("Error has occurred while purchasing product: " + e.getMessage() + "\nPress any key to return: ");
        }
    }

    // Метод для вывода всех товаров, купленных покупателем
    public static void printProductInfo() {
        Scanner scanner = new Scanner(System.in);
        try {
            // Парсинг данных из txt файла
            ArrayList<ConsumerProduct> consumerProducts = ConsumerProductParser.parse();
            ArrayList<Product> products = ProductParser.parse();

            // Запрос ID покупателя
            System.out.println("Enter the ID of the consumer you want to get products: ");
            int consumerId = scanner.nextInt();
            scanner.nextLine();

            // Вывод заголовка таблицы
            System.out.println("Consumer's products information");
            System.out.println("---------------------------------------------------------------------------");

            String headerFormat = "%-5s | %-20s | %-20s | %-10s | %-6s%n";
            System.out.printf(headerFormat, "ID", "Name", "Manufacturer", "Price", "Amount");
            System.out.println("---------------------------------------------------------------------------");

            // Установление формата для вывода строк таблицы
            String rowFormat = "%-5d | %-20s | %-20s | %-10s | %-6d%n";

            // Поиск и вывод в консоль найденные товары, купленные покупателем
            for (ConsumerProduct consumerProduct : consumerProducts) {
                if (consumerProduct.getConsumerId() == consumerId) {
                    for (Product product : products) {
                        if (product.getId() == consumerProduct.getProductId()) {
                            System.out.printf(rowFormat, product.getId(), product.getName(), product.getManufacturer(), product.getSalePrice(), consumerProduct.getQuantity());
                        }
                    }
                }
            }

            System.out.println("---------------------------------------------------------------------------");

            System.out.println("Press any key to return: ");
        } catch (Exception e) {
            System.out.println("Error has occurred while parsing data: " + e.getMessage() + "\nPress any key to return: ");
        }
    }
}
