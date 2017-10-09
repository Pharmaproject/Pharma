package in.optho.opthoremedies.Models;

import java.io.Serializable;

/**
 * Created by krishna on 4/10/17.
 */

public class Product implements Serializable {
    private String id;
    private String code;
    private String pDefault;
    private String name;
    private String category;
    private String design;
    private byte[] brand;
    private byte[] openpunch;
    private byte[] graphic;
    private byte[] carton;
    private byte[] indication;
    private byte[] description;
    private byte[] closepunch;
    private byte[] customicon;

    private String datetime;
    private int counter;


    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public Product(String id, String code, String pDefault, String name, String category, String design,
                   byte[] brand, byte[] openpunch, byte[] graphic, byte[] carton, byte[] indication,
                   byte[] description, byte[] closepunch, byte[] customicon, String datetime, int counter) {
        this.id = id;
        this.code = code;
        this.pDefault = pDefault;
        this.name = name;
        this.category = category;
        this.design = design;
        this.brand = brand;
        this.openpunch = openpunch;
        this.graphic = graphic;
        this.carton = carton;
        this.indication = indication;
        this.description = description;
        this.closepunch = closepunch;
        this.customicon = customicon;
        this.datetime = datetime;

        this.counter = counter;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getpDefault() {
        return pDefault;
    }

    public void setpDefault(String pDefault) {
        this.pDefault = pDefault;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDesign() {
        return design;
    }

    public void setDesign(String design) {
        this.design = design;
    }

    public byte[] getBrand() {
        return brand;
    }

    public void setBrand(byte[] brand) {
        this.brand = brand;
    }

    public byte[] getOpenpunch() {
        return openpunch;
    }

    public void setOpenpunch(byte[] openpunch) {
        this.openpunch = openpunch;
    }

    public byte[] getGraphic() {
        return graphic;
    }

    public void setGraphic(byte[] graphic) {
        this.graphic = graphic;
    }

    public byte[] getCarton() {
        return carton;
    }

    public void setCarton(byte[] carton) {
        this.carton = carton;
    }

    public byte[] getIndication() {
        return indication;
    }

    public void setIndication(byte[] indication) {
        this.indication = indication;
    }

    public byte[] getDescription() {
        return description;
    }

    public void setDescription(byte[] description) {
        this.description = description;
    }

    public byte[] getClosepunch() {
        return closepunch;
    }

    public void setClosepunch(byte[] closepunch) {
        this.closepunch = closepunch;
    }

    public byte[] getCustomicon() {
        return customicon;
    }

    public void setCustomicon(byte[] customicon) {
        this.customicon = customicon;
    }


    public int compareTo(Product compareProduct) {

        int compareQuantity = Integer.parseInt( compareProduct.getId());

        //ascending order
        return Integer.parseInt(this.id) - compareQuantity;

        //descending order
        //return compareQuantity - this.quantity;

    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }
}
