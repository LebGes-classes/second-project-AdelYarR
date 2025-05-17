package menu;

import console.SellingPointConsole;

import java.util.Scanner;

// Консольное меню выбора действий для работы с пунктами продаж
public class SellingPointMenu extends Menu {

    // Метод для вывода меню в консоль
    @Override
    protected void printMenu() {
        String menuString = "Selling Point Menu\n" +
                "1 - Open new selling point\n" +
                "2 - Close selling point\n" +
                "3 - Move product back to warehouse\n" +
                "4 - Change responsible person\n" +
                "5 - Get selling point information\n" +
                "6 - Get selling point products information\n" +
                "7 - Get information about profitability\n" +
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
                SellingPointConsole.open();
                scanner.nextLine();
                break;
            case "2":
                SellingPointConsole.close();
                scanner.nextLine();
                return false;
            case "3":
                SellingPointConsole.moveProduct();
                scanner.nextLine();
                return false;
            case "4":
                SellingPointConsole.changeResponsible();
                scanner.nextLine();
                break;
            case "5":
                SellingPointConsole.printInfo();
                scanner.nextLine();
                break;
            case "6":
                SellingPointConsole.printProductsInfo();
                scanner.nextLine();
                break;
            case "7":
                SellingPointConsole.printProfitInfo();
                scanner.nextLine();
                break;
            case "q":
                return false;
        }

        return running;
    }
}
