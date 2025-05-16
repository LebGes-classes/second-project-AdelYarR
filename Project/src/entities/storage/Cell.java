package entities.storage;

import entities.interfaces.HasId;

// Класс Cell для представления ячейки для товаров,
// содержит поля ID, ID места склада, адрес, ID склада, доходности
public class Cell implements HasId {
    private int id;
    private int storageId;
    private int productId;
    private int capacity;
    private int quantity;

    public Cell(int id, int storageId, int productId, int capacity, int quantity) {
        this.id = id;
        this.storageId = storageId;
        this.productId = productId;
        this.capacity = capacity;
        this.quantity = quantity;
    }

    public String toString() {
        return "[" + id + "] [" + storageId + "] [" + productId + "] [" + capacity + "] [" + quantity + "] ";
    }

    public int getRemainingSpace() {
        return capacity - quantity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStorageId() { return storageId; }

    public int getProductId() { return productId; }

    public int getCapacity() {
        return capacity;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setProductId(int productId) { this.productId = productId; }
}
