package edu.hitsz.aircraft;

import edu.hitsz.application.Main;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.EnemyBullet;
import edu.hitsz.factory.*;
import edu.hitsz.prop.AbstractProp;
import edu.hitsz.strategy.ScatterShootStrategy;

import java.util.LinkedList;
import java.util.List;

public class ElitePlusEnemy extends EnemyAircraft{

    /**
     * 被消灭时获得分数
     */
    private final int score = 50;

    public ElitePlusEnemy(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp, new ScatterShootStrategy());
        this.direction = 1;
        this.power = 20;
        this.shootNum = 3;
    }

    @Override
    public int getScore() {
        return score;
    }

    @Override
    public List<AbstractProp> getProps() {
        PropFactory propFactory;
        List<AbstractProp> props = new LinkedList<>();
        double dropChance = 0.9;
        if (Math.random() < dropChance) {
            double prob = Math.random();
            int px = this.getLocationX();
            int py = this.getLocationY();
            AbstractProp prop;
            if (prob < 0.25) {
                propFactory = new HpSupplyFactory();
            } else if (prob < 0.5) {
                propFactory = new FireSupplyFactory();
            } else if (prob < 0.75) {
                propFactory = new BombSupplyFactory();
            } else {
                propFactory = new FirePlusSupplyFactory();
            }
            prop = propFactory.createProp(px, py);
            props.add(prop);
        }
        return props;
    }
}
