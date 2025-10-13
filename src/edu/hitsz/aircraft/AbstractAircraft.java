package edu.hitsz.aircraft;

import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.basic.AbstractFlyingObject;
import edu.hitsz.strategy.ShootStrategy;

import java.util.List;

/**
 * 所有种类飞机的抽象父类：
 * 敌机（BOSS, ELITE, MOB），英雄飞机
 *
 * @author hitsz
 */
public abstract class AbstractAircraft extends AbstractFlyingObject {
    /**
     * 生命值
     */
    protected int maxHp;
    protected int hp;
    protected int direction = 1;
    protected int shootNum = 1;
    protected int power = 30;

    private ShootStrategy shootStrategy;

    public AbstractAircraft(int locationX, int locationY, int speedX, int speedY, int hp, ShootStrategy shootStrategy) {
        super(locationX, locationY, speedX, speedY);
        this.hp = hp;
        this.maxHp = hp;
        this.shootStrategy = shootStrategy;
    }

    public void decreaseHp(int decrease){
        hp -= decrease;
        if(hp <= 0){
            hp=0;
            vanish();
        }
    }

    public int getHp() {
        return hp;
    }


    /**
     * 策略模式飞机射击方法
     */
    public List<BaseBullet> shoot() {
        return shootStrategy.shoot(this);
    }

    public int getDirection() {
        return direction;
    }

    public int getPower() {
        return power;
    }

    public void setShootStrategy(ShootStrategy shootStrategy) {
        this.shootStrategy = shootStrategy;
    }
}


