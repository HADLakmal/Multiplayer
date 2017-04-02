package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Damindu on 3/18/2017.
 */
public class Car extends Sprite
{
    Vector2 position;

    public Car(Texture texture){
        super(texture);
        position = new Vector2(getX(),getY());
    }

    public boolean hasMoved(){
        if (position.x!=getX()||position.y!=getY()){
            position.x = getX();
            position.y = getY();
            return true;
        }
        return false;
    }


}
