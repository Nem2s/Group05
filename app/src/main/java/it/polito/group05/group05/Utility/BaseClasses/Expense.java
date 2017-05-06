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
        super(edb);
        map=new HashMap<>();
     /*   for(String i : edb.getMembers().keySet()){
            User_expense ue = new User_expense();
            ue.setDebt(edb.getMembers().get(i));
            ue.setId(i);
        }*/
    }
}

