package ca.team4152.powerupsim;

import ca.team4152.powerupsim.utils.CubeStation;
import ca.team4152.powerupsim.utils.Path;
import ca.team4152.powerupsim.utils.Plate;
import ca.team4152.powerupsim.utils.PowerUp;
import ca.team4152.powerupsim.utils.Robot;
import ca.team4152.powerupsim.utils.RobotTask;
import ca.team4152.powerupsim.utils.SaveGUI;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Random;

public class Simulator {

    private static final int MATCH_LENGTH = 150;
    public static final int TEAM_NONE = 0;
    public static final int TEAM_RED = 1;
    public static final int TEAM_BLUE = 2;

    private Robot robot0;
    private Robot robot1;
    private Robot robot2;

    private Plate allianceSwitch;
    private Plate opponentSwitch;
    private Plate scale;

    private ArrayList<PowerUp> powerUpQueue = new ArrayList<>();
    private PowerUp currentPowerUp = null;
    private int currentPowerUpTime;
    private int levitate;
    private int boost;
    private int force;

    private int blueAllianceMax;
    private int blueOpponentMax;
    private int blueScaleMax;
    private int[] pointRecord = new int[MATCH_LENGTH];
    private String[] taskRecord = new String[MATCH_LENGTH];
    private String tasksThisRound = "";

    private int matchTime;
    private int points;
    private int pointsThisRound;
    private int vaultCubes = 0;
    private int climbers = 0;

    public void run(double robot0Speed, double robot1Speed, double robot2Speed, ArrayList<PowerUp> powerUps,
                    ArrayList<RobotTask> robot0Tasks, ArrayList<RobotTask> robot1Tasks, ArrayList<RobotTask> robot2Tasks){
        init(robot0Speed, robot1Speed, robot2Speed);
        setupPowerUps(powerUps);
        setupRobotTasks(robot0Tasks, robot1Tasks, robot2Tasks);

        for(matchTime = 0; matchTime < MATCH_LENGTH; matchTime++){
            pointsThisRound = 0;
            tasksThisRound = "";

            if(currentPowerUp == null){
                try{
                    for(PowerUp powerUp : powerUpQueue){
                        if(matchTime >= powerUp.getStartTime() && vaultCubes >= powerUp.getLevel()){
                            currentPowerUp = powerUp;
                            break;
                        }
                    }
                }catch (IndexOutOfBoundsException e){
                    e.printStackTrace();
                }
            }

            if(currentPowerUp != null){
                if(currentPowerUpTime == 0){
                    if(currentPowerUp.getId() == PowerUp.LEVITATE){
                        levitate = currentPowerUp.getLevel();
                    }else if(currentPowerUp.getId() == PowerUp.FORCE){
                        force = currentPowerUp.getLevel();
                    }else if(currentPowerUp.getId() == PowerUp.BOOST){
                        boost = currentPowerUp.getLevel();
                    }

                    vaultCubes -= currentPowerUp.getLevel();
                }

                if(currentPowerUpTime < currentPowerUp.getDuration() - 1){
                    currentPowerUpTime++;
                }else{
                    currentPowerUpTime = force = boost = 0;
                    powerUpQueue.remove(currentPowerUp);
                    currentPowerUp = null;
                }
            }

            double matchPercent = matchTime / MATCH_LENGTH;
            if(opponentSwitch.getBlueCubes() / blueAllianceMax <= matchPercent){
                opponentSwitch.addCube(TEAM_BLUE);
            }
            if(allianceSwitch.getBlueCubes() / blueOpponentMax <= matchPercent){
                allianceSwitch.addCube(TEAM_BLUE);
            }
            if(scale.getBlueCubes() / blueScaleMax < matchPercent){
                scale.addCube(TEAM_BLUE);
            }

            robot0.update();
            robot1.update();
            robot2.update();
            allianceSwitch.update();
            opponentSwitch.update();
            scale.update();

            if(allianceSwitch.getOwner() == TEAM_RED || force == 1 || force == 3){
                int switchMultiplier = (boost == 1 || boost == 3) ? 2 : 1;
                if(allianceSwitch.hasOwnerChanged()){
                    if(matchTime < 15){
                        pointsThisRound += 2;
                    }else{
                        pointsThisRound += 1;
                    }
                }else{
                    if(matchTime < 15){
                        pointsThisRound += (2 * switchMultiplier);
                    }else{
                        pointsThisRound += switchMultiplier;
                    }
                }
            }

            if(scale.getOwner() == TEAM_RED || force == 2 || force == 3){
                int scaleMultiplier = (boost == 2 || boost == 3) ? 2 : 1;
                if(scale.hasOwnerChanged()){
                    if(matchTime < 15){
                        pointsThisRound += 2;
                    }else{
                        pointsThisRound += 1;
                    }
                }else{
                    if(matchTime < 15){
                        pointsThisRound += (2 * scaleMultiplier);
                    }else{
                        pointsThisRound += scaleMultiplier;
                    }
                }
            }

            points += pointsThisRound;
            pointRecord[matchTime] = pointsThisRound;

            if(tasksThisRound.isEmpty()){
                tasksThisRound = "No tasks completed";
            }
            taskRecord[matchTime] = tasksThisRound;
        }

        if(climbers < 3 && levitate == 3){
            pointRecord[MATCH_LENGTH - 1] += 30;
        }

        createGameReport();
    }

