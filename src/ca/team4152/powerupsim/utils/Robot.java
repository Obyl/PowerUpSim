package ca.team4152.powerupsim.utils;

import ca.team4152.powerupsim.Simulator;
import ca.team4152.powerupsim.utils.math.Vector2d;

import java.util.ArrayList;

public class Robot {

    //Task handling.
    private ArrayList<RobotTask> taskQueue;
    private RobotTask currentTask = null;
    private int currentTaskIndex;
    private int currentTaskTime = -1;
    private double currentMoveTime = -1;
    private double currentMoveLength;

    //Robot variables.
    private String robotName = "";
    private int holdingCubes = 1;
    private Vector2d position;
    private double speed;
    private double moveAddition;
    private Simulator simulator;

    //Robot stat tracking.
    private int cubesExchanged;
    private int cubesPutOnAllianceSwitch;
    private int cubesPutOnOpponentSwitch;
    private int cubesPutOnScale;
    private boolean climbed;
    private boolean autoMobility;

    public Robot(Simulator simulator, int robotNum, int x, int y){
        this.position = new Vector2d(x, y);
        this.simulator = simulator;
        this.robotName = "Robot #" + robotNum;

        taskQueue = new ArrayList<>();
    }

    public String getName(){
        return robotName;
    }

    public double getX(){
        return position.getX();
    }

    public double getY(){
        return position.getY();
    }

    public double getSpeed(){
        return speed;
    }

    public void setSpeed(double speed){
        this.speed = speed;
    }

    public void giveCube(){
        holdingCubes++;
    }

    public void takeCube(){
        if(hasCubes()){
            holdingCubes--;
        }
    }

    public boolean hasCubes(){
        return holdingCubes > 0;
    }

    public int getCubesExchanged(){
        return cubesExchanged;
    }

    public void addCubesExchanged(){
        cubesExchanged++;
    }

    public int getCubesPutOnAllianceSwitch(){
        return cubesPutOnAllianceSwitch;
    }

    public void addCubePutOnAllianceSwitch(){
        cubesPutOnAllianceSwitch++;
    }

    public int getCubesPutOnOpponentSwitch(){
        return cubesPutOnOpponentSwitch;
    }

    public void addCubePutOnOpponentSwitch(){
        cubesPutOnOpponentSwitch++;
    }

    public int getCubesPutOnScale(){
        return cubesPutOnScale;
    }

    public void addCubePutOnScale(){
        cubesPutOnScale++;
    }

    public boolean completedAutoMobility(){
        return autoMobility;
    }

    public void finishAutoMobility(){
        autoMobility = true;
    }

    public boolean completedClimb(){
        return climbed;
    }

    public void finishClimb(){
        climbed = true;
    }

    public void update(){
        if(currentTask == null){
            try{
                currentTask = taskQueue.get(currentTaskIndex);
                currentTask.calcRobotTarget(this);
            }catch (IndexOutOfBoundsException e){
                //no print stack trace because this will fail many times and it's ok
                return;
            }
        }

        if(!completedAutoMobility() && getX() >= 124){
            simulator.robotFinishedTask(this, RobotTask.AUTO_MOVE);
        }

        if(currentTask.getX() != position.getX() || currentTask.getY() != position.getY()){
            if(currentTask.getX() == -1 && currentTask.getY() == -1){
                currentTaskIndex++;
                return;
            }

            if(currentMoveTime < 0){
                Vector2d start = new Vector2d(position.getX(), position.getY());
                Vector2d finish = new Vector2d(currentTask.getX(), currentTask.getY());

                currentMoveLength = Path.getPathLength(start, finish);
                moveAddition = currentMoveLength / speed;
                currentMoveTime = 0;
            }

            if(currentMoveTime < currentMoveLength){
                currentMoveTime += moveAddition;
            }else{
                position.setX(currentTask.getX());
                position.setY(currentTask.getY());
                currentMoveTime = -1;
                currentMoveLength = 0;
                moveAddition = -1;
                update();
            }
        }else{
            if(currentTaskTime < currentTask.getTimeToComplete()){
                currentTaskTime++;
            }else{
                simulator.robotFinishedTask(this, currentTask.getId());
                currentTaskIndex++;
                currentTaskTime = 0;
                currentTask = null;
            }
        }
    }

    public void addTask(RobotTask task){
        taskQueue.add(task);
    }

    public ArrayList<RobotTask> getTasks(){
        return taskQueue;
    }
}