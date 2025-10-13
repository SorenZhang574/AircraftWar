package edu.hitsz.aircraft;

import edu.hitsz.factory.BombSupplyFactory;
import edu.hitsz.factory.FireSupplyFactory;
import edu.hitsz.factory.HpSupplyFactory;
import edu.hitsz.factory.PropFactory;
import edu.hitsz.prop.AbstractProp;
import edu.hitsz.factory.FirePlusSupplyFactory;
import edu.hitsz.strategy.StraightShootStrategy;

import java.util.LinkedList;
import java.util.List;

public class EliteEnemy extends EnemyAircraft {

    /**
     * 被消灭时获得分数
     */
    private final int score = 20;

    public EliteEnemy(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp, new StraightShootStrategy());
        this.speedX = 0;
        this.power = 10;
        this.direction = 1;
        this.shootNum = 1;
    }

    @Override
    public int getScore() {
        return score;
    }

    @Override
    public List<AbstractProp> getProps() {
        PropFactory propFactory;
        List<AbstractProp> props = new LinkedList<>();
        double dropChance = 0.75;
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
