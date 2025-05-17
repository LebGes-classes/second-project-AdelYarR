package entities.storage.warehouse;

import parsers.SellingPointParser;
import entities.storage.Storage;
import entities.storage.selling_point.SellingPoint;

import java.io.FileNotFoundException;
import java.util.ArrayList;

// Класс Warehouse для представления склада,
// содержит поля ID, ID ответственного лица, адреса.
public class Warehouse extends Storage {

    public Warehouse(int id, int employeeId, String address) {
        super(id, employeeId, address);
    }

    public String toString() {
        return "[" + id + "] [" + employeeId + "] [" + address + "]";
    }

    public ArrayList<SellingPoint> getSellingPoints() throws FileNotFoundException {
        ArrayList<SellingPoint> allSellingPoints = SellingPointParser.parse();
        ArrayList<SellingPoint> sellingPoints = new ArrayList<>();
        for (SellingPoint sellingPoint : allSellingPoints) {
            if (sellingPoint.getWarehouseId() == id) {
                sellingPoints.add(sellingPoint);
            }
        }

        return sellingPoints;
    }
}
