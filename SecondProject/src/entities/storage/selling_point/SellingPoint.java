package entities.storage.selling_point;

import entities.storage.Storage;

// Класс SellingPoint для представления пункта продаж,
// содержит поля ID, ID ответственного лица, адреса, ID склада, доходности.
public class SellingPoint extends Storage {

    private int warehouseId;
    private double profit = 0.00;

    public SellingPoint(int id, int warehouseId, int employeeId, String address, double profit) {
        super(id, employeeId, address);
        this.warehouseId = warehouseId;
        this.profit = profit;
    }

    public String toString() {
        return "[" + id + "] [" + warehouseId + "] [" + employeeId + "] [" + address + "] [" + profit + "]";
    }

    public double getProfit() {
        return profit;
    }

    public void setProfit(double profit) { this.profit = profit; }

    public int getWarehouseId() { return warehouseId; }
}
