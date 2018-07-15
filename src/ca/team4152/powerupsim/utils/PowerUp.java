package ca.team4152.powerupsim.utils;

import java.util.ArrayList;

public class PowerUp {

    private static ArrayList<PowerUp> powerUps = new ArrayList<>();
    public static final int LEVITATE = 0;
    public static final int FORCE = 1;
    public static final int BOOST = 2;

    static {
        powerUps.add(new PowerUp(LEVITATE, -1, -1, 0));
        powerUps.add(new PowerUp(FORCE, -1, 10, 0));
        powerUps.add(new PowerUp(BOOST, -1, 10, 0));
    }

    private int id;
    private int startTime;
    private int duration;
    private int level;

    private PowerUp(int id, int startTime, int duration, int level){
        this.id = id;
        this.startTime = startTime;
        this.duration = duration;
        this.level = level;
    }

    public int getId(){
        return id;
    }

    public int getDuration(){
        return duration;
    }

    public int getLevel(){
        return level;
    }

    public void setLevel(int level){
        this.level = level;
    }

    public int getStartTime(){
        return startTime;
    }

    public void setStartTime(int startTime){
        this.startTime = startTime;
    }

    public static PowerUp newPowerUp(int id, int startTime, int level){
        for(PowerUp powerUp : powerUps){
            if(powerUp.getId() == id){
                return new PowerUp(powerUp.getId(), startTime, powerUp.getDuration(), level);
            }
        }
        return null;
    }
}