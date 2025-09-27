package edu.hitsz.factory;

import edu.hitsz.aircraft.EnemyAircraft;

public interface EnemyFactory {
    /**
     * 创建敌机的抽象方法（工厂方法）
     * 具体创建哪种敌机，由子类实现
     */
    public abstract EnemyAircraft createEnemy();
}
