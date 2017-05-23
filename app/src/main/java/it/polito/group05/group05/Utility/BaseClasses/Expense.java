package it.polito.group05.group05.Utility.BaseClasses;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Marco on 02/05/2017.
 */

public class Expense extends ExpenseDatabase {
    private List<User_expense> list_userExpense;

    public List< User_expense> getUsersExpense() {
        return list_userExpense;
    }

    public void setUserExpense(List<User_expense> list_userExpense) {
        this.list_userExpense = list_userExpense;
    }

    public Expense(){
        super();
        list_userExpense=new ArrayList<>();
    }
    public Expense(ExpenseDatabase edb){
        super(edb);
        list_userExpense=new ArrayList<>();
     /*   for(String i : edb.getMembers().keySet()){
            User_expense ue = new User_expense();
            ue.setDebt(edb.getMembers().get(i));
            ue.setId(i);
        }*/
    }
}

