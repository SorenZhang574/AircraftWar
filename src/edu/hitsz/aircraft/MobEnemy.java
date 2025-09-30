package edu.hitsz.aircraft;

import edu.hitsz.prop.AbstractProp;

import java.util.LinkedList;
import java.util.List;

/**
 * 普通敌机
 * 不可射击
 *
 * @author hitsz
 */
public class MobEnemy extends EnemyAircraft {

    /**
     * 被消灭时获得分数
     */
    private final int score = 10;

    public MobEnemy(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp);
    }

    @Override
    public int getScore() {
        return score;
    }

    @Override
    public List<AbstractProp> getProps() {
        return new LinkedList<>();
    }
}
