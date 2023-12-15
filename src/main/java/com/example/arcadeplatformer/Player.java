package com.example.arcadeplatformer;

import com.example.arcadeplatformer.aabb_collision.Collidable;
import com.example.arcadeplatformer.aabb_collision.Collision;
import com.example.arcadeplatformer.aabb_collision.CollisionGeometry;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;

import java.util.ArrayList;

public class Player extends SolidObject{
    private int state=1;
    private boolean right=false;
    private  boolean left=false;
    private  boolean up=false;
    private boolean down=false;
    private float speed=1f;
    private float grav =.9f;
    private float max_vsp=8f;
    private float max_hsp=5f;
    protected PixelReader pixelReader;

    private float j_speed=15;
    private Image image;
    public void setMaze(Image image){
        this.image = image;
        pixelReader= image.getPixelReader();
    }
    @Override
    public boolean toDo() {
        //if hit ground stop jump animation
        if (grounded()&&state<0){
            state= Math.abs(state);
            if (state==1){
                loadSprite("MainWalkingRight.gif");
            }
            if (state==2){
                loadSprite("MainWalkingLeft.gif");
            }
        }
        //fetch inputs from controller
        right= Controller.isRight();
        left= Controller.isLeft();
        up= Controller.isUp();
        down= Controller.isDown();
        //reset speed

        if (right&&!left){setHsp(getHsp()+speed);
            if(state!=1&&state!=-1){
            loadSprite("MainWalkingRight.gif");state=1;}
        }
        if (!right&&left){setHsp(getHsp()-speed);
            if(state!=2&&state!=-2){
                loadSprite("MainWalkingLeft.gif");state=2;}
        }
        if (up&&!down){jump();
            if(state==2){
                loadSprite("MainJumpLeft.gif");state=-2;}
            if(state==1){
                loadSprite("MainJumpRight.gif");state=-1;}
        }
        if (!up&&down){setVsp(speed);}
        //was for debuging
       // if (left&&right){
       //     System.out.println(getX()+","+getY());
       // }
        //apply gravity
        setVsp(getVsp()+grav);
        //apply friction
        setHsp(getHsp()*.95f);
        //limit max speed
        if(getVsp()>max_vsp){
            setVsp(max_vsp);
        }
        if (getHsp()>max_hsp){
            setHsp(max_hsp);
        }
        if (getHsp()<-max_hsp){
            setHsp(-max_hsp);
        }
        if (pixelReader!=null){
            //checks for collision based on pixel values will use mask values instead
            mazeCollide();
        }
        setX(getX()+getHsp());
        setY(getY()+getVsp());
        super.toDo();
        return isMarkedForDeletion();
    }

    @Override
    public boolean debug() {
        return true;
    }
    private boolean grounded(){
        if (Collision.pixelCollision(this,pixelReader,0,1)){
           return true;
        }else return false;
    }

    private void jump(){
        //check if grounded
    if (grounded()){
        setVsp(getVsp()-j_speed);
    }
}

    //maze collision
    private void mazeCollide(){
        //check if collision will occur next frame
        float tmpVsp= (int) getVsp();
        float tmpHsp = (int) getHsp();
        //check in x dimension
        if (Collision.pixelCollision(this, pixelReader,Math.round(tmpHsp+(tmpHsp/Math.abs(tmpHsp)/2)),0)){
            //step back before steping forward to collide
            setX(Math.round(getX()-(tmpHsp/Math.abs(tmpHsp)/2)));
            if (Math.round(tmpHsp+(tmpHsp/Math.abs(tmpHsp)/2))!=0) {int c=0;while(!Collision.pixelCollision(this, pixelReader, Math.round(tmpHsp+(tmpHsp/Math.abs(tmpHsp)/2))/Math.abs(Math.round(tmpHsp+(tmpHsp/Math.abs(tmpHsp)/2))),0)&&c<200){setX(getX()+tmpHsp/Math.abs(tmpHsp));c+=1;}}
            setHsp(0);
        };
        //check in y dimension
        if (Collision.pixelCollision(this, pixelReader, 0,Math.round(tmpVsp+(tmpVsp/Math.abs(tmpVsp)/2)))){
            setY(Math.round(getY()-(tmpVsp/Math.abs(tmpVsp)/2)));
            if (Math.round(tmpVsp+(tmpVsp/Math.abs(tmpVsp)/2))!=0) {int c=0;while(!Collision.pixelCollision(this, pixelReader, 0,Math.round(tmpVsp+(tmpVsp/Math.abs(tmpVsp)/2))/Math.abs(Math.round(tmpVsp+(tmpVsp/Math.abs(tmpVsp)/2))))&&c<200){setY(getY()+tmpVsp/Math.abs(tmpVsp));c+=1;}}
            setVsp(0);
        };
    }

    @Override
    public CollisionGeometry getGeometry() {
       return new CollisionGeometry(getbBoxW(),getbBoxH());
    }

    @Override
    public double getMass() {
        return 1;
    }

    @Override
    public int getMask() {
        return 2;
    }

    @Override
    public void collisionEvent(Collidable collision) {
                    collision.markForDelettion();
    }

    @Override
    public void loadSprite(String image) {
        {
            sprite= loadImage(image,getbBoxW(),getbBoxH());
        }
    }
}
