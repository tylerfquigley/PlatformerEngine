package com.example.arcadeplatformer;

import com.example.arcadeplatformer.aabb_collision.Collidable;
import com.example.arcadeplatformer.aabb_collision.Collision;
import com.example.arcadeplatformer.rendering.Renderable;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;

import static java.lang.System.currentTimeMillis;

public class Controller implements Runnable,inputHandler{
    private boolean run=false;
    private static boolean up;
    private static boolean down;

    private static boolean left;

    private static boolean right;

    private Scene s;

    private int frameRate=120;
    public GraphicsContext gc;
    private Thread t1;
    private Image maze1;

    private AnimationTimer a1;
    private Level level;



    @FXML
    Canvas can;

    @FXML
    public void initialize(){
        gc = can.getGraphicsContext2D();
        maze1 = new Image("levelmask.png");
        level = Level.getLevel();
        level.setHeight(maze1.getHeight());
        level.setWidth(maze1.getWidth());
        level.setGc(gc);
        can.setWidth(level.getWidth());
        can.setHeight(level.getHeight());
    }
    @FXML
    private void reset() {
        for (GameObject gameObject : level.gameObjects) {
                gameObject.reset();
        }
    }
    public void startRendering(){

        a1 = new AnimationTimer() {
            @Override
            public void handle(long now) {

                //first maze

                gc.clearRect(0,0,can.getWidth(),can.getHeight());
                gc.drawImage(maze1,0,0);
                //get objects that implement renderable
                ArrayList<Renderable> ren =Level.getInstances(Renderable.class);
                for (Renderable renderable: ren){
                    renderable.draw();
                }


            }
        };
        a1.start();
        //start backend logic when rending begins
       t1 = new Thread(this);
        t1.start();
    }

    public void stopRendering(){
        if (a1!=null){a1.stop();}
        if (t1!=null){
           this.run=false;
        }
    }
    public void addToLevel(GameObject gameObject){
        level.gameObjects.add(gameObject);
    }
    @Override
    //backend logic
    public void run() {
        //set run true and loop until made false
        run=true;
        long c;
        long s = currentTimeMillis();
        while(run){try {


            c= currentTimeMillis();
            if((c-s)>(1000/frameRate)){
                c = currentTimeMillis();
                s = currentTimeMillis();

                iterateThroughAll();
                Collision.handleCollisions(Level.getInstances(Collidable.class));}
            }catch (ConcurrentModificationException e){
            //do nothing
        }
        }
    }
    private void iterateThroughAll(){
        for (GameObject gameObject : level.gameObjects) {
            if (gameObject.isMarkedForDeletion()){
                level.gameObjects.remove(gameObject);
            }else {
            gameObject.toDo();}
        }
    }
    @Override
    public void setInputs(KeyEvent e) {
        switch (e.getCode()) {
            case W -> up=true;
            case D -> right=true;
            case S -> down=true;
            case A -> left=true;
            case UP -> up=true;
            case RIGHT -> right=true;
            case DOWN -> down=true;
            case LEFT -> left=true;

        }
    }

    @Override
    public void unSetInputs(KeyEvent e) {
        switch (e.getCode()){
            case W -> up=false;
            case D -> right=false;
            case S -> down=false;
            case A -> left=false;
            case UP -> up=false;
            case RIGHT -> right=false;
            case DOWN -> down=false;
            case LEFT -> left=false;
        }
    }

    @Override
    public void bindToScene(Scene scene) {
        scene.setOnKeyReleased(this::unSetInputs);
        scene.setOnKeyPressed(this::setInputs);
    }
    public static boolean isRight(){
        return right;
    };
    public static boolean isLeft(){
        return left;
    };
    public static boolean isUp(){
        return up;
    };
    public static boolean isDown(){
        return down;
    };

    public static void resetInputs(){
        up=false;
        down=false;
        right=false;
        left=false;
    }
}