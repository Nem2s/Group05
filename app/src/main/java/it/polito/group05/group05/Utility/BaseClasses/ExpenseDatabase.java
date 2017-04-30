package it.polito.group05.group05.Utility.BaseClasses;


public class ExpenseDatabase {

    public String id;
    public String name;
    public String description;
    public String owner;
    public double price;
    public String deadline;
    public String timestamp;
    //public String fileUrl;
    //public String pictureUrl;


    public ExpenseDatabase(String id, String name, String description, String owner, double price, String deadline, String timestamp) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.owner = owner;
        this.price = price;
        this.deadline = deadline;
        this.timestamp = timestamp;
    }

    public ExpenseDatabase() {
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
