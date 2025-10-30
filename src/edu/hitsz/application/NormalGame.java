package edu.hitsz.application;

import edu.hitsz.aircraft.EnemyAircraft;
import edu.hitsz.factory.BossEnemyFactory;

public class NormalGame extends Game {
    private long lastDifficultyIncreaseTime = 0;
    public NormalGame(Runnable onGameOver) {
        super(onGameOver);
        System.out.println("Game Mode: Normal");
    }

    @Override
    protected void setInitialParameters() {
        this.setEnemyMaxNumber(6);
        this.setCycleDurationEnemyShoot(850);
        this.setScoreThreshold(850);
        this.setEliteProb(0.25);
        this.setBackground(ImageManager.BACKGROUND_NORMAL_IMAGE);
    }

    @Override
    protected void increaseDifficultyOverTime() {
        // 15s update difficulty
        if (this.getTime() - lastDifficultyIncreaseTime > 15000) {
            lastDifficultyIncreaseTime = this.getTime();

            this.setEnemyMaxNumber(this.getEnemyMaxNumber() + 1);
            this.setCycleDurationEnemyShoot(Math.max(200, this.getCycleDurationEnemyShoot() - 20));
            this.setEliteProb(Math.min(1.0, this.getEliteProb() + 0.01));

            int newCycle = Math.max(300, this.getCycleDurationEnemyGenerate() - 40);
            this.setCycleDurationEnemyGenerate(newCycle);
            System.out.println(
                    "Normal Mode: Difficulty Increased! " +
                            "MaxEnemies: " + this.getEnemyMaxNumber() +
                            ", EliteProb: " + String.format("%.2f", this.getEliteProb()) +
                            ", SpawnCycle: " + newCycle+ "ms"
            );
        }
    }

    @Override
    protected void spawnBossIfRequired() {
        if (this.getScore() >= this.getScoreThreshold() * (this.getBossKillCount() + 1) && !this.isBossExisting()) {
            this.setBossExisting(true);
            EnemyAircraft boss = new BossEnemyFactory().createEnemy();
            this.getEnemyAircrafts().add(boss);
            this.registerEnemy(boss);

            MusicManager.playBgm("/videos/bgm_boss_changed.wav");
        }
    }
}
