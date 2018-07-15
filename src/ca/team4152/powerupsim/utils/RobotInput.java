package ca.team4152.powerupsim.utils;

import javafx.scene.layout.VBox;

import java.util.ArrayList;

public class RobotInput {

    private VBox gui = new VBox();
    private double speed = 10;
    private ArrayList<RobotTask> tasks = new ArrayList<>();

    public VBox getGui(){
        return gui;
    }

    public void setGui(VBox gui){
        this.gui = gui;
    }

    public double getSpeed(){
        return speed;
    }

    public void setSpeed(double speed){
        this.speed = speed;
    }

    public ArrayList<RobotTask> getTasks(){
        return tasks;
    }

}