package menu;

import console.ConsumerConsole;
import utils.ConsoleCleaner;

import java.util.Scanner;

// Консольное меню выбора действий для работы с покупателями
public class ConsumerMenu extends Menu {

    // Метод для вывода меню в консоль
    @Override
    protected void printMenu() {
        String menuString = "Consumer Menu\n" +
                "1 - Purchase product\n" +
                "2 - Return product\n" +
                "3 - Get information about all purchased products\n" +
                "q - Return to main menu";
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
                ConsumerConsole.purchaseProduct();
                scanner.nextLine();
                break;
            case "2":
                ConsumerConsole.returnProduct();
                scanner.nextLine();
                break;
            case "3":
                ConsumerConsole.printProductInfo();
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
