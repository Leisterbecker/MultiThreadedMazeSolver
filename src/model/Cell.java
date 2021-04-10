package model;

public  class Cell  implements Comparable<Cell>{
    private Cell parent;
    public Dimension dim;
    public boolean visited;
    public boolean north;
    public boolean south;
    public boolean east;
    public boolean west;
    public int workerId;

    public Cell(Dimension dim, boolean north, boolean south, boolean west, boolean east){
        parent = null;
        this.dim = dim;
        this.north = north;
        this.south = south;
        this.west = west;
        this.east = east;
        visited = false;
        workerId = 0;
    }

    public Cell getParent(){
        return parent;
    }

    public void setParent(Cell parent){
        this.parent = parent;
    }

    @Override
    public int compareTo(Cell other){
        return this.dim.compareTo(other.dim);
    }

    @Override
    public int hashCode(){
        return this.dim.hashCode();
    }

    @Override
    public boolean equals(Object o){
        if(this == o) return true;
        if(o == null) return false;
        if(this.getClass() != o.getClass()) return false;
        Cell other = (Cell) o;
        return this.compareTo(other) == 0;
    }
}
