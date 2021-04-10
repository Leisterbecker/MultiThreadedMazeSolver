package view;

import controller.solver.ParallelBfsSolver;
import controller.Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.HashMap;

public class View {
    private final Controller ctr;
    private MazePanel mpanel;
    private ControlPanel cpanel;
    private final JFrame frame;
    private Timer loop;
    private int delay;
    private boolean running;

    public View(int size) {
        ctr = new Controller(size);
        cpanel = new ControlPanel(this);
        mpanel = new MazePanel(this);
        frame = new JFrame("Parallel Maze controller.solver.Solver");
        frame.setLayout(new BorderLayout());
        delay = 1;

        ActionListener taskPerformer = evt -> {
            if (!ctr.getSolver().reachedExit()) {
                ctr.solveStep();
                frame.repaint();
            } else {
                if(ctr.getSolver().path.size()==0){
                    ctr.getSolver().generateShortestPath();
                    cpanel.setVisitedCellsCount(mpanel.countVisitedCells());
                    cpanel.setCellsInPathCount(mpanel.countCellsInPath());
                }
                frame.repaint();
                if (ctr.getSolver() instanceof ParallelBfsSolver) {
                    ((ParallelBfsSolver) ctr.getSolver()).clearParallelSolver();
                }
            }
        };
        loop = new Timer(delay, taskPerformer);
        running = false;
        mpanel.setSolver(cpanel.getAlgorithms()[cpanel.getAlgorithmChooser().getSelectedIndex()]);

        frame.add(cpanel, BorderLayout.WEST);
        frame.add(mpanel, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(Frame.MAXIMIZED_BOTH);
        frame.setVisible(true);

    }

    public Controller getController(){
        return ctr;
    }

    public ControlPanel getControlPanel(){
        return cpanel;
    }

    public MazePanel getMazePanel(){
        return mpanel;
    }

    public Timer getLoop(){
        return loop;
    }

    public boolean getRunning(){
        return running;
    }

    public void setRunning(boolean running){
        this.running = running;
    }
}
