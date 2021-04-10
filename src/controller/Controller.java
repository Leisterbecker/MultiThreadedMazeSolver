package controller;

import controller.solver.*;
import model.Cell;
import model.Model;

public class Controller {
    private Model model;
    private Solver solver;
    private String name;
    private int size;

    public Controller(int size){
        this.size = size;
        model = Model.create(size);
        solver = new BfsSolver(model);
        name = "Sequential Solver";
    }

    public void generateMaze(){
        model = Model.create(size);
        solver.setModel(model);
    }

    public Solver getSolver(){
        return solver;
    }

    public void setSolver(String name){
       this.name = name;
       if(name.equals("Parallel Solver")) solver = new ParallelBfsSolver(model);
       else solver = new BfsSolver(model);
    }

    public void solveStep(){ solver.step(solver.id); }

    public Cell getExit(){
        return model.getExit();
    }

    public Cell getStartCell(){
        return model.getStart();
    }

    public Cell getCell(int x, int y){
        return model.getCell(x,y);
    }

    public int getSize(){
        return model.getSize();
    }

    public void setSize(int size){
        this.size = size;
    }

    public void resetModel(){
        model.setUnvisited();
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }
}
