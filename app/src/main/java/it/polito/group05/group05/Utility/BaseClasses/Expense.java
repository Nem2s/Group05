package it.polito.group05.group05.Utility.BaseClasses;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Marco on 02/05/2017.
 */

public class Expense extends ExpenseDatabase {
    private Map<String, User_expense> map;

    public Map<String, User_expense> getUsersExpense() {
        return map;
    }

    public void setUserExpense(Map<String, User_expense> map) {
        this.map = map;
    }

    public Expense(){
        super();
        map=new HashMap<>();
    }
    public Expense(ExpenseDatabase edb){
        super();
        this.setId(edb.getId());
        this.setName(edb.getName());
        this.setDeadline(edb.getDeadline());
        this.setOwner(edb.getOwner());
        this.setFile(edb.getFile());
        this.setDescription(edb.getDescription());
        this.setPrice(edb.getPrice());
        this.setTimestamp(edb.getTimestamp());
        this.setType(edb.getType());
        map=new HashMap<>();
    }
}

