package edu.hitsz.factory;

import edu.hitsz.aircraft.BossEnemy;
import edu.hitsz.aircraft.EnemyAircraft;
import edu.hitsz.application.ImageManager;
import edu.hitsz.application.Main;



public class BossEnemyFactory implements EnemyFactory{
    @Override
    public EnemyAircraft createEnemy() {
        int locationX =  (int) (Math.random() * (Main.WINDOW_WIDTH - ImageManager.BOSS_ENEMY_IMAGE.getWidth()));
        int locationY = (int) (Math.random() * (Main.WINDOW_HEIGHT - ImageManager.BOSS_ENEMY_IMAGE.getHeight()) * 0.5);
        int speedX = (Math.random() < 0.5 ? 1 : -1);
        int speedY = 0;
        int hp = 200;
        return new BossEnemy(locationX, locationY, speedX, speedY, hp);
    }
}
