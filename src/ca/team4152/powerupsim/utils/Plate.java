package ca.team4152.powerupsim.utils;

import ca.team4152.powerupsim.Simulator;

/*
This class represents a cube "plate"
A cube plate is anything cubes can be placed on, this cale the scale and the two switches.
 */
public class Plate {

    private int owner;
    private int lastOwner;
    private int redCubes;
    private int blueCubes;
    private boolean ownerChanged;

    public int getRedCubes(){
        return redCubes;
    }

    public int getBlueCubes(){
        return blueCubes;
    }

    public int getOwner(){
        return owner;
    }

    public boolean hasOwnerChanged(){
        return ownerChanged;
    }

    public void update(){
        ownerChanged = lastOwner != owner;
        lastOwner = owner;
    }

    public void addCube(int owner){
        if(owner == Simulator.TEAM_RED){
            redCubes++;
        }else if(owner == Simulator.TEAM_BLUE){
            blueCubes++;
        }

        if(redCubes > blueCubes){
            this.owner = Simulator.TEAM_RED;
        }else if(blueCubes > redCubes){
            this.owner = Simulator.TEAM_BLUE;
        }else{
            this.owner = Simulator.TEAM_NONE;
        }
    }

}