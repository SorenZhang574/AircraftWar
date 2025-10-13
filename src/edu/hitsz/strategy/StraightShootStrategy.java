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
            for (int i = 0; i < shootNum; i++) {
                bullet = new HeroBullet(x, y, speedX, speedY, power);
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
