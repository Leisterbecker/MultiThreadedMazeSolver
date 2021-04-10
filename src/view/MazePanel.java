package view;

import controller.Controller;
import model.Cell;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.stream.Stream;

public class MazePanel extends JPanel {

    private int cellSize;
    private View view;
    private Cell exit;
    private Cell start;
    private HashMap<Integer, Color> workerColors;

    private int offsetX;
    private int offsetY;

    private int mouseX;
    private int mouseY;

    public MazePanel(View view){
        this.view = view;
        workerColors = new HashMap<>();
        Controller ctr = view.getController();
        cellSize = (int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight() - 60)/ctr.getSize();
        offsetX = (cellSize * ctr.getSize())/2 - view.getControlPanel().getWidth();
        offsetY = 10;

        this.addMouseWheelListener(e -> {
            int direction = e.getWheelRotation();
            System.out.println("scroll amount: " + e.getScrollAmount());;
            System.out.println(direction);
            if(direction < 0){
                if(cellSize <= 50) cellSize+=1;
            }
            else{
                if(cellSize >= 5) cellSize-=1;
            }
        });

        this.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e){
                mouseX = e.getX();
                mouseY = e.getY();
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
                int newMouseX = e.getX();
                int newMouseY = e.getY();

                int panelWidth = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth() - view.getControlPanel().getWidth();
                int panelHeight = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();

                int dragAmount = 5;


                if(newMouseX < mouseX){
                    if((offsetX-dragAmount) > 0) offsetX -= dragAmount;
                }
                else if(newMouseX > mouseX){
                    if((offsetX+dragAmount + (ctr.getSize() * cellSize)) < panelWidth) offsetX += dragAmount;
                    System.out.println("offset x: " + offsetX);
                    System.out.println("panelwidth: " + panelWidth);
                }
                if(newMouseY < mouseY){
                    if((offsetY-dragAmount) > 0) offsetY -= dragAmount;
                }
                else if(newMouseY > mouseY){
                    if((offsetY+dragAmount + (ctr.getSize() * cellSize)) < panelHeight) offsetY += dragAmount;
                }
                mouseX = newMouseX;
                mouseY = newMouseY;
                repaint();
            }
        });
    }

    public void setSolver(String solver){
        Controller ctr = view.getController();
        initWorkerColors(0);
        view.getControlPanel().setVisitedCellsCount(0);
        view.getControlPanel().setCellsInPathCount(0);
        ctr.setSolver(solver);
        exit = ctr.getExit();
        start = ctr.getStartCell();
    }

    public int countVisitedCells(){
        Controller ctr = view.getController();
        int visitedCount = 0;
        int mazeSize = ctr.getSize();
        for(int i = 0; i < mazeSize; i++){
            for(int j = 0; j < mazeSize; j++){
                Cell cell = ctr.getCell(j,i);
                if(cell.visited) visitedCount++;
            }
        }
        return visitedCount;
    }

    public int countCellsInPath(){
        return view.getController().getSolver().path.size();
    }


    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        Controller ctr = view.getController();
        int mazeSize = ctr.getSize();
        Graphics2D g2d = (Graphics2D) g;
        for(int i = 0; i < mazeSize; i++){
            for(int j = 0; j < mazeSize; j++){
                Cell cell = ctr.getCell(j,i);
                paintExit(g2d, cell, offsetX, offsetY);
                if(cell.visited){
                    paintStart(g2d, cell, offsetX, offsetY);
                    if(!cell.equals(exit) && !cell.equals(start)){
                        if (ctr.getSolver().inPath(cell)) {
                            //g2d.setColor(Color.YELLOW);
                            g2d.setColor(workerColors.get(cell.workerId));
                        }
                        else g2d.setColor(workerColors.get(cell.workerId));
                        g2d.fillRect(offsetX+i*cellSize, offsetY+j*cellSize,cellSize, cellSize);
                    }
                }
                paintWalls(g2d, cell, offsetX, offsetY, i, j);
            }
        }
    }

    public void paintStart(Graphics2D g2d, Cell cell, int offsetX, int offsetY){
        if(cell.equals(start)){
            g2d.setColor(Color.BLUE);
            g2d.fillRect(offsetX+start.dim.getY()*cellSize, offsetY+start.dim.getX()*cellSize,cellSize,cellSize);
        }
    }

    public void paintExit(Graphics2D g2d, Cell cell, int offsetX, int offsetY){
        if(cell.equals(exit)){
            g2d.setColor(Color.GREEN);
            g2d.fillRect(offsetX+exit.dim.getY()*cellSize, offsetY+exit.dim.getX()*cellSize,cellSize,cellSize);
        }
    }

    public void paintWalls(Graphics2D g2d, Cell cell, int offsetX, int offsetY, int i, int j){
        g2d.setColor(Color.BLACK);
        if(cell.east) g2d.drawLine(offsetX + i*cellSize,offsetY + (j*cellSize)+cellSize,offsetX + (i*cellSize)+cellSize,offsetY + (j*cellSize)+cellSize);
        if(cell.west) g2d.drawLine(offsetX + i*cellSize,offsetY + j*cellSize,offsetX + (i*cellSize)+cellSize,offsetY + j*cellSize);
        if(cell.north) g2d.drawLine(offsetX + i*cellSize,offsetY + j*cellSize,offsetX + i*cellSize,offsetY + (j*cellSize)+cellSize);
        if(cell.south) g2d.drawLine(offsetX + (i*cellSize)+cellSize,offsetY + j*cellSize,offsetX + (i*cellSize)+cellSize,offsetY + (j*cellSize)+cellSize);
    }

    public void initWorkerColors(int mode){
        if(!workerColors.isEmpty()) workerColors.clear();
        int workers = 40;
        if(mode == 0){
            for(int i = 0; i < workers; i++){
                int r = (int) (Math.random() * 255);
                int g = (int) (Math.random() * 255);
                int b = (int) (Math.random() * 255);
                workerColors.put(i,new Color(r,g,b));
            }
        }
        else{
            double factor = 1.0/workers;
            double currentFactor = factor;
            Color from, to, next;
            do{
                from = getRandomColor();
                to = getRandomColor();
            }while(from.equals(to));
            next = from;
            for(int i = 0; i < workers; i++){
                next = gradientColor(from, to, currentFactor);
                workerColors.put(i, next);
                currentFactor += factor;
            }
        }
    }

    public Color gradientColor(Color from, Color to, double factor){
        int red = (int) (from.getRed() * factor + to.getRed() * (1.0-factor));
        int green = (int) (from.getGreen() * factor + to.getGreen() * (1.0-factor));
        int blue = (int) (from.getBlue() * factor + to.getBlue() * (1.0-factor));
        return new Color(red,green,blue);
    }

    public Color getRandomColor(){
        Integer[] rgb = Stream.of(0,0,0).map(e -> (int) (Math.random() * 255)).toArray(Integer[]::new);
        return new Color(rgb[0], rgb[1], rgb[2]);
    }
}
