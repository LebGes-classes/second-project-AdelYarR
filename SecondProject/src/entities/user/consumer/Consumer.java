package entities.user.consumer;

import entities.user.User;

// Класс Consumer для представления покупателя,
// содержит поля ID, полного имени, номера телефона.
public class Consumer extends User {

    public Consumer(int id, String fullName, String phoneNumber) {
        super(id, fullName, phoneNumber);
    }

    public String toString() {
        return "[" + id + "] [" + fullName + "] [" + phoneNumber + "]";
    }
}
