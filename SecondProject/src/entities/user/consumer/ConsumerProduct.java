package entities.user.consumer;

// Класс Consumer Product для представления товаров покупателя,
// содержит поля ID, ID покупателя, ID товара, количество купленного товара.
public class ConsumerProduct {
    private int id;
    private int consumerId;
    private int productId;
    private int quantity;

    public ConsumerProduct(int id, int consumerId, int productId, int quantity) {
        this.id = id;
        this.consumerId = consumerId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public String toString() {
        return "[" + id + "] [" + consumerId + "] [" + productId + "] [" + quantity + "]";
    }

    public int getId() { return id; }

    public int getConsumerId() {
        return consumerId;
    }

    public void setConsumerId(int consumerId) {
        this.consumerId = consumerId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
