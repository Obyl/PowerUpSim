package ca.team4152.powerupsim.utils;

import ca.team4152.powerupsim.utils.math.Distance;

import java.util.ArrayList;

public class RobotTask {

    private static ArrayList<RobotTask> tasks = new ArrayList<>();
    public static final int AUTO_MOVE = 0;
    public static final int GET_CUBE = 1;
    public static final int CUBE_EXCHANGE = 2;
    public static final int CUBE_ALLIANCE_SWITCH = 3;
    public static final int CUBE_OPPONENT_SWITCH = 4;
    public static final int CUBE_SCALE = 5;
    public static final int CLIMB = 6;

    static {
        tasks.add(new RobotTask(AUTO_MOVE, 1, 132, 65, 132, 186, 132, 253));
        tasks.add(new RobotTask(GET_CUBE, 2, 629, 19, 629, 300, 122, 161, 529, 161, 205, 95, 205,
                124, 205, 148, 205, 175, 205, 201, 205, 227, 445, 95, 445, 124, 445, 175,
                445, 201, 445, 227));
        tasks.add(new RobotTask(CUBE_EXCHANGE, 1, 3, 125));
        tasks.add(new RobotTask(CUBE_ALLIANCE_SWITCH, 2, 140, 110, 168, 84));
        tasks.add(new RobotTask(CUBE_OPPONENT_SWITCH, 2, 480, 237, 512, 210));
        tasks.add(new RobotTask(CUBE_SCALE, 4, 325, 72));
        tasks.add(new RobotTask(CLIMB, 15, 305, 161));
    }

    private int id;
    private int timeToComplete;
    private int robotTarget;
    private int[] x, y;

    private RobotTask(int id, int timeToComplete, int... location){
        this.id = id;
        this.timeToComplete = timeToComplete;

        x = new int[location.length / 2];
        y = new int[location.length / 2];
        for(int i = 0; i < location.length / 2; i++){
            x[i] = location[i * 2];
            y[i] = location[(i * 2) + 1];
        }
    }

    public int getId(){
        return id;
    }

    public int getTimeToComplete(){
        return timeToComplete;
    }

    public void setTimeToComplete(int timeToComplete){
        this.timeToComplete = timeToComplete;
    }

    public int getX(){
        return x[robotTarget];
    }

    public int getY(){
        return y[robotTarget];
    }

    public String getName(){
        switch (id){
            case AUTO_MOVE:
                return "Auto Movement";
            case GET_CUBE:
                return "Get Cube";
            case CUBE_EXCHANGE:
                return "Cube Exchange";
            case CUBE_ALLIANCE_SWITCH:
                return "Put Cube on Alliance Switch";
            case CUBE_OPPONENT_SWITCH:
                return "Put Cube on Opponent Switch";
            case CUBE_SCALE:
                return "Put Cube on Scale";
            case CLIMB:
                return "Climb";
            default:
                return "";
        }
    }

    public static RobotTask newTask(int id){
        RobotTask result = null;

        for(RobotTask task : tasks){
            if(task.getId() == id){
                result = task;
                break;
            }
        }

        return result;
    }

    public static RobotTask getTask(int id){
        RobotTask result = null;

        for(RobotTask task : tasks){
            if(task.getId() == id){
                result = task;
                break;
            }
        }

        return result;
    }

    public void calcRobotTarget(Robot robot){
        if(x.length == 1){
            robotTarget = 0;
            return;
        }

        double shortestDistance = 1000000D;
        int shortestDistanceIndex = -1;
        for(int i = 0; i < x.length; i++){
            double distanceToTarget = Distance.calculateDistance(robot.getX(), robot.getY(), x[i], y[i]);

            if(distanceToTarget < shortestDistance){
                if(id == GET_CUBE && !CubeStation.getCubeStation(x[i], y[i]).hasCubes()){
                    continue;
                }

                shortestDistance = distanceToTarget;
                shortestDistanceIndex = i;
            }
        }

        if(shortestDistanceIndex == -1){
            x[0] = -1;
            y[0] = -1;
            shortestDistanceIndex = 0;
        }

        robotTarget = shortestDistanceIndex;
    }
}