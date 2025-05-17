package entities.user.employee;

import entities.user.User;

// Класс Consumer для представления покупателя,
// содержит поля ID, полного имени, номера телефона, ID места работы, позиции на работе, статуса работы.
public class Employee extends User {
    private int storageId;
    private String job;
    private boolean isWork;

    public Employee(int id, int storageId, String fullName, String phoneNumber, String job, boolean isWork) {
        super(id, fullName, phoneNumber);
        this.storageId = storageId;
        this.job = job;
        this.isWork = isWork;
    }

    public String toString() {
        return "[" + id + "] [" + storageId + "] [" + fullName + "] [" + phoneNumber + "] [" + job + "] [" + (isWork ? 1 : 0) + "]";
    }

    public void manageEmployee(String job, int storageId) {
        this.job = job;
        this.storageId = storageId;
    }

    public void fireEmployee() {
        this.isWork = false;
    }

    public boolean isWorking() {
        return this.isWork;
    }

    public String getJob() {
        return job;
    }

    public int getStorageId() {
        return storageId;
    }

    public void setStorageId(int storageId) {
        this.storageId = storageId;
    }
}
