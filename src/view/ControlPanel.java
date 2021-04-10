package view;

import controller.Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ControlPanel extends JPanel {

    private View view;

    private JLabel visitedCellsCount;
    private JLabel cellsInPathCount;
    private JComboBox mazeChooser;
    private JComboBox algChooser;
    private String[] mazeTypes = {"BFS", "DFS"};
    private String[] algs = {"Sequential Solver", "Parallel Solver"};
    private String[] colorModes = {"Standard", "Gradient"};
    private final int delayStep = 10;

    public ControlPanel(View view){
        this.view = view;
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.add(createConfigPanel());
        this.add(createMazeTypePanel());
        this.add(createAlgorithmPanel());
        this.add(createStatisticsPanel());

        for(Component c: this.getComponents()){
            ((JComponent) c).setAlignmentX(Component.LEFT_ALIGNMENT);
        }
    }

    public JPanel createConfigPanel(){
        JPanel configPanel = new JPanel();
        configPanel.setLayout(new BoxLayout(configPanel, BoxLayout.Y_AXIS));
        JComboBox colorMode = new JComboBox(colorModes);
        colorMode.setSelectedIndex(0);
        colorMode.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(colorMode.getSelectedIndex()==0){
                    view.getMazePanel().initWorkerColors(0);
                }
                else{
                    view.getMazePanel().initWorkerColors(1);
                }
            }
        });
        JTextField mazeSize = new JTextField();
        mazeSize.setName("Maze size");
        JButton generateMaze = new JButton("Generate maze");
        generateMaze.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateMaze(mazeSize);
            }
        });
        configPanel.add(createExecutePanel());
        configPanel.add(mazeSize);
        configPanel.add(generateMaze);
        configPanel.add(createSpeedPanel());
        configPanel.add(colorMode);
        configPanel.setBorder(BorderFactory.createMatteBorder(0,0,1,0,Color.BLACK));
        configPanel.setMaximumSize(configPanel.getPreferredSize());
        return configPanel;
    }

    public JPanel createExecutePanel(){
        JPanel executePanel = new JPanel();
        executePanel.setLayout(new FlowLayout());
        JButton startStop = new JButton("Start/Stop");
        JButton reset = new JButton("Reset");
        reset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                reset();

            }
        });
        startStop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Timer loop = view.getLoop();
                if(loop.isRunning()){
                    loop.stop();
                    view.setRunning(false);
                }
                else{
                    loop.start();
                    view.setRunning(true);
                }
            }
        });
        executePanel.add(startStop);
        executePanel.add(reset);
        return executePanel;
    }

    public JPanel createSpeedPanel(){
        JPanel speedPanel = new JPanel();
        speedPanel.setLayout(new FlowLayout());
        JLabel speedLabel = new JLabel("Speed:");
        JTextField speed = new JTextField();
        JButton increaseSpeed = new JButton("-");
        JButton decreaseSpeed = new JButton("+");
        speed.setPreferredSize(new Dimension(50,20));
        increaseSpeed.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setSpeed(true);
            }
        });
        decreaseSpeed.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setSpeed(false);
            }
        });
        speedPanel.add(speedLabel);
        speedPanel.add(increaseSpeed);
        speedPanel.add(decreaseSpeed);
        return speedPanel;
    }

    public JPanel createMazeTypePanel(){
        JPanel mazetypePanel = new JPanel();
        mazetypePanel.setLayout(new FlowLayout());
        JLabel mazetypeLabel = new JLabel("Maze type:");
        mazeChooser = new JComboBox(mazeTypes);
        mazeChooser.setMaximumSize(new Dimension(150,20));
        mazeChooser.setSelectedIndex(0);
        mazetypePanel.add(mazetypeLabel);
        mazetypePanel.add(mazeChooser);
        mazetypePanel.add(new JSeparator(SwingConstants.HORIZONTAL),2);
        mazetypePanel.setBorder(BorderFactory.createMatteBorder(0,0,1,0,Color.BLACK));
        mazetypePanel.setMaximumSize(mazetypePanel.getPreferredSize());
        return mazetypePanel;
    }

    public JPanel createAlgorithmPanel(){
        JPanel algorithmPanel = new JPanel();
        algorithmPanel.setLayout(new FlowLayout());
        JLabel algorithmLabel = new JLabel("Solve algorithm:");
        algChooser = new JComboBox(algs);
        algChooser.setMaximumSize(new Dimension(150,20));
        algChooser.setSelectedIndex(0);
        algChooser.addActionListener( ae -> {
            chooseAlgorithm();
        });
        algorithmPanel.add(algorithmLabel);
        algorithmPanel.add(algChooser);
        algorithmPanel.setBorder(BorderFactory.createMatteBorder(0,0,1,0,Color.BLACK));
        algorithmPanel.setMaximumSize(algorithmPanel.getPreferredSize());
        return algorithmPanel;
    }

    public JPanel createStatisticsPanel(){
        JPanel statisticsPanel = new JPanel();
        statisticsPanel.setLayout(new BoxLayout(statisticsPanel, BoxLayout.Y_AXIS));
        JPanel visitedCellsPanel = new JPanel();
        visitedCellsPanel.setLayout(new FlowLayout());
        JLabel visitedCellsLabel = new JLabel("Visited cells:");
        visitedCellsCount = new JLabel();
        visitedCellsPanel.add(visitedCellsLabel);
        visitedCellsPanel.add(visitedCellsCount);
        visitedCellsPanel.setMaximumSize(visitedCellsPanel.getPreferredSize());

        JPanel cellsInPathPanel = new JPanel();
        cellsInPathPanel.setLayout(new FlowLayout());
        JLabel cellsInPathLabel = new JLabel("Cells in path:");
        cellsInPathCount = new JLabel();
        cellsInPathPanel.add(cellsInPathLabel);
        cellsInPathPanel.add(cellsInPathCount);
        cellsInPathPanel.setMaximumSize(cellsInPathPanel.getPreferredSize());

        statisticsPanel.add(visitedCellsPanel);
        statisticsPanel.add(cellsInPathPanel);
        statisticsPanel.setMaximumSize(statisticsPanel.getPreferredSize());
        return statisticsPanel;
    }

    public String[] getAlgorithms(){
        return algs;
    }

    public JComboBox getAlgorithmChooser(){
        return algChooser;
    }

    public void setVisitedCellsCount(int value){
        this.visitedCellsCount.setText(String.valueOf(value));
    }

    public void setCellsInPathCount(int value){
        this.cellsInPathCount.setText(String.valueOf(value));
    }


    public void setSpeed(boolean increase){
        Timer loop = view.getLoop();
        if(increase){
            if(loop.getDelay() + delayStep < 250) {
                loop.stop();
                loop.setDelay(loop.getDelay() + delayStep);
                System.out.println("increase, delay: " + loop.getDelay());
                loop.start();
            }
        }
        else{
            if (loop.getDelay() - delayStep > 0){
                loop.stop();
                loop.setDelay(loop.getDelay() - delayStep);
                loop.start();
            }
        }
    }

    public void generateMaze(JTextField mazeSize){
        Controller ctr = view.getController();
        if(mazeSize.getText()!=null && isNumeric(mazeSize.getText())){
            ctr.setSize(Integer.parseInt(mazeSize.getText()));
            ctr.generateMaze();
            view.getMazePanel().setSolver(ctr.getName());
            if(!view.getRunning()){
                view.getMazePanel().repaint();
            }
        }
    }

    public void chooseAlgorithm(){
        if(algChooser.getSelectedIndex()==0){
            view.getMazePanel().setSolver("Sequential Solver");
            reset();
        }
        else{
            view.getMazePanel().setSolver("Parallel Solver");
            reset();
        }
    }

    public void reset(){
        Controller ctr = view.getController();
        ctr.resetModel();
        view.getMazePanel().setSolver(ctr.getName());
        if(!view.getRunning()) view.getMazePanel().repaint();
    }

    public boolean isNumeric(String value){
        try{
            Integer.parseInt(value);
            return true;
        }catch(NumberFormatException ne){
            System.out.println("No number");
            return false;
        }
    }
}
