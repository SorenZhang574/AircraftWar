package edu.hitsz.dao;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class GameScoreDaoImpl implements GameScoreDao {

    private static final String FILENAME = "leaderboard.txt";
    private static final String TITLE = "得分排行榜";
    private static final String HEADER = "排名,玩家,分数,记录时间";

    private final Path filePath;
    private final List<GameScore> scores;
    public GameScoreDaoImpl() {
        this.filePath = Paths.get(FILENAME);
        this.scores = loadFromFile();
    }

    @Override
    public List<GameScore> getAllScores() {
        return scores.stream()
                .sorted(Comparator.comparingInt(GameScore::getScore).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public void addScore(GameScore score) {
        this.scores.add(score);
    }
    @Override
    public void saveScores() {
        List<String> lines = new LinkedList<>();
        lines.add(TITLE);
        lines.add(HEADER);
        List<GameScore> sortedScores = getAllScores();
        for (int i = 0; i < sortedScores.size(); i++) {
            GameScore score = sortedScores.get(i);
            lines.add((i + 1) + "," + score.toDataString());
        }
        try {
            Files.write(filePath, lines, StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.err.println("Error saving leaderboard: " + e.getMessage());
        }
    }
    /**
     * 从文件加载数据
     */
    private List<GameScore> loadFromFile() {
        if (!Files.exists(filePath)) {
            return new LinkedList<>();
        }
        try (BufferedReader reader = Files.newBufferedReader(filePath, StandardCharsets.UTF_8)) {
            return reader.lines()
                    .skip(2)
                    .map(line -> {
                        int firstCommaIndex = line.indexOf(',');
                        return line.substring(firstCommaIndex + 1);
                    })
                    .map(GameScore::fromDataString)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            System.err.println("Error loading leaderboard: " + e.getMessage());
            return new LinkedList<>();
        }
    }
    /**
     * 删除记录
     */
    @Override
    public void deleteScore(String playerName, int score, Date recordTime) {
        scores.removeIf(s ->
                s.getPlayerName().equals(playerName) &&
                        s.getScore() == score &&
                        s.getRecordTime().equals(recordTime)
        );
    }
}
