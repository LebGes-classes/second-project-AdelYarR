package menu;

import java.util.Scanner;

import console.EmployeeConsole;
import utils.ConsoleCleaner;

// Консольное меню выбора действий для работы с сотрудниками
public class EmployeeMenu extends Menu {

    // Метод для вывода меню в консоль
    @Override
    protected void printMenu() {
        String menuString = "Employee Menu\n" +
                "1 - Get employee information\n" +
                "2 - Change position\n" +
                "3 - Hire employee\n" +
                "4 - Fire employee\n" +
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
                EmployeeConsole.printInfo();
                scanner.nextLine();
                break;
            case "2":
                EmployeeConsole.changePosition();
                scanner.nextLine();
                break;
            case "3":
                EmployeeConsole.hireEmployee();
                scanner.nextLine();
                break;
            case "4":
                EmployeeConsole.fireEmployee();
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
