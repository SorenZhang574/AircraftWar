package edu.hitsz.aircraft;

import edu.hitsz.application.ImageManager;
import edu.hitsz.application.Main;
import edu.hitsz.strategy.RingShootStrategy;
import edu.hitsz.strategy.ScatterShootStrategy;
import edu.hitsz.strategy.StraightShootStrategy;

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

    /**
     * 懒汉式实现英雄机创建
     */
    private static HeroAircraft instance;

    private ScheduledFuture<?> fireTimer;
    private static final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
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
                    0, 0, 100
            );
        }
        return instance;
    }

    @Override
    public void forward() {
        // 英雄机由鼠标控制，不通过forward函数移动
    }


    public void increaseHp(int increase){
        this.hp = Math.min(this.hp + increase, this.maxHp);
    }
}
