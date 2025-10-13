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
        int direction = aircraft.getDirection();
        int y = aircraft.getLocationY() + direction*2;
        int speedX = 5;
        int speedY = aircraft.getSpeedY() + direction*2;
        int shootNum = 3;
        int power = aircraft.getPower();
        BaseBullet bullet;
        if (aircraft instanceof HeroAircraft) {
            speedY = -5;
            for(int i=0; i<shootNum; i++){
                // 散射弹道
                bullet = new HeroBullet(x, y, (i-1)*speedX, speedY, power);
                res.add(bullet);
            }
        } else {
            for(int i=0; i<shootNum; i++){
                // 散射弹道
                bullet = new EnemyBullet(x, y, (i-1)*speedX, speedY, power);
                res.add(bullet);
            }
        }

        return res;
    }
}
