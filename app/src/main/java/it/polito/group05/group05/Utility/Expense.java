package it.polito.group05.group05.Utility;

import java.io.File;
import java.security.Timestamp;
import java.util.TreeMap;

/**
 * Created by Anna on 29/03/2017.
 */
enum TYPE_EXPENSE {MANDATORY, NOTMANDATORY};

public class Expense {
    private String id;
    private String name;
    private String description;
    private Double price;
    private TYPE_EXPENSE type;
    private File file;
    private int deadline;           //days
    private Timestamp timestamp;
    private TreeMap<String, User> lista_partecipanti;

    //METHODS
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public void setType(TYPE_EXPENSE type) {
        this.type = type;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public int getDeadline() {
        return deadline;
    }

    public void setDeadline(int deadline) {
        this.deadline = deadline;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void addPartecipant(User u){
        if(!lista_partecipanti.containsKey(u.getId())){
            lista_partecipanti.put(u.getId(),u);
        }
    }
    public User getPartecipants(String id){
        return lista_partecipanti.get(id);
    }


}

