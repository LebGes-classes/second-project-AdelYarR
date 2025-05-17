package menu;

import console.WarehouseConsole;

import java.util.Scanner;

// Консольное меню выбора действий для работы со складами
public class WarehouseMenu extends Menu {

    // Метод для вывода меню в консоль
    @Override
    protected void printMenu() {
        String menuString = "Warehouse Menu\n" +
                "1 - Open new warehouse\n" +
                "2 - Close warehouse\n" +
                "3 - Move product to selling point\n" +
                "4 - Change responsible person\n" +
                "5 - Get warehouse information\n" +
                "6 - Get warehouse products information\n" +
                "q - Return to main menu";
        System.out.println(menuString);
    }

    // Метод для обработки выбранного действия
    @Override
    protected boolean switchChoice(String choice) {
        Scanner scanner = new Scanner(System.in);

        boolean running = true;
        switch(choice) {
            case "1":
                WarehouseConsole.open();
                scanner.nextLine();
                break;
            case "2":
                WarehouseConsole.close();
                scanner.nextLine();
                return false;
            case "3":
                WarehouseConsole.moveProduct();
                scanner.nextLine();
                return false;
            case "4":
                WarehouseConsole.changeResponsible();
                scanner.nextLine();
                break;
            case "5":
                WarehouseConsole.printInfo();
                scanner.nextLine();
                break;
            case "6":
                WarehouseConsole.printProductsInfo();
                scanner.nextLine();
                break;
            case "q":
                return false;
        }

        return running;
    }
}
