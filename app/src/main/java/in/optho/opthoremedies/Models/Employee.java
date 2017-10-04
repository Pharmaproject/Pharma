package in.optho.opthoremedies.Models;

/**
 * Created by krishna on 4/10/17.
 */

public class Employee {
    private String id;
    private String pin;

    public Employee(String id, String pin) {
        this.id = id;
        this.pin = pin;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }
}
