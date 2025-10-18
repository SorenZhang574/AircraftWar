package edu.hitsz.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GameScore {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private final String playerName;
    private final int score;
    private final Date recordTime;

    public GameScore(String playerName, int score) {
        this.playerName = playerName;
        this.score = score;
        this.recordTime = new Date();
    }

    private GameScore(String playerName, int score, Date recordTime) {
        this.playerName = playerName;
        this.score = score;
        this.recordTime = recordTime;
    }

    public String getPlayerName() {
        return playerName;
    }
    public int getScore() {
        return score;
    }

    public String toDataString() {
        return playerName + "," + score + "," + DATE_FORMAT.format(recordTime);
    }

    public static GameScore fromDataString(String dataLine) {
        if (dataLine == null || dataLine.trim().isEmpty()) return null;
        String[] parts = dataLine.split(",");
        if (parts.length != 3) return null;

        try {
            return new GameScore(parts[0], Integer.parseInt(parts[1]), DATE_FORMAT.parse(parts[2]));
        } catch (NumberFormatException | ParseException e) {
            System.err.println("Failed to parse score data: " + dataLine);
            return null;
        }
    }

    public Date getRecordTime() {
        return recordTime;
    }
}
