package controller.solver;

import model.Cell;
import model.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class BfsSolver extends Solver implements ISolver {

    public BfsSolver(Model model){
        id = 0;
        this.model = model;
        stack = new Stack<>();
        path = new ArrayList<>();
        cells = model.getCells();
        start = null;
        started = true;
        finished = false;
        start = model.getStart();
        cells[start.dim.getX()][start.dim.getY()].visited = true;
        stack.add(cells[start.dim.getX()][start.dim.getY()]);
    }

    @Override
    public void step(int id){
        if(!finished){
            Cell cur = stack.pop();
            if(!stack.isEmpty()) {
                step(id);
            }
            if(cur == model.getExit()) finished = true;
            List<Cell> neigbours = getAvailableNeighbours(cur);
            for(Cell c: neigbours){
                c.visited = true;
                c.workerId = id;
                c.setParent(cur);
                stack.push(c);
            }
            cur.visited=true;
        }
    }
}
