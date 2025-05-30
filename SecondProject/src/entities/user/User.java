package entities.user;

import entities.interfaces.HasId;

// Класс Consumer для представления покупателя,
// содержит поля ID, полного имени, номера телефона.
public class User implements HasId {
    protected int id;
    protected String fullName;
    protected String phoneNumber;

    public User(int id, String fullName, String phoneNumber) {
        this.id = id;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
    }

    public int getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}
