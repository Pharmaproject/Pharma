package in.optho.opthoremedies.Models;

/**
 * Created by krishna on 4/10/17.
 */

public class Product {
    private String id;
    private String code;
    private String pDefault;
    private String name;
    private String category;
    private String design;
    private String brand;
    private String openpunch;
    private String graphic;
    private String carton;
    private String indication;
    private String description;
    private String closepunch;
    private String customicon;

    public Product(String id, String code, String pDefault, String name, String category, String design, String brand, String openpunch, String graphic, String carton, String indication, String description, String closepunch, String customicon) {
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
        //
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

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getOpenpunch() {
        return openpunch;
    }

    public void setOpenpunch(String openpunch) {
        this.openpunch = openpunch;
    }

    public String getGraphic() {
        return graphic;
    }

    public void setGraphic(String graphic) {
        this.graphic = graphic;
    }

    public String getCarton() {
        return carton;
    }

    public void setCarton(String carton) {
        this.carton = carton;
    }

    public String getIndication() {
        return indication;
    }

    public void setIndication(String indication) {
        this.indication = indication;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getClosepunch() {
        return closepunch;
    }

    public void setClosepunch(String closepunch) {
        this.closepunch = closepunch;
    }

    public String getCustomicon() {
        return customicon;
    }

    public void setCustomicon(String customicon) {
        this.customicon = customicon;
    }
}
