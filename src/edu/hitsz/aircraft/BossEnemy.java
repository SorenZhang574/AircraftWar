package edu.hitsz.aircraft;

import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.EnemyBullet;
import edu.hitsz.factory.BombSupplyFactory;
import edu.hitsz.factory.FireSupplyFactory;
import edu.hitsz.factory.HpSupplyFactory;
import edu.hitsz.factory.PropFactory;
import edu.hitsz.prop.AbstractProp;
import edu.hitsz.application.ImageManager;

import java.util.LinkedList;
import java.util.List;

public class BossEnemy extends EnemyAircraft {
    /**攻击方式 */

    /**
     * 子弹一次发射数量
     */
    private int shootNum = 20;

    /**
     * 子弹伤害
     */
    private int power = 20;

    /**
     * 子弹射击方向 (向上发射：-1，向下发射：1)
     */
    private int direction = 1;

    /**
     * 被消灭时获得分数
     */
    private final int score = 100;

    public BossEnemy(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp);
    }

    @Override
    public List<BaseBullet> shoot() {
        List<BaseBullet> res = new LinkedList<>();
        int x = this.getLocationX();
        int y = this.getLocationY();
        double angleStep = 2 * Math.PI / shootNum;
        int bulletSpeed = 8;
        for (int i = 0; i < shootNum; i++) {
            double currentAngle = i * angleStep;
            double speedX = bulletSpeed * Math.cos(currentAngle);
            double speedY = bulletSpeed * Math.sin(currentAngle);
            BaseBullet bullet = new EnemyBullet(x, y, (int) speedX, (int) speedY, power);
            res.add(bullet);
        }
        return res;
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
            if (prob < 0.33) {
                propFactory = new HpSupplyFactory();
            } else if (prob < 0.66) {
                propFactory = new FireSupplyFactory();
            } else {
                propFactory = new BombSupplyFactory();
            }

            int currentX = startX + i * (propWidth + gap);
            props.add(propFactory.createProp(currentX, py));
        }
        return props;
    }
}