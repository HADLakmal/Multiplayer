package com.mygdx.game;

/**
 * Created by Damindu on 3/18/2017.
 */
public class Car
{
    private String ID;
    private int x;
    private int y;

    public Car(String ID,int x, int y){
        this.ID = ID;
        this.x = x;
        this.y = y;

    }

    public String getID() {
        return ID;
    }

    public int getY() {
        return y;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void click(int cordinate){
        switch (cordinate){
            case 1:
                y+=5;
            case 2:
                x+=5;
            case 3:
                y-=5;
            case 4:
                x-=5;
                default:

        }

    }


}