    public void robotFinishedTask(Robot robot, int task){
        if(robot.completedClimb()){
            return;
        }

        boolean registerTask = false;
        switch (task){
            case RobotTask.AUTO_MOVE:
                if(!robot.completedAutoMobility() && matchTime < 15){
                    robot.finishAutoMobility();
                    pointsThisRound += 5;
                    registerTask = true;
                }
                break;
            case RobotTask.GET_CUBE:
                robot.giveCube();
                CubeStation.getCubeStation((int) robot.getX(), (int) robot.getY()).takeCube();
                registerTask = true;
                break;
            case RobotTask.CUBE_EXCHANGE:
                if(robot.hasCubes() && matchTime >= 15){
                    robot.takeCube();
                    robot.addCubesExchanged();
                    pointsThisRound += 5;
                    vaultCubes++;
                    registerTask = true;
                }
                break;
            case RobotTask.CUBE_ALLIANCE_SWITCH:
                if(robot.hasCubes()){
                    robot.takeCube();
                    robot.addCubePutOnAllianceSwitch();
                    allianceSwitch.addCube(TEAM_RED);
                    registerTask = true;
                }
                break;
            case RobotTask.CUBE_OPPONENT_SWITCH:
                if(robot.hasCubes()){
                    robot.takeCube();
                    robot.addCubePutOnOpponentSwitch();
                    opponentSwitch.addCube(TEAM_RED);
                    registerTask = true;
                }
                break;
            case RobotTask.CUBE_SCALE:
                if(robot.hasCubes()){
                    robot.takeCube();
                    robot.addCubePutOnScale();
                    scale.addCube(TEAM_RED);
                    registerTask = true;
                }
                break;
            case RobotTask.CLIMB:
                robot.finishClimb();
                climbers++;
                pointsThisRound += 30;
                registerTask = true;
                break;
        }

        if(!registerTask){
            return;
        }

        if(!tasksThisRound.isEmpty()){
            tasksThisRound += ", ";
        }
        tasksThisRound += robot.getName() + " " + RobotTask.getTask(task).getName();
    }

    private void init(double robot0Speed, double robot1Speed, double robot2Speed){
        Path.initPaths();

        points = pointsThisRound = 0;
        pointRecord = new int[MATCH_LENGTH];

        robot0 = new Robot(this, 1, 17, 65);
        robot0.setSpeed(robot0Speed);

        robot1 = new Robot(this, 2, 17, 186);
        robot1.setSpeed(robot1Speed);

        robot2 = new Robot(this, 3, 17, 253);
        robot2.setSpeed(robot2Speed);

        allianceSwitch = new Plate();
        opponentSwitch = new Plate();
        scale = new Plate();

        Random cubePlacer = new Random();
        blueAllianceMax = cubePlacer.nextInt(5) + 5;
        blueOpponentMax = cubePlacer.nextInt(5) + 3;
        blueScaleMax = cubePlacer.nextInt(5) + 3;
    }

    private void setupPowerUps(ArrayList<PowerUp> powerUps){
        powerUpQueue = powerUps;
    }

    private void setupRobotTasks(ArrayList<RobotTask> robot0Tasks,
                                 ArrayList<RobotTask> robot1Tasks,
                                 ArrayList<RobotTask> robot2Tasks){
        for(RobotTask task : robot0Tasks){
            robot0.addTask(task);
        }
        for(RobotTask task : robot1Tasks){
            robot1.addTask(task);
        }
        for(RobotTask task : robot2Tasks){
            robot2.addTask(task);
        }
    }

