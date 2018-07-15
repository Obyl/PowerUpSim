package ca.team4152.powerupsim.utils;

import java.util.ArrayList;

/*
This describes all the stations that hold cubes.
Robots can visit these stations and pick up cubes.
CubeStations can be portals or cubes placed on the floor.
 */
public class CubeStation {

    private static ArrayList<CubeStation> stations = new ArrayList<>();

    static {
        stations.add(new CubeStation(7, 629, 19));   //Top portal.
        stations.add(new CubeStation(7, 629, 300));  //Bottom portal.

        stations.add(new CubeStation(10, 122, 161)); //Red cube pyramid.
        stations.add(new CubeStation(10, 529, 161)); //Blue cube pyramid.

        stations.add(new CubeStation(1, 205, 95));   //Red switch #1.
        stations.add(new CubeStation(1, 205, 124));  //Red switch #2.
        stations.add(new CubeStation(1, 205, 148));  //Red switch #3.
        stations.add(new CubeStation(1, 205, 175));  //Red switch #4.
        stations.add(new CubeStation(1, 205, 201));  //Red switch #5.
        stations.add(new CubeStation(1, 205, 227));  //Red switch #6.

        stations.add(new CubeStation(1, 445, 95));   //Blue switch #1.
        stations.add(new CubeStation(1, 445, 124));  //Blue switch #2.
        stations.add(new CubeStation(1, 445, 148));  //Blue switch #3.
        stations.add(new CubeStation(1, 445, 175));  //Blue switch #4.
        stations.add(new CubeStation(1, 445, 201));  //Blue switch #5.
        stations.add(new CubeStation(1, 445, 227));  //Blue switch #6.
    }

    private int cubes;
    private int x;
    private int y;

    public CubeStation(int cubes, int x, int y){
        this.cubes = cubes;
        this.x = x;
        this.y = y;
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }

    public boolean hasCubes(){
        return cubes > 0;
    }

    public void takeCube(){
        if(hasCubes()){
            cubes--;
        }
    }

    public static CubeStation getCubeStation(int x, int y){
        CubeStation result = null;

        for(CubeStation station : stations){
            if(station.getX() == x && station.getY() == y){
                result = station;
                break;
            }
        }

        return result;
    }

}