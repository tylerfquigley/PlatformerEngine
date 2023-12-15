package com.example.arcadeplatformer;

import com.example.arcadeplatformer.aabb_collision.Collidable;
import com.example.arcadeplatformer.aabb_collision.CollisionGeometry;
import com.example.arcadeplatformer.rendering.Renderable;
import javafx.scene.image.Image;

public class Coin implements Collidable, Renderable {
    private Image sprite;
    private double x;
    private double y;
    private boolean delete=false;
    Coin(){
        loadSprite("inGameCoin.gif");
    }
    @Override
    public void setX(double x) {
                this.x=x;
    }

    @Override
    public void setY(double y) {
               this.y=y;
    }

    @Override
    public double getX() {
        return x;
    }

    @Override
    public double getY() {
        return y;
    }

    @Override
    public double getHsp() {
        return 0;
    }

    @Override
    public double getVsp() {
        return 0;
    }

    @Override
    public void setVsp(double vsp) {

    }

    @Override
    public void setHsp(double hsp) {

    }

    @Override
    public void reset() {

    }

    @Override
    public void markForDelettion() {
            delete = true;
    }

    @Override
    public boolean isMarkedForDeletion() {
        return delete;
    }

    @Override
    public boolean toDo() {
        return false;
    }

    @Override
    public boolean debug() {
        return false;
    }

    @Override
    public CollisionGeometry getGeometry() {
        return new CollisionGeometry(sprite.getWidth(),sprite.getHeight());
    }

    @Override
    public double getMass() {
        return -1;
    }

    @Override
    public int getMask() {
        return 0;
    }

    @Override
    public void collisionEvent(Collidable collision) {

    }

    @Override
    public void loadSprite(String image) {
                sprite=loadImage(image,35,35);
    }

    @Override
    public void draw() {
                getGc().drawImage(sprite,getX(),getY());
    }
}
