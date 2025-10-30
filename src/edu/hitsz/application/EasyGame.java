package edu.hitsz.application;

public class EasyGame extends Game {
    public EasyGame(Runnable onGameOver) {
        super(onGameOver);
        System.out.println("Game Mode: Easy");
    }
    @Override
    protected void setInitialParameters() {
        this.setEnemyMaxNumber(5);
        this.setCycleDurationEnemyShoot(1000);
        this.setScoreThreshold(1000);
        this.setEliteProb(0.20);
        this.setBackground(ImageManager.BACKGROUND_EASY_IMAGE);
    }

    @Override
    protected void increaseDifficultyOverTime() {}

    @Override
    protected void spawnBossIfRequired() {}
}
