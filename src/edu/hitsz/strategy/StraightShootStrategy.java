package edu.hitsz.strategy;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.EnemyBullet;
import edu.hitsz.bullet.HeroBullet;

import java.util.LinkedList;
import java.util.List;

public class StraightShootStrategy implements ShootStrategy{
    public List<BaseBullet> shoot(AbstractAircraft aircraft) {
        List<BaseBullet> res = new LinkedList<>();
        int x = aircraft.getLocationX();
        int direction = aircraft.getDirection();
        int y = aircraft.getLocationY() + direction*2;
        int speedX = 0;
        int speedY = aircraft.getSpeedY() + direction*5;
        int shootNum = 1;
        int power = aircraft.getPower();
        BaseBullet bullet;
        if (aircraft instanceof HeroAircraft) {
            shootNum = aircraft.getShootNum();
            int gap = 20;
            double startX = x - (shootNum - 1) * gap / 2.0;
            for (int i = 0; i < shootNum; i++) {
                int currentX = (int) (startX + i * gap);
                bullet = new HeroBullet(currentX, y, speedX, speedY, power);
                res.add(bullet);
            }
        } else {
            for (int i = 0; i < shootNum; i++) {
                bullet = new EnemyBullet(x, y, speedX, speedY, power);
                res.add(bullet);
            }
        }
        return res;
    }
}
