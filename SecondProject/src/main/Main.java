package main;

import excel_parser.ExcelParser;
import menu.*;

public class Main {
    public static void main(String[] args) {
        // Массив из названий таблиц Excel/файлов JSON, для парсинга данных
        String[] sheetNames = {"warehouse", "selling_point", "cell", "product", "employee", "consumer", "consumer_product"};

        // Парсинг данных из Excel файла в JSON
        try {
            ExcelParser.parse("data/excel.xlsx", sheetNames);
        } catch(Exception e) {
            System.out.println("Failed to parse information from excel file: " + e.getMessage());
        }

        // Экземпляры меню для вывода в консоль
        CompanyMenu companyMenu = new CompanyMenu();
        WarehouseMenu warehouseMenu = new WarehouseMenu();
        SellingPointMenu sellingPointMenu = new SellingPointMenu();
        EmployeeMenu employeeMenu = new EmployeeMenu();
        ConsumerMenu consumerMenu = new ConsumerMenu();

        // Создание и запуск главного меню в консоли
        MainMenu mainMenu = new MainMenu(companyMenu, warehouseMenu, sellingPointMenu, employeeMenu, consumerMenu);
        mainMenu.runMenu();

        // Сохранение изменений из JSON файлов в Excel
        try {
            ExcelParser.serialize("data/excel.xlsx", sheetNames);
        } catch(Exception e) {
            System.out.println("Failed to serialize information to excel file: " + e.getMessage());
        }
    }
}