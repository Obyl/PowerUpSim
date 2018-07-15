package ca.team4152.powerupsim.utils.math;

public class Vector2d {

    private double x;
    private double y;

    public Vector2d(double x, double y){
        this.x = x;
        this.y = y;
    }

    public double getX(){
        return x;
    }

    public void setX(double x){
        this.x = x;
    }

    public double getY(){
        return y;
    }

    public void setY(double y){
        this.y = y;
    }

    @Override
    public boolean equals(Object object){
        return !(object == null || !(object instanceof Vector2d)) &&
                ((Vector2d) object).getX() == this.getX() &&
                ((Vector2d) object).getY() == this.getY();
    }

}