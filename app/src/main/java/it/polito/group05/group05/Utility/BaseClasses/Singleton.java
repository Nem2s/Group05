package it.polito.group05.group05.Utility;

/**
 * Created by Marco on 28/03/2017.
 */

public class Singleton {
    private static Singleton mInstance = null;

    private Group mCurrentGroup;

    private Singleton(){
        mCurrentGroup = new Group();
    }

    public static Singleton getInstance(){
        if(mInstance == null)
        {
            mInstance = new Singleton();
        }
        return mInstance;
    }

    public void setmCurrentGroup(Group g) {
    this.mCurrentGroup = g;}

    public Group getmCurrentGroup() {
        return this.mCurrentGroup;
    }
}