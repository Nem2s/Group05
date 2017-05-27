package it.polito.group05.group05.Utility.BaseClasses;

import java.security.Timestamp;

/**
 * Created by andre on 24-May-17.
 */

public class HistoryClass {

    private String who;
    private String what;
    private long when;
    private int type;

    public HistoryClass() {
    }

    public HistoryClass(String who, String what, long when, int type) {
        this.who = who;
        this.what = what;
        this.when = when;
        this.type = type;
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

    public long getWhen() {
        return when;
    }

    public void setWhen(long when) {
        this.when = when;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
