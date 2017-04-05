package it.polito.group05.group05.Utility.BaseClasses;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by antonino on 04/04/2017.
 */

public class User_expense extends User {
    Group group;
    Expense expense;
    Double debt=0.0;
    TYPE_EXPENSE typeExpense;
    public User_expense(){
        super();}

    public static List<User> createListUserExpense(Group g,Expense e){
        List<User> user_expenses = new ArrayList<>();
        List<User> u = g.getMembers();
        for(User i : u){
            User_expense x = new User_expense(g,e,e.getType(),i.getId());
            x.setProfile_image(i.getProfile_image());
         //   User_expense x = new User_expense();
            x.setId(i.getId());
            x.setUser_name(i.getUser_name());
            user_expenses.add(x);
        }
        return user_expenses;
    }
    public static void createListUserExpense(List<User> users,Expense e){
        List<User_expense> user_expenses = new ArrayList<>();
    }

    public User_expense(Group p,Expense s,TYPE_EXPENSE t, String user){
        this.group=p;
        this.expense=s;
        this.typeExpense=t;
        int i = user.compareTo(s.getOwner().getId());
        if(t==TYPE_EXPENSE.MANDATORY && i!=0) {
            debt -= s.getPrice();
            debt/=(float)p.getMembers().size();
        }
        else if(t==TYPE_EXPENSE.MANDATORY && i==0) debt = s.getPrice()-(s.getPrice()/(float)p.getMembers().size());
        else if (t==TYPE_EXPENSE.NOTMANDATORY) debt = 0.0;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group p) {
        this.group = p;
    }

    public Expense getExpense() {
        return expense;
    }

    public void setExpense(Expense s) {
        this.expense = s;
    }

    public Double getDebt() {
        return debt;
    }

    public void setDebt(Double debt) {
        this.debt = debt;
    }

    public TYPE_EXPENSE getTe() {
        return typeExpense;
    }

    public void setTe(TYPE_EXPENSE te) {
        this.typeExpense = te;
    }
}
