package menu;

import java.util.Scanner;

import console.CompanyConsole;
import utils.ConsoleCleaner;

// Консольное меню выбора действий для предприятия
public class CompanyMenu extends Menu {

    // Метод для вывода меню в консоль
    @Override
    protected void printMenu() {
        String menuString = "Company Menu\n" +
                "1 - Get products for purchase information\n" +
                "2 - Purchase product\n" +
                "3 - Get information about profitability\n" +
                "q - Quit menu";
        System.out.println(menuString);
    }

    // Метод для обработки выбранного действия
    @Override
    protected boolean switchChoice(String choice) {
        Scanner scanner = new Scanner(System.in);

        boolean running = true;
        ConsoleCleaner.clear();
        switch(choice) {
            case "1":
                CompanyConsole.printProductInfo();
                scanner.nextLine();
                break;
            case "2":
                CompanyConsole.purchaseProduct();
                scanner.nextLine();
                break;
            case "3":
                CompanyConsole.printCompanyProfitInfo();
                scanner.nextLine();
                break;
            case "q":
                return false;
            default:
                ConsoleCleaner.clear();
        }

        return running;
    }
}
