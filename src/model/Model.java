package model;

import java.util.ArrayList;
import java.util.List;

public class Model {
    private final int size;
    private Cell[][] cells;
    private Cell start;
    private Cell exit;


    public enum Direction{
        NORTH,
        SOUTH,
        WEST,
        EAST
    }

    private Model(int size){
        this.size = size;
        exit = null;
        initMaze();
        generateMaze();
        generateExit();
    }

    public static Model create(int size){
        return new Model(size);
    }

    public Cell getCell(int x, int y){
        return cells[x][y];
    }

    public Cell[][] getCells(){
        return cells;
    }

    public int getSize(){
        return size;
    }


    public Cell getExit(){
        return exit;
    }

    public Cell getStart(){
        return start;
    }

    private void initMaze(){
        cells = new Cell[size][size];
        for(int i = 0; i < size; i++){
            for(int j = 0; j < size; j++){
                cells[i][j] = new Cell(new Dimension(i,j), true, true, true, true);
            }
        }
    }


    public void generateStart(){
        int ran = (int) (Math.random() * 4);
        int ran_axis = (int) (Math.random() * (size-1));
        switch (ran) {
            case 0 -> {
                start = cells[0][ran_axis];
            }
            case 1 -> {
                start = cells[size - 1][ran_axis];
            }
            case 2 -> {
                start = cells[ran_axis][size - 1];
            }
            case 3 -> {
                start = cells[ran_axis][0];
            }
        }
    }

    private void generateExit(){
        int ran = (int) (Math.random() * 4);
        int ran_axis = (int) (Math.random() * (size-1));
        switch (ran) {
            case 0 -> {
                cells[0][ran_axis].west = false;
                exit = cells[0][ran_axis];
            }
            case 1 -> {
                cells[size - 1][ran_axis].east = false;
                exit = cells[size - 1][ran_axis];
            }
            case 2 -> {
                cells[ran_axis][size - 1].south = false;
                exit = cells[ran_axis][size - 1];
            }
            case 3 -> {
                cells[ran_axis][0].north = false;
                exit = cells[ran_axis][0];
            }
        }
    }

    private void generateMaze(){
        generateStart();
        generate(start);
        setUnvisited();
    }

    private void generate(Cell current){
        current.visited = true;
        Cell next;
        while((next = getUnvisitedNeighbour(current)) != null){
            Direction dir = getDirection(current, next);
            removeBorders(current,next,dir);
            generate(next);
        }
    }

    public void setUnvisited(){
        for(int i = 0; i < size; i++){
            for(int j = 0; j < size; j++){
                cells[i][j].visited=false;
            }
        }
    }

    private Cell getUnvisitedNeighbour(Cell current){
        List<Cell> neighbours = new ArrayList<>();
        Dimension cur = current.dim;
        Dimension[] dims = new Dimension[]{new Dimension((cur.getX()-1), cur.getY()),
                                           new Dimension(cur.getX()+1, cur.getY()),
                                           new Dimension(cur.getX(), cur.getY()-1),
                                           new Dimension(cur.getX(), cur.getY()+1)};
        for (Dimension dim : dims) {
            if (inBounds(dim.getX(), dim.getY())) {
                Cell neigh = cells[dim.getX()][dim.getY()];
                if (!neigh.visited) {
                    if(!neigh.dim.equals(cur)){
                        neighbours.add(neigh);
                    }
                }
            }
        }
        if(neighbours.isEmpty()) return null;
        return neighbours.get((int) (Math.random() * neighbours.size()));
    }

    public boolean inBounds(int x, int y){
        if(x < 0 || x >= size) return false;
        if(y < 0 || y >= size) return false;
        return true;
    }

    private void removeBorders(Cell from, Cell to, Direction dir){
        switch (dir) {
            case NORTH -> {
                from.north = false;
                to.south = false;
            }
            case SOUTH -> {
                from.south = false;
                to.north = false;
            }
            case WEST -> {
                from.west = false;
                to.east = false;
            }
            case EAST -> {
                from.east = false;
                to.west = false;
            }
        }
    }

    private Direction getDirection(Cell from, Cell to){
        if(from.dim.getX()==to.dim.getX()){
            if(from.dim.getY() < to.dim.getY()){
                return Direction.SOUTH;
            }
            else{
                return Direction.NORTH;
            }
        }
        else{
            if(from.dim.getX() < to.dim.getX()){
                return Direction.EAST;
            }
            else{
                return Direction.WEST;
            }
        }
    }


}
