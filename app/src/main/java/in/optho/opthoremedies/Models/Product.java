package in.optho.opthoremedies.Models;

/**
 * Created by krishna on 4/10/17.
 */

public class Product {
    private String id;
    private String name;
    private String priority;
    private String counter;

    public Product(String id, String name, String priority, String counter) {
        this.id = id;
        this.name = name;
        this.priority = priority;
        this.counter = counter;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getCounter() {
        return counter;
    }

    public void setCounter(String counter) {
        this.counter = counter;
    }
}
