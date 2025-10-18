package edu.hitsz;

import edu.hitsz.application.Game;
import edu.hitsz.application.MusicManager;

import javax.swing.*;
import java.awt.*;

public class MainMenuForm {
    private JPanel mainPanel;
    private JButton easyButton;
    private JButton normalButton;
    private JButton hardButton;
    private JLabel music;
    private JComboBox<String> musicComboBox;
    private JLabel HIT;

    private JFrame mainFrame;

    public MainMenuForm() {
        easyButton.addActionListener(e -> startGame("EASY"));
        normalButton.addActionListener(e -> startGame("NORMAL"));
        hardButton.addActionListener(e -> startGame("HARD"));
    }

    private void startGame(String difficulty) {
        if (mainFrame == null) {
            mainFrame = (JFrame) SwingUtilities.getWindowAncestor(mainPanel);
        }
        MusicManager.isSoundOn = "开".equals(musicComboBox.getSelectedItem());

        // 回调
        Runnable onGameOver = () -> {
            if (MusicManager.isSoundOn) {
                MusicManager.stopBgm();
            }
            showLeaderboard(Game.getStaticScore(), difficulty);
        };
        Game gamePanel = new Game(difficulty, onGameOver);

        switchPanel(gamePanel);
        gamePanel.action();
        if (MusicManager.isSoundOn) {
            MusicManager.playBgm("/videos/bgm.wav");
        }
    }

    /**
     * 切换到排行榜面板
     */
    private void showLeaderboard(int score, String difficulty) {
        LeaderboardForm leaderboardForm = new LeaderboardForm();
        leaderboardForm.recordNewScore(score, difficulty); // 弹出输入框并记录分数
        switchPanel(leaderboardForm.getMainPanel());
    }

    /**
     * 辅助方法：在主窗口中无缝切换面板
     */
    private void switchPanel(JPanel newPanel) {
        if (mainFrame != null) {
            mainFrame.setContentPane(newPanel);
            mainFrame.revalidate();
            mainFrame.repaint();
        } else {
            System.err.println("Main frame is not initialized!");
        }
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public class BackgroundPanel extends JPanel {
        private final Image bgImage;

        public BackgroundPanel(String imagePath) {
            bgImage = new ImageIcon(getClass().getResource(imagePath)).getImage();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
        }
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        mainPanel = new BackgroundPanel("/images/mainMenu.jpg");
        ImageIcon originalIcon = new ImageIcon(getClass().getResource("/images/logo-blue.png"));
        Image originalImage = originalIcon.getImage();
        int targetWidth = 128;
        int targetHeight = 128;
        Image scaledImage = originalImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);
        HIT = new JLabel(scaledIcon);
    }
}
