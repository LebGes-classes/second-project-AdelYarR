package entities.storage;

import parsers.CellParser;
import entities.interfaces.HasId;

import java.io.FileNotFoundException;
import java.util.ArrayList;

// Класс Storage для представления места, которое содержит ячейки с продуктами,
// наследуется классами Warehouse и SellingPoint,
// содержит поля ID, ID ответственного лица, адреса.
public class Storage implements HasId {
    protected int id;
    protected int employeeId;
    protected String address;

    public Storage(int id, int employeeId, String address) {
        this.id = id;
        this.employeeId = employeeId;
        this.address = address;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public ArrayList<Cell> getCells() throws FileNotFoundException {
        ArrayList<Cell> allCells = CellParser.parse();
        ArrayList<Cell> cells = new ArrayList<>();
        for (Cell cell : allCells) {
            if (cell.getStorageId() == id) {
                cells.add(cell);
            }
        }

        return cells;
    }
}
