package it.polito.group05.group05.Utility.BaseClasses;

import android.widget.ImageView;

/**
 * Created by Andrea on 25-Mar-17.
 */

public class Expense {

    private String name;
    private String description;
    private String owner;
    private double amount;
    //private String amount;
    private ImageView e_image;
    //File
    //Deadline
    public Expense(String expenseName, double expenseAmount, String expenseOwner, ImageView expenseImage )
    {
        this.e_image = expenseImage;
        this.name = expenseName;
        this.amount= expenseAmount;
        this.owner = expenseOwner;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ImageView getE_image() {
        return e_image;
    }

    public void setE_image(ImageView e_image) { this.e_image = e_image; }

    public String getOwner() { return owner; }

    public void setOwner(String owner) { this.owner = owner; }
}
