package menu;

import java.util.Scanner;
import utils.ConsoleCleaner;

// Абстрактный класс, который наследуется всеми меню
public abstract class Menu {

    // Метод для вывода списка действий в менеджере
    protected void printMenu() {
    }

    // Метод, обрабатывающий выбор пользователя
    protected boolean switchChoice(String choice) {
        return false;
    }

    // Метод, который собирает и запускает printMenu и switchChoice
    public void runMenu() {
        Scanner scanner = new Scanner(System.in);

        String choice;
        boolean running = true;
        while(running) {
            ConsoleCleaner.clear();

            printMenu();

            choice = scanner.nextLine();
            running = switchChoice(choice);
        }
    }
}
