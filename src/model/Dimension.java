package model;

public class Dimension implements Comparable<Dimension>{
    private int x;
    private int y;

    public Dimension(int x, int y){
        this.x=x;
        this.y=y;
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }

    @Override
    public int compareTo(Dimension other){
        if(this.x == other.x && this.y == other.y) return 0;
        else{
            if(this.x < other.x || this.y < other.y) return -1;
            else return 1;
        }
    }

    @Override
    public int hashCode(){
        return Integer.parseInt(String.valueOf(this.x)+ this.y);
    }

    @Override
    public boolean equals(Object o){
        if(this == o) return true;
        if(o == null) return false;
        if(this.getClass() != o.getClass()) return false;
        Dimension other = (Dimension) o;
        return this.compareTo(other) == 0;
    }
}