package edu.hitsz;

import edu.hitsz.dao.GameScore;
import edu.hitsz.dao.GameScoreDao;
import edu.hitsz.dao.GameScoreDaoImpl;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LeaderboardForm {
    private JPanel mainPanel;
    private JPanel topPanel;
    private JLabel difficultyLabel;
    private JLabel titleLabel;
    private JScrollPane scrollPanel;
    private JTable leaderboardTable;
    private JPanel bottomPanel;
    private JButton deleteButton;
    private JLabel HIT;

    private final GameScoreDao gameScoreDao;
    private DefaultTableModel tableModel;
    private List<GameScore> displayedScores;

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public LeaderboardForm() {
        this("ALL");
    }

    public LeaderboardForm(String difficulty) {
        this.gameScoreDao = new GameScoreDaoImpl();
        this.displayedScores = new ArrayList<>();

        setupUI();
        difficultyLabel.setText("难度: " + difficulty.toUpperCase());
        setupTable();
        refreshTable();
    }

    private void setupUI() {
        titleLabel.setFont(new Font("Serif", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        deleteButton.addActionListener(e -> handleDelete());
    }

    private void setupTable() {
        String[] columnNames = {"名次", "玩家名", "得分", "记录时间"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        leaderboardTable.setModel(tableModel);

        leaderboardTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        leaderboardTable.getColumnModel().getColumn(1).setPreferredWidth(150);
        leaderboardTable.getColumnModel().getColumn(2).setPreferredWidth(80);
        leaderboardTable.getColumnModel().getColumn(3).setPreferredWidth(120);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < leaderboardTable.getColumnCount(); i++) {
            leaderboardTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    }

    /**
     * 从DAO获取数据并刷新JTable和内部数据列表
     */
    private void refreshTable() {
        displayedScores = gameScoreDao.getAllScores();
        tableModel.setRowCount(0);

        int rank = 1;
        for (GameScore score : displayedScores) {
            tableModel.addRow(new Object[]{
                    rank++,
                    score.getPlayerName(),
                    score.getScore(),
                    DATE_FORMAT.format(score.getRecordTime())
            });
        }
    }

    /**
     * 处理删除按钮的点击事件
     */
    private void handleDelete() {
        int selectedRow = leaderboardTable.getSelectedRow();
        int choice = JOptionPane.showConfirmDialog(mainPanel, "是否确定删除选中的记录?", "确认删除", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (choice == JOptionPane.YES_OPTION) {
            GameScore scoreToDelete = displayedScores.get(selectedRow);

            String playerName = scoreToDelete.getPlayerName();
            int scoreValue = scoreToDelete.getScore();
            Date recordTime = scoreToDelete.getRecordTime();
            gameScoreDao.deleteScore(playerName, scoreValue, recordTime);
            gameScoreDao.saveScores();

            refreshTable();
        }
    }

    /**
     * 供外部调用，当游戏结束时记录新分数
     */
    public void recordNewScore(int score, String difficulty) {
        difficultyLabel.setText("难度: " + difficulty.toUpperCase());

        String playerName = JOptionPane.showInputDialog(mainPanel, "游戏结束, 你的得分为 " + score + "。\n请输入名字记录得分:", "记录分数", JOptionPane.QUESTION_MESSAGE);
        if (playerName != null && !playerName.trim().isEmpty()) {
            GameScore newScore = new GameScore(playerName.trim(), score);
            gameScoreDao.addScore(newScore);
            gameScoreDao.saveScores();
            refreshTable();
        }
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        ImageIcon originalIcon = new ImageIcon(getClass().getResource("/images/logo-blue.png"));
        Image originalImage = originalIcon.getImage();
        int targetWidth = 128;
        int targetHeight = 128;
        Image scaledImage = originalImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);
        HIT = new JLabel(scaledIcon);
    }
}
