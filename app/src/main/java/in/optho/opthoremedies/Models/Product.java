package in.optho.opthoremedies.Models;


import android.os.Parcel;
import android.os.Parcelable;

public class Product implements Parcelable {
    private int id;
    private String code;
    private int priority;
    private String name;
    private int category;
    private int design;

    private String datetime;
    private int counter;

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public Product(String datetime, int id, String code, int priority, String name, int category, int design, int counter) {
        this.id = id;
        this.code = code;
        this.priority = priority;
        this.name = name;
        this.category = category;
        this.design = design;

        this.datetime = datetime;

        this.counter = counter;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int pDefault) {
        this.priority = pDefault;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public int getDesign() {
        return design;
    }

    public void setDesign(int design) {
        this.design = design;
    }

  


    public int compareTo(Product compareProduct) {

        int compareQuantity =  compareProduct.getId();

        //ascending order
        return this.id - compareQuantity;

        //descending order
        //return compareQuantity - this.quantity;

    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.code);
        dest.writeInt(this.priority);
        dest.writeString(this.name);
        dest.writeInt(this.category);
        dest.writeInt(this.design);
        dest.writeString(this.datetime);
        dest.writeInt(this.counter);
    }

    protected Product(Parcel in) {
        this.id = in.readInt();
        this.code = in.readString();
        this.priority = in.readInt();
        this.name = in.readString();
        this.category = in.readInt();
        this.design = in.readInt();
        this.datetime = in.readString();
        this.counter = in.readInt();
    }

    public static final Parcelable.Creator<Product> CREATOR = new Parcelable.Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel source) {
            return new Product(source);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };
}
