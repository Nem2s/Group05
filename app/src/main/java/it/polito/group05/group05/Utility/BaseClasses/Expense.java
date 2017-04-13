package it.polito.group05.group05.Utility.BaseClasses;

import android.net.Uri;

import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;



public class Expense{
    private String id;
    private User owner;
    private String name;
    private String description;
    private Double price;
    private TYPE_EXPENSE type;
    private Uri uri_file;
    private File file;
    private int deadline;//days
    private Timestamp timestamp;
    private TreeMap<String, User> lista_partecipanti=new TreeMap<>();
    private String image;
    private boolean hasFile = false;


    public Expense(String id, User owner, String name, String description, Double price,
                   TYPE_EXPENSE type, Uri uri_file, File file, int deadline,
                   Timestamp timestamp){
        this.id = id;
        this.name= name;
        this.description= description;
        this.price=price;
        this.type=type;
        this.uri_file=uri_file;
        this.file = file;
        this.deadline= deadline;

        this.timestamp= timestamp;
    }
    public Expense(String id, User owner, String name, String description, Double price, TYPE_EXPENSE type, int deadline,
                   Timestamp timestamp){
        this.id = id;
        this.name= name;
        this.description= description;
        this.price=price;
        this.type=type;
        this.owner = owner;
        this.deadline= deadline;
        this.timestamp= timestamp;
    }

    //METHODS
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public User getOwner() {return owner;}

    public void setOwner(User owner) {this.owner = owner;}

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
    public Uri getUri_file(){
        return  uri_file;
    }

    public void addFile( Uri u){
        file = new File(u.getPath());
        uri_file = u;
    }

    public void setFileTrue(){
        hasFile = true;
    }
    public boolean getFilePresence(){
        return hasFile;
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
    public List<User> getPartecipants(){
List<User> l = new ArrayList<>(lista_partecipanti.values());
        return l;
    }
    public  void setPartecipants(List<User> list){

        for(User i : list){
            lista_partecipanti.put(i.getId(),i);
        }
    }
    public  void setPartecipant(User user){
        lista_partecipanti.put(user.getId(),user);
    }


    public Double getDebtUser(String id){

        User s =this.lista_partecipanti.get(id);
        if(s instanceof User_expense) return ((User_expense) s).getDebt();
        else return -1.0;


    }

    public void setDebtUser(String id,Double d){

        User s =this.lista_partecipanti.get(id);
        if(s instanceof User_expense)
            ((User_expense) s).setDebt(d);



    }

}

