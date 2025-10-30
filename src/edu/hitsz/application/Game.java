package edu.hitsz.application;

import edu.hitsz.aircraft.*;
import edu.hitsz.basic.AbstractFlyingObject;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.factory.EliteEnemyFactory;
import edu.hitsz.factory.ElitePlusEnemyFactory;
import edu.hitsz.factory.MobEnemyFactory;
import edu.hitsz.prop.BombSupply;
import edu.hitsz.prop.AbstractProp;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;
import java.util.concurrent.*;

public abstract class Game extends JPanel {
    private BufferedImage background;

    private int shakeOffsetX = 0;
    private int shakeOffsetY = 0;
    private int shakeDuration = 0;
    private final int SHAKE_INTENSITY = 5;
    private int enemyMaxNumber;
    private int scoreThreshold;
    private double eliteProb;
    private int cycleDurationEnemyShoot;
    private boolean isBossExisting = false;
    private int bossKillCount = 0;

    private int backGroundTop = 0;
    private final ScheduledExecutorService executorService;
    private final int timeInterval = 40;
    private final HeroAircraft heroAircraft;
    private final List<EnemyAircraft> enemyAircrafts;
    private final List<BaseBullet> heroBullets;
    private final List<BaseBullet> enemyBullets;
    private final List<AbstractProp> props;
    private int score = 0;
    private int time = 0;
    private final int cycleDuration = 600; // 英雄机射击周期
    private int cycleTime = 0;
    private int cycleDurationEnemyGenerate = 600; // 敌机产生周期
    private int cycleTimeEnemyGenerate = 0;
    private int cycleTimeEnemyShoot = 0;
    private final Runnable onGameOver;
    private static int staticScore = 0;

    public Game(Runnable onGameOver) {
        heroAircraft = HeroAircraft.getInstance();
        enemyAircrafts = new LinkedList<>();
        heroBullets = new LinkedList<>();
        enemyBullets = new LinkedList<>();
        props = new LinkedList<>();
        this.onGameOver = onGameOver;
        setInitialParameters();

        this.executorService = new ScheduledThreadPoolExecutor(1,
                new BasicThreadFactory.Builder().namingPattern("game-action-%d").daemon(true).build());
        new HeroController(this, heroAircraft);
    }

    protected abstract void setInitialParameters();
    protected abstract void increaseDifficultyOverTime();
    protected abstract void spawnBossIfRequired();

    /**
     * 敌人生成逻辑
     * @return a new enemy aircraft, or null
     */
    protected EnemyAircraft spawnEnemy() {
        if (this.getEnemyAircrafts().size() >= this.getEnemyMaxNumber()) {
            return null;
        }
        double prob = Math.random();
        if (prob < this.getEliteProb() * 0.25) {
            return new ElitePlusEnemyFactory().createEnemy(); // 假设你有一个
        } else if (prob < this.getEliteProb()) {
            return new EliteEnemyFactory().createEnemy();
        } else {
            return new MobEnemyFactory().createEnemy();
        }
    }

