package ca.team4152.powerupsim.utils.math;

public class VecPair {

    private Vector2d vec0;
    private Vector2d vec1;

    public VecPair(Vector2d vec0, Vector2d vec1){
        this.vec0 = vec0;
        this.vec1 = vec1;
    }

    public Vector2d getVec0(){
        return vec0;
    }

    public void setVec0(Vector2d vec0){
        this.vec0 = vec0;
    }

    public Vector2d getVec1(){
        return vec1;
    }

    public void setVec1(Vector2d vec1){
        this.vec1 = vec1;
    }

    @Override
    public boolean equals(Object object){
        return !(object == null || !(object instanceof VecPair)) &&
                ((VecPair) object).getVec0().equals(getVec0()) &&
                ((VecPair) object).getVec1().equals(getVec1());
    }

}