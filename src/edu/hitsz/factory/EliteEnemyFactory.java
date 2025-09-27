package edu.hitsz.factory;

import edu.hitsz.aircraft.EnemyAircraft;
import edu.hitsz.aircraft.EliteEnemy;
import edu.hitsz.application.ImageManager;
import edu.hitsz.application.Main;

/**
 * 具体工厂：精英敌机工厂 (Concrete Creator for EliteEnemy)
 */
public class EliteEnemyFactory implements EnemyFactory {
    @Override
    public EnemyAircraft createEnemy() {
        int locationX =  (int) (Math.random() * (Main.WINDOW_WIDTH - ImageManager.ELITE_ENEMY_IMAGE.getWidth()));
        int locationY = (int) (Math.random() * Main.WINDOW_HEIGHT * 0.05);
        int speedX = 0;
        int speedY = 10;
        int hp = 45;
        return new EliteEnemy(locationX, locationY, speedX, speedY, hp);
    }
}