    private void createGameReport(){
        SaveGUI saveGUI = new SaveGUI();
        String savePath = saveGUI.getFileDir();

        if(savePath.isEmpty()){
            return;
        }

        try{
            LocalDateTime time = LocalDateTime.now();
            BufferedWriter writer = new BufferedWriter(new FileWriter(savePath));

            writer.write(time.toLocalDate() + " " + time.toLocalTime());
            writer.newLine();
            writer.write("====================");
            writer.newLine();
            writer.write("  Simulator Report");
            writer.newLine();
            writer.write("====================");
            writer.newLine();
            writer.newLine();

            writer.write("Points: " + points);
            writer.newLine();
            writer.newLine();

            writer.write("Cubes (red-blue):");
            writer.newLine();
            writer.write("Alliance Switch Cubes: " + allianceSwitch.getRedCubes() +
                    "-" + allianceSwitch.getBlueCubes());
            writer.newLine();
            writer.write("Opponent Switch Cubes: " + opponentSwitch.getRedCubes() +
                    "-" + opponentSwitch.getBlueCubes());
            writer.newLine();
            writer.write("Scale Cubes: " + scale.getRedCubes() +
                    "-" + scale.getBlueCubes());
            writer.newLine();
            writer.newLine();

            writer.write("Robot #1:");
            writer.newLine();
            writer.write("Moving Speed: " + robot0.getSpeed() + "ft/s");
            writer.newLine();
            String tasks0 = "";
            int taskNum0 = 0;
            for(RobotTask task : robot0.getTasks()){
                tasks0 += task.getName();
                if(++taskNum0 < robot0.getTasks().size()){
                    tasks0 += ", ";
                }
            }
            writer.write("Tasks: " + tasks0);
            writer.newLine();
            writer.write("Auto Mobility: " + robot0.completedAutoMobility());
            writer.newLine();
            writer.write("Climbed: " + robot0.completedClimb());
            writer.newLine();
            writer.write("Cubes Exchanged: " + robot0.getCubesExchanged());
            writer.newLine();
            writer.write("Cubes Put on Alliance Switch: " + robot0.getCubesPutOnAllianceSwitch());
            writer.newLine();
            writer.write("Cubes Put on Opponent Switch: " + robot0.getCubesPutOnOpponentSwitch());
            writer.newLine();
            writer.write("Cubes Put on Scale: " + robot0.getCubesPutOnScale());
            writer.newLine();
            writer.newLine();

            writer.write("Robot #2:");
            writer.newLine();
            writer.write("Moving Speed: " + robot1.getSpeed() + "ft/s");
            writer.newLine();
            String tasks1 = "";
            int taskNum1 = 0;
            for(RobotTask task : robot1.getTasks()){
                tasks1 += task.getName();
                if(++taskNum1 < robot1.getTasks().size()){
                    tasks1 += ", ";
                }
            }
            writer.write("Tasks: " + tasks1);
            writer.newLine();
            writer.write("Auto Mobility: " + robot1.completedAutoMobility());
            writer.newLine();
            writer.write("Climbed: " + robot1.completedClimb());
            writer.newLine();
            writer.write("Cubes Exchanged: " + robot1.getCubesExchanged());
            writer.newLine();
            writer.write("Cubes Put on Alliance Switch: " + robot1.getCubesPutOnAllianceSwitch());
            writer.newLine();
            writer.write("Cubes Put on Opponent Switch: " + robot1.getCubesPutOnOpponentSwitch());
            writer.newLine();
            writer.write("Cubes Put on Scale: " + robot1.getCubesPutOnScale());
            writer.newLine();
            writer.newLine();

            writer.write("Robot #3:");
            writer.newLine();
            writer.write("Moving Speed: " + robot2.getSpeed() + "ft/s");
            writer.newLine();
            String tasks2 = "";
            int taskNum2 = 0;
            for(RobotTask task : robot2.getTasks()){
                tasks2 += task.getName();
                if(++taskNum2 < robot2.getTasks().size()){
                    tasks2 += ", ";
                }
            }
            writer.write("Tasks: " + tasks2);
            writer.newLine();
            writer.write("Auto Mobility: " + robot2.completedAutoMobility());
            writer.newLine();
            writer.write("Climbed: " + robot2.completedClimb());
            writer.newLine();
            writer.write("Cubes Exchanged: " + robot2.getCubesExchanged());
            writer.newLine();
            writer.write("Cubes Put on Alliance Switch: " + robot2.getCubesPutOnAllianceSwitch());
            writer.newLine();
            writer.write("Cubes Put on Opponent Switch: " + robot2.getCubesPutOnOpponentSwitch());
            writer.newLine();
            writer.write("Cubes Put on Scale: " + robot2.getCubesPutOnScale());
            writer.newLine();
            writer.newLine();

            writer.write("=====================");
            writer.newLine();
            writer.write("  Game Play-by-Play");
            writer.newLine();
            writer.write("=====================");
            writer.newLine();
            writer.write("<second>: <points that round> (<tasks completed that round>)");
            writer.newLine();
            for(int i = 0; i < MATCH_LENGTH; i++){
                writer.write((i + 1) + ": " + pointRecord[i] + " (" + taskRecord[i] + ")");
                writer.newLine();
            }

            writer.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}