package edu.hitsz.aircraft;

import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.EnemyBullet;
import edu.hitsz.factory.*;
import edu.hitsz.prop.AbstractProp;
import edu.hitsz.application.ImageManager;
import edu.hitsz.strategy.RingShootStrategy;

import java.util.LinkedList;
import java.util.List;

public class BossEnemy extends EnemyAircraft {

    /**
     * 被消灭时获得分数
     */
    private final int score = 100;

    @Override
    public void update() {
    }

    public BossEnemy(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp, new RingShootStrategy());
        this.power = 20;
        this.direction = 1;
        this.shootNum = 20;
        this.maxHp = hp;
    }

    @Override
    public int getScore() {
        return score;
    }

    @Override
    public List<AbstractProp> getProps() {
        List<AbstractProp> props = new LinkedList<>();

        double dropRoll = Math.random();
        int propCount = 0;
        if (dropRoll < 0.10) {
            propCount = 3;
        } else if (dropRoll < 0.30) {
            propCount = 2;
        } else if (dropRoll < 0.60) {
            propCount = 1;
        }
        if (propCount == 0) {
            return props;
        }
        int px = this.getLocationX();
        int py = this.getLocationY();

        int propWidth = Math.max(ImageManager.HP_SUPPLY_IMAGE.getWidth(), ImageManager.BOMB_SUPPLY_IMAGE.getWidth());
        propWidth = Math.max(propWidth, ImageManager.FIRE_SUPPLY_IMAGE.getWidth());
        int gap = 10;
        int totalWidth = propCount * propWidth + (propCount - 1) * gap;

        int startX = px - totalWidth / 2;
        for (int i = 0; i < propCount; i++) {
            PropFactory propFactory;
            double prob = Math.random();
            if (prob < 0.25) {
                propFactory = new HpSupplyFactory();
            } else if (prob < 0.5) {
                propFactory = new FireSupplyFactory();
            } else if (prob < 0.75) {
                propFactory = new BombSupplyFactory();
            } else {
                propFactory = new FirePlusSupplyFactory();
            }

            int currentX = startX + i * (propWidth + gap);
            props.add(propFactory.createProp(currentX, py));
        }
        return props;
    }
}