package edu.hitsz.application;

import edu.hitsz.aircraft.EnemyAircraft;
import edu.hitsz.factory.BossEnemyFactory;

public class HardGame extends Game {
    private long lastDifficultyIncreaseTime = 0;
    private final int baseBossHp = 400;
    private final int bossHpIncrease = 100;
    public HardGame(Runnable onGameOver) {
        super(onGameOver);
        System.out.println("Game Mode: Hard");
    }

    @Override
    protected void setInitialParameters() {
        this.setEnemyMaxNumber(7);
        this.setCycleDurationEnemyShoot(700);
        this.setScoreThreshold(600);
        this.setEliteProb(0.30);
        this.setBackground(ImageManager.BACKGROUND_HARD_IMAGE);
    }

    @Override
    protected void increaseDifficultyOverTime() {
        // 12s update difficulty
        if (this.getTime() - lastDifficultyIncreaseTime > 12000) {
            lastDifficultyIncreaseTime = this.getTime();
            this.setCycleDurationEnemyShoot(Math.max(200, this.getCycleDurationEnemyShoot() - 30));
            this.setEliteProb(Math.min(1.0, this.getEliteProb() + 0.02));

            int newCycle = Math.max(200, this.getCycleDurationEnemyGenerate() - 50);
            this.setCycleDurationEnemyGenerate(newCycle);
            System.out.println(
                    "Hard Mode: Difficulty Increased! " +
                            "EnemyShootCycle: " + this.getCycleDurationEnemyShoot() +
                            ", EliteProb: " + String.format("%.2f", this.getEliteProb()) +
                            ", SpawnCycle: " + newCycle + "ms"
            );
        }
    }

    @Override
    protected void spawnBossIfRequired() {
        if (this.getScore() >= this.getScoreThreshold() * (this.getBossKillCount() + 1) && !this.isBossExisting()) {
            this.setBossExisting(true);

            EnemyAircraft boss = new BossEnemyFactory().createEnemy();
            int currentBossHp = baseBossHp + this.getBossKillCount() * bossHpIncrease;
            boss.setHp(currentBossHp);
            boss.setMaxHp(currentBossHp);

            this.getEnemyAircrafts().add(boss);
            this.registerEnemy(boss);

            System.out.println("Hard Mode: A stronger Boss is coming with HP: " + currentBossHp);
            MusicManager.playBgm("/videos/bgm_boss_changed.wav");
        }
    }
}
