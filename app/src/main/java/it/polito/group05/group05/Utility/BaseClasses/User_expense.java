package it.polito.group05.group05.Utility.BaseClasses;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by antonino on 04/04/2017.
 */

public class User_expense extends UserDatabase {
    GroupDatabase group;
    ExpenseDatabase expense;
    Double debt=0.0;
    TYPE_EXPENSE typeExpense;
    boolean isSelected;
    private double customValue;

    public User_expense(UserDatabase userDatabase){
        super();
        this.name = userDatabase.getName();
        this.id = userDatabase.getId();
        this.authKey = userDatabase.getAuthKey();
        this.telNumber = userDatabase.getTelNumber();
        this.email = userDatabase.getEmail();
        this.iProfile = userDatabase.getiProfile();
        this.balance = userDatabase.getBalance();
        this.customValue = 0.0;
        isSelected = true;
    }


    public boolean hasCustomValue() {
             return customValue > 0;
    }
    public double getCustomValue() {
               return customValue;
    }
    public User_expense setCustomValue(double customValue) {
                this.customValue = customValue;
        return this;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public User_expense setSelected(boolean selected) {
        isSelected = selected;
        return this;
    }


    public User_expense setCustomValue(Double customValue) {
        this.customValue = customValue;return this;
    }

    public static List<UserDatabase> createListUserExpense(GroupDatabase g,Expense e){
        List<UserDatabase> user_expenses = new ArrayList<>();
        Map<String,Object> u = g.getMembers();
        for(Object i1 : u.values()){
            if(!(i1 instanceof UserDatabase)) continue;
            UserDatabase i = (UserDatabase)i1;
            User_expense x = new User_expense(g,e,e.isMandatory(),i.getId());
            x.setiProfile(i.getiProfile());
            x.setId(i.getId());
            x.setName(i.getName());
            user_expenses.add(x);
        }
        return user_expenses;
    }
    public static void createListUserExpense(List<UserDatabase> users,Expense e){
        List<User_expense> user_expenses = new ArrayList<>();
    }

    public TYPE_EXPENSE getTypeExpense() {
        return typeExpense;
    }

    public void setTypeExpense(TYPE_EXPENSE typeExpense) {
        this.typeExpense = typeExpense;
    }



    public User_expense(GroupDatabase p,Expense s,boolean isMandatory, String user){
        this.group=p;
        this.expense=s;
        this.typeExpense=(isMandatory)?TYPE_EXPENSE.MANDATORY:TYPE_EXPENSE.NOTMANDATORY;
        int i = user.compareTo(s.getOwner());
        if(isMandatory && i!=0) {
            debt -= s.getPrice();
            debt/=(float)p.getMembers().size();
        }
        else if(isMandatory&& i==0) debt = s.getPrice()-(s.getPrice()/(float)p.getMembers().size());
        else if (!isMandatory) debt = 0.0;
    }

    public GroupDatabase getGroup() {
        return group;
    }

    public void setGroup(GroupDatabase p) {
        this.group = p;
    }

    public ExpenseDatabase getExpense() {
        return expense;
    }

    public void setExpense(ExpenseDatabase s) {
        this.expense = s;
    }

    public Double getDebt() {
        return customValue;
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