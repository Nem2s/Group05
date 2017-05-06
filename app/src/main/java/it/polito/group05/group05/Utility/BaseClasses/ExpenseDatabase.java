package it.polito.group05.group05.Utility.BaseClasses;

import android.Manifest;

import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import it.polito.group05.group05.Utility.Interfaces.Namable;


public class ExpenseDatabase implements Namable{
    private String id;
    private String owner;
    private String name;
    private String description;
    private Double price;
    private TYPE_EXPENSE type;
    private String file;
    private int deadline;           //days
    private String timestamp;
    private Map<String,Double> members;
    protected int type_int;


    public ExpenseDatabase() {
        members= new HashMap<>();
        id="";
        owner="";
        name="";
        description="";
         price=0.0;
        type_int=0;
        file="";
        deadline=0;
    }

    public ExpenseDatabase(ExpenseDatabase edb) {
        this.setId(edb.getId());
        this.setName(edb.getName());
        this.setDeadline(edb.getDeadline());
        this.setOwner(edb.getOwner());
        this.setFile(edb.getFile());
        this.setDescription(edb.getDescription());
        this.setPrice(edb.getPrice());
        this.setTimestamp(edb.getTimestamp().toString());
        this.setType(edb.type_int);
        members = new HashMap<>();
    }

    //METHODS
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOwner() {return owner;}
    public void setOwner(String owner) {this.owner = owner;}
    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name= name;
    }
    public String getDescription(){
        return description;
    }
    public void setDescription(String description){
        this.description= description;
    }
    public Double getPrice() {
        return price;
    }
    public void setPrice(Double price){
        this.price=price;
    }
    public TYPE_EXPENSE getType() {
        return type;
    }
    public void setType(int type) {
        this.type = (type==0)?TYPE_EXPENSE.MANDATORY:TYPE_EXPENSE.NOTMANDATORY;
        type_int=type;
    }
    public void setFile(String file) {
        this.file = file;
    }
    public String getFile(){return file;}
    public int getDeadline() {
        return deadline;
    }
    public void setDeadline(int deadline) {
        this.deadline = deadline;
    }
    public String getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(String timestamp) {
        this.timestamp = (timestamp);
    }
    public Map<String, Double> getMembers() {
        return members;
    }
    public  void setMembers(Map<String, Double> map) {
        this.members = map;



    }

}

