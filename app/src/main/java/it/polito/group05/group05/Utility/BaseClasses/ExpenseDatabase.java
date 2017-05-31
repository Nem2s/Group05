package it.polito.group05.group05.Utility.BaseClasses;

import java.util.HashMap;
import java.util.Map;

import it.polito.group05.group05.Utility.Interfaces.Namable;


public class ExpenseDatabase implements Namable{
    private String id;
    private String owner;
    private String name;
    private Double price;
    private String file;
    private long timestamp;
    private Map<String,Double> members;
    private Map<String,Object> payed;


    public ExpenseDatabase() {
        members= new HashMap<>();
        payed= new HashMap<>();
        id="";
        owner="";
        name="";
        price = 0.0;
        file = null;
    }

    public ExpenseDatabase(ExpenseDatabase edb) {
        this.id=edb.id;
        this.name=edb.name;
        this.owner=edb.owner;
        this.file=edb.file;
        this.price = edb.price;
        this.timestamp=edb.timestamp;
        if(edb.members== null) members = new HashMap<>();
        if(edb.payed== null) payed = new HashMap<>();
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
    public Double getPrice() {
        return price;
    }
    public void setPrice(Double price){
        this.price=price;
    }
    public void setFile(String file) {
        this.file = file;
    }
    public String getFile(){return file;}

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = (timestamp);
    }
    public Map<String, Double> getMembers() {
        return members;
    }

    public  void setMembers(Map<String, Double> map) {
        this.members = map;
    }

        public Map<String, Object> getPayed() {
        return payed;
    }

    public void setPayed(Map<String, Object> payed) {
        this.payed = payed;
    }

}

