package view;

import javax.swing.*;
import view.View;


public class Main {

    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(() -> new View(50));
    }
}
