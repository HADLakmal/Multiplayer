package com.mygdx.game;

/**
 * Created by Damindu on 3/19/2017.
 */
public class Mainthread extends Thread {

    private Multiplayer multiplayer;
    public Mainthread(Multiplayer multiplayer){

        this.multiplayer = multiplayer;

    }

    @Override
    public void run(){
        multiplayer.create();
    }
}
