package edu.hitsz.strategy;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.EnemyBullet;
import edu.hitsz.bullet.HeroBullet;

import java.util.LinkedList;
import java.util.List;

public class ScatterShootStrategy implements ShootStrategy{
    @Override
    public List<BaseBullet> shoot(AbstractAircraft aircraft) {
        List<BaseBullet> res = new LinkedList<>();
        int x = aircraft.getLocationX();
        int y = aircraft.getLocationY() + aircraft.getDirection() * 2;
        int power = aircraft.getPower();
        int direction = aircraft.getDirection();
        int shootNum;
        double spreadAngle;

        if (aircraft instanceof HeroAircraft) {
            shootNum = aircraft.getShootNum() + 2;
            spreadAngle = Math.toRadians(60);
        } else {
            shootNum = 3;
            spreadAngle = Math.toRadians(40);
        }

        double baseAngle = (direction == -1) ? -Math.PI / 2 : Math.PI / 2;
        for (int i = 0; i < shootNum; i++) {
            double offset = ((double)i - (shootNum - 1) / 2.0) / (shootNum - 1);
            double angle = baseAngle + offset * spreadAngle;

            int speedHero = 10;
            int speedEnemy = 15;
            int speedX = (int)(speedHero * Math.cos(angle));
            int speedY = (int)(speedHero * Math.sin(angle));
            int speedYEnemy = (int)(speedEnemy * Math.sin(angle));

            BaseBullet bullet = (aircraft instanceof HeroAircraft)
                    ? new HeroBullet(x, y, speedX, speedY, power)
                    : new EnemyBullet(x, y, speedX, speedYEnemy, power);
            res.add(bullet);
        }
        return res;
    }

}
