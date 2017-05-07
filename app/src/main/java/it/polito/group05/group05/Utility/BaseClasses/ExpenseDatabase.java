package it.polito.group05.group05.Utility.BaseClasses;

import java.util.HashMap;
import java.util.Map;

import it.polito.group05.group05.Utility.Interfaces.Namable;


public class ExpenseDatabase implements Namable{
    private String id;
    private String owner;
    private String name;
    private String description;
    private Double price;
    private String file;
    private int deadline;           //days
    private String timestamp;
    private Map<String,Double> members;
    protected int type;


    public ExpenseDatabase() {
        members= new HashMap<>();
        id="";
        owner="";
        name="";
        description="";
         price=0.0;
        type=0;
        file="";
        deadline=0;
    }

    public ExpenseDatabase(ExpenseDatabase edb) {
        this.id=edb.id;
        this.name=edb.name;
        this.deadline=edb.deadline;
        this.owner=edb.owner;
        this.file=edb.file;
        this.description=edb.getDescription();
        this.price = edb.price;
        this.timestamp=edb.timestamp;
        this.type=edb.type;
        if(edb.members== null) members = new HashMap<>();
        else  members = edb.members;


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
    public boolean isMandatory() {
        return type==0;
    }
    public int getType() {
        return type;
    }
    public void setType(int type) {
        type=type;
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

