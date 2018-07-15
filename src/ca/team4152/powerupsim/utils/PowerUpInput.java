package ca.team4152.powerupsim.utils;

import javafx.scene.layout.VBox;

import java.util.ArrayList;

public class PowerUpInput {

    private VBox gui = new VBox();
    private ArrayList<PowerUp> powerUps = new ArrayList<>();

    public VBox getGui(){
        return gui;
    }

    public void setGui(VBox gui){
        this.gui = gui;
    }

    public ArrayList<PowerUp> getPowerUps(){
        return powerUps;
    }

}