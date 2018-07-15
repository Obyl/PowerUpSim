package ca.team4152.powerupsim.utils.math;

public class Distance {

    public static double calculateDistance(Vector2d pos0, Vector2d pos1){
        return calculateDistance(pos0.getX(), pos0.getY(), pos1.getX(), pos1.getY());
    }

    public static double calculateDistance(double x0, double x1, double y0, double y1){
        double rise = y0 - y1;
        double run = x0 - x1;

        return Math.sqrt((rise * rise) + (run * run));
    }

}