package it.polito.group05.group05.Utility.BaseClasses;

/**
 * Created by andre on 24-May-17.
 */

public class HistoryClass {

    private String who;
    private String what;
    private String when;
    private int type;

    public HistoryClass() {
    }

    public String getWho() {
        return who;
    }

    public void setWho(String who) {
        this.who = who;
    }

    public String getWhat() {
        return what;
    }

    public void setWhat(String what) {
        this.what = what;
    }

    public String getWhen() {
        return when;
    }

    public void setWhen(String when) {
        this.when = when;
    }

    public int getType() { return type; }

    public void setType(int type) { this.type = type; }
}
