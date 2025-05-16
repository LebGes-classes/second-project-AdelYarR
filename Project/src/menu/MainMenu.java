package menu;

import utils.ConsoleCleaner;

public class MainMenu extends Menu {

    CompanyMenu companyMenu;
    WarehouseMenu warehouseMenu;
    SellingPointMenu sellingPointMenu;
    EmployeeMenu employeeMenu;
    ConsumerMenu consumerMenu;

    public MainMenu(CompanyMenu coM, WarehouseMenu wM, SellingPointMenu spM, EmployeeMenu eM, ConsumerMenu cM) {
        this.companyMenu = coM;
        this.warehouseMenu = wM;
        this.sellingPointMenu = spM;
        this.employeeMenu = eM;
        this.consumerMenu = cM;
    }

    // Вывод основного меню выбора
    protected void printMenu() {
        String menuString = "Application For Turnover\n" +
                "1 - Company menu\n" +
                "2 - Warehouse menu\n" +
                "3 - Selling point menu\n" +
                "4 - Employee menu\n" +
                "5 - Consumer menu\n" +
                "q - Quit menu";

        System.out.println(menuString);
    }

    // Метод для обработки выбранного действия
    protected boolean switchChoice(String choice) {
        boolean running = true;
        switch(choice) {
            case "1":
                companyMenu.runMenu();
                break;
            case "2":
                warehouseMenu.runMenu();
                break;
            case "3":
                sellingPointMenu.runMenu();
                break;
            case "4":
                employeeMenu.runMenu();
                break;
            case "5":
                consumerMenu.runMenu();
                break;
            case "q":
                running = false;
                break;
            default:
                ConsoleCleaner.clear();
        }

        return running;
    }
}
