package controller.solver;

import model.Cell;
import model.Dimension;
import model.Model;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public abstract class Solver{

    public Model model;
    public Cell[][] cells;
    public Stack<Cell> stack;
    public List<Cell> path;
    public Cell start;
    public boolean started;
    public boolean finished;
    public int id;


    public abstract void step(int id);


    public boolean inPath(Cell cell){
        return path.contains(cell);
    }

    public void generateShortestPath(){
        if(finished){
            Cell cur = model.getExit();
            while(cur.getParent()!=null){
                path.add(cur);
                cur = cur.getParent();
            }
        }
    }

    public void setModel(Model model){
        this.model = model;
    }

    public boolean reachedExit(){
        return finished;
    }

    public List<Cell> getAvailableNeighbours(Cell cell){
        List<Cell> neighbours = new ArrayList<>();
        Dimension[] neighbourDims = new Dimension[]{new Dimension(cell.dim.getX(),cell.dim.getY()-1),
                                                    new Dimension(cell.dim.getX(),cell.dim.getY()+1),
                                                    new Dimension(cell.dim.getX()-1,cell.dim.getY()),
                                                    new Dimension(cell.dim.getX()+1,cell.dim.getY())};

        if(validNeighbour(neighbourDims[0].getX(), neighbourDims[0].getY(), cell.north)) neighbours.add(cells[cell.dim.getX()][cell.dim.getY()-1]);
        if(validNeighbour(neighbourDims[1].getX(), neighbourDims[1].getY(),cell.south)) neighbours.add(cells[cell.dim.getX()][cell.dim.getY()+1]);
        if(validNeighbour(neighbourDims[2].getX(), neighbourDims[2].getY(),cell.west)) neighbours.add(cells[cell.dim.getX()-1][cell.dim.getY()]);
        if(validNeighbour(neighbourDims[3].getX(), neighbourDims[3].getY(),cell.east)) neighbours.add(cells[cell.dim.getX()+1][cell.dim.getY()]);
        return neighbours;
    }

    public boolean validNeighbour(int x, int y, boolean direction){
        return !direction && model.inBounds(x,y) && !cells[x][y].visited;
    }
}
