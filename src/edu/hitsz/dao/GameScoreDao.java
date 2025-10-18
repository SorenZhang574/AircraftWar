package edu.hitsz.dao;

import java.util.Date;
import java.util.List;

public interface GameScoreDao {

    /**
     * 获取所有得分记录
     */
    List<GameScore> getAllScores();
    /**
     * 向数据源中添加一条新的得分记录
     */
    void addScore(GameScore score);
    /**
     * 将内存中的所有得分记录保存到文件。
     */
    void saveScores();
    /**
     * 删除记录
     */
    void deleteScore(String playerName, int score, Date recordTime);
}
