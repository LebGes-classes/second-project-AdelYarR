package entities.product;

import entities.interfaces.HasId;

// Класс Product для представления товара,
// содержит поля ID, названия, производителя, цена закупки, цена продажи.
public class Product implements HasId {
    private int id;
    private String name;
    private String manufacturer;
    private double purchasePrice;
    private double salePrice;

    public Product(int id, String name, String manufacturer, double purchasePrice, double salePrice) {
        this.id = id;
        this.name = name;
        this.manufacturer = manufacturer;
        this.purchasePrice = purchasePrice;
        this.salePrice = salePrice;
    }

    public String toString() {
        return "[" + id + "] [" + name + "] [" + manufacturer + "] [" + purchasePrice + "] ["  + salePrice + "]";
    }

    public double getMarginal() {
        return salePrice - purchasePrice;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPurchasePrice() {
        return purchasePrice;
    }

    public double getSalePrice() {
        return salePrice;
    }

    public String getManufacturer() {
        return manufacturer;
    }
}
