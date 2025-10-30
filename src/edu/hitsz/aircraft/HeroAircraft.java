package edu.hitsz.aircraft;

import edu.hitsz.application.ImageManager;
import edu.hitsz.application.Main;
import edu.hitsz.application.MusicManager;
import edu.hitsz.strategy.RingShootStrategy;
import edu.hitsz.strategy.ScatterShootStrategy;
import edu.hitsz.strategy.StraightShootStrategy;

import java.awt.image.BufferedImage;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * 英雄飞机，游戏玩家操控
 * @author hitsz
 */
public class HeroAircraft extends AbstractAircraft {

    /**
     * @param locationX 英雄机位置x坐标
     * @param locationY 英雄机位置y坐标
     * @param speedX 英雄机射出的子弹的基准速度（英雄机无特定速度）
     * @param speedY 英雄机射出的子弹的基准速度（英雄机无特定速度）
     * @param hp    初始生命值
     */

    private int level = 1;              // 初始等级为 1
    private int exp = 0;                // 当前经验值
    private int expToNextLevel = 100;

    /**
     * 懒汉式实现英雄机创建
     */
    private static HeroAircraft instance;

    private ScheduledFuture<?> fireTimer;
    private static final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    private int animationIndex = 0;
    private int animationCounter = 0; // 动画帧计数器
    /**
     * 一个方法用于在游戏循环中更新动画状态
     */
    public void updateAnimation() {
        animationCounter++;
        if (animationCounter % 4 == 0) {
            animationIndex = (animationIndex + 1) % ImageManager.HERO_FIRE_ANIMATION.size();
        }
    }

    /**
     * 用于获取当前应该显示的火焰图片
     * @return 当前帧的火焰图片
     */
    public BufferedImage getFireImage() {
        return ImageManager.HERO_FIRE_ANIMATION.get(animationIndex);
    }

    public synchronized void activateFire(int level, long durationMs) {
        switch (level) {
            case 1:
                this.shootStrategy = new ScatterShootStrategy();
                break;
            case 2:
                this.shootStrategy = new RingShootStrategy();
                break;
            default:
                this.shootStrategy = new StraightShootStrategy();
        }

        if (fireTimer != null && !fireTimer.isDone()) {
            fireTimer.cancel(true);
        }

        fireTimer = scheduler.schedule(() -> {
            synchronized (HeroAircraft.this) {
                this.shootStrategy = new StraightShootStrategy();
            }
        }, durationMs, TimeUnit.MILLISECONDS);
    }

    @Override
    public void update() {
    }

    private HeroAircraft(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp, new StraightShootStrategy());
        this.direction = -1;
        this.shootNum = 1;
        this.power = 30;
    }

    public static synchronized HeroAircraft getInstance() {
        if (instance == null) {
            instance = new HeroAircraft(
                    Main.WINDOW_WIDTH / 2,
                    Main.WINDOW_HEIGHT - ImageManager.HERO_IMAGE.getHeight(),
                    0, 0, 200
            );
        }
        instance.resetStatus();
        return instance;
    }

    /**
     * 重置英雄机状态
     * 当新游戏开始时，所有属性恢复到初始值
     */
    public void resetStatus() {
        this.hp = this.maxHp;
        this.level = 1;
        this.exp = 0;
        this.expToNextLevel = 100;
        this.shootNum = 1;
        this.power = 30;
        this.direction = -1;
    }
    /**
     * 增加经验值
     * @param gainedExp 获得的经验值
     */
    public void increaseExp(int gainedExp) {
        this.exp += gainedExp;
        // 循环检查是否可以升级，支持一次性升多级
        while (this.exp >= this.expToNextLevel) {
            this.exp -= this.expToNextLevel;
            levelUp();
        }
    }
    /**
     * 升级
     */
    private void levelUp() {
        this.level++;
        this.expToNextLevel = (int)(this.expToNextLevel * 1.5);

        System.out.println("Hero Level Up! Current Level: " + this.level);
        MusicManager.playSoundEffect("/videos/level_up.wav");
        if (this.level % 5 == 0) {
            this.shootNum++;
            System.out.println("Shoot Number Increased to: " + this.shootNum);
        } else {
            this.power += 4;
            System.out.println("Bullet Power Increased to: " + this.power);
        }

        this.increaseHp(50);
    }

    public int getLevel() { return level; }
    public int getExp() { return exp; }
    public int getExpToNextLevel() { return expToNextLevel; }

    @Override
    public void forward() {
        // 英雄机由鼠标控制，不通过forward函数移动
    }


    public void increaseHp(int increase){
        this.hp = Math.min(this.hp + increase, this.maxHp);
    }
}
