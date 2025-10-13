package edu.hitsz.strategy;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.EnemyBullet;
import edu.hitsz.bullet.HeroBullet;

import java.util.LinkedList;
import java.util.List;

public class RingShootStrategy implements ShootStrategy{
    @Override
    public List<BaseBullet> shoot(AbstractAircraft aircraft) {
        List<BaseBullet> res = new LinkedList<>();
        int x = aircraft.getLocationX();
        int y = aircraft.getLocationY();
        int shootNum = 20;
        int power = aircraft.getPower();
        double angleStep = 2 * Math.PI / shootNum;
        int bulletSpeed = 8;
        if (aircraft instanceof HeroAircraft) {
            for (int i = 0; i < shootNum; i++) {
                double currentAngle = i * angleStep;
                double speedX = bulletSpeed * Math.cos(currentAngle);
                double speedY = bulletSpeed * Math.sin(currentAngle);
                BaseBullet bullet = new HeroBullet(x, y, (int) speedX, (int) speedY, power);
                res.add(bullet);
            }
        } else {
            for (int i = 0; i < shootNum; i++) {
                double currentAngle = i * angleStep;
                double speedX = bulletSpeed * Math.cos(currentAngle);
                double speedY = bulletSpeed * Math.sin(currentAngle);
                BaseBullet bullet = new EnemyBullet(x, y, (int) speedX, (int) speedY, power);
                res.add(bullet);
            }
        }
        return res;
    }
}
