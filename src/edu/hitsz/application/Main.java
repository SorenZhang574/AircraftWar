package edu.hitsz.application;

import edu.hitsz.MainMenuForm;

import javax.swing.*;
import java.awt.*;

/**
 * 程序入口
 * @author hitsz
 */
public class Main {

    public static final int WINDOW_WIDTH = 512;
    public static final int WINDOW_HEIGHT = 768;

    public static void main(String[] args) {

        System.out.println("Hello Aircraft War");

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Aircraft War");
            frame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
            frame.setResizable(false);

            frame.setLocationRelativeTo(null);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            MainMenuForm mainMenuForm = new MainMenuForm();

            frame.setContentPane(mainMenuForm.getMainPanel());
            frame.setVisible(true);
        });
    }
}
