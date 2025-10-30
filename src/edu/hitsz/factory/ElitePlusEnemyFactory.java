package edu.hitsz.factory;

import edu.hitsz.aircraft.ElitePlusEnemy;
import edu.hitsz.aircraft.EnemyAircraft;
import edu.hitsz.application.ImageManager;
import edu.hitsz.application.Main;

public class ElitePlusEnemyFactory implements EnemyFactory{
    @Override
    public EnemyAircraft createEnemy() {
        int locationX =  (int) (Math.random() * (Main.WINDOW_WIDTH - ImageManager.ELITE_PLUS_ENEMY_IMAGE.getWidth()));
        int locationY = (int) (Math.random() * Main.WINDOW_HEIGHT * 0.05);
        int speedX = (Math.random() < 0.5 ? 1 : -1) * 2;
        int speedY = 10;
        int hp = 200;
        return new ElitePlusEnemy(locationX, locationY, speedX, speedY, hp);
    }
}