    public void action() {
        Runnable task = () -> {
            time += timeInterval;
            increaseDifficultyOverTime();
            spawnBossIfRequired();

            if (timeCountAndNewCycleJudgeEnemyGenerate()) {
                EnemyAircraft newEnemy = spawnEnemy();
                if (newEnemy != null) {
                    enemyAircrafts.add(newEnemy);
                    registerEnemy(newEnemy);
                }
            }

            if (timeCountAndNewCycleJudgeEnemyShoot()) {
                shootActionEnemy();
            }

            if (timeCountAndNewCycleJudge()) {
                shootActionHero();
            }

            heroAircraft.updateAnimation();

            updateShake();

            bulletsMoveAction();

            aircraftsMoveAction();

            crashCheckAction();

            postProcessAction();

            repaint();

            if (heroAircraft.getHp() <= 0) {
                executorService.shutdown();
                MusicManager.stopBgm();
                MusicManager.playSoundEffect("/videos/game_over.wav");
                System.out.println("Game Over!");
                staticScore = this.score;
                if (onGameOver != null) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    SwingUtilities.invokeLater(onGameOver);
                }
            }
        };
        executorService.scheduleWithFixedDelay(task, timeInterval, timeInterval, TimeUnit.MILLISECONDS);
    }


    private void crashCheckAction() {
        // 敌机子弹攻击英雄
        for (BaseBullet bullet : enemyBullets) {
            if (bullet.notValid()) continue;
            if (heroAircraft.crash(bullet)) {
                heroAircraft.decreaseHp(bullet.getPower());
                bullet.vanish();
            }
        }

        // 英雄子弹攻击敌机
        for (BaseBullet bullet : heroBullets) {
            if (bullet.notValid()) continue;
            for (EnemyAircraft enemyAircraft : enemyAircrafts) {
                if (enemyAircraft.crash(bullet)) {
                    enemyAircraft.decreaseHp(bullet.getPower());
                    bullet.vanish();
                    MusicManager.playSoundEffect("/videos/bullet_hit.wav");

                    if (enemyAircraft.notValid()) {
                        int gainedScore = enemyAircraft.getScore();
                        score += enemyAircraft.getScore();
                        heroAircraft.increaseExp(gainedScore);
                        props.addAll(enemyAircraft.getProps());
                    }
                }

                // 英雄机与敌机相撞
                if (enemyAircraft.crash(heroAircraft) || heroAircraft.crash(enemyAircraft)) {
                    enemyAircraft.vanish();
                    heroAircraft.decreaseHp(Integer.MAX_VALUE);
                }
            }
        }

        // 我方获得道具
        for (AbstractProp prop : props) {
            if (prop.notValid()) continue;
            prop.forward();
            if (prop.crash(heroAircraft)) {
                MusicManager.playSoundEffect("/videos/get_supply.wav");
                if (prop instanceof BombSupply) {
                    MusicManager.playSoundEffect("/videos/bomb_explosion.wav");
                    int scoreFromBomb = 0;
                    for (EnemyAircraft enemy : enemyAircrafts) {
                        if (!(enemy instanceof ElitePlusEnemy) && !(enemy instanceof BossEnemy)) {
                            scoreFromBomb += enemy.getScore();
                        }
                    }
                    this.score += scoreFromBomb;
                    heroAircraft.increaseExp(scoreFromBomb);
                }
                prop.activate(heroAircraft);
            }
        }
    }

    private void postProcessAction() {
        heroBullets.removeIf(AbstractFlyingObject::notValid);
        props.removeIf(AbstractFlyingObject::notValid);

        enemyBullets.removeIf(bullet -> {
            if (bullet.notValid()) {
                BombSupply.removeObserve(bullet);
                return true;
            }
            return false;
        });

        enemyAircrafts.removeIf(enemy -> {
            if (enemy.notValid()) {
                BombSupply.removeObserve(enemy);
                if (enemy instanceof BossEnemy) {
                    shakeScreen(2000);
                    this.isBossExisting = false;
                    this.bossKillCount++;
                    MusicManager.playSoundEffect("/videos/boss_explode.wav");
                    MusicManager.playBgm("/videos/bgm.wav");
                }
                return true;
            }
            return false;
        });
    }


    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.translate(shakeOffsetX, shakeOffsetY);
        g.drawImage(this.background, 0, this.backGroundTop - Main.WINDOW_HEIGHT, null);
        g.drawImage(this.background, 0, this.backGroundTop, null);
        this.backGroundTop += 1;
        if (this.backGroundTop == Main.WINDOW_HEIGHT) {
            this.backGroundTop = 0;
        }
        paintImageWithPositionRevised(g, enemyBullets);
        paintImageWithPositionRevised(g, heroBullets);
        paintImageWithPositionRevised(g, enemyAircrafts);
        paintImageWithPositionRevised(g, props);
        BufferedImage fireImage = heroAircraft.getFireImage();
        int fireX = heroAircraft.getLocationX() - fireImage.getWidth() / 2;
        int fireY = heroAircraft.getLocationY() + heroAircraft.getHeight() / 3;
        g.drawImage(fireImage, fireX, fireY, null);
        g.drawImage(ImageManager.HERO_IMAGE, heroAircraft.getLocationX() - ImageManager.HERO_IMAGE.getWidth() / 2, heroAircraft.getLocationY() - ImageManager.HERO_IMAGE.getHeight() / 2, null);
        paintScoreAndLife(g);
        drawBossHealthBar(g);
        drawExperienceBar(g);
        g.translate(-shakeOffsetX, -shakeOffsetY);
    }

    /**
     * 触发屏幕震动
     */
    public void shakeScreen(int durationInMs) {
        // 将毫秒转换为帧数
        this.shakeDuration = durationInMs / cycleDuration;
    }

    /**
     * 在游戏主循环中调用的，用于更新震动状态
     */
    private void updateShake() {
        if (shakeDuration > 0) {
            shakeDuration--;
            // 生成一个在[-INTENSITY, INTENSITY]范围内的随机偏移量
            shakeOffsetX = (int) (Math.random() * (SHAKE_INTENSITY * 2 + 1)) - SHAKE_INTENSITY;
            shakeOffsetY = (int) (Math.random() * (SHAKE_INTENSITY * 2 + 1)) - SHAKE_INTENSITY;

            if (shakeDuration <= 0) {
                // 震动结束，恢复画布到原位
                shakeOffsetX = 0;
                shakeOffsetY = 0;
            }
        }
    }

    /**
     * 专门用于绘制Boss血条
     */
    private void drawBossHealthBar(Graphics g) {
        if (this.isBossExisting) {
            EnemyAircraft currentBoss = null;
            for (EnemyAircraft enemy : enemyAircrafts) {
                if (enemy instanceof BossEnemy) {
                    currentBoss = enemy;
                    break;
                }
            }

            if (currentBoss != null) {
                int maxHp = currentBoss.getMaxHp();
                int currentHp = currentBoss.getHp();
                double healthPercentage = (double) currentHp / maxHp;
                int barWidth = 300;
                int barHeight = 20;
                int x = (Main.WINDOW_WIDTH - barWidth) / 2; // 水平居中
                int y = 40;
                g.setColor(Color.GRAY);
                g.fillRect(x, y, barWidth, barHeight);
                g.setColor(Color.RED);
                int currentBarWidth = (int) (barWidth * healthPercentage);
                g.fillRect(x, y, currentBarWidth, barHeight);

                g.setColor(Color.BLACK);
                g.drawRect(x, y, barWidth, barHeight);
                g.setColor(Color.WHITE);
                g.setFont(new Font("SansSerif", Font.BOLD, 16));
                String hpText = "BOSS: " + currentHp + " / " + maxHp;
                int stringWidth = g.getFontMetrics().stringWidth(hpText);
                g.drawString(hpText, x + (barWidth - stringWidth) / 2, y + barHeight - 5);
            }
        }
    }

    /**
     * 绘制英雄机等级和经验条
     */
    private void drawExperienceBar(Graphics g) {
        int level = heroAircraft.getLevel();
        int currentExp = heroAircraft.getExp();
        int expToNextLevel = heroAircraft.getExpToNextLevel();
        double expPercentage = (double) currentExp / expToNextLevel;
        int barWidth = 200;
        int barHeight = 15;
        int x = 10;
        int y = 70;
        g.setColor(new Color(0, 0, 100));
        g.setColor(new Color(50, 150, 255));
        int currentBarWidth = (int) (barWidth * expPercentage);
        g.fillRect(x, y, currentBarWidth, barHeight);
        g.setColor(Color.WHITE);
        g.drawRect(x, y, barWidth, barHeight);
        g.setFont(new Font("SansSerif", Font.BOLD, 14));
        g.setColor(Color.WHITE);
        String expText = "LV: " + level + " | " + currentExp + " / " + expToNextLevel;
        g.drawString(expText, x + 5, y + barHeight - 2);
    }

    private void paintImageWithPositionRevised(Graphics g, List<? extends AbstractFlyingObject> objects) {
        if (objects.isEmpty()) return;
        for (AbstractFlyingObject object : objects) {
            BufferedImage image = object.getImage();
            if (image != null) {
                g.drawImage(image, object.getLocationX() - image.getWidth() / 2, object.getLocationY() - image.getHeight() / 2, null);
            }
        }
    }

    private void paintScoreAndLife(Graphics g) {
        int x = 10; int y = 25;
        g.setColor(new Color(0x1E1F22));
        g.setFont(new Font("SansSerif", Font.BOLD, 22));
        g.drawString("SCORE:" + this.score, x, y);
        y = y + 20;
        g.drawString("LIFE:" + this.heroAircraft.getHp(), x, y);
    }

    private void shootActionEnemy() {
        for (EnemyAircraft enemy : enemyAircrafts) {
            List<BaseBullet> newBullets = enemy.shoot();
            for (BaseBullet bullet : newBullets) { BombSupply.registerObserver(bullet); }
            enemyBullets.addAll(newBullets);
        }
    }

    public void registerEnemy(EnemyAircraft enemy) { BombSupply.registerObserver(enemy); }
    public int getTime() { return this.time; }
    public int getScore() { return this.score; }
    public List<EnemyAircraft> getEnemyAircrafts() { return this.enemyAircrafts; }
    public int getEnemyMaxNumber() { return enemyMaxNumber; }
    public void setEnemyMaxNumber(int enemyMaxNumber) { this.enemyMaxNumber = enemyMaxNumber; }
    public int getScoreThreshold() { return scoreThreshold; }
    public void setScoreThreshold(int scoreThreshold) { this.scoreThreshold = scoreThreshold; }
    public double getEliteProb() { return eliteProb; }
    public void setEliteProb(double eliteProb) { this.eliteProb = eliteProb; }
    public int getCycleDurationEnemyShoot() { return cycleDurationEnemyShoot; }
    public void setCycleDurationEnemyShoot(int cycleDurationEnemyShoot) { this.cycleDurationEnemyShoot = cycleDurationEnemyShoot; }
    public int getCycleDurationEnemyGenerate() { return this.cycleDurationEnemyGenerate;}
    public void setCycleDurationEnemyGenerate(int cycleDurationEnemyGenerate) { this.cycleDurationEnemyGenerate = cycleDurationEnemyGenerate;}
    public boolean isBossExisting() { return isBossExisting; }
    public void setBossExisting(boolean bossExisting) { this.isBossExisting = bossExisting; }
    public int getBossKillCount() { return bossKillCount; }
    public void setBackground(BufferedImage background) { this.background = background; }
    public static int getStaticScore() { return staticScore; }
    private boolean timeCountAndNewCycleJudge() { cycleTime += timeInterval; if (cycleTime >= cycleDuration) { cycleTime %= cycleDuration; return true; } else { return false; } }
    private boolean timeCountAndNewCycleJudgeEnemyGenerate() { cycleTimeEnemyGenerate += timeInterval; if (cycleTimeEnemyGenerate >= cycleDurationEnemyGenerate) { cycleTimeEnemyGenerate %= cycleDurationEnemyGenerate; return true; } else { return false; } }
    private boolean timeCountAndNewCycleJudgeEnemyShoot() { cycleTimeEnemyShoot += timeInterval; if (cycleTimeEnemyShoot >= this.cycleDurationEnemyShoot) { cycleTimeEnemyShoot %= this.cycleDurationEnemyShoot; return true; } else { return false; } }
    private void shootActionHero() { heroBullets.addAll(heroAircraft.shoot()); }
    private void bulletsMoveAction() { for (BaseBullet b : heroBullets) b.forward(); for (BaseBullet b : enemyBullets) b.forward(); }
    private void aircraftsMoveAction() { for (EnemyAircraft a : enemyAircrafts) a.forward(); }

}
