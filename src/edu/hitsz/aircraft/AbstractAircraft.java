package edu.hitsz.aircraft;

import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.basic.AbstractFlyingObject;
import edu.hitsz.prop.BombObserver;
import edu.hitsz.strategy.ShootStrategy;

import java.util.List;

/**
 * 所有种类飞机的抽象父类：
 * 敌机（BOSS, ELITE, MOB），英雄飞机
 *
 * @author hitsz
 */
public abstract class AbstractAircraft extends AbstractFlyingObject implements BombObserver {
    /**
     * 生命值
     */
    protected int maxHp;
    protected int hp;
    protected int direction = 1;
    protected int shootNum = 1;
    protected int power = 30;

    protected ShootStrategy shootStrategy;

    public int getShootNum () {
        return shootNum;
    }

    @Override
    public void update() {
        this.vanish();  // default action: vanish
    }

    public int getMaxHp() {
        return maxHp;
    }

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
}


