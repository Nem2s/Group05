package it.polito.group05.group05.Utility.Event;

/**
 * Created by antonino on 11/05/2017.
 */

public class LeaveGroupEvent {
    boolean b;
    Double d ;
    public LeaveGroupEvent(Double d){
        b=(d<-0.001 || d>0.001)?false:true;
        this.d=d;

    }
    public  boolean canLeave(){
        return b;
    }
    public boolean isCredit(){
        return d>0;
    }


}
