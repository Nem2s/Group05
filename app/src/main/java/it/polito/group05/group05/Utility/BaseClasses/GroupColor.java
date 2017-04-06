package it.polito.group05.group05.Utility.BaseClasses;

/**
 * Created by Marco on 02/04/2017.
 */

public class GroupColor {
    private int toolbarColor;
    private int toolbarTextColor;
    private int cardsColor;

    public GroupColor(int toolbarColor, int toolbarTextColor, int cardsColor) {
        this.toolbarColor = toolbarColor;
        this.toolbarTextColor = toolbarTextColor;
        this.cardsColor = cardsColor;
    }

    public GroupColor() {};

    public int getToolbarColor() {
        return toolbarColor;
    }

    public void setToolbarColor(int toolbarColor) {
        this.toolbarColor = toolbarColor;
    }

    public int getToolbarTextColor() {
        return toolbarTextColor;
    }

    public void setToolbarTextColor(int toolbarTextColor) {
        this.toolbarTextColor = toolbarTextColor;
    }

    public int getCardsColor() {
        return cardsColor;
    }

    public void setCardsColor(int cardsColor) {
        this.cardsColor = cardsColor;
    }
}
