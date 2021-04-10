package controller.solver;

import model.Cell;
import model.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ParallelBfsSolver extends Solver implements ISolver{

    private final ArrayList<ParallelBfsSolver.WorkerThread> workers;

    public ParallelBfsSolver(Model model){
        id = 0;
        this.model = model;
        stack = new Stack<>();
        path = new ArrayList<>();
        cells = model.getCells();
        start = null;
        started = true;
        finished = false;
        start = null;
        workers = new ArrayList<>();
        workers.add(new ParallelBfsSolver.WorkerThread(this,id, true));
        workers.get(0).start();
    }

    @Override
    public void step(int id){
        boolean allWaiting = true;
        for(int i = 0; i < workers.size(); i++){
            if(!workers.get(i).getState().equals(Thread.State.WAITING)) allWaiting = false;
        }
        if(allWaiting){
            System.out.println("all waiting: sending signals");
            for(int i = 0; i < workers.size(); i++){
                workers.get(i).lock.lock();
                workers.get(i).condition.signal();
                workers.get(i).lock.unlock();
            }
        }
    }

    public void clearParallelSolver(){
        for(int i = 0; i < workers.size(); i++){
            workers.remove(i);
        }
        System.out.println("operation completed, threads stopped and workers removed");
    }


    static class WorkerThread extends Thread {

        private boolean isStartThread;
        private ParallelBfsSolver solver;
        private boolean deadEnd;
        private Lock lock;
        private Condition condition;
        private int id;
        private Stack<Cell> stack;

        public WorkerThread(ParallelBfsSolver solver, int id, boolean startThread){
            isStartThread = startThread;
            this.deadEnd = false;
            this.solver = solver;
            this.id = id;
            stack = new Stack<>();
            lock = new ReentrantLock();
            condition = lock.newCondition();
            if(isStartThread){
                solver.start = solver.model.getStart();
                solver.cells[solver.start.dim.getX()][solver.start.dim.getY()].visited = true;
                stack.add(solver.cells[solver.start.dim.getX()][solver.start.dim.getY()]);
            }
        }

        public void run(){
            while(!solver.finished) {
                lock.lock();
                try {
                    step(id);
                    System.out.println("worker " + id + " waits");
                    condition.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finally {
                    lock.unlock();
                }
            }
        }


        public void step(int id){
            System.out.println("Worker " + id + " step");
            if(!solver.finished && !stack.isEmpty()){
                Cell cur = stack.pop();
                if(cur == solver.model.getExit()) solver.finished = true;
                List<Cell> neigbours = solver.getAvailableNeighbours(cur);
                cur.visited=true;
                if(neigbours.size()==3){
                    createNewWorker(neigbours.get(1),cur,id+1);
                    createNewWorker(neigbours.get(2),cur,id+2);
                    proceedToNeighbour(cur, neigbours);
                }
                else if(neigbours.size()==2){
                    createNewWorker(neigbours.get(1),cur,id+1);
                    proceedToNeighbour(cur, neigbours);
                }
                else if(neigbours.size()==1){
                    proceedToNeighbour(cur, neigbours);
                }
                else{
                    System.out.println("dead end");
                    deadEnd = true;
                }
            }
        }

        public void proceedToNeighbour(Cell current, List<Cell> neighbours){
            neighbours.get(0).workerId = id;
            neighbours.get(0).setParent(current);
            stack.add(neighbours.get(0));
        }

        public void createNewWorker(Cell currentNeighbour, Cell parent, int id){
            ParallelBfsSolver.WorkerThread w = new ParallelBfsSolver.WorkerThread(solver, id, false);
            currentNeighbour.workerId = id;
            currentNeighbour.setParent(parent);
            w.stack.add(currentNeighbour);
            solver.workers.add(w);
            w.start();
        }
    }
}


